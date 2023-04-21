package com.sneproj.chameleon.model;

public class FavModal {
    String favname;

    public FavModal(String favname) {
        this.favname = favname;
    }

    public FavModal() {
    }

    public String getFavname() {
        return favname;
    }

    public void setFavname(String favname) {
        this.favname = favname;
    }
}
