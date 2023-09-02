package indi.mofan.resp.baidu.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mofan
 * @date 2023/9/2 20:05
 */
@Getter
@Setter
public class CurrentWeather {
    /**
     * 当前温度
     */
    private String temp;

    /**
     * 当前天气现象
     */
    private String text;

    /**
     * 当前体感温度
     */
    @JsonProperty("feels_like")
    private String feelsLike;

    /**
     * 风力等级
     */
    @JsonProperty("wind_class")
    private String windClass;

    /**
     * 风力描述
     */
    @JsonProperty("wind_dir")
    private String windDir;
}
