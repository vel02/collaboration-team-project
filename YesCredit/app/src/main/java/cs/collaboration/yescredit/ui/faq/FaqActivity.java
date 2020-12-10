package cs.collaboration.yescredit.ui.faq;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.ActivityFaqBinding;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class FaqActivity extends BaseActivity {

    private static final String TAG = "FaqActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityFaqBinding binding;

    private FaqViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(FaqViewModel.class);
        init();
    }

    private void init() {

        binding.contentFaq.contentFaqQuestionOneDropdown.setOnClickListener(v
                -> expand(binding.contentFaq.contentFaqQuestionOneContent, binding.contentFaq.contentFaqQuestionOneDropdown));

        binding.contentFaq.contentFaqQuestionTwoDropdown.setOnClickListener(v -> {
            expand(binding.contentFaq.contentFaqQuestionTwoContent, binding.contentFaq.contentFaqQuestionTwoDropdown);
        });

        binding.contentFaq.contentFaqQuestionThreeDropdown.setOnClickListener(v -> {
            expand(binding.contentFaq.contentFaqQuestionThreeContent, binding.contentFaq.contentFaqQuestionThreeDropdown);
        });
    }

    private void expand(ConstraintLayout layout, Button button) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            button.setBackgroundResource(R.drawable.ic_arrow_down);
        } else {
            layout.setVisibility(View.GONE);
            button.setBackgroundResource(R.drawable.ic_arrow_right);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}