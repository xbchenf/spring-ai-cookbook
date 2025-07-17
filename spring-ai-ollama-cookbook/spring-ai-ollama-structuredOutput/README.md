
# List输出响应

## 测试提问：
```
http://localhost:8080/ollama/list?message=水果
```

## 模型响应
```json
[
  "李白",
  "杜甫",
  "王维",
  "岑参",
  "白行简"
]
```



# map输出响应

## 测试提问：
```
http://localhost:8080/ollama/map?message=李白
```

## 模型响应
```json
{
  "年份": "742-762",
  "author": "李白",
  "朝代": "唐代",
  "标题": "静夜思",
  "内容": "床前明月光，疑是地上霜。举头望银汉，有情人终不见。\n"
}
```


# bean输出响应

## 测试提问：
```
http://localhost:8080/ollama/bean?message=李小龙
```

## 模型响应
```json
{
  "actor": "李小龙",
  "movies": [
    "精武门",
    " Fist of Fury",
    "死亡罗汉"
  ]
}
```
