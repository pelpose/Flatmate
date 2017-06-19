package com.example.pelpo.flatee;

/**
 * Created by Jin on 2017-06-19.
 */

public class UserInformation {

    public String fName;
    public String lName;
    public String dob;
    public String phone;
    public String roomNum;
    public String address;

    public UserInformation(String fName, String lName, String dob, String phone, String address, String roomNum) {
        this.fName = fName;
        this.lName = lName;
        this.dob = dob;
        this.phone = phone;
        this.roomNum = roomNum;
        this.address = address;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfName() {
        return fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getlName() {
        return lName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
