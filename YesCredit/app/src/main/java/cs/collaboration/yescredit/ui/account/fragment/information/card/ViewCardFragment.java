package cs.collaboration.yescredit.ui.account.fragment.information.card;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentViewCardBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.account.model.Card;
import dagger.android.support.DaggerFragment;


public class ViewCardFragment extends DaggerFragment {

    private static final String TAG = "ViewCardFragment";

    private FragmentViewCardBinding binding;
    private DatabaseReference reference;
    private Hostable hostable;
    private Activity activity;
    private Card card;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewCardBinding.inflate(inflater);
        reference = FirebaseDatabase.getInstance().getReference();
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void initialization() {
        ImageLoader.getInstance().displayImage(card.getImage(), binding.fragmentViewCardImage);
        setFourDigitNumber();
        setNameWithNumber();
        setExpirationDate();
        binding.fragmentViewCardAddress.setText(card.getBill_address());
    }

    private void setToolbarTitle(Toolbar toolbar) {
        String number = card.getNumber();
        if (!number.isEmpty()) {
            number = "Credit card ●●●●" + number.substring(number.length() - 4);
            toolbar.setTitle(number);
        }
    }

    private void setFourDigitNumber() {
        String number = card.getNumber();
        if (!number.isEmpty()) {
            number = "●●●●" + number.substring(number.length() - 4);
            binding.fragmentViewCardNumber.setText(number);
        }
    }

    private void setNameWithNumber() {
        String number = card.getNumber();
        String name = card.getName();
        if (!number.isEmpty()) {
            name = name + " Credit card ●●●●" + number.substring(number.length() - 4);
            binding.fragmentViewCardName.setText(name);
        }
    }

    private void setExpirationDate() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Query query = reference.child(getString(R.string.database_node_cards))
                    .orderByChild("card_id").equalTo(card.getId());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        cs.collaboration.yescredit.model.Card current = singleShot.getValue(cs.collaboration.yescredit.model.Card.class);
                        assert current != null;
                        binding.fragmentViewCardExpiration.setText(current.getCard_expiration());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void deleteUserCard() {

        if (!card.getStatus().equals("primary")) {
            reference.child(getString(R.string.database_node_cards))
                    .child(card.getId()).removeValue();
            requireActivity().onBackPressed();
        } else {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                Query query = reference.child(getString(R.string.database_node_cards))
                        .orderByChild(getString(R.string.database_field_user_id_underscore))
                        .equalTo(user.getUid());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot singleShot : snapshot.getChildren()) {

                            cs.collaboration.yescredit.model.Card current = singleShot.getValue(cs.collaboration.yescredit.model.Card.class);
                            assert current != null;
                            if (current.getCard_status().equals("not-primary")) {
                                reference.child(getString(R.string.database_node_cards))
                                        .child(current.getCard_id())
                                        .child(getString(R.string.database_field_card_status))
                                        .setValue("primary");

                                reference.child(getString(R.string.database_node_cards))
                                        .child(current.getCard_id())
                                        .child(getString(R.string.database_field_card_selected))
                                        .setValue("selected");

                                reference.child(getString(R.string.database_node_cards))
                                        .child(card.getId()).removeValue();
                                requireActivity().onBackPressed();
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
    }

    private void editUserCard() {
        NavDirections action = ViewCardFragmentDirections.actionViewCardFragmentToEditCardFragment(card);
        Navigation.findNavController(ViewCardFragment.this.getView()).navigate(action);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.menu_card, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_edit) {
            editUserCard();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            deleteUserCard();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        card = ViewCardFragmentArgs.fromBundle(getArguments()).getCard();
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
        setToolbarTitle(toolbar);
        initialization();
    }
}