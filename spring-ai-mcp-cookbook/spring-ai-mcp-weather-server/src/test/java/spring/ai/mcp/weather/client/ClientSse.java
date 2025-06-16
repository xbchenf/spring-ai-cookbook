package spring.ai.mcp.weather.client;

import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;


public class ClientSse {

	public static void main(String[] args) {
		HttpClientSseClientTransport transport = HttpClientSseClientTransport.builder("http://localhost:8080").build();
		new SampleClient(transport).run();
	}

}
