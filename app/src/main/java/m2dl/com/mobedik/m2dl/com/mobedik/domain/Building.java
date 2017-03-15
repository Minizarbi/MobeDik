package m2dl.com.mobedik.m2dl.com.mobedik.domain;

public class Building {
    public Building() {

    }

    private double lat;
    private double lng;
    private String name;

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