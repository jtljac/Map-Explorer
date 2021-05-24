package com.datdev.model;

import java.net.MalformedURLException;
import java.net.URL;

public class MapFoundry {
    String file_name;
    String file_link;
    String grid_size;

    public MapFoundry(Map theMap, String host) throws MalformedURLException {
        file_name = theMap.getFileName();

        URL url = new URL(host);

        file_link = "http://" + url.getHost() + "/maps/" + theMap.filePath;

        if (theMap.squareWidth != null) {
            grid_size = String.valueOf(theMap.width / theMap.squareWidth);
        } else {
            grid_size = null;
        }
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_link() {
        return file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getGrid_size() {
        return grid_size;
    }

    public void setGrid_size(String grid_size) {
        this.grid_size = grid_size;
    }
}
