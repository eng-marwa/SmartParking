package com.example.marwatalaat.smartparking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marwatalaat.smartparking.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterCar extends AppCompatActivity {

    @BindView(R.id.login)
    Button loginButton;
    @BindView(R.id.phone)
    EditText phoneNumberField;
    @BindView(R.id.name)
    EditText nameField;
    @BindView(R.id.car)
    EditText carNumberField;

    //to use real time db
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_car);
        ButterKnife.bind(this);

        //to get object from firebase auth
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        pref = getSharedPreferences("userReg",MODE_PRIVATE);


    }

    @OnClick(R.id.login)
    public void register() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String phone = phoneNumberField.getText().toString().trim();
        String name = nameField.getText().toString().trim();
        String car = carNumberField.getText().toString().trim();
        Gson gson = new Gson();
        User user = new User(name, phone, car);

        String userSaved = gson.toJson(user);
        pref.edit().putString("user",userSaved).commit();

        databaseRef.child(currentUser.getUid()).setValue(user);
        Toast.makeText(RegisterCar.this, "Car registration saved...", Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("user",user);
        startActivity(i);

    }
}
