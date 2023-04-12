package com.sneproj.chameleon.model;

import java.io.Serializable;

public class User implements Serializable {
    public String uid, profile, name, uname, bio, number, email, location, nativeLang, gender, rank, countrycode;

    public User() {

    }

    public User(String uid, String profile, String name, String uname, String bio, String number, String email, String location, String nativeLang, String gender, String rank, String countrycode) {
        this.uid = uid;
        this.profile = profile;
        this.name = name;
        this.uname = uname;
        this.bio = bio;
        this.number = number;
        this.email = email;
        this.location = location;
        this.nativeLang = nativeLang;
        this.gender = gender;
        this.rank = rank;
        this.countrycode = countrycode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNativeLang() {
        return nativeLang;
    }

    public void setNativeLang(String nativeLang) {
        this.nativeLang = nativeLang;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
}
