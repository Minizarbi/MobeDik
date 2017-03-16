package m2dl.com.mobedik.m2dl.com.mobedik.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Building {
    public Building() {

    }

    private HashMap<String, Integer> freq;
    private double lat;
    private double lng;
    private String name;

    public HashMap<String, Integer> getFreq() { return freq; }

    public void set(HashMap<String, Integer> freq) { this.freq = freq; }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}