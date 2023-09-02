package indi.mofan.resp.baidu.weather;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mofan
 * @date 2023/9/2 14:32
 */
@Getter
@Setter
public class Alert {
    private String type;
    private String level;
    private String title;
    private String desc;
}
