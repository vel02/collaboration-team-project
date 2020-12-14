package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.FragmentStepFourBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import dagger.android.support.DaggerFragment;


public class StepFourFragment extends DaggerFragment {

    private static final String TAG = "StepFourFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentStepFourBinding binding;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepFourBinding.inflate(inflater);
        getUserInfo();
        navigation();
        return binding.getRoot();
    }

//    private void getUserInfo() {
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        assert user != null;
//
//        Query query = reference.child(getString(R.string.database_node_users))
//                .orderByChild(getString(R.string.database_field_user_id))
//                .equalTo(user.getUid());
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
//                    User single = singleSnapshot.getValue(User.class);
//                    if (single != null) {
//                        String personal_info =
//                                single.getLast_name() + ", " + single.getFirst_name() + " " + single.getMiddle_name()
//                                        + "\n" + single.getGender() + "\n" + single.getDate_of_birth();
//                        String address = single.getStreet_address() + ", " + single.getBarangay_address()
//                                + "\n" + single.getCity_address() + "\n" + single.getProvince_address()
//                                + "\n" + single.getPostal_address();
//                        binding.fragmentFourPersonalInfo.setText(personal_info);
//                        binding.fragmentFourAddress.setText(address);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    private void getUserInfo() {

        sessionManager.observeApplicationForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeApplicationForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                String personal_info =
                        form.getLast_name() + ", " + form.getFirst_name() + " " + form.getMiddle_name()
                                + "\n" + form.getGender() + "\n" + form.getDate_of_birth();
                String address = form.getStreet_address() + ", " + form.getBarangay_address()
                        + "\n" + form.getCity_address() + "\n" + form.getProvince_address()
                        + "\n" + form.getPostal_address() + " " + form.getProvince_address().toUpperCase();
                binding.fragmentFourPersonalInfo.setText(personal_info);
                binding.fragmentFourAddress.setText(address);
            }
        });

    }

    private void navigation() {

        binding.fragmentFourPersonalInfoRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.fragmentFourAddressRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.fragmentFourNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
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
}