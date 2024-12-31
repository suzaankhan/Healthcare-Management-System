package com.sem7project.sehatmitr.utils;

import java.util.List;

public class HealthRecord{

    private String hospitalName;
    private  String hospitalAddress;
    private String admissionDate;
    private String dischargeDate;
    private List<String> medicines;
    private List<String> diseases;
    private String wardType;

    public HealthRecord(String hospitalName, String hospitalAddress,
                        String admissionDate, String dischargeDate,
                        List<String> medicines, List<String> diseases, String wardType) {
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.medicines = medicines;
        this.diseases = diseases;
        this.wardType = wardType;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public List<String> getMedicines() {
        return medicines;
    }

    public List<String> getDiseases() {
        return diseases;
    }

    public String getWardType() {
        return wardType;
    }

}
