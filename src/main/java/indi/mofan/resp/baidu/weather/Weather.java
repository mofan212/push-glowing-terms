package indi.mofan.resp.baidu.weather;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author mofan
 * @date 2023/9/2 14:01
 */
@Getter
@Setter
public class Weather {
    private List<Index> indexes;
    private List<Alert> alerts;
    private Forecast forecast;
}
