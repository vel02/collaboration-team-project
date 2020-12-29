package cs.collaboration.yescredit.ui.apply.model;

import androidx.annotation.NonNull;

public class UserForm {
    private String last_name;
    private String first_name;
    private String middle_name;
    private String gender;
    private String date_of_birth;
    private String government_id;
    private String street_address;
    private String barangay_address;
    private String city_address;
    private String province_address;
    private String postal_address;

    public UserForm(String last_name, String first_name, String middle_name, String gender,
                    String date_of_birth, String government_id, String street_address,
                    String barangay_address, String city_address, String province_address,
                    String postal_address) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.government_id = government_id;
        this.street_address = street_address;
        this.barangay_address = barangay_address;
        this.city_address = city_address;
        this.province_address = province_address;
        this.postal_address = postal_address;
    }

    public UserForm() {
    }

    private String checkNullValue(String value) {
        return (value != null) ? value : "";
    }

    @NonNull
    @Override
    public String toString() {
        return "ApplicationForm{" +
                "last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", gender='" + gender + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", government_id='" + government_id + '\'' +
                ", street_address='" + street_address + '\'' +
                ", barangay_address='" + barangay_address + '\'' +
                ", city_address='" + city_address + '\'' +
                ", province_address='" + province_address + '\'' +
                ", postal_address='" + postal_address + '\'' +
                '}';
    }

    public String getLast_name() {
        return checkNullValue(last_name);
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return checkNullValue(first_name);
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return checkNullValue(middle_name);
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getGender() {
        return checkNullValue(gender);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return checkNullValue(date_of_birth);
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGovernment_id() {
        return checkNullValue(government_id);
    }

    public void setGovernment_id(String government_id) {
        this.government_id = government_id;
    }

    public String getStreet_address() {
        return checkNullValue(street_address);
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getBarangay_address() {
        return checkNullValue(barangay_address);
    }

    public void setBarangay_address(String barangay_address) {
        this.barangay_address = barangay_address;
    }

    public String getCity_address() {
        return checkNullValue(city_address);
    }

    public void setCity_address(String city_address) {
        this.city_address = city_address;
    }

    public String getProvince_address() {
        return checkNullValue(province_address);
    }

    public void setProvince_address(String province_address) {
        this.province_address = province_address;
    }

    public String getPostal_address() {
        return checkNullValue(postal_address);
    }

    public void setPostal_address(String postal_address) {
        this.postal_address = postal_address;
    }
}
