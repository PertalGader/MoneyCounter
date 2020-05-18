package com.example.moneycounter.controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.moneycounter.R;
import com.example.moneycounter.dataModels.spendingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddSpendingActivity extends BaseActivity {

    private static final String TAG = "AddSpendAct:";

    List<String> categories;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    Button addNewSpendingButton;
    TextView spinnerDate, spinnerTime;
    EditText  spendingNameEdit, spendingNoteEdit, spendingSumEdit;
    Spinner spinnerCategory;
    Calendar dateAndTime = Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spending);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        categories = new ArrayList<>();


        addNewSpendingButton = findViewById(R.id.accept_spending);

        spinnerCategory = findViewById(R.id.category_spinner);
        spinnerDate = findViewById(R.id.date_spinner);
        spinnerTime = findViewById(R.id.time_spinner);
        spendingNameEdit = findViewById(R.id.spending_name_editText);
        spendingNoteEdit = findViewById(R.id.spending_note_editText);
        spendingSumEdit = findViewById(R.id.spending_sum_editText);
        setInitialDate();
        setInitialTime();
        DocumentReference categoriesRef = db.collection("users").document(user.getEmail());
        categoriesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        categories = (List<String>) document.get("categoriesList");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                } else {
                    Log.d(TAG, "get false");
                }
            }
        });

        System.out.println("22222222222222222" + categories);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public String emailFormatter(String email){
        String formattedEmail;
        formattedEmail = email.substring(0, email.indexOf("@"));
        formattedEmail +="_spendings";
        return formattedEmail;
    }

    public void addNewSpending(View view){
        if(validateForm()){
            String category, spendingName, spendingNote;
            double sum;
            Date spendingDate;

            category = spinnerCategory.getSelectedItem().toString();
            spendingName = spendingNameEdit.getText().toString();
            spendingNote = spendingNoteEdit.getText().toString();
            sum = Double.parseDouble(spendingSumEdit.getText().toString());

            spendingDate = dateAndTime.getTime();
            spendingModel newSpending = new spendingModel(null,sum,spendingDate,spendingNote,spendingName);
            System.out.println(newSpending.getSpendingsDate());

            db.collection("spendings").document(emailFormatter(user.getEmail()))
                    .collection(category).add(newSpending)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

        }
    }

    public boolean validateForm(){
        boolean valid = true;
        String date = spinnerDate.getText().toString();
        if(TextUtils.isEmpty(date)){
            spinnerDate.setError("Required");
            valid = false;
        }else{
            spinnerDate.setError(null);
        }
        String time = spinnerTime.getText().toString();
        if(TextUtils.isEmpty(time)) {
            spinnerTime.setError("Required");
            valid = false;
        }else{
            spinnerTime.setError(null);
        }
        String spendingName = spendingNameEdit.getText().toString();
        if(TextUtils.isEmpty(spendingName)){
            spendingNameEdit.setError("Required");
            valid = false;
        }else{
            spendingNameEdit.setError(null);
        }
        String spendingSum = spendingSumEdit.getText().toString();
        if(TextUtils.isEmpty(spendingSum)){
            spendingSumEdit.setError("Required");
            valid = false;
        }else{
            spendingSumEdit.setError(null);
        }
        return valid;
    }


    // Методи для відображення вибору часу і дати
    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(AddSpendingActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(AddSpendingActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    // установка начальных даты и времени
    private void setInitialDate() {
        spinnerDate.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }
    private void setInitialTime() {
        spinnerTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),DateUtils.FORMAT_SHOW_TIME));
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };
}

