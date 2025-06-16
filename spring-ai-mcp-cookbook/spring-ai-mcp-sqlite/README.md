
# Spring AI 模型上下文协议 SQLite数据库集成示例

一个演示应用程序，展示了如何使用模型上下文协议 （MCP） 将 Spring AI 与 SQLite 数据库集成。此应用程序支持通过命令行界面与您的 SQLite 数据库进行自然语言交互。

它使用 SQLite MCP-Server 来运行 SQL 查询、分析业务数据和自动生成业务洞察备忘录。


## 先决条件
Java 17 或更高版本
Maven 3.6+ 版本
UVX 包管理器
Git 公司
OpenAI API 密钥
SQLite（可选，用于数据库修改）

设置您的 OpenAI API 密钥：
export OPENAI_API_KEY='your-api-key-here'

示例 SQLite 数据库
SQLite 数据库文件可以跨作系统移植。此存储库包含一个名为 的示例数据库文件。test.db

它有一个表，并且是使用脚本创建的PRODUCTScreate-database.sh

## 安装uv
C:\Users\Administrator>pip install uv
Collecting uv
Downloading uv-0.7.13-py3-none-win_amd64.whl.metadata (12 kB)
Downloading uv-0.7.13-py3-none-win_amd64.whl (18.8 MB)
---------------------------------------- 18.8/18.8 MB 2.7 MB/s eta 0:00:00
Installing collected packages: uv
Successfully installed uv-0.7.13

C:\Users\Administrator>uv --version
uv 0.7.13 (62ed17b23 2025-06-12)


## SQLite 3 安装指南

SQLite 是一种轻量级的嵌入式数据库，具有零配置、易于使用的特点，广泛应用于各种开发场景。以下是不同操作系统的安装方法。

### 一、Windows 系统安装

