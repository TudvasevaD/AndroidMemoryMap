package ru.sai.darya.memorycalendar.model;

import com.google.android.gms.maps.model.LatLng;

public class Memory {
    private String data;
    private String name;
    private String comment;
    private String photoPath;
    private LatLng coord;

    public Memory(String data, String name, String comment, String photoPath, LatLng coord) {
        this.data = data;
        this.name = name;
        this.comment = comment;
        this.photoPath = photoPath;
        this.coord = coord;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNameEvent() {
        return name;
    }

    public void setNameEvent(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public LatLng getCoord() {
        return coord;
    }

    public void setCoord(LatLng coord) {
        this.coord = coord;
    }
}
