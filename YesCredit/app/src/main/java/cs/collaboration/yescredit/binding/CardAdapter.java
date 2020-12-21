package cs.collaboration.yescredit.binding;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import static cs.collaboration.yescredit.util.Keys.PROJECT_PACKAGE;

public class CardAdapter {

    @BindingAdapter({"sti_bgc:cardImage"})
    public static void setCardImage(ImageView view, String path) {
        if (path != null) {
            Uri uri = Uri.parse("android.resource://" + PROJECT_PACKAGE + "/drawable/" + path);
            view.setImageURI(uri);
        }
    }

    @BindingAdapter({"sti_bgc:cardName"})
    public static void setCardName(TextView view, String name) {
        if (name != null) {
            view.setText(name);
        }
    }

    @BindingAdapter({"sti_bgc:cardNumber"})
    public static void setCardNumber(TextView view, String number) {
        if (number != null) {
            number = number.substring(15);
            number = "Credit card ●●●●" + number;
            view.setText(number);
        }
    }


}
