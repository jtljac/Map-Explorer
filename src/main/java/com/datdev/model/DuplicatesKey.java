package com.datdev.model;

import java.io.Serializable;
import java.util.Objects;

public class DuplicatesKey implements Serializable {
    int map1;

    int map2;

    public DuplicatesKey(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DuplicatesKey that = (DuplicatesKey) o;
        return map1 == that.map1 && map2 == that.map2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(map1, map2);
    }
}
