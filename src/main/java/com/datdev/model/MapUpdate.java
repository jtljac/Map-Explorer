package com.datdev.model;

public class MapUpdate {
    Integer squareWidth;

    Integer squareHeight;

    String author;

    String uploader;

    public Integer getSquareWidth() {
        return squareWidth;
    }

    public void setSquareWidth(Integer squareWidth) {
        if (squareWidth != 0) this.squareWidth = squareWidth;
        else this.squareWidth = null;
    }

    public Integer getSquareHeight() {
        return squareHeight;
    }

    public void setSquareHeight(Integer squareHeight) {
        if (squareHeight != 0) this.squareHeight = squareHeight;
        else this.squareHeight = null;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (!author.isEmpty()) this.author = author;
        else this.author = null;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        if (!uploader.isEmpty()) this.uploader = uploader;
        else this.uploader = null;
    }
}
