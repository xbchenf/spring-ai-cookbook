package com.spring.ai.deepseek;


/**
 * 封装推理结果，包含最终答案和 CoT（Chain of Thought）内容
 */
public class ReasoningResult {
    /**
     * 模型生成的答案
     */
    private String answer;

    /**
     * 推理过程（CoT）
     */
    private String reasoning;

    public ReasoningResult(String answer, String reasoning) {
        this.answer = answer;
        this.reasoning = reasoning;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
}
