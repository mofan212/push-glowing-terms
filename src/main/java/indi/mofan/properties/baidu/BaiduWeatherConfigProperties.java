package indi.mofan.properties.baidu;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * @author mofan
 * @date 2023/9/2 14:02
 */
@Setter
@Getter
@Validated
public class BaiduWeatherConfigProperties {
    /**
     * 地方行政码
     */
    @NotEmpty
    @JsonProperty("district_id")
    private String districtId;

    /**
     * 数据类型
     */
    @NotEmpty
    @JsonProperty("data_type")
    private String dataType;

    /**
     * 应用的 ak
     */
    @NotEmpty
    private String ak;

    public Map<String, Object> toParamMap() {
        return Map.of(
                "district_id", this.districtId,
                "data_type", this.dataType,
                "ak", this.ak
        );
    }
}
