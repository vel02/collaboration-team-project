package cs.collaboration.yescredit.ui.account.fragment.information.card;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import cs.collaboration.yescredit.databinding.FragmentAddCardBinding;
import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.util.CodeCardFormatter;
import cs.collaboration.yescredit.util.CreditCardFormatter;
import cs.collaboration.yescredit.util.ExpirationCardFormatter;
import dagger.android.support.DaggerFragment;

public class AddCardFragment extends DaggerFragment {

    private static final String TAG = "AddCardFragment";

    private FragmentAddCardBinding binding;

    private DatabaseReference reference;
    private Hostable hostable;
    private Activity activity;

    private Address address;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCardBinding.inflate(inflater);
        initialization();
        navigation();
        return binding.getRoot();
    }

    private void initialization() {
        reference = FirebaseDatabase.getInstance().getReference();
    }


    private void getUserSelectedAddress() {

        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        if (current != null) {

            Query query = reference.child(getString(R.string.database_node_address))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(current.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Address address = singleShot.getValue(Address.class);
                        assert address != null;
                        if (address.getAddress_selected().equals("selected")) {
                            binding.fragmentAddCardBillingAddress.setText(addressFormatter(address));
                            AddCardFragment.this.address = address;
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

    private String addressFormatter(Address address) {
        return address.getAddress_street() + ", " + address.getAddress_barangay()
                + "\n" + address.getAddress_city() + "\n" + address.getAddress_province()
                + "\n" + address.getAddress_zipcode() + " " + address.getAddress_province().toUpperCase();
    }

    private void navigation() {

        CreditCardFormatter creditCardFormatter = new CreditCardFormatter(getResources(), " ", 5);
        creditCardFormatter.setViews(binding.fragmentAddCardNumber, binding.fragmentAddCardNext, binding.fragmentAddCardImage);

        ExpirationCardFormatter expirationCardFormatter = new ExpirationCardFormatter("/", 3);
        expirationCardFormatter.setView(this, binding.fragmentAddCardSelectedRoot, binding.fragmentAddCardCode, binding.fragmentAddCardAdd);

        CodeCardFormatter codeCardFormatter = new CodeCardFormatter();
        codeCardFormatter.setViews(this, binding.fragmentAddCardSelectedRoot, binding.fragmentAddCardAdd, binding.fragmentAddCardExpiration);

        binding.fragmentAddCardNumber.addTextChangedListener(creditCardFormatter);
        binding.fragmentAddCardExpiration.addTextChangedListener(expirationCardFormatter);
        binding.fragmentAddCardCode.addTextChangedListener(codeCardFormatter);


        binding.fragmentAddCardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFourDigitNumber();
                //hide to left card number
                binding.fragmentAddCardNumber.animate()
                        .translationX(-300)
                        .alpha(0.0f)
                        .setDuration(200)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                setViewGone(binding.fragmentAddCardNumber);
                                setViewVisible(binding.fragmentAddCardRelativeRoot);
                                binding.fragmentAddCardRelativeRoot.setAlpha(1.0f);
                                binding.fragmentAddCardRelativeRoot.setTranslationX(0);

                                if (binding.fragmentAddCardCode.getText().toString().isEmpty()) {
                                    binding.fragmentAddCardExpiration.requestFocus();
                                } else {
                                    int length = binding.fragmentAddCardCode.getText().length();
                                    binding.fragmentAddCardCode.requestFocus();
                                    binding.fragmentAddCardCode.setSelection(length);
                                    if (length == 4) {
                                        setViewVisible(binding.fragmentAddCardAdd);
                                        setViewVisible(binding.fragmentAddCardSelectedRoot);
                                    }
                                }

                                setDescription();
                            }
                        });

                binding.fragmentAddCardNumber.clearAnimation();
                setViewGone(binding.fragmentAddCardNext);
            }
        });

        binding.fragmentAddCardFourNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide to right
                binding.fragmentAddCardRelativeRoot.animate()
                        .translationX(300)
                        .alpha(0.0f)
                        .setDuration(200)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                setViewGone(binding.fragmentAddCardRelativeRoot);
                                setViewGone(binding.fragmentAddCardSelectedRoot);
                                setViewGone(binding.fragmentAddCardAdd);
                                setViewVisible(binding.fragmentAddCardNumber);
                                binding.fragmentAddCardNumber.setAlpha(1.0f);
                                binding.fragmentAddCardNumber.setTranslationX(0);
                                binding.fragmentAddCardNumber.requestFocus();
                                setDescription();
                            }
                        });

                binding.fragmentAddCardRelativeRoot.clearAnimation();
                setViewVisible(binding.fragmentAddCardNext);
            }
        });

        binding.fragmentAddCardSelectAddress.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_billing_address));
        });

        binding.fragmentAddCardAdd.setOnClickListener(v -> {
            //create card
        });

    }

    private void setFourDigitNumber() {
        String number = binding.fragmentAddCardNumber.getText().toString();
        if (!number.isEmpty())
            binding.fragmentAddCardFourNumber.setText(number.substring(number.length() - 4));
    }

    private void setDescription() {
        if (binding.fragmentAddCardNumber.hasFocus()) {
            binding.fragmentAddCardText.setText(getString(R.string.desc_let_s_start_with_your_card_number));
        } else if (binding.fragmentAddCardExpiration.hasFocus()) {
            binding.fragmentAddCardText.setText(getString(R.string.desc_now_enter_your_expiration_date));
        } else if (binding.fragmentAddCardCode.hasFocus()) {
            binding.fragmentAddCardText.setText(R.string.desc_enter_card_security_code);
        }
    }

    private void setViewVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void setViewGone(View view) {
        view.setVisibility(View.GONE);
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
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.white));

        if (!binding.fragmentAddCardCode.getText().toString().isEmpty()) {
            setViewGone(binding.fragmentAddCardNumber);
            setViewVisible(binding.fragmentAddCardRelativeRoot);
            int length = binding.fragmentAddCardCode.getText().length();
            binding.fragmentAddCardCode.requestFocus();
            binding.fragmentAddCardCode.setSelection(length);
        }

        //focus listener
        binding.fragmentAddCardConstraintRoot.getViewTreeObserver()
                .addOnGlobalFocusChangeListener(
                        (oldFocus, newFocus) -> {
                            setDescription();
                        });

        setFourDigitNumber();
        getUserSelectedAddress();
    }
}