package indi.mofan.properties.wechat;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author mofan
 * @date 2023/9/2 19:34
 */
@Getter
@Setter
@Component
@Validated
@ConfigurationProperties("we-chat")
public class WxConfigProperties {

    @NotEmpty
    private String appId;

    @NotEmpty
    private String appSecret;

    @NotEmpty
    private String toUserId;

    @NotEmpty
    private String templateId;
}
