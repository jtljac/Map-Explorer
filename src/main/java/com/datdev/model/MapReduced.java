package com.datdev.model;

import java.sql.Timestamp;

public class MapReduced {
    int id;

    String filePath;
    String thumbnail;

    int width;
    int height;

    Integer squareWidth;
    Integer squareHeight;

    String author;
    String uploader;

    Timestamp uploadDate;

    String imageHash;

    public MapReduced(Map map) {
        this.id = map.getId();
        this.filePath = map.getFilePath();
        this.thumbnail = map.getThumbnailPath();
        this.width = map.getWidth();
        this.height = map.getHeight();
        this.squareWidth = map.getSquareWidth();
        this.squareHeight = map.getSquareHeight();
        this.author = map.getAuthor();
        this.uploader = map.getUploader();
        this.uploadDate = map.getUploadDate();
        this.imageHash = map.getImageHash();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Integer getSquareWidth() {
        return squareWidth;
    }

    public void setSquareWidth(Integer squareWidth) {
        this.squareWidth = squareWidth;
    }

    public Integer getSquareHeight() {
        return squareHeight;
    }

    public void setSquareHeight(Integer squareHeight) {
        this.squareHeight = squareHeight;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getImageHash() {
        return imageHash;
    }

    public void setImageHash(String imageHash) {
        this.imageHash = imageHash;
    }
}
