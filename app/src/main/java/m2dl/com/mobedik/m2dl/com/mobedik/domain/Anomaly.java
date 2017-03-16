package m2dl.com.mobedik.m2dl.com.mobedik.domain;

import java.util.HashMap;

/**
 * Created by Timothee on 16/03/2017.
 */

public class Anomaly {
    public Anomaly() {

    }

    private String imageId;
    private double lat;
    private double lng;
    private String criticality;

    private HashMap<String, Float> orientation;

    public String getImageId() { return imageId; }

    public void setImageId(String imageId) { this.imageId = imageId; }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }

    public void setLng(double lng) { this.lng = lng; }

    public String getCriticality() { return criticality; }

    public void setCriticality(String criticality) { this.criticality = criticality; }

    public HashMap<String, Float> getOrientation() { return orientation; }

    public void setOrientation(HashMap<String, Float> orientation) { this.orientation = orientation; }
}
