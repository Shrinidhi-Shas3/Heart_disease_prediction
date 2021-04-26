package com.heart.prediction.admin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainingModel implements Parcelable {
    private String tid, diseaseName, age, gender, chestPain, bloodSugar, restECG, exang, slope, ca, thal
            , bloodPressure, cholesterol, thalach, oldPeak;

    public TrainingModel(String tid, String diseaseName, String age, String gender, String chestPain
            , String bloodSugar, String restECG, String exang, String slope, String ca
            , String thal, String bloodPressure, String cholesterol, String thalach, String oldPeak) {
        this.tid = tid;
        this.diseaseName = diseaseName;
        this.age = age;
        this.gender = gender;
        this.chestPain = chestPain;
        this.bloodSugar = bloodSugar;
        this.restECG = restECG;
        this.exang = exang;
        this.slope = slope;
        this.ca = ca;
        this.thal = thal;
        this.bloodPressure = bloodPressure;
        this.cholesterol = cholesterol;
        this.thalach = thalach;
        this.oldPeak = oldPeak;
    }

    protected TrainingModel(Parcel in) {
        tid = in.readString();
        diseaseName = in.readString();
        age = in.readString();
        gender = in.readString();
        chestPain = in.readString();
        bloodSugar = in.readString();
        restECG = in.readString();
        exang = in.readString();
        slope = in.readString();
        ca = in.readString();
        thal = in.readString();
        bloodPressure = in.readString();
        cholesterol = in.readString();
        thalach = in.readString();
        oldPeak = in.readString();
    }

    public static final Creator<TrainingModel> CREATOR = new Creator<TrainingModel>() {
        @Override
        public TrainingModel createFromParcel(Parcel in) {
            return new TrainingModel(in);
        }

        @Override
        public TrainingModel[] newArray(int size) {
            return new TrainingModel[size];
        }
    };

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getChestPain() {
        return chestPain;
    }

    public void setChestPain(String chestPain) {
        this.chestPain = chestPain;
    }

    public String getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(String bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public String getRestECG() {
        return restECG;
    }

    public void setRestECG(String restECG) {
        this.restECG = restECG;
    }

    public String getExang() {
        return exang;
    }

    public void setExang(String exang) {
        this.exang = exang;
    }

    public String getSlope() {
        return slope;
    }

    public void setSlope(String slope) {
        this.slope = slope;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getThal() {
        return thal;
    }

    public void setThal(String thal) {
        this.thal = thal;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getThalach() {
        return thalach;
    }

    public void setThalach(String thalach) {
        this.thalach = thalach;
    }

    public String getOldPeak() {
        return oldPeak;
    }

    public void setOldPeak(String oldPeak) {
        this.oldPeak = oldPeak;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tid);
        dest.writeString(diseaseName);
        dest.writeString(age);
        dest.writeString(gender);
        dest.writeString(chestPain);
        dest.writeString(bloodSugar);
        dest.writeString(restECG);
        dest.writeString(exang);
        dest.writeString(slope);
        dest.writeString(ca);
        dest.writeString(thal);
        dest.writeString(bloodPressure);
        dest.writeString(cholesterol);
        dest.writeString(thalach);
        dest.writeString(oldPeak);
    }
}
