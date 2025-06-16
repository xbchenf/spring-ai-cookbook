package spring.ai.mcp.weather.client;

import java.io.File;

import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;

public class ClientStdio {

	public static void main(String[] args) {

		System.out.println(new File(".").getAbsolutePath());

		ServerParameters stdioParams = ServerParameters.builder("java")
				.args("-Dspring.ai.mcp.server.stdio=true", "-Dspring.main.web-application-type=none",
						"-Dlogging.pattern.console=", "-jar",
						"D:/github/spring-ai-cookbook/spring-ai-mcp-cookbook/spring-ai-mcp-weather-server/target/spring-ai-mcp-weather-server-0.0.1-SNAPSHOT.jar")
				.build();

		StdioClientTransport transport = new StdioClientTransport(stdioParams);

		new SampleClient(transport).run();
	}

}
