package com.datdev.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity()
@Table(name = "maps")
public class Map {
    public transient final static Pattern pattern = Pattern.compile("^\\w{3,30}$", Pattern.CASE_INSENSITIVE);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="filePath")
    String filePath;

    @Column(name="width")
    int width;
    @Column(name="height")
    int height;

    @Column(name="squareWidth")
    Integer squareWidth;
    @Column(name="squareHeight")
    Integer squareHeight;

    @Column(name="uploader")
    String uploader;

    @Column(name="uploadDate")
    Timestamp uploadDate;

    @Column(name="imageHash")
    String imageHash;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "mapID"))
    @Column(name = "tag")
    List<String> tags = new ArrayList<>();

    public Map(String filePath, int width, int height, Integer squareWidth, Integer squareHeight, String uploader, String imageHash) {
        this.filePath = filePath;
        this.width = width;
        this.height = height;
        this.squareWidth = squareWidth;
        this.squareHeight = squareHeight;
        this.uploader = uploader;
        this.imageHash = imageHash;
    }

    public Map(){}

    public String getName() {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    public String getNameWithoutExtension() {
        int temp = getName().lastIndexOf(".");
        return (temp == -1 ? getName() : getName().substring(0, temp));
    }

    public int getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Integer getSquareWidth() {
        return squareWidth;
    }

    public Integer getSquareHeight() {
        return squareHeight;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean addTag(String tag) {
        Matcher matcher = pattern.matcher(tag);
        if (matcher.find()) {
            tags.add(tag);
            return true;
        }

        return false;
    }

    public String getTagsAsString() {
        if (tags.isEmpty()) return "";

        StringBuilder theString = new StringBuilder();
        for (String tag : tags) {
            theString.append(tag).append(",");
        }
        return theString.toString().substring(0, theString.length()-1);
    }

    @Override
    public String toString() {
        return "Map{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", squareWidth=" + squareWidth +
                ", squareHeight=" + squareHeight +
                ", uploader='" + uploader + '\'' +
                ", uploadDate=" + uploadDate +
                ", imageHash='" + imageHash + '\'' +
                ", tags=" + tags +
                '}';
    }
}
