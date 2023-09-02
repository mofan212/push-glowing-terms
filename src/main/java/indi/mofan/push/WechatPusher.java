package indi.mofan.push;


import indi.mofan.helper.BaiduWeatherHelper;
import indi.mofan.helper.EverydayEnglishHelper;
import indi.mofan.helper.GlowingTermHelper;
import indi.mofan.properties.CommonProperties;
import indi.mofan.properties.wechat.WxConfigProperties;
import indi.mofan.resp.baidu.weather.CurrentWeather;
import indi.mofan.resp.baidu.weather.Forecast;
import indi.mofan.resp.baidu.weather.Weather;
import indi.mofan.resp.tian.EverydayEnglish;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpMapConfigImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author mofan
 * @date 2023/9/2 18:00
 */
@Slf4j
@Component
public class WechatPusher {

    private final CommonProperties commonProperties;
    private final WxConfigProperties wxProperties;
    private final BaiduWeatherHelper weatherHelper;
    private final GlowingTermHelper glowingTermHelper;
    private final EverydayEnglishHelper englishHelper;

    public WechatPusher(CommonProperties commonProperties, WxConfigProperties wxProperties, BaiduWeatherHelper weatherHelper,
                        GlowingTermHelper glowingTermHelper, EverydayEnglishHelper englishHelper) {
        this.commonProperties = commonProperties;
        this.wxProperties = wxProperties;
        this.weatherHelper = weatherHelper;
        this.glowingTermHelper = glowingTermHelper;
        this.englishHelper = englishHelper;
    }

    public void push() {
        // 测试公众号配置
        WxMpMapConfigImpl config = new WxMpMapConfigImpl();
        config.setAppId(wxProperties.getAppId());
        config.setSecret(wxProperties.getAppSecret());
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);

        // 消息推送配置后
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(wxProperties.getToUserId())
                .templateId(wxProperties.getTemplateId())
                .build();

        // 推送的消息
        if (addData(templateMessage)) {
            try {
                log.info(wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage));
            } catch (Exception e) {
                log.info(templateMessage.toJson());
            }
        }
    }

    private boolean addData(WxMpTemplateMessage templateMessage) {
        Optional<Weather> weatherOptional = weatherHelper.getWeather();
        Optional<String> glowingTerm = glowingTermHelper.getGlowingTerm();
        Optional<EverydayEnglish> englishOptional = englishHelper.getEverydayEnglish();
        if (Stream.of(weatherOptional, glowingTerm, englishOptional).anyMatch(Optional::isEmpty)) {
            return false;
        }
        addWeather(templateMessage, weatherOptional.get());
        templateMessage.addData(new WxMpTemplateData("glowingTerm", glowingTerm.get(), "#FF69B4"));
        addEverydayEnglish(templateMessage, englishOptional.get());
        templateMessage.addData(new WxMpTemplateData("nickname", commonProperties.getNickname(), "#FF0000"));
        return true;
    }

    private void addWeather(WxMpTemplateMessage templateMessage, Weather weather) {
        CurrentWeather now = weather.getNow();
        Forecast forecast = weather.getForecast();
        templateMessage.addData(new WxMpTemplateData("date", forecast.getDate() + "  " + forecast.getWeek(), "#00BFFF"));
        templateMessage.addData(new WxMpTemplateData("weather", now.getText(), "#00FFFF"));
        templateMessage.addData(new WxMpTemplateData("temp", addTempUnit(now.getTemp()), "#EE212D"));
        templateMessage.addData(new WxMpTemplateData("low", addTempUnit(String.valueOf(forecast.getLow())), "#173177"));
        templateMessage.addData(new WxMpTemplateData("high", addTempUnit(String.valueOf(forecast.getHigh())), "#FF6347"));
        templateMessage.addData(new WxMpTemplateData("wind_class", now.getWindClass(), "#42B857"));
        templateMessage.addData(new WxMpTemplateData("wind_dir", now.getWindDir() + "", "#B95EA3"));
    }

    private void addEverydayEnglish(WxMpTemplateMessage templateMessage, EverydayEnglish everydayEnglish) {
        templateMessage.addData(new WxMpTemplateData("every_english_en", everydayEnglish.getContent(), "#C71585"));
        templateMessage.addData(new WxMpTemplateData("every_english_zh", everydayEnglish.getNote(), "#C71585"));
    }

    private String addTempUnit(String temp) {
        return temp + "°C";
    }
}
