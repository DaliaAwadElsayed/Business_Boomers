package com.example.dalia.businessboomers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.dalia.businessboomers.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Google Authentication
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Login clickListener
        activityMainBinding.loginId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
         //Google signin clicklistener
        activityMainBinding.googleSignId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void login() {
        activityMainBinding.progress.setVisibility(View.VISIBLE);
        if (inputValid()) {
            activityMainBinding.progress.setVisibility(View.GONE);
            Toast.makeText(this, R.string.string_success, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean inputValid() {
        return emailValid() && passwordIsValid();
    }

    private boolean emailValid() {
        String email = activityMainBinding.emailId.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            activityMainBinding.progress.setVisibility(View.GONE);
            activityMainBinding.emailId.setError(getResources().getString(R.string.string_email_condition));
            return false;
        }
    }

    private boolean passwordIsValid() {
        String password = activityMainBinding.passId.getText().toString();
        if (password.length() < 7) {
            activityMainBinding.progress.setVisibility(View.GONE);
            activityMainBinding.passId.setError(getResources().getString(R.string.string_pass_condition));
            return false;
        } else {
            return true;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Toast.makeText(this, getResources().getString(R.string.string_wlc) + " " + account.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.w("signInResult:failedcode", String.valueOf(e.getStatusCode()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
}