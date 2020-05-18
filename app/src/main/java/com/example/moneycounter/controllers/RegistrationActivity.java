package com.example.moneycounter.controllers;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moneycounter.R;
import com.example.moneycounter.dataModels.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends BaseActivity {
    private static final String TAG = "Sign/Reg Activity";


    private EditText emailEdit;
    private EditText passEdit;
    private EditText townEdit;
    private EditText dateEdit;
    private EditText nameEdit;


    Calendar dateAndTime = Calendar.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        emailEdit = findViewById(R.id.emailEditReg);
        passEdit = findViewById(R.id.passwordEditReg);
        townEdit = findViewById(R.id.townEditReg);
        dateEdit = findViewById(R.id.dateEditReg);
        nameEdit = findViewById(R.id.nameEditReg);

        findViewById(R.id.signUpButtonReg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(emailEdit.getText().toString(), passEdit.getText().toString());
            }
        });
        findViewById(R.id.datePickButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }
    private void createAccount(final String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressBar();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userModel newUser = new userModel(email,convertName(nameEdit.getText().toString()),
                                    townEdit.getText().toString(),dateAndTime.getTime());
                                    convertName(nameEdit.getText().toString());
                            db.collection("users").document(email).set(newUser);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressBar();
                    }
                });
    }
    private void updateUI(FirebaseUser user){
        if(user!=null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MainActivity.class));
        }else{
            Toast.makeText(this,"U Didnt signed in", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validateForm() {
        boolean valid = true;
        String email = emailEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Required");
            valid = false;
        } else {
            emailEdit.setError(null);
        }
        String password = passEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passEdit.setError("Required");
            valid = false;
        } else {
            passEdit.setError(null);
        }
        String name = nameEdit.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nameEdit.setError("Required");
            valid = false;
        } else {
            nameEdit.setError(null);
        }
        String town = townEdit.getText().toString();
        if (TextUtils.isEmpty(town)) {
            townEdit.setError("Required");
            valid = false;
        } else {
            townEdit.setError(null);
        }
        String date = dateEdit.getText().toString();
        if (TextUtils.isEmpty(date)) {
            dateEdit.setError("Required");
            valid = false;
        } else {
            dateEdit.setError(null);
        }

        return valid;
    }
    private void setInitialDateTime() {
        dateEdit.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_SHOW_YEAR));
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void setDate() {
        new DatePickerDialog(RegistrationActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public Map<String, Object> convertName(String name){
        Map<String, Object> mapName = new HashMap<>();
        String[] splittedName = name.split("\\\\s+");
        if(splittedName.length>1) {
            mapName.put("firstName", splittedName[0]);
            mapName.put("secondName", splittedName[1]);
        }else{
            mapName.put("firstName", splittedName[0]);
        }
        return mapName;
    }

}
