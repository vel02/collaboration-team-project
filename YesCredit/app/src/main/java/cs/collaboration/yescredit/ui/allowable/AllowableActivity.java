package cs.collaboration.yescredit.ui.allowable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.ActivityAllowableBinding;
import cs.collaboration.yescredit.model.Loan;
import cs.collaboration.yescredit.ui.apply.ApplyActivity;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

import static cs.collaboration.yescredit.util.Utility.currencyFormatter;

public class AllowableActivity extends BaseActivity {

    private static final String TAG = "AllowableActivity";

    @Inject
    ActivityAllowableBinding binding;

    @Inject
    ViewModelProviderFactory providerFactory;

    private AllowableViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(AllowableViewModel.class);

        getLoanInfo();
        navigation();
        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.observeIsAllowed().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAllowed) {
                if (isAllowed) {
                    Toast.makeText(AllowableActivity.this, "You are allowed to make new Transaction", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AllowableActivity.this, ApplyActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AllowableActivity.this, "You still have On-Going Transaction", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLoanInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            Query query = reference.child(getString(R.string.database_node_loans))
                    .orderByChild(getString(R.string.database_field_user_id))
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {
                        Loan loan = singleShot.getValue(Loan.class);
                        Log.d(TAG, "onDataChange: loan " + loan);
                        if (loan.getStatus().toLowerCase().equals("paid")) {
                            String limit = "PHP " + currencyFormatter(loan.getLimit());
                            binding.contentAllowable.contentAllowableLimit.setText(limit);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void navigation() {
        binding.contentAllowable.contentAllowableApply.setOnClickListener(v -> {
            viewModel.checkPendingLoan();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }
}