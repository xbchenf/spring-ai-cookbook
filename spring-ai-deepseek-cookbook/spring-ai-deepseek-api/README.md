

http://localhost:8080/deepSeekApi/ai/generate?message=你好？

```json
{"id":"0058ff3e-1907-457a-a036-676e739dc4bc","choices":[{"finish_reason":"stop","index":0,"message":{"content":"你好！\uD83D\uDE0A 很高兴见到你～有什么我可以帮你的吗？","role":"assistant"}}],"created":1750140976,"model":"deepseek-chat","system_fingerprint":"fp_8802369eaa_prod0425fp8","object":"chat.completion","usage":{"completion_tokens":15,"prompt_tokens":4,"total_tokens":19,"prompt_tokens_details":{"cached_tokens":0}}}

```
![img1.png](img1.png)

http://localhost:8080/deepSeekApi/ai/generate?message=你是谁？
```json
{"id":"3cf02e9b-a560-4065-ada4-f7030812ee40","choices":[{"finish_reason":"stop","index":0,"message":{"content":"我是DeepSeek Chat，由深度求索公司（DeepSeek）创造的智能AI助手！✨ 我的使命是帮助你解答问题、提供信息、陪你聊天，甚至帮你处理各种文本、文件等任务。无论是学习、工作，还是日常生活中的疑问，都可以来问我哦！\uD83D\uDE0A  \n\n有什么我可以帮你的吗？","role":"assistant"}}],"created":1750141034,"model":"deepseek-chat","system_fingerprint":"fp_8802369eaa_prod0425fp8","object":"chat.completion","usage":{"completion_tokens":70,"prompt_tokens":5,"total_tokens":75,"prompt_tokens_details":{"cached_tokens":0}}}
```
![img2.png](img2.png)

http://localhost:8080/deepSeekApi/ai/generateStream?message=你会唱歌么？

![img3.png](img3.png)