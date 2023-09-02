package indi.mofan.resolver;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author mofan
 * @date 2023/9/2 16:53
 */
@AllArgsConstructor(staticName = "from")
public class RestResolver<T> {
    private String urlTemplate;
    private Map<String, Object> urlVariables;
    private Predicate<JsonNode> respStatusPredicate;
    private Function<JsonNode, Optional<T>> respConvertor;

    private static final RestTemplate TEMPLATE = new RestTemplate();
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = SpringUtil.getBean(ObjectMapper.class);
    }

    public Optional<T> get() {
        String resp = TEMPLATE.getForObject(urlTemplate, String.class, urlVariables);
        JsonNode respJsonNode;
        try {
            respJsonNode = MAPPER.readTree(resp);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
        if (!respStatusPredicate.test(respJsonNode)) {
            return Optional.empty();
        }
        return respConvertor.apply(respJsonNode);
    }
}
