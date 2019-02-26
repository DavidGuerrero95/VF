package com.lenovoexample.tracingvf.Objects;

public class Supervisores {
    String name;
    String adress;
    String email;
    String cellphone;

    public Supervisores() {
    }

    public Supervisores(String name, String adress, String email, String cellphone) {
        this.name = name;
        this.adress = adress;
        this.email = email;
        this.cellphone = cellphone;
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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