1. **下载预编译的二进制文件**
    - 访问 [SQLite 官方下载页面](https://www.sqlite.org/download.html)，在 **Windows** 区域找到以下两个文件：
        - **sqlite-tools-win32-*.zip**：包含命令行工具（如 `sqlite3.exe`）。
        - **sqlite-dll-win32-*.zip**：包含动态链接库（`sqlite3.dll`）。
    - 下载这两个文件。

2. **解压文件**
    - 创建一个文件夹，例如 `C:\sqlite`。
    - 将下载的两个压缩文件解压到 `C:\sqlite` 文件夹中。
    - 解压完成后，文件夹中将包含以下文件：
        - `sqlite3.def`
        - `sqlite3.dll`
        - `sqlite3.exe`

3. **配置环境变量**
    - 将 `C:\sqlite` 添加到系统的 PATH 环境变量中。
    - 操作步骤：
        1. 右键单击“此电脑”或“我的电脑”，选择“属性”。
        2. 点击“高级系统设置”。
        3. 在“系统属性”窗口中，点击“环境变量”。
        4. 在“系统变量”区域，找到并选择“Path”，然后点击“编辑”。
        5. 点击“新建”，输入 `C:\sqlite`，然后点击“确定”保存。

4. **验证安装**
    - 打开命令提示符（CMD），输入以下命令并按回车：
      ```bash
      sqlite3
      ```
    - 如果安装成功，将显示类似以下内容：
      ```
      SQLite version 3.7.15.2 2013-01-09 11:53:05
      Enter ".help" for instructions
      Enter SQL statements terminated with a ";"
      sqlite>
      ```

### 二、Linux 系统安装

大多数 Linux 发行版默认已预装 SQLite。可以通过以下命令验证是否已安装：

```bash
sqlite3 --version
```

如果显示版本信息，则表示已安装，可以直接使用。

如果未安装，可以按照以下步骤安装：

1. **下载源代码**
    - 访问 [SQLite 官方下载页面](https://www.sqlite.org/download.html)，从 **Source Code** 区域下载 **sqlite-autoconf-*.tar.gz** 文件。

2. **编译安装**
    - 打开终端，执行以下命令：
      ```bash
      tar xvzf sqlite-autoconf-*.tar.gz
      cd sqlite-autoconf-*
      ./configure --prefix=/usr/local
      make
      sudo make install
      ```

3. **验证安装**
    - 输入以下命令：
      ```bash
      sqlite3 --version
      ```
    - 如果安装成功，将显示版本信息。

### 三、macOS 系统安装

最新版本的 macOS 预装了 SQLite。可以通过以下命令验证是否已安装：

```bash
sqlite3 --version
```

如果显示版本信息，则表示已安装，可以直接使用。

如果未安装，可以按照以下步骤安装：

1. **下载源代码**
    - 访问 [SQLite 官方下载页面](https://www.sqlite.org/download.html)，从 **Source Code** 区域下载 **sqlite-autoconf-*.tar.gz** 文件。

2. **编译安装**
    - 打开终端，执行以下命令：
      ```bash
      tar xvzf sqlite-autoconf-*.tar.gz
      cd sqlite-autoconf-*
      ./configure --prefix=/usr/local
      make
      sudo make install
      ```

3. **验证安装**
    - 输入以下命令：
      ```bash
      sqlite3 --version
      ```
    - 如果安装成功，将显示版本信息。

---

## 使用 SQLite

安装完成后，可以通过命令行工具 `sqlite3` 或其他支持 SQLite 的应用程序（如数据库管理工具、编程语言的库等）来使用 SQLite 数据库。

- 在命令行中输入 `sqlite3`，即可进入 SQLite 的交互式命令行界面。
- 输入 `.help` 可以查看可用的命令和指令。


## 启动运行项目
直接在idea运行（或者java -jar spring-ai-sqlite.jar）

控制台输出如下：
```bash
"D:\Program Files\jdk-17.0.12\bin\java.exe" -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:65039,suspend=y,server=n -agentpath:C:\Users\Administrator\AppData\Local\Temp\idea_libasyncProfiler_dll_temp_folder10\libasyncProfiler.dll=version,jfr,event=wall,interval=10ms,cstack=no,file=C:\Users\Administrator\IdeaSnapshots\Application__2__2025_06_15_150519.jfr,dbghelppath=C:\Users\Administrator\AppData\Local\Temp\idea_dbghelp_dll_temp_folder1\dbghelp.dll,log=C:\Users\Administrator\AppData\Local\Temp\Application__2__2025_06_15_150519.jfr.log.txt,logLevel=DEBUG -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true "-Dmanagement.endpoints.jmx.exposure.include=*" -javaagent:C:\Users\Administrator\AppData\Local\JetBrains\IntelliJIdea2024.3\captureAgent\debugger-agent.jar -Dkotlinx.coroutines.debug.enable.creation.stack.trace=false -Ddebugger.agent.enable.coroutines=true -Dkotlinx.coroutines.debug.enable.flows.stack.trace=true -Dkotlinx.coroutines.debug.enable.mutable.state.flows.stack.trace=true -Dfile.encoding=UTF-8 -classpath "D:\github\spring-ai-cookbook\spring-ai-mcp-cookbook\spring-ai-mcp-sqlite\target\classes;D:\maven-repository\org\springframework\boot\spring-boot-starter\3.4.5\spring-boot-starter-3.4.5.jar;D:\maven-repository\org\springframework\boot\spring-boot\3.4.5\spring-boot-3.4.5.jar;D:\maven-repository\org\springframework\spring-context\6.2.6\spring-context-6.2.6.jar;D:\maven-repository\org\springframework\spring-aop\6.2.6\spring-aop-6.2.6.jar;D:\maven-repository\org\springframework\spring-beans\6.2.6\spring-beans-6.2.6.jar;D:\maven-repository\org\springframework\spring-expression\6.2.6\spring-expression-6.2.6.jar;D:\maven-repository\io\micrometer\micrometer-observation\1.14.6\micrometer-observation-1.14.6.jar;D:\maven-repository\io\micrometer\micrometer-commons\1.14.6\micrometer-commons-1.14.6.jar;D:\maven-repository\org\springframework\boot\spring-boot-autoconfigure\3.4.5\spring-boot-autoconfigure-3.4.5.jar;D:\maven-repository\org\springframework\boot\spring-boot-starter-logging\3.4.5\spring-boot-starter-logging-3.4.5.jar;D:\maven-repository\ch\qos\logback\logback-classic\1.5.18\logback-classic-1.5.18.jar;D:\maven-repository\ch\qos\logback\logback-core\1.5.18\logback-core-1.5.18.jar;D:\maven-repository\org\apache\logging\log4j\log4j-to-slf4j\2.24.3\log4j-to-slf4j-2.24.3.jar;D:\maven-repository\org\apache\logging\log4j\log4j-api\2.24.3\log4j-api-2.24.3.jar;D:\maven-repository\org\slf4j\jul-to-slf4j\2.0.17\jul-to-slf4j-2.0.17.jar;D:\maven-repository\jakarta\annotation\jakarta.annotation-api\2.1.1\jakarta.annotation-api-2.1.1.jar;D:\maven-repository\org\springframework\spring-core\6.2.6\spring-core-6.2.6.jar;D:\maven-repository\org\springframework\spring-jcl\6.2.6\spring-jcl-6.2.6.jar;D:\maven-repository\org\yaml\snakeyaml\2.3\snakeyaml-2.3.jar;D:\maven-repository\org\springframework\ai\spring-ai-starter-model-openai\1.1.0-SNAPSHOT\spring-ai-starter-model-openai-1.1.0-20250613.142119-44.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-model-openai\1.1.0-SNAPSHOT\spring-ai-autoconfigure-model-openai-1.1.0-20250613.142119-51.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-model-tool\1.1.0-SNAPSHOT\spring-ai-autoconfigure-model-tool-1.1.0-20250613.142119-64.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-retry\1.1.0-SNAPSHOT\spring-ai-autoconfigure-retry-1.1.0-20250613.142119-65.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-model-chat-observation\1.1.0-SNAPSHOT\spring-ai-autoconfigure-model-chat-observation-1.1.0-20250613.142119-64.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-model-embedding-observation\1.1.0-SNAPSHOT\spring-ai-autoconfigure-model-embedding-observation-1.1.0-20250613.142119-64.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-model-image-observation\1.1.0-SNAPSHOT\spring-ai-autoconfigure-model-image-observation-1.1.0-20250613.142119-53.jar;D:\maven-repository\org\springframework\ai\spring-ai-openai\1.1.0-SNAPSHOT\spring-ai-openai-1.1.0-20250613.142119-65.jar;D:\maven-repository\org\springframework\ai\spring-ai-model\1.1.0-SNAPSHOT\spring-ai-model-1.1.0-20250613.193905-86.jar;D:\maven-repository\org\springframework\ai\spring-ai-commons\1.1.0-SNAPSHOT\spring-ai-commons-1.1.0-20250613.193905-86.jar;D:\maven-repository\io\micrometer\micrometer-core\1.14.6\micrometer-core-1.14.6.jar;D:\maven-repository\org\hdrhistogram\HdrHistogram\2.2.2\HdrHistogram-2.2.2.jar;D:\maven-repository\org\latencyutils\LatencyUtils\2.0.3\LatencyUtils-2.0.3.jar;D:\maven-repository\io\micrometer\context-propagation\1.1.3\context-propagation-1.1.3.jar;D:\maven-repository\org\springframework\ai\spring-ai-template-st\1.1.0-SNAPSHOT\spring-ai-template-st-1.1.0-20250613.193905-86.jar;D:\maven-repository\org\antlr\ST4\4.3.4\ST4-4.3.4.jar;D:\maven-repository\org\antlr\antlr-runtime\3.5.3\antlr-runtime-3.5.3.jar;D:\maven-repository\org\springframework\spring-messaging\6.2.6\spring-messaging-6.2.6.jar;D:\maven-repository\io\projectreactor\reactor-core\3.7.5\reactor-core-3.7.5.jar;D:\maven-repository\org\reactivestreams\reactive-streams\1.0.4\reactive-streams-1.0.4.jar;D:\maven-repository\org\antlr\antlr4-runtime\4.13.1\antlr4-runtime-4.13.1.jar;D:\maven-repository\com\fasterxml\jackson\core\jackson-databind\2.18.3\jackson-databind-2.18.3.jar;D:\maven-repository\com\fasterxml\jackson\core\jackson-annotations\2.18.3\jackson-annotations-2.18.3.jar;D:\maven-repository\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.18.3\jackson-datatype-jsr310-2.18.3.jar;D:\maven-repository\com\github\victools\jsonschema-module-swagger-2\4.37.0\jsonschema-module-swagger-2-4.37.0.jar;D:\maven-repository\io\swagger\core\v3\swagger-annotations\2.2.25\swagger-annotations-2.2.25.jar;D:\maven-repository\org\springframework\ai\spring-ai-retry\1.1.0-SNAPSHOT\spring-ai-retry-1.1.0-20250613.193905-86.jar;D:\maven-repository\org\springframework\retry\spring-retry\2.0.11\spring-retry-2.0.11.jar;D:\maven-repository\org\springframework\spring-web\6.2.6\spring-web-6.2.6.jar;D:\maven-repository\com\github\victools\jsonschema-generator\4.37.0\jsonschema-generator-4.37.0.jar;D:\maven-repository\com\fasterxml\classmate\1.7.0\classmate-1.7.0.jar;D:\maven-repository\com\fasterxml\jackson\core\jackson-core\2.18.3\jackson-core-2.18.3.jar;D:\maven-repository\com\github\victools\jsonschema-module-jackson\4.37.0\jsonschema-module-jackson-4.37.0.jar;D:\maven-repository\org\springframework\spring-context-support\6.2.6\spring-context-support-6.2.6.jar;D:\maven-repository\org\springframework\spring-webflux\6.2.6\spring-webflux-6.2.6.jar;D:\maven-repository\org\slf4j\slf4j-api\2.0.17\slf4j-api-2.0.17.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-model-chat-client\1.1.0-SNAPSHOT\spring-ai-autoconfigure-model-chat-client-1.1.0-20250613.142119-53.jar;D:\maven-repository\org\springframework\ai\spring-ai-client-chat\1.1.0-SNAPSHOT\spring-ai-client-chat-1.1.0-20250613.193905-86.jar;D:\maven-repository\com\fasterxml\jackson\module\jackson-module-jsonSchema\2.18.3\jackson-module-jsonSchema-2.18.3.jar;D:\maven-repository\javax\validation\validation-api\1.1.0.Final\validation-api-1.1.0.Final.jar;D:\maven-repository\com\knuddels\jtokkit\1.1.0\jtokkit-1.1.0.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-model-chat-memory\1.1.0-SNAPSHOT\spring-ai-autoconfigure-model-chat-memory-1.1.0-20250613.142119-47.jar;D:\maven-repository\org\springframework\ai\spring-ai-starter-mcp-client\1.1.0-SNAPSHOT\spring-ai-starter-mcp-client-1.1.0-20250613.142119-44.jar;D:\maven-repository\org\springframework\ai\spring-ai-autoconfigure-mcp-client\1.1.0-SNAPSHOT\spring-ai-autoconfigure-mcp-client-1.1.0-20250613.142119-45.jar;D:\maven-repository\org\springframework\ai\spring-ai-mcp\1.1.0-SNAPSHOT\spring-ai-mcp-1.1.0-20250613.142119-45.jar;D:\maven-repository\io\modelcontextprotocol\sdk\mcp\0.10.0\mcp-0.10.0.jar;D:\Program Files\JetBrains\IntelliJ IDEA 2024.3.5\lib\idea_rt.jar" spring.ai.mcp.sqlite.Application
Connected to the target VM, address: '127.0.0.1:65039', transport: 'socket'

.   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/

:: Spring Boot ::                (v3.4.5)

2025-06-15T15:05:21.031+08:00  INFO 11876 --- [mcp] [           main] spring.ai.mcp.sqlite.Application         : Starting Application using Java 17.0.12 with PID 11876 (D:\github\spring-ai-cookbook\spring-ai-mcp-cookbook\spring-ai-mcp-sqlite\target\classes started by Administrator in D:\github\spring-ai-cookbook)
2025-06-15T15:05:21.036+08:00  INFO 11876 --- [mcp] [           main] spring.ai.mcp.sqlite.Application         : No active profile set, falling back to 1 default profile: "default"
2025-06-15T15:05:21.640+08:00  INFO 11876 --- [mcp] [           main] f.a.AutowiredAnnotationBeanPostProcessor : Autowired annotation is not supported on static fields: private static java.lang.String spring.ai.mcp.sqlite.Application.db_path
2025-06-15T15:05:23.203+08:00  INFO 11876 --- [mcp] [pool-2-thread-1] i.m.client.McpAsyncClient                : Server response with Protocol: 2024-11-05, Capabilities: ServerCapabilities[completions=null, experimental={}, logging=null, prompts=PromptCapabilities[listChanged=false], resources=ResourceCapabilities[subscribe=false, listChanged=false], tools=ToolCapabilities[listChanged=false]], Info: Implementation[name=sqlite, version=0.1.0] and Instructions null
MCP Initialized: InitializeResult[protocolVersion=2024-11-05, capabilities=ServerCapabilities[completions=null, experimental={}, logging=null, prompts=PromptCapabilities[listChanged=false], resources=ResourceCapabilities[subscribe=false, listChanged=false], tools=ToolCapabilities[listChanged=false]], serverInfo=Implementation[name=sqlite, version=0.1.0], instructions=null]
2025-06-15T15:05:23.342+08:00  INFO 11876 --- [mcp] [           main] spring.ai.mcp.sqlite.Application         : Started Application in 2.754 seconds (process running for 3.652)
Running predefined questions with AI model responses:

QUESTION: Can you connect to my SQLite database and tell me what products are available, and their prices?
ASSISTANT: It appears that there are no tables in your SQLite database. As a result, I cannot retrieve any product information or prices. If you need assistance with creating a table or inserting data, please let me know!

QUESTION: What's the average price of all products in the database?
2025-06-15T15:05:33.222+08:00  INFO 11876 --- [mcp] [pool-5-thread-1] i.m.c.transport.StdioClientTransport     : STDERR Message received: Database error executing query: no such table: products
ASSISTANT: It appears that there are no tables in the database, which is why I couldn't find the "products" table. Would you like to create a new table or perform another action?

QUESTION: Can you analyze the price distribution and suggest any pricing optimizations?
ASSISTANT: It appears that there are no tables available in the database, which means I cannot access any pricing data for analysis.

To proceed, we would need to create a table to store pricing information or check if there are any other sources from which I can obtain this data. Would you like to create a new table for pricing data? If so, please provide the structure (columns) you'd like for the table.

QUESTION: Could you help me design and create a new table for storing customer orders?
ASSISTANT: Sure! To design a table for storing customer orders, we need to consider what information we want to store. A typical customer orders table might include the following columns:

1. **OrderID**: A unique identifier for each order (Primary Key).
2. **CustomerID**: A unique identifier for each customer (Foreign Key).
3. **OrderDate**: The date the order was placed.
4. **TotalAmount**: The total amount for the order.
5. **OrderStatus**: The current status of the order (e.g., Pending, Shipped, Delivered, Cancelled).
6. **ShippingAddress**: The address where the order will be shipped.
7. **PaymentMethod**: The method used for payment (e.g., Credit Card, PayPal).

Based on this structure, the SQL statement to create the table would look like this:

......

Would you like to proceed with creating this table, or do you want to modify the design?

Predefined questions completed. Exiting application.
2025-06-15T15:05:55.557+08:00  WARN 11876 --- [mcp] [     parallel-4] i.m.c.transport.StdioClientTransport     : Process terminated with code 1
2025-06-15T15:05:55.557+08:00  WARN 11876 --- [mcp] [     parallel-5] i.m.c.transport.StdioClientTransport     : Process terminated with code 1

```

## 技术架构
Spring AI 与 MCP 的集成遵循一个简单的组件链：

MCP Client 为您的数据库提供基本通信层
函数回调将数据库作公开为 AI 可调用的函数
Chat Client 将这些函数连接到 AI 模型
bean 定义如下所述，从ChatClient

### 聊天客户端
```java
@Bean
@Profile("!chat")
public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
                                             List<McpFunctionCallback> functionCallbacks,
                                             ConfigurableApplicationContext context) {
   return args -> {
      var chatClient = chatClientBuilder.defaultFunctions(functionCallbacks.toArray(new McpFunctionCallback[0]))
              .build();
      // Run Predefined Questions
      System.out.println(chatClient.prompt(
              "Can you connect to my SQLite database and tell me what products are available, and their prices?").call().content());
      // ...
   };
}
```

聊天客户端设置非常简单 - 它只需要从 MCP 工具自动创建的函数回调。Spring 的依赖注入处理所有连接，使集成无缝。


### 函数回调

该应用程序使用函数回调向 Spring AI 注册 MCP 工具：

```java
@Bean
public List<McpFunctionCallback> functionCallbacks(McpSyncClient mcpClient) {
    return mcpClient.listTools(null)
            .tools()
            .stream()
            .map(tool -> new McpFunctionCallback(mcpClient, tool))
            .toList();
}
```

#### 目的

此 bean 负责：
从客户端发现可用的 MCP 工具
将每个工具转换为 Spring AI 函数回调
使这些回调可用于 ChatClient

#### 如何运作
mcpClient.listTools(null)查询 MCP 服务器以获取所有可用工具
   该参数表示分页游标null
   当 null 时，返回结果的第一页
   可以提供游标字符串以获取该位置之后的结果
.tools()从响应中提取工具列表
每个工具都转化为一个使用McpFunctionCallback.map()
这些回调使用.toArray(McpFunctionCallback[]::new)

#### 用法
注册的回调使 ChatClient 能够：

在对话期间访问 MCP 工具
处理 AI 模型请求的函数调用
针对 MCP 服务器执行工具（例如 SQLite 数据库）

### MCP 客户端

应用程序使用同步 MCP 客户端与 SQLite 数据库通信：
```java
@Bean(destroyMethod = "close")
public McpSyncClient mcpClient() {
    var stdioParams = ServerParameters.builder("uvx")
            .args("mcp-server-sqlite", "--db-path", getDbPath())
            .build();

    var mcpClient = McpClient.sync(new StdioServerTransport(stdioParams),
            Duration.ofSeconds(10), new ObjectMapper());

    var init = mcpClient.initialize();
    System.out.println("MCP Initialized: " + init);

    return mcpClient;
}
```
说明：
创建与 MCP 服务器通信的基于 stdio 的传输层uvx
指定 SQLite 作为后端数据库及其位置
为 10 秒的作设置 10 秒的超时
使用 Jackson 进行 JSON 序列化
初始化与 MCP 服务器的连接
该批注可确保在应用程序关闭时进行适当的清理。destroyMethod = "close"
