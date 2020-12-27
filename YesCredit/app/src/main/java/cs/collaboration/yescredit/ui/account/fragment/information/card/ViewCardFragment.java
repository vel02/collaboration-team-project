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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        setHasOptionsMenu(true);
        initialization();
        return binding.getRoot();
    }

    private void initialization() {
        reference = FirebaseDatabase.getInstance().getReference();
        ImageLoader.getInstance().displayImage(card.getImage(), binding.fragmentViewCardImage);
        setFourDigitNumber();
        setNameWithNumber();
        binding.fragmentViewCardExpiration.setText(card.getExp_date());
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

    private void deleteUserCard() {
        reference.child(getString(R.string.database_node_cards))
                .child(card.getId()).removeValue();
        requireActivity().onBackPressed();
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
    }
}