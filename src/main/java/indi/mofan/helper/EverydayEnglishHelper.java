package indi.mofan.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.mofan.properties.tianapi.TianApiProperties;
import indi.mofan.resolver.RestResolver;
import indi.mofan.resp.tian.EverydayEnglish;
import indi.mofan.util.TianApiUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author mofan
 * @date 2023/9/2 17:40
 */
@Component
public class EverydayEnglishHelper {

    public static final String EVERYDAY_ENGLISH_TEMPLATE = "https://apis.tianapi.com/everyday/index?key={api_key}";

    private final TianApiProperties properties;
    private final ObjectMapper mapper;

    public EverydayEnglishHelper(TianApiProperties properties, ObjectMapper mapper) {
        this.properties = properties;
        this.mapper = mapper;
    }

    public Optional<EverydayEnglish> getEverydayEnglish() {
        return RestResolver.from(
                EVERYDAY_ENGLISH_TEMPLATE,
                properties.toApiKeyMap(),
                TianApiUtil::isSuccess,
                this::getContent
        ).get();
    }

    private Optional<EverydayEnglish> getContent(JsonNode resp) {
        return TianApiUtil.getRespResult(resp).map(i -> mapper.convertValue(i, EverydayEnglish.class));
    }
}
