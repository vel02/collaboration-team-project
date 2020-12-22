package cs.collaboration.yescredit.model;

public class Address {

    private String user_id;
    private String address_id;
    private String address_street;
    private String address_barangay;
    private String address_city;
    private String address_zipcode;
    private String address_province;
    private String address_status;//primary or not-primary
    private String address_selected;//selected or not-selected

    public Address(String user_id, String address_id, String address_street,
                   String address_barangay, String address_city, String address_zipcode,
                   String address_province, String address_status, String address_selected) {
        this.user_id = user_id;
        this.address_id = address_id;
        this.address_street = address_street;
        this.address_barangay = address_barangay;
        this.address_city = address_city;
        this.address_zipcode = address_zipcode;
        this.address_province = address_province;
        this.address_status = address_status;
        this.address_selected = address_selected;
    }

    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "user_id='" + user_id + '\'' +
                ", address_id='" + address_id + '\'' +
                ", address_street='" + address_street + '\'' +
                ", address_barangay='" + address_barangay + '\'' +
                ", address_city='" + address_city + '\'' +
                ", address_zipcode='" + address_zipcode + '\'' +
                ", address_province='" + address_province + '\'' +
                ", address_status='" + address_status + '\'' +
                ", address_selected='" + address_selected + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public String getAddress_barangay() {
        return address_barangay;
    }

    public void setAddress_barangay(String address_barangay) {
        this.address_barangay = address_barangay;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_zipcode() {
        return address_zipcode;
    }

    public void setAddress_zipcode(String address_zipcode) {
        this.address_zipcode = address_zipcode;
    }

    public String getAddress_province() {
        return address_province;
    }

    public void setAddress_province(String address_province) {
        this.address_province = address_province;
    }

    public String getAddress_status() {
        return address_status;
    }

    public void setAddress_status(String address_status) {
        this.address_status = address_status;
    }

    public String getAddress_selected() {
        return address_selected;
    }

    public void setAddress_selected(String address_selected) {
        this.address_selected = address_selected;
    }
}
