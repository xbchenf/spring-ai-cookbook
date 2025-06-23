package com.spring.ai.deepseek;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.util.Assert;

import java.util.function.Function;

/**
 * 天气服务工具配置类
 *
 * 提供天气查询相关的工具方法注册功能
 * 主要包含一个获取当前天气信息的工具
 *
 * @author 寻道AI小兵
 */
@Configuration(proxyBeanMethods = false)
class WeatherTools {

    /**
     * 当前天气查询工具的Bean名称常量
     * 用于在Spring上下文中标识当前天气查询工具
     */
    public static final String CURRENT_WEATHER_TOOL = "currentWeather";

    /**
     * 创建并返回当前天气查询工具实例
     *
     * 该工具用于获取指定地点的当前天气信息
     * 支持摄氏度(C)和华氏度(F)两种温度单位
     *
     * @return Function<WeatherRequest, WeatherResponse> 返回封装好的天气查询函数
     */
    @Bean(CURRENT_WEATHER_TOOL)
    @Description("Get the weather in location")
    Function<WeatherRequest, WeatherResponse> currentWeather() {
        return request -> {
            Assert.notNull(request, "Weather request cannot be null");

            // 模拟天气数据获取
            double temperature = fetchTemperature(request.location());

            return new WeatherResponse(temperature, request.unit());
        };
    }

    /**
     * 温度单位枚举
     *
     * 表示支持的温度单位：
     * C - 摄氏度
     * F - 华氏度
     */
    public enum Unit {
        /** 摄氏度 */ C,
        /** 华氏度 */ F
    }

    /**
     * 天气请求记录类
     *
     * 用于封装天气查询请求参数
     * 包含地点和期望的温度单位
     *
     * @param location 请求查询的地理位置名称
     * @param unit     期望返回的温度单位（摄氏度或华氏度）
     */
    public record WeatherRequest(String location, WeatherService.Unit unit) {}

    /**
     * 天气响应记录类
     *
     * 用于封装天气查询结果
     * 包含当前温度和使用的温度单位
     *
     * @param temp  当前温度值
     * @param unit  返回的温度单位（摄氏度或华氏度）
     */
    public record WeatherResponse(double temp, WeatherService.Unit unit) {}

    /**
     * 模拟从外部服务获取温度数据
     *
     * @param location 地理位置名称
     * @return 模拟的当前温度值
     */
    private double fetchTemperature(String location) {
        // 实际实现中应该调用真实的天气API
        // 这里使用随机数模拟不同地点的温度变化
        return (Math.random() * 30 + 10); // 温度范围10~40°C
    }
}
