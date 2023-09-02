package indi.mofan.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.mofan.properties.baidu.BaiduProperties;
import indi.mofan.resolver.RestResolver;
import indi.mofan.resp.baidu.weather.Alert;
import indi.mofan.resp.baidu.weather.CurrentWeather;
import indi.mofan.resp.baidu.weather.Forecast;
import indi.mofan.resp.baidu.weather.Index;
import indi.mofan.resp.baidu.weather.Weather;
import indi.mofan.util.JsonNodeUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * @author mofan
 * @date 2023/9/2 14:48
 */
@Component
public class BaiduWeatherHelper {

    public static final String WEATHER_TEMPLATE = "https://api.map.baidu.com/weather/v1/?district_id={district_id}&data_type={data_type}&ak={ak}";

    private final BaiduProperties baiduProperties;
    private final ObjectMapper mapper;

    public BaiduWeatherHelper(BaiduProperties baiduProperties, ObjectMapper mapper) {
        this.baiduProperties = baiduProperties;
        this.mapper = mapper;
    }

    public Optional<Weather> getWeather() {
        return RestResolver.from(
                WEATHER_TEMPLATE,
                baiduProperties.getWeatherConfig().toParamMap(),
                resp -> JsonNodeUtil.hasTargetIntegerValue(resp, "status", 0),
                resp -> JsonNodeUtil.getObjectValue(resp, "result").flatMap(this::getWeather)
        ).get();
    }

    private Optional<Weather> getWeather(JsonNode tree) {
        // 解析当前天气信息
        Optional<CurrentWeather> nowOptional = JsonNodeUtil.getObjectValue(tree, "now")
                .map(i -> mapper.convertValue(i, CurrentWeather.class));
        if (nowOptional.isEmpty()) {
            return Optional.empty();
        }
        // 解析天气预报
        Optional<Forecast> forecastOptional = JsonNodeUtil.getArrayValue(tree, "forecasts").stream()
                .flatMap(i -> StreamSupport.stream(i.spliterator(), false))
                .findFirst()
                .map(i -> mapper.convertValue(i, Forecast.class));
        if (forecastOptional.isEmpty()) {
            return Optional.empty();
        }

        Weather weather = new Weather();
        weather.setNow(nowOptional.get());
        weather.setForecast(forecastOptional.get());

        // 解析 Index
        List<Index> indexes = JsonNodeUtil.getArrayValue(tree, "indexes").stream()
                .flatMap(i -> StreamSupport.stream(i.spliterator(), false))
                .filter(i -> Index.REQUIRE_INDEX_NAMES.stream().anyMatch(indexName -> JsonNodeUtil.hasTargetStringValue(i, "name", indexName)))
                .map(i -> mapper.convertValue(i, Index.class))
                .toList();
        weather.setIndexes(indexes);

        // 解析 Alter
        List<Alert> alerts = JsonNodeUtil.getArrayValue(tree, "alerts").stream()
                .flatMap(i -> StreamSupport.stream(i.spliterator(), false))
                .map(i -> mapper.convertValue(i, Alert.class))
                .toList();
        weather.setAlerts(alerts);
        return Optional.of(weather);
    }
}
