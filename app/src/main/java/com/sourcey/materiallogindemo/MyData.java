package com.sourcey.materiallogindemo;

/**
 * Created by ASUS on 5/22/2017.
 */

public class MyData {
    private int staff;
    private String email,title, description, longitude, latitude, isvalidated ;
    //image_link;

    public MyData(int staff, String title, String description, String longitude, String latitude, String isvalidated) {
//        this.email = email;
        this.staff = staff;
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isvalidated = isvalidated;
//        this.image_link = image_link;
    }

    public int getStaff() {
        return staff;
    }

    public void setStaff(int staff) {
        this.staff = staff;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIsvalidated() {
        return isvalidated;
    }

    public void setIsvalidated(String isvalidated) {
        this.isvalidated = isvalidated;
    }

//    public String getImage_link() {
//        return image_link;
//    }

//    public void setImage_link(String image_link) {
//        this.image_link = image_link;
//    }
}
