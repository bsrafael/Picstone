package com.example.picstone;

import com.google.android.gms.maps.model.LatLng;

public class User {
    private LatLng location = null;
    private String username = null;
    private String email = null;
    private String password = null;

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

    public static User getInstance() {
        if (user == null){
            user = new User();
        }
        return user;
    }
}
