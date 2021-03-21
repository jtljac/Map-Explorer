package com.datdev.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "collection")
public class MapCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="name")
    String name;

    @Column(name="description")
    String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mapCollection", joinColumns = {@JoinColumn(name = "collectionID")}, inverseJoinColumns = {@JoinColumn(name = "mapID")})
    List<Map> maps;
}
