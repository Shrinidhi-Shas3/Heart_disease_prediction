package com.heart.prediction.admin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String Id, Name, Gender, Age, Address, Mobile, Email;
    //Name,Address, Mobile, Email, Age, Gender,
    public UserModel(String id, String name, String address, String mobile, String email, String age, String gender) {
        this.Id = id;
        this.Name = name;
        this.Gender = gender;
        this.Age = age;
        this.Address = address;
        this.Mobile = mobile;
        this.Email = email;
    }

    protected UserModel(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Gender = in.readString();
        Age = in.readString();
        Address = in.readString();
        Mobile = in.readString();
        Email = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(Gender);
        dest.writeString(Age);
        dest.writeString(Address);
        dest.writeString(Mobile);
        dest.writeString(Email);
    }
}
