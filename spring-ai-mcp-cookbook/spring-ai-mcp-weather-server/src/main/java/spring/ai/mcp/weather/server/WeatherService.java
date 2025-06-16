package spring.ai.mcp.weather.server;

// 导入必要的依赖库
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.ai.tool.annotation.Tool; // Spring AI 工具注解支持
import org.springframework.stereotype.Service; // Spring 服务组件注解
import org.springframework.web.client.RestClient; // 用于发起 REST 请求
import org.springframework.web.client.RestClientException; // REST 请求异常处理

/**
 * 天气服务类，提供基于经纬度获取天气预报和天气警报的功能。
 * 该类通过调用美国国家气象局（weather.gov）API 获取数据。
 */
@Service
public class WeatherService {

	// 天气 API 的基础 URL 地址
	private static final String BASE_URL = "https://api.weather.gov";

	// 使用 RestClient 发起 HTTP 请求
	private final RestClient restClient;

	/**
	 * 构造函数初始化 RestClient 实例
	 */
	public WeatherService() {
		this.restClient = RestClient.builder()
			.baseUrl(BASE_URL) // 设置基础 URL
			.defaultHeader("Accept", "application/geo+json") // 接收 JSON 格式响应
			.defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)") // 用户代理标识
			.build();
	}

	/**
	 * 表示一个包含地理位置信息的记录（如预报链接）
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Points(@JsonProperty("properties") Props properties) {
		/**
		 * 包含具体属性信息的内部记录，例如天气预报的 URL
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Props(@JsonProperty("forecast") String forecast) {}
	}

	/**
	 * 表示天气预报的记录，包括多个时间段的天气详情
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Forecast(@JsonProperty("properties") Props properties) {
		/**
		 * 包含时间段列表的内部记录
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Props(@JsonProperty("periods") List<Period> periods) {}

		/**
		 * 每个时间段的具体天气信息记录
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Period(
				@JsonProperty("number") Integer number,
				@JsonProperty("name") String name,
				@JsonProperty("startTime") String startTime,
				@JsonProperty("endTime") String endTime,
				@JsonProperty("isDaytime") Boolean isDayTime,
				@JsonProperty("temperature") Integer temperature,
				@JsonProperty("temperatureUnit") String temperatureUnit,
				@JsonProperty("temperatureTrend") String temperatureTrend,
				@JsonProperty("probabilityOfPrecipitation") Map probabilityOfPrecipitation,
				@JsonProperty("windSpeed") String windSpeed,
				@JsonProperty("windDirection") String windDirection,
				@JsonProperty("icon") String icon,
				@JsonProperty("shortForecast") String shortForecast,
				@JsonProperty("detailedForecast") String detailedForecast) {}
	}

	/**
	 * 表示天气警报信息的记录，包含多个警报特征
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Alert(@JsonProperty("features") List<Feature> features) {

		/**
		 * 表示每个警报特征的记录
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Feature(@JsonProperty("properties") Properties properties) {}

		/**
		 * 表示警报详细信息的记录
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Properties(
				@JsonProperty("event") String event,
				@JsonProperty("areaDesc") String areaDesc,
				@JsonProperty("severity") String severity,
				@JsonProperty("description") String description,
				@JsonProperty("instruction") String instruction) {}
	}

	/**
	 * 获取指定经纬度的天气预报信息
	 *
	 * @param latitude 纬度坐标
	 * @param longitude 经度坐标
	 * @return 返回格式化的天气预报文本
	 * @throws RestClientException 如果请求失败则抛出异常
	 */
	@Tool(description = "Get weather forecast for a specific latitude/longitude")
	public String getWeatherForecastByLocation(double latitude, double longitude) {
		// 获取地理点位信息
		var points = restClient.get()
			.uri("/points/{latitude},{longitude}", latitude, longitude)
			.retrieve()
			.body(Points.class);

		// 根据返回的预报 URL 获取详细天气信息
		var forecast = restClient.get().uri(points.properties().forecast()).retrieve().body(Forecast.class);

		// 将各个时间段的天气信息整理为字符串输出
		String forecastText = forecast.properties().periods().stream().map(p -> {
			return String.format("""
					%s:
					Temperature: %s %s
					Wind: %s %s
					Forecast: %s
					""", p.name(), p.temperature(), p.temperatureUnit(), p.windSpeed(), p.windDirection(),
					p.detailedForecast());
		}).collect(Collectors.joining());

		return forecastText;
	}

	/**
	 * 获取指定地区的天气警报信息
	 *
	 * @param state 两位数的州代码（如 CA、NY）
	 * @return 返回格式化的人类可读警报信息
	 * @throws RestClientException 如果请求失败则抛出异常
	 */
	@Tool(description = "Get weather alerts for a US state. Input is Two-letter US state code (e.g. CA, NY)")
	public String getAlerts(String state) {
		// 调用 API 获取天气警报数据
		Alert alert = restClient.get().uri("/alerts/active/area/{state}", state).retrieve().body(Alert.class);

		// 将警报信息格式化为字符串输出
		return alert.features()
			.stream()
			.map(f -> String.format("""
					Event: %s
					Area: %s
					Severity: %s
					Description: %s
					Instructions: %s
					""", f.properties().event(), f.properties.areaDesc(), f.properties.severity(),
					f.properties.description(), f.properties.instruction()))
			.collect(Collectors.joining("\n"));
	}

	/**
	 * 主函数，用于测试天气服务功能
	 */
	public static void main(String[] args) {
		WeatherService client = new WeatherService();
		System.out.println(client.getWeatherForecastByLocation(47.6062, -122.3321)); // 示例：西雅图天气
		System.out.println(client.getAlerts("NY")); // 示例：纽约天气警报
	}
}
