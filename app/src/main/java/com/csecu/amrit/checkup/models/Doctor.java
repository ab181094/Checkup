package com.csecu.amrit.checkup.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable {
    String name, phone, password, qualification, speciality, chamber, patient, fee, sex, day, start;
    String end, image, registration, token;

    public Doctor() {
    }

    public Doctor(String name, String phone, String password, String qualification,
                  String speciality, String chamber, String patient, String fee, String sex,
                  String day, String start, String end, String image, String registration,
                  String token) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.qualification = qualification;
        this.speciality = speciality;
        this.chamber = chamber;
        this.patient = patient;
        this.fee = fee;
        this.sex = sex;
        this.day = day;
        this.start = start;
        this.end = end;
        this.image = image;
        this.registration = registration;
        this.token = token;
    }

    protected Doctor(Parcel in) {
        name = in.readString();
        phone = in.readString();
        password = in.readString();
        qualification = in.readString();
        speciality = in.readString();
        chamber = in.readString();
        patient = in.readString();
        fee = in.readString();
        sex = in.readString();
        day = in.readString();
        start = in.readString();
        end = in.readString();
        image = in.readString();
        registration = in.readString();
        token = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(password);
        parcel.writeString(qualification);
        parcel.writeString(speciality);
        parcel.writeString(chamber);
        parcel.writeString(patient);
        parcel.writeString(fee);
        parcel.writeString(sex);
        parcel.writeString(day);
        parcel.writeString(start);
        parcel.writeString(end);
        parcel.writeString(image);
        parcel.writeString(registration);
        parcel.writeString(token);
    }
}
