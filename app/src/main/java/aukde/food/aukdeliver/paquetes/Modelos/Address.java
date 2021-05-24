package aukde.food.aukdeliver.paquetes.Modelos;

import java.io.Serializable;

public class Address implements Serializable {

    String user_id;
    String name;
    String mobileNumber;

    String address;
    String zipCode;
    String additionalNote;

    String type;
    String otherDetails;
    Double latitude = 0.0;
    Double longitude = 0.0;
    String id;

    public Address(){}

    public Address(String user_id, String name, String mobileNumber, String address, String zipCode,
                   String additionalNote, String type, String otherDetails, Double latitude, Double longitude, String id) {
        this.user_id = user_id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.zipCode = zipCode;
        this.additionalNote = additionalNote;
        this.type = type;
        this.otherDetails = otherDetails;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
