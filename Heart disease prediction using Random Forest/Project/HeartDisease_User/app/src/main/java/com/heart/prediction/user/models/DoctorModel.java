
package com.heart.prediction.user.models;


public class DoctorModel {
    private String did, name, address, mobile, email, age, gender, specialization;

    public DoctorModel(String did, String name, String address, String mobile, String email, String age
            , String gender, String specialization) {
        this.did = did;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.specialization = specialization;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}


