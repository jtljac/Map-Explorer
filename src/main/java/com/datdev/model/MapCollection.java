package com.datdev.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collection")
public class MapCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="name")
    String name;

    @Column(name="creator")
    String creator;

    @Column(name="description")
    String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mapCollection", joinColumns = {@JoinColumn(name = "collectionID")}, inverseJoinColumns = {@JoinColumn(name = "mapID")})
    List<Map> maps = new ArrayList<>();

    public MapCollection() {
    }

    public MapCollection(String name, String creator, String description) {
        this.name = name;
        this.creator = creator;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Map> getMaps() {
        return maps;
    }

    public void setMaps(List<Map> maps) {
        this.maps = maps;
    }
}
