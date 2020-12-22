package cs.collaboration.yescredit.binding;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class AddressAdapter {

    @BindingAdapter({"sti_bgc:address_visibility"})
    public static void setVisibility(TextView view, String value) {
        if (value != null) {
            view.setVisibility(!value.equals("primary") ? View.GONE : View.VISIBLE);
        }
    }

    @BindingAdapter({"sti_bgc:address_visibility"})
    public static void setVisibility(ImageView view, String value) {
        if (value != null) {
            view.setVisibility(!value.equals("selected") ? View.GONE : View.VISIBLE);
        }
    }

    @BindingAdapter({"sti_bgc:address_status"})
    public static void setStatus(TextView view, String value) {
        if (value != null) {
            if (value.equals("primary")) {
                view.setText("Primary Address");
            }
        }
    }


}