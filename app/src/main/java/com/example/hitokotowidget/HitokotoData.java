package com.example.hitokotowidget;

/**
 * 一句话数据模型
 */
public class HitokotoData {
    private String hitokoto;  // 一句话内容
    private String from;      // 来源/作者

    public HitokotoData() {
    }

    public HitokotoData(String hitokoto, String from) {
        this.hitokoto = hitokoto;
        this.from = from;
    }

    public String getHitokoto() {
        return hitokoto;
    }

    public void setHitokoto(String hitokoto) {
        this.hitokoto = hitokoto;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "HitokotoData{" +
                "hitokoto='" + hitokoto + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
