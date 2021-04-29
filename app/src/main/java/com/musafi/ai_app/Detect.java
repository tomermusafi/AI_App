package com.musafi.ai_app;

public class Detect {
    private String label;
    private float score;
    private MyLocation location;

    public Detect() {
    }

    public Detect(String label, float score, MyLocation location) {
        this.label = label;
        this.score = score;
        this.location = location;
    }

    public String getLabel() {
        return label;
    }

    public Detect setLabel(String label) {
        this.label = label;
        return this;
    }

    public float getScore() {
        return score;
    }

    public Detect setScore(float score) {
        this.score = score;
        return this;
    }

    public MyLocation getLocation() {
        return location;
    }

    public Detect setLocation(MyLocation location) {
        this.location = location;
        return this;
    }
}
