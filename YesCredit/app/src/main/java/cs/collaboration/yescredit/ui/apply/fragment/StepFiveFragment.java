package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepFiveBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import dagger.android.support.DaggerFragment;

public class StepFiveFragment extends DaggerFragment {

    private static final String TAG = "StepFiveFragment";

    private FragmentStepFiveBinding binding;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepFiveBinding.inflate(inflater);

        navigation();

        return binding.getRoot();
    }

    private void navigation() {

        String[] education_achieved_array = getActivity().getResources().getStringArray(R.array.education_achieved_array);
        String[] reason_loaning_array = getActivity().getResources().getStringArray(R.array.reason_loaning_array);

        ArrayAdapter<CharSequence> adapter;

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.education_achieved_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentFiveEducationAchieved.setAdapter(adapter);
        binding.fragmentFiveEducationAchieved.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + education_achieved_array[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.reason_loaning_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentFiveReasonLoaning.setAdapter(adapter);
        binding.fragmentFiveReasonLoaning.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + reason_loaning_array[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.fragmentFiveNext.setOnClickListener(new View.OnClickListener() {
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