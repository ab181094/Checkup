package com.csecu.amrit.checkup.models;

/**
 * Created by Amrit on 2/21/2018.
 */

public class Message {
    String reg, date, string;

    public Message(String reg, String date, String string) {
        this.reg = reg;
        this.date = date;
        this.string = string;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
