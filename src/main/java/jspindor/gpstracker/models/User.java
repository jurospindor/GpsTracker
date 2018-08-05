package jspindor.gpstracker.models;

import com.google.android.gms.maps.model.LatLng;

public class User {
    private String id;
    private String name;
    private boolean notifications;
    private LatLng location;

    public User() {
    }

    public User(String id, String name, boolean notifications, LatLng location) {
        this.id = id;
        this.name = name;
        this.notifications = notifications;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
