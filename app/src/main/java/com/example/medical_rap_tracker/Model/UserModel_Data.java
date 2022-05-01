package com.example.medical_rap_tracker.Model;

public class UserModel_Data {
    String dr_name,cinicname,special_name,medicne_name,dr_about_name,location,picurl,like_decion,sample_decision;

    public UserModel_Data(String dr_name, String cinicname, String special_name, String medicne_name, String dr_about_name, String location, String picurl, String like_decion, String sample_decision) {
        this.dr_name = dr_name;
        this.cinicname = cinicname;
        this.special_name = special_name;
        this.medicne_name = medicne_name;
        this.dr_about_name = dr_about_name;
        this.location = location;
        this.picurl = picurl;
        this.like_decion = like_decion;
        this.sample_decision = sample_decision;
    }

    public String getDr_name() {
        return dr_name;
    }

    public void setDr_name(String dr_name) {
        this.dr_name = dr_name;
    }

    public String getCinicname() {
        return cinicname;
    }

    public void setCinicname(String cinicname) {
        this.cinicname = cinicname;
    }

    public String getSpecial_name() {
        return special_name;
    }

    public void setSpecial_name(String special_name) {
        this.special_name = special_name;
    }

    public String getMedicne_name() {
        return medicne_name;
    }

    public void setMedicne_name(String medicne_name) {
        this.medicne_name = medicne_name;
    }

    public String getDr_about_name() {
        return dr_about_name;
    }

    public void setDr_about_name(String dr_about_name) {
        this.dr_about_name = dr_about_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getLike_decion() {
        return like_decion;
    }

    public void setLike_decion(String like_decion) {
        this.like_decion = like_decion;
    }

    public String getSample_decision() {
        return sample_decision;
    }

    public void setSample_decision(String sample_decision) {
        this.sample_decision = sample_decision;
    }
}
