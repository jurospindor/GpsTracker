package jspindor.gpstracker;

import java.util.List;

public class Datalist {
    private List<Data> data;

    public Datalist(){}

    public Datalist(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
