package com.musafi.ai_app;

public class MyLocation {
   private int top;
   private int bottom;
   private int left;
   private int right;

    public MyLocation() {
    }

    public MyLocation(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public MyLocation setTop(int top) {
        this.top = top;
        return this;
    }

    public int getBottom() {
        return bottom;
    }

    public MyLocation setBottom(int bottom) {
        this.bottom = bottom;
        return this;
    }

    public int getLeft() {
        return left;
    }

    public MyLocation setLeft(int left) {
        this.left = left;
        return this;
    }

    public int getRight() {
        return right;
    }

    public MyLocation setRight(int right) {
        this.right = right;
        return this;
    }
}
