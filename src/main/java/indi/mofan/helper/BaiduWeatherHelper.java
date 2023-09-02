package indi.mofan.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.mofan.properties.baidu.BaiduProperties;
import indi.mofan.resp.baidu.weather.Alert;
import indi.mofan.resp.baidu.weather.Forecast;
import indi.mofan.resp.baidu.weather.Index;
import indi.mofan.resp.baidu.weather.Weather;
import indi.mofan.util.JsonNodeUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
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
        RestTemplate template = new RestTemplate();
        Map<String, Object> param = mapper.convertValue(baiduProperties.getWeatherConfig(), new TypeReference<>() {
        });
        String resp = template.getForObject(WEATHER_TEMPLATE, String.class, param);
        JsonNode tree;
        try {
            tree = mapper.readTree(resp);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
        if (!JsonNodeUtil.hasTargetIntegerValue(tree, "status", 0)) {
            return Optional.empty();
        }
        return JsonNodeUtil.getObjectValue(tree, "result").map(this::getWeather);
    }

    private Weather getWeather(JsonNode tree) {
        // 首先解析天气预报
        Optional<JsonNode> forecastOptional = JsonNodeUtil.getArrayValue(tree, "forecasts").stream()
                .flatMap(i -> StreamSupport.stream(i.spliterator(), false))
                .findFirst();
        if (forecastOptional.isEmpty()) {
            return null;
        }
        Forecast forecast = mapper.convertValue(forecastOptional.get(), Forecast.class);
        Weather weather = new Weather();
        weather.setForecast(forecast);

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
        return weather;
    }
}
