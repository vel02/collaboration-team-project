package cs.collaboration.yescredit.ui.apply.fragment.one;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepOneBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.dialog.DatePickerFragment;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class StepOneFragment extends DaggerFragment implements DatePickerFragment.OnDatePickerListener {

    private static final String TAG = "StepOneFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentStepOneBinding binding;
    private StepOneViewModel viewModel;
    private UserForm userForm;
    private Hostable hostable;

    private String selectedUserGender;
    private String selectedUserDateOfBirth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepOneBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(StepOneViewModel.class);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void navigation() {
        //reference: https://stackoverflow.com/questions/3554377/handling-click-events-on-a-drawable-within-an-edittext
        binding.fragmentOneBirthDate.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.fragmentOneBirthDate.getRight() - binding.fragmentOneBirthDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    DatePickerFragment dialog = new DatePickerFragment();
                    dialog.setOnDatePickerListener(StepOneFragment.this);
                    dialog.show(requireActivity().getSupportFragmentManager(), getString(R.string.tag_dialog_fragment_date_picker));
                    return true;
                }
            }
            return false;
        });

        binding.fragmentOneGenderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selected = group.getCheckedRadioButtonId();
            RadioButton button = binding.getRoot().findViewById(selected);
            selectedUserGender = button.getTag().toString();
        });

        binding.fragmentOneNext.setOnClickListener(view -> {
            hostable.onEnlist(enlistUserInformation());
            hostable.onInflate(view, getString(R.string.tag_fragment_step_two));
        });
    }

    private void subscribeObservers() {

        viewModel.observedUserForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                StepOneFragment.this.userForm = form;
                binding.fragmentOneLastName.setText(form.getLast_name());
                binding.fragmentOneFirstName.setText(form.getFirst_name());
                binding.fragmentOneMiddleName.setText(form.getMiddle_name());
                if (form.getGender() != null) {
                    switch (form.getGender().toLowerCase()) {
                        case "male":
                            binding.fragmentOneGenderMale.setChecked(true);
                            break;
                        case "female":
                            binding.fragmentOneGenderFemale.setChecked(true);
                            break;
                        default:
                    }
                } else binding.fragmentOneGenderMale.setChecked(true);
                binding.fragmentOneBirthDate.setText(form.getDate_of_birth() != null ? form.getDate_of_birth() : "");
            }
        });

    }

    private UserForm enlistUserInformation() {

        String last_name = binding.fragmentOneLastName.getText().toString();
        String first_name = binding.fragmentOneFirstName.getText().toString();
        String middle_name = binding.fragmentOneMiddleName.getText().toString();

        userForm.setLast_name(last_name);
        userForm.setFirst_name(first_name);
        userForm.setMiddle_name(middle_name);
        userForm.setGender(selectedUserGender);
        userForm.setDate_of_birth(selectedUserDateOfBirth);

        return userForm;
    }

    @Override
    public void onProcessDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);
        binding.fragmentOneBirthDate.setText(dateMessage);
        selectedUserDateOfBirth = dateMessage;
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