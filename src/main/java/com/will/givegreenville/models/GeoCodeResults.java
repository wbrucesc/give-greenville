package com.will.givegreenville.models;

import javax.validation.constraints.Size;
import java.util.List;

public class GeoCodeResults {
    private List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}
