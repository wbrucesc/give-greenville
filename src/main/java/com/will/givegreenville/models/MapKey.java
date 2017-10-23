package com.will.givegreenville.models;

public class MapKey {
    private final String MAP_KEY = System.getenv("mapkey");

    public String getMAP_KEY() {
        return MAP_KEY;
    }

    @Override
    public String toString() {
        return MAP_KEY;
    }
}
