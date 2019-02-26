package com.lenovoexample.tracingvf.Objects;

public class Usuarios {

    String name;
    String adress;
    String email;
    Double lat;
    Double lng;

    public Usuarios() {
    }

    public Usuarios(String name, String adress, String email, Double lat, Double lng) {
        this.name = name;
        this.adress = adress;
        this.email = email;
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
