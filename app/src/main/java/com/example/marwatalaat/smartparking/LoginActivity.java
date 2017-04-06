package com.example.marwatalaat.smartparking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marwatalaat.smartparking.model.BlockedUsers;
import com.example.marwatalaat.smartparking.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.buttonSignin)
    Button buttonSignin;
    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.textViewSignUp)
    TextView textViewSignUp;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    List<String> blockedEmails;
    private android.app.AlertDialog alertDialog;
    private boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        getAllBlockedUser();
        if (mAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("user", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("user", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        progressDialog = new ProgressDialog(this);

    }

    private void getAllBlockedUser() {
        blockedEmails = new ArrayList<String>();

        DatabaseReference blockedUser = FirebaseDatabase.getInstance().getReference().child("BlockedUsers");
        blockedUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println(child.getChildrenCount()+" child");

                   String email = child.child("email").getValue().toString();
                   String carNumber = child.child("carNumber").getValue().toString();
                    blockedEmails.add(email);
                   Log.i("text", email);

                }

                System.out.println(blockedEmails.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }



    @OnClick(R.id.buttonSignin)
    public void userLogin() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();

        if (!isBlocked(email)) {
            //logging in the user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            //if the task is successfull
                            if (task.isSuccessful()) {
                                //start the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }
                    });
        }else{
            progressDialog.dismiss();

                showErrorDialoge();

        }
    }

    private void showErrorDialoge() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Sorry");
        builder.setMessage("Your account has been blocked");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();

            }
        });
         alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isBlocked(String email) {
        b = true;
        System.out.println(blockedEmails.contains(email));
        return blockedEmails.contains(email);
    }

    @OnClick(R.id.textViewSignUp)
    public void signUp() {
        finish();
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}
