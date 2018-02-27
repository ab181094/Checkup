package com.csecu.amrit.checkup.models;

/**
 * Created by Amrit on 2/20/2018.
 */

public class Icon {
    int image;
    String name;

    public Icon(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public Icon() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
