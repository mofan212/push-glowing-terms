package indi.mofan.properties.tianapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * @author mofan
 * @date 2023/9/2 16:38
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("tian")
public class TianApiProperties {

    @NotEmpty
    @JsonProperty("api_key")
    private String apiKey;

    public Map<String, Object> toApiKeyMap() {
        return Map.of("api_key", this.apiKey);
    }
}
