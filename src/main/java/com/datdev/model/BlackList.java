package com.datdev.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "blacklist")
public class BlackList {
    @Id
    @Column(name="imagehash")
    String imageHash;

    @Column(name="banDate", nullable = false, updatable = false, insertable = false)
    @CreationTimestamp
    Timestamp banDate;

    public BlackList() {

    }

    public BlackList(String imageHash) {
        this.imageHash = imageHash;
    }

    public String getImageHash() {
        return imageHash;
    }

    public void setImageHash(String imageHash) {
        this.imageHash = imageHash;
    }

    public Timestamp getBanDate() {
        return banDate;
    }

    public void setBanDate(Timestamp banDate) {
        this.banDate = banDate;
    }
}
