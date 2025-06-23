package com.spring.ai.deepseek.vo;


/**
 * 封装模型返回的结构化结果，包含答案和推理过程
 */
public class ReasoningResult {

    public ReasoningResult(String answer, String reasoning) {
        this.answer = answer;
        this.reasoning = reasoning;
    }

    /**
     * 最终答案
     */
    private String answer;

    /**
     * 推理过程（Chain of Thought）
     */
    private String reasoning;

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
