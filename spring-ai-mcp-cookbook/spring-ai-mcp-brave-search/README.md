
# Spring AI - 模型上下文协议 (MCP) Brave Search 示例

本示例演示了如何在 Spring Boot 自动配置的支持下，使用 **Spring AI 模型上下文协议 (MCP)** 与 [Brave Search MCP Server](https://github.com/modelcontextprotocol/servers/tree/main/src/brave-search) 进行集成。  
该应用支持通过自然语言与 Brave Search 交互，实现通过对话接口进行互联网搜索的功能。

运行该应用时会展示 MCP 客户端的能力：提出一个特定问题：“**Does Spring AI support the Model Context Protocol? Please provide some references.**”（Spring AI 是否支持模型上下文协议？请提供参考资料）。MCP 客户端使用 Brave Search 找到相关信息，并返回全面的回答。回答完成后，程序将自动退出。

与手动配置不同，本示例使用了 Spring Boot Starter 自动创建并配置 MCP 客户端

## 依赖项

项目主要使用以下依赖：

```xml
<dependencies>
   <dependency>
      <groupId>org.springframework.ai</groupId>
      <artifactId>spring-ai-starter-model-openai</artifactId>
   </dependency>
   
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-starter-mcp-client</artifactId>
   </dependency>
</dependencies>
```



## 环境要求

- Java 17 或更高版本
- Maven 3.6+
- npx 包管理器
- Anthropic API Key
- Brave Search API Key ([获取地址](https://brave.com/search/api/))

## 安装步骤

1. 安装 `npx`：
   首先确保 [npm 已安装]，然后运行：
   ```bash
   npm -v
   ```


2. 克隆仓库：
   ```bash
   git clone https://github.com/spring-projects/spring-ai-examples.git
   cd model-context-protocol/brave-starter
   ```


3. 设置 API 密钥：
   ```bash
   export OPENAI_API_KEY='your-openai-api-key-here'
   export BRAVE_API_KEY='your-brave-api-key-here'
   ```


4. 构建项目：
   ```bash
   mvn clean package
   ```


## 运行应用

使用 java 启动应用：
```bash
java  -jar target/spring-ai-examples-0.0.1-SNAPSHOT.jar
```


应用将执行一个示例问题，验证 Spring AI 与 MCP 的集成能力。

## 原理说明

本项目基于 Spring Boot 自动配置机制，完成 MCP 客户端的初始化。主要通过两个文件进行配置。

### 项目依赖说明

使用以下 Spring Boot starters：

```xml
<dependencies>
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-starter-mcp-client</artifactId>
   </dependency>
   
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-starter-model-anthropic</artifactId>
   </dependency>
</dependencies>
```


### 配置方式 （STDIO 传输配置）


MCP 客户端可通过两种方式进行配置，效果相同：

#### 方式一：直接在 application.properties 中配置

```properties
spring.application.name=mcp
spring.main.web-application-type=none
spring.ai.openai.api-key=${OPENAI_API_KEY}

# 直接配置 MCP 客户端
spring.ai.mcp.client.stdio.connections.brave-search.command=npx
spring.ai.mcp.client.stdio.connections.brave-search.args=-y,@modelcontextprotocol/server-brave-search
```


#### 方式二：使用外部 JSON 文件配置

1. 在 [application.properties] 中启用外部配置文件，并注释掉直接配置：
```properties
spring.application.name=mcp
spring.main.web-application-type=none
spring.ai.openai.api-key=${OPENAI_API_KEY}

# 启用外部配置文件
spring.ai.mcp.client.stdio.servers-configuration=classpath:/mcp-servers-config.json

# 注释掉以下内容
# spring.ai.mcp.client.stdio.connections.brave-search.command=npx
# spring.ai.mcp.client.stdio.connections.brave-search.args=-y,@modelcontextprotocol/server-brave-search
```


2. 在 [mcp-servers-config.json]中定义 MCP 服务配置：
```json
{
   "mcpServers": {
      "brave-search": {
         "command": "D:\\Program Files\\nodejs\\npx.cmd",
         "args": [
            "-y",
            "@modelcontextprotocol/server-brave-search"
         ],
         "env": {
         }
      }
   }
}
```


备注：关于SSE 传输配配置
也可以使用 HttpClient 连接到 Server-Sent Events (SSE)服务器。请参考 SSE 配置属性文档。
#SSE 传输的属性以 spring.ai.mcp.client.sse 为前缀：
```properties
spring.ai.mcp.client.sse.connections.server1.url=http://localhost:8080
spring.ai.mcp.client.sse.connections.server2.url=http://localhost:8081
```

### 应用代码说明

主类 [Application.java] 使用自动配置组件创建一个聊天客户端，并执行一个预设问题：

```java
@SpringBootApplication
public class Application {

   public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
   }

   @Bean
   public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpSyncClients,
                                                ConfigurableApplicationContext context) {

      return args -> {

         var chatClient = chatClientBuilder
                 .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
                 .build();

         String question = "Does Spring AI support the Model Context Protocol? Please provide some references.";

         System.out.println("QUESTION: " + question);
         System.out.println("ASSISTANT: " + chatClient.prompt(question).call().content());

         context.close();
      };
   }
}
```


Spring Boot 自动配置帮助我们完成了以下工作：
- 将应用配置为命令行工具（非 Web）
- 根据 API 密钥设置 Anthropic 集成
- 启用 MCP STDIO 客户端并与 Brave Search 服务器通信
- 提供包含 Brave Search 功能的 `List<ToolCallback>` Bean，并自动注入到 `CommandLineRunner`
