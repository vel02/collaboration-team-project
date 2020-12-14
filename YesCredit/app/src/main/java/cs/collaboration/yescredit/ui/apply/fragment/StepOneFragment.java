package cs.collaboration.yescredit.ui.apply.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import java.util.Objects;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepOneBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.dialog.DatePickerFragment;
import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;
import dagger.android.support.DaggerFragment;

public class StepOneFragment extends DaggerFragment {

    private static final String TAG = "StepOneFragment";

    private FragmentStepOneBinding binding;

    @Inject
    SessionManager sessionManager;

    private Hostable hostable;

    private String gender;
    private String dateOfBirth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepOneBinding.inflate(inflater);
        Log.d(TAG, "onCreateView: started.");
        navigation();
        getUserInfo();
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void navigation() {
        binding.fragmentOneBirthDate.setOnTouchListener(new View.OnTouchListener() {
            //reference: https://stackoverflow.com/questions/3554377/handling-click-events-on-a-drawable-within-an-edittext
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.fragmentOneBirthDate.getRight() - binding.fragmentOneBirthDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Log.d(TAG, "onTouch: na touch ako sobra!");
                        DialogFragment dialog = new DatePickerFragment();
                        dialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), getString(R.string.tag_dialog_fragment_date_picker));
                        return true;
                    }
                }
                return false;
            }
        });

        binding.fragmentOneGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selected = group.getCheckedRadioButtonId();

                RadioButton button = binding.getRoot().findViewById(selected);
                gender = button.getTag().toString();
                Log.d(TAG, "onCheckedChanged: gender: " + gender);
            }
        });

        binding.fragmentOneNext.setOnClickListener(view -> {

            //fill up user form ....
            hostable.onFillUp(userInfo());

            //call other form ....
            hostable.onInflate(getString(R.string.tag_fragment_step_two));
        });
    }

    private void getUserInfo() {
        Log.d(TAG, "getUserInfo: started");
//        sessionManager.observeApplicationForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeApplicationForm().observe(getViewLifecycleOwner(), new Observer<ApplicationForm>() {
            @Override
            public void onChanged(ApplicationForm form) {
                Log.d(TAG, "onChanged: started with form: ");
                if (form != null) {
                    Log.d(TAG, "onChanged: started");
                    binding.fragmentOneLastName.setText(form.getLast_name());
                    binding.fragmentOneFirstName.setText(form.getFirst_name());
                    binding.fragmentOneMiddleName.setText(form.getMiddle_name());
                    switch (form.getGender().toLowerCase()) {
                        case "male":
                            binding.fragmentOneGenderMale.setChecked(true);
                            break;
                        case "female":
                            binding.fragmentOneGenderFemale.setChecked(true);
                            break;
                        default:
                    }
                    binding.fragmentOneBirthDate.setText(form.getDate_of_birth() != null ? form.getDate_of_birth() : getString(R.string.date_format_label));
                } else Log.d(TAG, "onChanged: is null");
            }
        });
    }

    private ApplicationForm userInfo() {

        ApplicationForm info = new ApplicationForm();
        String last_name = binding.fragmentOneLastName.getText().toString();
        String first_name = binding.fragmentOneFirstName.getText().toString();
        String middle_name = binding.fragmentOneMiddleName.getText().toString();

        info.setLast_name(last_name);
        info.setFirst_name(first_name);
        info.setMiddle_name(middle_name);
        info.setGender(gender);
        info.setDate_of_birth(dateOfBirth);

        return info;
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);
        binding.fragmentOneBirthDate.setText(dateMessage);
        dateOfBirth = dateMessage;
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

    @Override
    public void onResume() {
        super.onResume();
    }
}