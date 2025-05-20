package com.spring.ai.openai.model;

public class Poem {
    private String author;
    private String year;
    private String theme;
    private String context;

    public Poem(String author, String year, String theme, String context) {
        this.author = author;
        this.year = year;
        this.theme = theme;
        this.context = context;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
