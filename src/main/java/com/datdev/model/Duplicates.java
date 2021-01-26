package com.datdev.model;

import javax.persistence.*;

@Entity(name="duplicates")
@IdClass(DuplicatesKey.class)
public class Duplicates {
    @Id
    @ManyToOne()
    @JoinColumn(name = "map1")
    Map map1;

    @Id()
    @ManyToOne()
    @JoinColumn(name = "map2")
    Map map2;

    @Column(name = "pct")
    float pct;

    public Map getMap1() {
        return map1;
    }

    public void setMap1(Map map1) {
        this.map1 = map1;
    }

    public Map getMap2() {
        return map2;
    }

    public void setMap2(Map map2) {
        this.map2 = map2;
    }

    public float getPct() {
        return pct;
    }

    public void setPct(float pct) {
        this.pct = pct;
    }
}
