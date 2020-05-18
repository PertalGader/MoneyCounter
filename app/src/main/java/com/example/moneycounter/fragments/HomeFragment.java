package com.example.moneycounter.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.moneycounter.R;
import com.example.moneycounter.controllers.AddSpendingActivity;
import com.example.moneycounter.controllers.MainActivity;
import com.example.moneycounter.dataModels.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment:";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button addSpendingButton, addEarningButton;
    private TextView textTodaysSpent, textWeekSpent, textMonthSpent;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private List<String> categories;

    private Calendar dateAndTime = Calendar.getInstance();

    private double todaysSumSpent, weekSumSpent, monthSumSpent;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        addSpendingButton = v. findViewById(R.id.add_spending);
        addSpendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSpendingActivity.class);
                startActivity(intent);
            }
        });
        addEarningButton = v.findViewById(R.id.add_earning);
        addEarningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeekSpending();
            }
        });
        textTodaysSpent = v.findViewById(R.id.textview_today_spent);
        textWeekSpent = v.findViewById(R.id.textview_week_spent);
        textMonthSpent = v.findViewById(R.id.textview_month_spent);

        System.out.println("Testing dates - "+getDate("monthEnd"));
        return v;
    }

    public void getTodaysSpendings(){
        DocumentReference categoriesRef = db.collection("users").document(user.getEmail());
        categoriesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        categories = (List<String>)document.get("categoriesList");
                    }
                    for(int i = 0; i < categories.size(); i++) {
                        db.collection("spendings").document(emailFormatter(user.getEmail()))
                                .collection(categories.get(i)).whereGreaterThan("spendingsDate", getDate("today"))
                                .whereLessThan("spendingsDate", getDate("tomorrow")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        todaysSumSpent += Double.parseDouble(document.get("sum").toString());
                                    }
                                    textTodaysSpent.setText(String.valueOf(todaysSumSpent));
                                } else {
                                    Log.d(TAG, "Error getting doc", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });
        todaysSumSpent = 0.0;

    }

    public void getWeekSpending(){
        DocumentReference categoriesRef = db.collection("users").document(user.getEmail());
        categoriesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        categories = (List<String>)document.get("categoriesList");
                    }
                    for(int i = 0; i < categories.size(); i++) {
                        db.collection("spendings").document(emailFormatter(user.getEmail()))
                                .collection(categories.get(i)).whereGreaterThan("spendingsDate", getDate("weekStart"))
                                .whereLessThan("spendingsDate", getDate("weekEnd")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        weekSumSpent += Double.parseDouble(document.get("sum").toString());
                                    }
                                    textWeekSpent.setText(String.valueOf(weekSumSpent));
                                } else {
                                    Log.d(TAG, "Error getting doc", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });
        weekSumSpent = 0.0;
    }
    public void getMonthSpending(){
        DocumentReference categoriesRef = db.collection("users").document(user.getEmail());
        categoriesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        categories = (List<String>)document.get("categoriesList");
                    }
                    for(int i = 0; i < categories.size(); i++) {
                        db.collection("spendings").document(emailFormatter(user.getEmail()))
                                .collection(categories.get(i)).whereGreaterThan("spendingsDate", getDate("monthStart"))
                                .whereLessThan("spendingsDate", getDate("monthEnd")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        monthSumSpent += Double.parseDouble(document.get("sum").toString());
                                    }
                                    textMonthSpent.setText(String.valueOf(monthSumSpent));
                                } else {
                                    Log.d(TAG, "Error getting doc", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });
        monthSumSpent = 0.0;
    }

    public String emailFormatter(String email){
        String formattedEmail;
        formattedEmail = email.substring(0, email.indexOf("@"));
        formattedEmail +="_spendings";
        return formattedEmail;
    }

    public Date getDate(String feature){
        Date todaysDate;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.AM_PM);
        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        switch (feature) {
            case "today":
                todaysDate = cal.getTime();
                break;
            case "tomorrow":
                cal.add(Calendar.DATE,1);
                todaysDate = cal.getTime();
                break;
            case "weekStart":
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                todaysDate = cal.getTime();
                break;
            case "weekEnd":
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                todaysDate = cal.getTime();
                break;
            case "monthStart":
                cal.set(Calendar.DAY_OF_MONTH, 1);
                todaysDate = cal.getTime();
                break;
            case "monthEnd":
                cal.add(Calendar.MONTH, 1);
                todaysDate = cal.getTime();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + feature);
        }
        return todaysDate;
    }


    @Override
    public void onStart() {
        super.onStart();
        getTodaysSpendings();
        getWeekSpending();
        getMonthSpending();
    }
}