package cs.collaboration.yescredit.ui.account.fragment.information;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPaymentPreferenceBinding;
import cs.collaboration.yescredit.model.Card;
import cs.collaboration.yescredit.ui.account.Hostable;
import dagger.android.support.DaggerFragment;

public class PaymentPreferenceFragment extends DaggerFragment {

    private static final String TAG = "PaymentPreferenceFragme";

    private FragmentPaymentPreferenceBinding binding;
    private DatabaseReference reference;
    private Activity activity;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentPreferenceBinding.inflate(inflater);
        initialization();
        return binding.getRoot();
    }

    private void initialization() {
        reference = FirebaseDatabase.getInstance().getReference();
        getUserCard();
    }

    private void getUserCard() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Query query = reference.child(getString(R.string.database_node_cards))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Card current = singleShot.getValue(Card.class);
                        assert current != null;
                        if (current.getCard_status().equals("primary")) {
                            binding.fragmentPaymentPrefCardName.setText(current.getCard_name());
                            setFourDigitNumber(current);
                            return;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void setFourDigitNumber(Card card) {
        String number = card.getCard_number();
        if (!number.isEmpty()) {
            number = "Credit card ●●●●" + number.substring(number.length() - 4);
            binding.fragmentPaymentPrefCardNumber.setText(number);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
        if (!(activity instanceof Hostable)) {
            assert activity != null;
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement Hostable interface.");
        }
        hostable = (Hostable) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostable = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.account_base));
    }

}