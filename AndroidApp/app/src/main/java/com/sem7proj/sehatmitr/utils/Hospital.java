package com.sem7project.sehatmitr.utils;

public class Hospital {

    private String name;
    private String address;
    private String phoneNumber;

    public Hospital(String name, String address, String phoneNumber){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

}
