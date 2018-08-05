package jspindor.gpstracker;

public class Data {
    private int id;
    private double longitude;
    private double latitude;
    private String date;

    public Data(){}

    public Data(int id, double longitude, double latitude, String date) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
