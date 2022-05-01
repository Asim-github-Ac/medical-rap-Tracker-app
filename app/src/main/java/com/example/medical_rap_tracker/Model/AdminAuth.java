package com.example.medical_rap_tracker.Model;

public class AdminAuth {
    String fullnamre,type,email;

    public AdminAuth(String fullnamre, String type,String email) {
        this.fullnamre = fullnamre;
        this.type = type;
        this.email=email;
    }

    public AdminAuth() {
    }

    public String getFullnamre() {
        return fullnamre;
    }

    public void setFullnamre(String fullnamre) {
        this.fullnamre = fullnamre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
