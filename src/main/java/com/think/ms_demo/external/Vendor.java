package com.think.ms_demo.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vendor {

    @JsonProperty("vendorId")
    private Long vendorId;
    @JsonProperty("vendorName")
    private String vendorName;
    @JsonProperty("vendorAddress")
    private String vendorAddress;
    @JsonProperty("vendorPhoneNumber")
    private String vendorPhoneNumber;
    @JsonProperty("vendorAge")
    private String vendorAge;


    public Long getVendorId() {
        return vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    public String getVendorName() {
        return vendorName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public String getVendorAddress() {
        return vendorAddress;
    }
    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }
    public String getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }
    public void setVendorPhoneNumber(String vendorPhoneNumber) {
        this.vendorPhoneNumber = vendorPhoneNumber;
    }
    public String getVendorAge() {
        return vendorAge;
    }
    public void setVendorAge(String vendorAge) {
        this.vendorAge = vendorAge;
    }

}
