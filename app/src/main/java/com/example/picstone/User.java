package com.example.picstone;

import android.location.Location;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class User {
    private LatLng location;
    private String username;
    private String token;

    private Uri DEBUG_SAMPLE_MARKER_PICTURE = null; //TODO: Remove this

    private static User user = null;


    private User(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static User getInstance() {
        if (user == null){
            user = new User();
        }
        return user;
    }

    public Uri getDEBUG_SAMPLE_MARKER_PICTURE() {
        return DEBUG_SAMPLE_MARKER_PICTURE;
    }

    public void setDEBUG_SAMPLE_MARKER_PICTURE(Uri DEBUG_SAMPLE_MARKER_PICTURE) {
        this.DEBUG_SAMPLE_MARKER_PICTURE = DEBUG_SAMPLE_MARKER_PICTURE;
    }
}
