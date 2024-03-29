package com.datdev.model.uploads;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class MapUploader {
    public MultipartFile image;

    public String name;

    public String author;

    public Integer squareWidth;

    public Integer squareHeight;

    public List<String> tags = new ArrayList<>();

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
