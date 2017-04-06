package com.example.marwatalaat.smartparking;


import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.db.circularcounter.CircularCounter;
import com.example.marwatalaat.smartparking.model.BlockedUsers;
import com.example.marwatalaat.smartparking.model.Reservation;
import com.example.marwatalaat.smartparking.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.marwatalaat.smartparking.ParkingFragment.currV;
import static com.example.marwatalaat.smartparking.ParkingFragment.selectedFloor;
import static com.example.marwatalaat.smartparking.ParkingFragment.selectedRoom;
import static com.example.marwatalaat.smartparking.R.array.colors;



public class ProgressFragment extends Fragment {

    @BindView(R.id.meter)
    CircularCounter meter;

    @BindView(R.id.stopTimer)
    Button stopTimer;

    @BindView(R.id.confirmReservation)
    Button confirmReservation;

    private String[] colors;

    private Handler handler;
    private DatabaseReference databaseRef;

    private Runnable r;
    private FirebaseAuth mAuth;
    private User user;
    private Calendar calendar;
    private SharedPreferences resvPref;
    private Date date;
    private int id;
    private AlertDialog alertDialog;
    private boolean isCancelled;
    private boolean isZero=true;
    int counter;
    private boolean isMissedOut;

    public static ProgressFragment getInstance(User user) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        fragment.setArguments(bundle);
        return fragment;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        ButterKnife.bind(this, v);
        mAuth = FirebaseAuth.getInstance();

        databaseRef = FirebaseDatabase.getInstance().getReference();
        calendar = Calendar.getInstance();
        resvPref = getActivity().getSharedPreferences("reservation", Context.MODE_PRIVATE);


        meter.setFirstWidth(getResources().getDimension(R.dimen.first))
                .setFirstColor(getResources().getColor(R.color.semiWhile, null))

                .setSecondWidth(getResources().getDimension(R.dimen.second))
                .setFirstColor(getResources().getColor(R.color.colorPrimary, null))

                .setThirdWidth(getResources().getDimension(R.dimen.third))
                .setFirstColor(getResources().getColor(R.color.color4, null))

                .setBackgroundColor(getResources().getColor(android.R.color.black, null));

        Log.i("hahaha",currV+"");

        handler = new Handler();
        r = new Runnable() {
            boolean go = true;

            public void run() {
                if (currV == 31 && go)
                    go = false;
                else if (currV == 0 && !go)
                    go = true;

                if (!go)

                    currV--;
                if (currV == 0 || currV == 1) meter.setMetricText("minute");
                if (currV == 0 && !isZero) {missedOut(); return;}

                meter.setValues(currV, currV * 2, currV * 3);
                handler.postDelayed(this, 5000);
            }
        };
        return v;

    }

    @OnClick(R.id.confirmReservation)
    public void confirmReservation() {
        if(!isCancelled || !isMissedOut) {
            currV=0;
            date = calendar.getTime();
            Log.i("date",date.toString());
            id = resvPref.getInt(user.getCarNumber(), 0);
            counter = resvPref.getInt(user.getCarNumber()+"_counter",0);
            Reservation r = new Reservation(id, date, "Confirm",counter,selectedFloor , selectedRoom);
            databaseRef.child(mAuth.getCurrentUser().getUid()).child(user.getCarNumber()).child(date.toString()).setValue(r);
            Toast.makeText(getActivity(), "Confirmed", Toast.LENGTH_LONG).show();
            ((MainActivity)getActivity()).switchToMesFragment(true);
        }
    }

    @OnClick(R.id.stopTimer)
    public void setStopTimer() {
        viewAlertMessage();
    }

    private void viewAlertMessage() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle("Attention");
        builder.setMessage("Are you sure you want to cancel this reservation?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                currV = 0;
                id = resvPref.getInt(user.getCarNumber(), 0);
                date = calendar.getTime();
                counter = resvPref.getInt(user.getCarNumber()+"_counter",0);

                ++counter;

                resvPref.edit().putInt(user.getCarNumber()+"_counter",counter).commit();
                counter = resvPref.getInt(user.getCarNumber()+"_counter",0);
                Reservation r = new Reservation(id, date, "Cancelled" ,counter,selectedFloor ,selectedRoom);
                databaseRef.child(mAuth.getCurrentUser().getUid()).child(user.getCarNumber()).child(date.toString()).setValue(r);
                ((MainActivity)getActivity()).switchToMesFragment(false);
                isCancelled = true;
                if(counter==3){
                    blockUser();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

    }

    private void blockUser() {
        BlockedUsers bUser = new BlockedUsers(mAuth.getCurrentUser().getEmail(),user.getCarNumber());
        databaseRef.child("BlockedUsers").push().setValue(bUser);
        mAuth.signOut();;
        startActivity(new Intent(getActivity(),LoginActivity.class));

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(r, 500);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = bundle.getParcelable("user");
            resvPref.edit().putInt(user.getCarNumber()+"_counter",counter).commit();

        }
    }



    private void missedOut() {
        isCancelled = false;
        date = calendar.getTime();
        id = resvPref.getInt("reserve", 0);
        counter = resvPref.getInt(user.getCarNumber()+"_counter",0);
        ++counter;
        resvPref.edit().putInt(user.getCarNumber()+"_counter",counter).commit();
        Reservation r = new Reservation(id, date,  "Missed Out" ,counter
                ,selectedFloor ,selectedRoom);
        databaseRef.child(mAuth.getCurrentUser().getUid()).child(user.getCarNumber()).child(date.toString()).setValue(r);

        Toast.makeText(getActivity(), "You Missed Out", Toast.LENGTH_LONG).show();
        ((MainActivity)getActivity()).switchToMesFragment(false);

        isMissedOut = true;
        if(counter==3){
            blockUser();
        }

        return;

    }

}
