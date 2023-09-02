package indi.mofan.resp.baidu.weather;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 生活指数
 *
 * @author mofan
 * @date 2023/9/2 14:32
 */
@Getter
@Setter
public class Index {

    public static final Set<String> REQUIRE_INDEX_NAMES = Sets.newLinkedHashSet(List.of("感冒指数", "紫外线指数", "穿衣指数"));

    private String name;
    private String brief;
    private String detail;
}
