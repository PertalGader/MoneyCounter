package com.example.moneycounter.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moneycounter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends BaseActivity {

    private static final String TAG = "Sign/Reg Activity";

    private FirebaseAuth mAuth;

    private TextView statusTextView;

    private EditText emailEdit;
    private EditText passwordEdit;

    private Button signInButton;
    private Button signUpbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        emailEdit = findViewById(R.id.emailEditReg);
        passwordEdit = findViewById(R.id.passwordEditReg);
        statusTextView = findViewById(R.id.statusTextView);

        setProgressBar(R.id.progressBar);

        findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emailEdit.getText().toString(),passwordEdit.getText().toString());
            }
        });
        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpMenu();
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn(String email, String password){
        Log.d(TAG,"signIn:" + email);
        if(!validateForm()){
            return;
        }
        showProgressBar();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Log.d(TAG,"signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Log.d(TAG,"signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this,"Authentification failed"
                                    ,Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        if(!task.isSuccessful()){
                            statusTextView.setText("Ну вдалося увійти");
                        }
                        hideProgressBar();
                    }
                });
    }

    private void signUpMenu(){
        startActivity(new Intent(this,RegistrationActivity.class));
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = emailEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Required.");
            valid = false;
        } else {
            emailEdit.setError(null);
        }

        String password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("Required.");
            valid = false;
        } else {
            passwordEdit.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user){
        if(user!=null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Toast.makeText(this,"U Didnt signed in", Toast.LENGTH_LONG).show();
        }
    }
}
