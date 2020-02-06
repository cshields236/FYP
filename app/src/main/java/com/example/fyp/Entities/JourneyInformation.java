package com.example.fyp.Entities;

public class JourneyInformation {
    private String name;
    private String time;
    private double leftEye;
    private double rightEye;



    public JourneyInformation() {
    }


    public JourneyInformation(String name, String time, double leftEye, double rightEye) {
        this.name = name;
        this.time = time;
        this.leftEye = leftEye;
        this.rightEye = rightEye;
    }

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

    @Override
    public String toString() {
        return "JourneyInfo{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", leftEye=" + leftEye +
                ", rightEye=" + rightEye +
                '}';
    }
}
