package indi.mofan.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @author mofan
 * @date 2023/9/2 14:52
 */
public final class JsonNodeUtil {
    private JsonNodeUtil() {
    }

    public static boolean hasTargetStringValue(JsonNode jsonNode, String key, String value) {
        if (jsonNode == null || StringUtils.isEmpty(key)) {
            return false;
        }
        return getStringValue(jsonNode, key).map(i -> i.equals(value)).orElse(Boolean.FALSE);
    }

    public static boolean hasTargetIntegerValue(JsonNode jsonNode, String key, Integer value) {
        if (jsonNode == null || StringUtils.isEmpty(key)) {
            return false;
        }
        return getIntegerValue(jsonNode, key).map(i -> i.equals(value)).orElse(Boolean.FALSE);
    }

    public static Optional<Integer> getIntegerValue(JsonNode jsonNode, String key) {
        return getValue(jsonNode, key).filter(JsonNode::isInt).map(JsonNode::asInt);
    }

    public static Optional<String> getStringValue(JsonNode jsonNode, String key) {
        return getValue(jsonNode, key).filter(JsonNode::isTextual).map(JsonNode::asText);
    }

    public static Optional<JsonNode> getValue(JsonNode jsonNode, String key) {
        return Optional.ofNullable(jsonNode).map(i -> i.get(key));
    }

    public static Optional<ObjectNode> getObjectValue(JsonNode jsonNode, String key) {
        return getValue(jsonNode, key).filter(i -> i instanceof ObjectNode).map(i -> ((ObjectNode) i));
    }

    public static Optional<ArrayNode> getArrayValue(JsonNode jsonNode, String key) {
        return getValue(jsonNode, key).filter(JsonNode::isArray).map(i -> ((ArrayNode) i));
    }
}
