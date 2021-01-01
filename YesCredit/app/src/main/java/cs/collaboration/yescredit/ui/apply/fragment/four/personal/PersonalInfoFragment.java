package cs.collaboration.yescredit.ui.apply.fragment.four.personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPersonalInfoBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.dialog.DatePickerFragment;
import cs.collaboration.yescredit.ui.apply.fragment.validation.ViewTextWatcher;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class PersonalInfoFragment extends DaggerFragment implements DatePickerFragment.OnDatePickerListener {

    private static final String TAG = "PersonalInfoFragment";

    @Override
    public void onProcessDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);
        Log.d(TAG, "processDatePickerResult: " + dateMessage);
        binding.fragmentPersonalInfoBirthDate.setText(dateMessage);
        selectedUserDateOfBirth = dateMessage;
    }

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentPersonalInfoBinding binding;
    private PersonalInfoViewModel viewModel;
    private UserForm userForm;
    private Hostable hostable;

    private String selectedUserGender;
    private String selectedUserDateOfBirth = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalInfoBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(PersonalInfoViewModel.class);
        subscribeObservers();
        initialization();
        navigation();
        return binding.getRoot();
    }

    private void initialization() {
        List<EditText> views = new ArrayList<>();
        views.add(binding.fragmentPersonalInfoLastName);
        views.add(binding.fragmentPersonalInfoFirstName);
        views.add(binding.fragmentPersonalInfoMiddleName);
        views.add(binding.fragmentPersonalInfoBirthDate);

        ViewTextWatcher viewTextWatcher = new ViewTextWatcher(views, binding.fragmentPersonalInfoUpdate, "one");
        binding.fragmentPersonalInfoLastName.addTextChangedListener(viewTextWatcher);
        binding.fragmentPersonalInfoFirstName.addTextChangedListener(viewTextWatcher);
        binding.fragmentPersonalInfoMiddleName.addTextChangedListener(viewTextWatcher);
        binding.fragmentPersonalInfoBirthDate.addTextChangedListener(viewTextWatcher);
    }


    private void subscribeObservers() {

        viewModel.observedUserForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                PersonalInfoFragment.this.userForm = form;
                binding.fragmentPersonalInfoLastName.setText(form.getLast_name());
                binding.fragmentPersonalInfoFirstName.setText(form.getFirst_name());
                binding.fragmentPersonalInfoMiddleName.setText(form.getMiddle_name());
                if (!form.getGender().isEmpty()) {
                    switch (form.getGender().toLowerCase()) {
                        case "male":
                            binding.fragmentPersonalInfoGenderMale.setChecked(true);
                            break;
                        case "female":
                            binding.fragmentPersonalInfoGenderFemale.setChecked(true);
                            break;
                        default:
                    }
                }
                binding.fragmentPersonalInfoBirthDate.setText(form.getDate_of_birth());
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void navigation() {

        //reference: https://stackoverflow.com/questions/3554377/handling-click-events-on-a-drawable-within-an-edittext
        binding.fragmentPersonalInfoBirthDate.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.fragmentPersonalInfoBirthDate.getRight() - binding.fragmentPersonalInfoBirthDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    DatePickerFragment dialog = new DatePickerFragment();
                    dialog.setOnDatePickerListener(PersonalInfoFragment.this);
                    dialog.show(requireActivity().getSupportFragmentManager(), getString(R.string.tag_dialog_fragment_date_picker));
                    return true;
                }
            }
            return false;
        });

        binding.fragmentPersonalInfoGenderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selected = group.getCheckedRadioButtonId();
            RadioButton button = binding.getRoot().findViewById(selected);
            selectedUserGender = button.getTag().toString();
        });
        binding.fragmentPersonalInfoUpdate.setOnClickListener(v -> {
            enlistUserInformation();
            requireActivity().onBackPressed();
        });

    }

    private void enlistUserInformation() {

        String last_name = binding.fragmentPersonalInfoLastName.getText().toString();
        String first_name = binding.fragmentPersonalInfoFirstName.getText().toString();
        String middle_name = binding.fragmentPersonalInfoMiddleName.getText().toString();

        userForm.setLast_name(last_name);
        userForm.setFirst_name(first_name);
        userForm.setMiddle_name(middle_name);
        userForm.setGender(selectedUserGender);
        userForm.setDate_of_birth(!selectedUserDateOfBirth.isEmpty() ? selectedUserDateOfBirth : binding.fragmentPersonalInfoBirthDate.getText().toString());

        hostable.onEnlist(userForm);
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