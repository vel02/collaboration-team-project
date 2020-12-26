package cs.collaboration.yescredit.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Card implements Parcelable {

    private String user_id;
    private String card_id;
    private String card_name;//optional
    private String card_code;
    private String card_number;
    private String card_expiration;
    private String card_street;
    private String card_barangay;
    private String card_city;
    private String card_zipcode;
    private String card_province;
    private String card_image;//optional
    private String card_status;//primary or not-primary
    private String card_selected;//selected or not-selected

    public Card(String user_id, String card_id, String card_name, String card_code, String card_number,
                String card_expiration, String card_street, String card_barangay,
                String card_city, String card_zipcode, String card_province, String card_image,
                String card_status, String card_selected) {
        this.user_id = user_id;
        this.card_id = card_id;
        this.card_name = card_name;
        this.card_code = card_code;
        this.card_number = card_number;
        this.card_expiration = card_expiration;
        this.card_street = card_street;
        this.card_barangay = card_barangay;
        this.card_city = card_city;
        this.card_zipcode = card_zipcode;
        this.card_province = card_province;
        this.card_image = card_image;
        this.card_status = card_status;
        this.card_selected = card_selected;
    }

    public Card() {
    }

    protected Card(Parcel in) {
        user_id = in.readString();
        card_id = in.readString();
        card_name = in.readString();
        card_code = in.readString();
        card_number = in.readString();
        card_expiration = in.readString();
        card_street = in.readString();
        card_barangay = in.readString();
        card_city = in.readString();
        card_zipcode = in.readString();
        card_province = in.readString();
        card_image = in.readString();
        card_status = in.readString();
        card_selected = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(card_id);
        dest.writeString(card_name);
        dest.writeString(card_code);
        dest.writeString(card_number);
        dest.writeString(card_expiration);
        dest.writeString(card_street);
        dest.writeString(card_barangay);
        dest.writeString(card_city);
        dest.writeString(card_zipcode);
        dest.writeString(card_province);
        dest.writeString(card_image);
        dest.writeString(card_status);
        dest.writeString(card_selected);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "Card{" +
                "user_id='" + user_id + '\'' +
                ", card_id='" + card_id + '\'' +
                ", card_name='" + card_name + '\'' +
                ", card_code='" + card_code + '\'' +
                ", card_number='" + card_number + '\'' +
                ", card_expiration='" + card_expiration + '\'' +
                ", card_street='" + card_street + '\'' +
                ", card_barangay='" + card_barangay + '\'' +
                ", card_city='" + card_city + '\'' +
                ", card_zipcode='" + card_zipcode + '\'' +
                ", card_province='" + card_province + '\'' +
                ", card_image='" + card_image + '\'' +
                ", card_status='" + card_status + '\'' +
                ", card_selected='" + card_selected + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_expiration() {
        return card_expiration;
    }

    public void setCard_expiration(String card_expiration) {
        this.card_expiration = card_expiration;
    }

    public String getCard_street() {
        return card_street;
    }

    public void setCard_street(String card_street) {
        this.card_street = card_street;
    }

    public String getCard_barangay() {
        return card_barangay;
    }

    public void setCard_barangay(String card_barangay) {
        this.card_barangay = card_barangay;
    }

    public String getCard_city() {
        return card_city;
    }

    public void setCard_city(String card_city) {
        this.card_city = card_city;
    }

    public String getCard_zipcode() {
        return card_zipcode;
    }

    public void setCard_zipcode(String card_zipcode) {
        this.card_zipcode = card_zipcode;
    }

    public String getCard_province() {
        return card_province;
    }

    public void setCard_province(String card_province) {
        this.card_province = card_province;
    }

    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getCard_status() {
        return card_status;
    }

    public void setCard_status(String card_status) {
        this.card_status = card_status;
    }

    public String getCard_selected() {
        return card_selected;
    }

    public void setCard_selected(String card_selected) {
        this.card_selected = card_selected;
    }
}
