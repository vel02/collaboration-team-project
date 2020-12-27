package cs.collaboration.yescredit.ui.account.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {

    private String id;
    private String name;
    private String number;
    private String exp_date;
    private String bill_address;
    private String image;
    private String status;

    public Card(String id, String name, String number, String exp_date, String bill_address, String image, String status) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.exp_date = exp_date;
        this.bill_address = bill_address;
        this.image = image;
        this.status = status;
    }

    public Card() {
    }

    protected Card(Parcel in) {
        id = in.readString();
        name = in.readString();
        number = in.readString();
        exp_date = in.readString();
        bill_address = in.readString();
        image = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(exp_date);
        dest.writeString(bill_address);
        dest.writeString(image);
        dest.writeString(status);
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

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", exp_date='" + exp_date + '\'' +
                ", bill_address='" + bill_address + '\'' +
                ", image='" + image + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getBill_address() {
        return bill_address;
    }

    public void setBill_address(String bill_address) {
        this.bill_address = bill_address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
