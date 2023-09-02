package indi.mofan.helper;

import com.fasterxml.jackson.databind.JsonNode;
import indi.mofan.properties.tianapi.TianApiProperties;
import indi.mofan.resolver.RestResolver;
import indi.mofan.util.JsonNodeUtil;
import indi.mofan.util.TianApiUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author mofan
 * @date 2023/9/2 17:18
 */
@Component
public class GlowingTermHelper {
    public static final String GLOWING_TERM_TEMPLATE = "https://apis.tianapi.com/caihongpi/index?key={api_key}";

    private final TianApiProperties apiProperties;

    public GlowingTermHelper(TianApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    public Optional<String> getGlowingTerm() {
        return RestResolver.from(
                GLOWING_TERM_TEMPLATE,
                apiProperties.toApiKeyMap(),
                TianApiUtil::isSuccess,
                this::getContent
        ).get();
    }

    private Optional<String> getContent(JsonNode resp) {
        return TianApiUtil.getRespResult(resp).flatMap(i -> JsonNodeUtil.getStringValue(i, "content"));
    }
}
