package indi.mofan.properties.baidu;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author mofan
 * @date 2023/9/2 14:07
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("baidu")
public class BaiduProperties {
    /**
     * 百度天气配置
     */
    @NestedConfigurationProperty
    private BaiduWeatherConfigProperties weatherConfig;
}
