package cs.collaboration.yescredit.ui.apply.fragment.four;

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
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPersonalInfoBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.dialog.DatePickerFragment;
import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;
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
        dateOfBirth = dateMessage;
    }

    @Inject
    SessionManager sessionManager;
    private FragmentPersonalInfoBinding binding;
    private Hostable hostable;

    private String gender;
    private String dateOfBirth;
    private String governmentId;
    private String street;
    private String barangay;
    private String city;
    private String province;
    private String postal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalInfoBinding.inflate(inflater);
        getUserInfo();
        navigation();
        return binding.getRoot();
    }

    private void getUserInfo() {
        Log.d(TAG, "getUserInfo: started");
        sessionManager.observeApplicationForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeApplicationForm().observe(getViewLifecycleOwner(), new Observer<ApplicationForm>() {
            @Override
            public void onChanged(ApplicationForm form) {
                if (form != null) {
                    Log.d(TAG, "onChanged: started");
                    binding.fragmentPersonalInfoLastName.setText(form.getLast_name());
                    binding.fragmentPersonalInfoFirstName.setText(form.getFirst_name());
                    binding.fragmentPersonalInfoMiddleName.setText(form.getMiddle_name());
                    if (form.getGender() != null) {
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


                    dateOfBirth = form.getDate_of_birth();
                    if (dateOfBirth != null) {
                        binding.fragmentPersonalInfoBirthDate.setText(dateOfBirth);
                    }

                    governmentId = form.getGovernment_id();
                    street = form.getStreet_address();
                    barangay = form.getBarangay_address();
                    city = form.getCity_address();
                    province = form.getProvince_address();
                    postal = form.getPostal_address();
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void navigation() {

        binding.fragmentPersonalInfoBirthDate.setOnTouchListener(new View.OnTouchListener() {
            //reference: https://stackoverflow.com/questions/3554377/handling-click-events-on-a-drawable-within-an-edittext
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.fragmentPersonalInfoBirthDate.getRight() - binding.fragmentPersonalInfoBirthDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Log.d(TAG, "onTouch: na touch ako sobra!");
                        DatePickerFragment dialog = new DatePickerFragment();
                        dialog.setOnDatePickerListener(PersonalInfoFragment.this);
                        dialog.show(requireActivity().getSupportFragmentManager(), getString(R.string.tag_dialog_fragment_date_picker));
                        return true;
                    }
                }
                return false;
            }
        });

        binding.fragmentPersonalInfoGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selected = group.getCheckedRadioButtonId();

                RadioButton button = binding.getRoot().findViewById(selected);
                gender = button.getTag().toString();
                Log.d(TAG, "onCheckedChanged: gender: " + gender);
            }
        });

        binding.fragmentPersonalInfoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostable.onEnlist(userInfo());
                Snackbar.make(v, "Update Successful!", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private ApplicationForm userInfo() {

        ApplicationForm info = new ApplicationForm();
        String last_name = binding.fragmentPersonalInfoLastName.getText().toString();
        String first_name = binding.fragmentPersonalInfoFirstName.getText().toString();
        String middle_name = binding.fragmentPersonalInfoMiddleName.getText().toString();

        info.setLast_name(last_name);
        info.setFirst_name(first_name);
        info.setMiddle_name(middle_name);
        info.setGender(gender);
        info.setDate_of_birth(dateOfBirth);


        info.setGovernment_id(governmentId);
        info.setStreet_address(street);
        info.setBarangay_address(barangay);
        info.setCity_address(city);
        info.setProvince_address(province);
        info.setPostal_address(postal);

        return info;
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