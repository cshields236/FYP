package com.example.fyp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Journey implements Parcelable {
    private List<FaceInformation> faceInformationsses = new ArrayList<FaceInformation>();
    private String time;
    private String id;


    public Journey() {

    }


    public Journey(List<FaceInformation> faceInformationsses) {
        this.faceInformationsses = faceInformationsses;
    }
    public Journey(List<FaceInformation> faceInformationsses, String time) {
        this.faceInformationsses = faceInformationsses;
        this.time = time;
    }

    public Journey(String id) {

        this.id = id;
    }

    protected Journey(Parcel in) {
        faceInformationsses = in.createTypedArrayList(FaceInformation.CREATOR);
        time = in.readString();
        id = in.readString();
    }

    public static final Creator<Journey> CREATOR = new Creator<Journey>() {
        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey(in);
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };

    public List<FaceInformation> getFaceInformationsses() {
        return faceInformationsses;
    }

    public void setFaceInformationsses(List<FaceInformation> faceInformationsses) {
        this.faceInformationsses = faceInformationsses;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "faceInformationsses=" + faceInformationsses +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(faceInformationsses);
        dest.writeString(time);
        dest.writeString(id);
    }
}
