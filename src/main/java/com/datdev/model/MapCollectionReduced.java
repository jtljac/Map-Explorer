package com.datdev.model;

public class MapCollectionReduced {
    int id;

    String name;

    String creator;

    String description;

    String firstImage;

    public MapCollectionReduced() {}

    public MapCollectionReduced(MapCollection collection) {
        this.id = collection.getId();
        this.name = collection.getName();
        this.description = collection.getDescription();

        if (!collection.getMaps().isEmpty()) {
            Map map = collection.getMaps().get(0);
            this.firstImage = (map.thumbnail ? map.getThumbnailPath() : map.getFilePath());
        } else {
            firstImage = "";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }
}
