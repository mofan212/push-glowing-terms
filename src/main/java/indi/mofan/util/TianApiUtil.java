package indi.mofan.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

/**
 * @author mofan
 * @date 2023/9/2 17:45
 */
public final class TianApiUtil {
    private TianApiUtil() {
    }

    public static boolean isSuccess(JsonNode resp) {
        return JsonNodeUtil.hasTargetIntegerValue(resp, "code", 200);
    }

    public static Optional<ObjectNode> getRespResult(JsonNode resp) {
        return JsonNodeUtil.getObjectValue(resp, "result");
    }
}
