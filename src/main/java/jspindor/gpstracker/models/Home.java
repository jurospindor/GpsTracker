package jspindor.gpstracker.models;

import com.google.android.gms.maps.model.LatLng;

public class Home {
    private String userName;
    private String homeName;
    private LatLng location;
    private long radius;

    public Home(){}

    public Home(String userName, String homeName, LatLng location, long radius) {
        this.userName = userName;
        this.homeName = homeName;
        this.location = location;
        this.radius = radius;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public long getRadius() {
        return radius;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }
}
