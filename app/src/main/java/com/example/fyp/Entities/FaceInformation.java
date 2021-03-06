package com.example.fyp.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class FaceInformation implements Parcelable {
    private String name;
    private String time;
    private double leftEye;
    private double rightEye;
    private int blink;
    private boolean isBlinking;



    public FaceInformation() {
    }


    public FaceInformation(String name, String time, double leftEye, double rightEye, int blink) {
        this.name = name;
        this.time = time;
        this.leftEye = leftEye;
        this.rightEye = rightEye;
        this.blink = blink;
    }

    public FaceInformation(String name, String time, double leftEye, double rightEye) {

        this.name = name;
        this.time = time;
        this.leftEye = leftEye;
        this.rightEye = rightEye;
    }

    protected FaceInformation(Parcel in) {
        name = in.readString();
        time = in.readString();
        leftEye = in.readDouble();
        rightEye = in.readDouble();
    }

    public static final Creator<FaceInformation> CREATOR = new Creator<FaceInformation>() {
        @Override
        public FaceInformation createFromParcel(Parcel in) {
            return new FaceInformation(in);
        }

        @Override
        public FaceInformation[] newArray(int size) {
            return new FaceInformation[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLeftEye() {
        return leftEye;
    }


    public void setLeftEye(double leftEye) {
        this.leftEye = leftEye;
    }

    public double getRightEye() {
        return rightEye;
    }

    public void setRightEye(double rightEye) {
        this.rightEye = rightEye;
    }

    public int getBlink() {
        return blink;
    }
    public boolean isBlinking() {
        return isBlinking;
    }

    public void setBlinking(boolean blinking) {
        isBlinking = blinking;
    }
    public void setBlink(int blink) {
        this.blink = blink;
    }

    @Override
    public String toString() {
        return "JourneyInfo{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", leftEye=" + leftEye +
                ", rightEye=" + rightEye +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(time);
        dest.writeDouble(leftEye);
        dest.writeDouble(rightEye);
    }
}
