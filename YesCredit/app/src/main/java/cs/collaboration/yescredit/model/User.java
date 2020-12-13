package cs.collaboration.yescredit.model;

public class User {

    private String user_id;
    private String last_name;
    private String first_name;
    private String middle_name;
    private String gender;
    private String date_of_birth;
    private String street_address;
    private String barangay_address;
    private String city_address;
    private String province_address;
    private String postal_address;
    private String government_image;
    private String profile_image;

    public User() {
    }

    public User(String user_id, String last_name, String first_name, String middle_name,
                String gender, String date_of_birth, String street_address, String barangay_address,
                String city_address, String province_address, String postal_address,
                String government_image, String profile_image) {
        this.user_id = user_id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.street_address = street_address;
        this.barangay_address = barangay_address;
        this.city_address = city_address;
        this.province_address = province_address;
        this.postal_address = postal_address;
        this.government_image = government_image;
        this.profile_image = profile_image;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", gender='" + gender + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", street_address='" + street_address + '\'' +
                ", barangay_address='" + barangay_address + '\'' +
                ", city_address='" + city_address + '\'' +
                ", province_address='" + province_address + '\'' +
                ", postal_address='" + postal_address + '\'' +
                ", government_image='" + government_image + '\'' +
                ", profile_image='" + profile_image + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getBarangay_address() {
        return barangay_address;
    }

    public void setBarangay_address(String barangay_address) {
        this.barangay_address = barangay_address;
    }

    public String getCity_address() {
        return city_address;
    }

    public void setCity_address(String city_address) {
        this.city_address = city_address;
    }

    public String getProvince_address() {
        return province_address;
    }

    public void setProvince_address(String province_address) {
        this.province_address = province_address;
    }

    public String getPostal_address() {
        return postal_address;
    }

    public void setPostal_address(String postal_address) {
        this.postal_address = postal_address;
    }

    public String getGovernment_image() {
        return government_image;
    }

    public void setGovernment_image(String government_image) {
        this.government_image = government_image;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
