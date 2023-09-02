package indi.mofan.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author mofan
 * @date 2023/9/2 20:46
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("your")
public class CommonProperties {
    @NotEmpty
    private String nickname;
}
