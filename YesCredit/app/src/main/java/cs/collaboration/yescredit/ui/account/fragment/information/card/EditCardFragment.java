package cs.collaboration.yescredit.ui.account.fragment.information.card;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentEditCardBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.account.model.Card;
import cs.collaboration.yescredit.util.ExpirationCardFormatter;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.hideSoftKeyboard;


public class EditCardFragment extends DaggerFragment {

    private static final String TAG = "EditCardFragment";

    private FragmentEditCardBinding binding;
    private DatabaseReference reference;
    private Hostable hostable;
    private Activity activity;
    private Card card;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditCardBinding.inflate(inflater);
        initialization();
        return binding.getRoot();
    }

    private void initialization() {
        reference = FirebaseDatabase.getInstance().getReference();
        ImageLoader.getInstance().displayImage(card.getImage(), binding.fragmentEditCardImage);
        setFourDigitNumber();
        setExpirationDate();

        ExpirationCardFormatter formatter = new ExpirationCardFormatter("/", 3);
        formatter.setView(EditCardFragment.this, null, binding.fragmentEditCardCode, binding.fragmentEditCardSave);

        binding.fragmentEditCardExpiration.addTextChangedListener(formatter);
        binding.fragmentEditCardCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) return;

                if (!binding.fragmentEditCardCode.getText().toString().isEmpty()
                        && binding.fragmentEditCardCode.getText().toString().length() == 4) {
                    binding.fragmentEditCardSave.setVisibility(View.VISIBLE);
                    hideSoftKeyboard(EditCardFragment.this);
                } else binding.fragmentEditCardSave.setVisibility(View.GONE);

            }
        });

        binding.fragmentEditCardSave.setOnClickListener(v -> {
            saveUserCardInfo();
            NavDirections action = EditCardFragmentDirections.actionEditCardFragmentToCardAccountFragment();
            Navigation.findNavController(EditCardFragment.this.getView()).navigate(action);
        });

    }

    private void saveUserCardInfo() {
        reference.child(getString(R.string.database_node_cards))
                .child(card.getId())
                .child(getString(R.string.database_field_card_expiration))
                .setValue(binding.fragmentEditCardExpiration.getText().toString());

        reference.child(getString(R.string.database_node_cards))
                .child(card.getId())
                .child(getString(R.string.database_field_card_code))
                .setValue(binding.fragmentEditCardCode.getText().toString());
    }

    private void setFourDigitNumber() {
        String number = card.getNumber();
        if (!number.isEmpty()) {
            number = "●●●●" + number.substring(number.length() - 4);
            binding.fragmentEditCardNumber.setText(number);
        }
    }

    private void setExpirationDate() {
        String date = card.getExp_date();
        if (!date.isEmpty()) {
            binding.fragmentEditCardExpiration.setText(date);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
        assert activity != null;
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.account_base));
        if (!(activity instanceof Hostable)) {
            assert activity != null;
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement Hostable interface.");
        }
        hostable = (Hostable) activity;


        card = EditCardFragmentArgs.fromBundle(getArguments()).getCard();
        Log.d(TAG, "onAttach: " + card);
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
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.white));
    }
}