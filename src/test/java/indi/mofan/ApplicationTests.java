package indi.mofan;

import indi.mofan.helper.BaiduWeatherHelper;
import indi.mofan.helper.EverydayEnglishHelper;
import indi.mofan.helper.GlowingTermHelper;
import indi.mofan.resp.baidu.weather.Weather;
import indi.mofan.resp.tian.EverydayEnglish;
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
    private StringEncryptor encryptor;
    @Autowired
    private BaiduWeatherHelper weatherHelper;
    @Autowired
    private GlowingTermHelper glowingTermHelper;
    @Autowired
    private EverydayEnglishHelper englishHelper;

    @Test
    public void testEncrypt() {
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

    @Test
    public void testGetGlowingTerm() {
        Optional<String> optional = glowingTermHelper.getGlowingTerm();
        if (optional.isEmpty()) {
            Assertions.fail();
        }
        assertThat(optional.get()).isNotEmpty();
    }

    @Test
    public void testGetEverydayEnglish() {
        Optional<EverydayEnglish> optional = englishHelper.getEverydayEnglish();
        if (optional.isEmpty()) {
            Assertions.fail();
        }
        assertThat(optional.get()).isNotNull()
                .extracting(EverydayEnglish::getContent, EverydayEnglish::getNote)
                .doesNotContainNull();
    }
}
