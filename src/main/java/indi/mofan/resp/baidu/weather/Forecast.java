package indi.mofan.resp.baidu.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 当天的天气预报
 * @author mofan
 * @date 2023/9/2 14:37
 */
@Getter
@Setter
public class Forecast {
    /**
     * 白天天气
     */
    @JsonProperty("text_day")
    private String textDay;

    /**
     * 晚间天气
     */
    @JsonProperty("text_night")
    private String textNight;

    private Integer high;

    private Integer low;

    /**
     * 白天风力
     */
    @JsonProperty("wc_day")
    private String wcDay;

    /**
     * 白天风向
     */
    @JsonProperty("wd_day")
    private String wdDay;

    /**
     * 晚上风力
     */
    @JsonProperty("wc_night")
    private String wcNight;

    /**
     * 晚上风向
     */
    @JsonProperty("wd_night")
    private String wdNight;

    /**
     * 日期
     */
    private String date;

    /**
     * 星期
     */
    private String week;
}
