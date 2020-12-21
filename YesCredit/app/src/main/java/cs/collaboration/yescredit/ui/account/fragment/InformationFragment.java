package cs.collaboration.yescredit.ui.account.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentInformationBinding;
import cs.collaboration.yescredit.model.User;
import cs.collaboration.yescredit.ui.account.Hostable;
import dagger.android.support.DaggerFragment;

public class InformationFragment extends DaggerFragment {

    private static final String TAG = "PersonalInformationFrag";

    private FragmentInformationBinding binding;
    private Activity activity;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentInformationBinding.inflate(inflater);
        getUserInfo();
        navigation();
        return binding.getRoot();
    }

    private void getUserInfo() {
        Log.d(TAG, "getUserInfo: started");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            Query query = reference.child(getString(R.string.database_node_users))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(currentUser.getUid());

            Log.d(TAG, "getUserInfo: id " + currentUser.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: shot " + snapshot.getChildren());
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        User user = singleShot.getValue(User.class);
                        Log.d(TAG, "onDataChange: user " + user);
                        if (user != null) {
                            Log.d(TAG, "onDataChange: image " + user.getProfile_image());
                            ImageLoader.getInstance().displayImage(user.getProfile_image(), binding.fragmentInformationImage);
                            binding.fragmentInformationEmail.setText(emailFormatter(Objects.requireNonNull(currentUser.getEmail())));
                            binding.fragmentInformationName.setText(nameFormatter(user.getFirst_name(), user.getLast_name()));
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private String nameFormatter(String first_name, String last_name) {
        return "@" + first_name + " " + last_name;
    }

    private String emailFormatter(String email) {
        return email.substring(0, email.indexOf("@"));
    }

    private void navigation() {

        binding.fragmentInformationOut.setOnClickListener(v -> hostable.onLogout());

        binding.fragmentInformationInfo.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_personal));
        });

        binding.fragmentInformationCards.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_card));
        });

        binding.fragmentInformationPreference.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_preference));
        });

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