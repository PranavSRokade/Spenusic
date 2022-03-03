package com.example.spenusic;

import java.io.Serializable;

public class Music implements Serializable {
    public String name, path;

    public Music() {
    }

    public Music(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
