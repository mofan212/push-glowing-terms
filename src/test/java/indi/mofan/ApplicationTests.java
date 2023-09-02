package indi.mofan;

import indi.mofan.helper.BaiduWeatherHelper;
import indi.mofan.resp.baidu.weather.Weather;
import lombok.SneakyThrows;
import org.assertj.core.api.WithAssertions;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ApplicationTests implements WithAssertions {
    @Autowired
    private BaiduWeatherHelper weatherHelper;
    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void test() {
        final String originPwd = "";
        String encryptedPwd = encryptor.encrypt(originPwd);
        Assertions.assertEquals(originPwd, encryptor.decrypt(encryptedPwd));
        System.out.println("未加密数据：" + originPwd);
        System.out.println("加密后的数据：" + encryptedPwd);
    }

    @Test
    @SneakyThrows
    public void testGetWeather() {
        Optional<Weather> optional = weatherHelper.getWeather();
        if (optional.isEmpty()) {
            Assertions.fail();
        }
        Weather weather = optional.get();
        assertThat(weather).isNotNull()
                .extracting(Weather::getForecast)
                .isNotNull();
    }
}
