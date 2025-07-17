
curl -fsSL https://ollama.com/install.sh | sh

ollama serve



# API请求测试一：

http://localhost:8080/ollama/chat?message=你好

请求响应：
```text
好的，用户发来“你好”，我需要友好回应。首先，要保持亲切自然，用中文回应。可以简单问候，比如“你好！有什么我可以帮你的吗？”这样既礼貌又开放，鼓励用户进一步交流。同时要注意语气友好，避免过于正式或生硬。另外，保持简洁，不要冗长，让用户容易理解和回应。确认没有其他隐藏需求，直接回应即可。 你好！有什么我可以帮你的吗？😊

```




# API请求测试二：

http://localhost:8080/ollama/chatMode?message=请自我介绍一下

请求响应：
```text
你好！我是 LLaMA，一个由 Meta 开发的大型语言模型。我的主要功能是理解和生成人类语言，我可以用多种方式与用户交互，如回答问题、提供信息、创作文本等。我不断学习和改进，以便更好地服务于用户。
```




# API请求测试三：

http://localhost:8080/ollama/chatClient?message=是谁创造了你？

请求响应：
```text
我是由元认知技术有限公司开发的语言模型。
```
