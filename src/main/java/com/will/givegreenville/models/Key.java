package com.will.givegreenville.models;

public class Key {
    private final String GEO_KEY = System.getenv("geokey");

    public String getGEO_KEY() {
        return GEO_KEY;
    }

    @Override
    public String toString() {
        return GEO_KEY;
    }
}
