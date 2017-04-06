package com.example.marwatalaat.smartparking;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.marwatalaat.smartparking.model.Reservation;
import com.example.marwatalaat.smartparking.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import garin.artemiy.quickaction.library.QuickAction;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParkingFragment extends Fragment {

    @BindView(R.id.imageButton)
    Button imageButton;

    @BindView(R.id.imageButton4)
    Button imageButton4;
    @BindView(R.id.imageButton10)
    Button imageButton10;
    @BindView(R.id.spinner_nav)
    Spinner floorSpinner;

    @BindView(R.id.imageButton11)
    Button imageButton11;
    @BindView(R.id.imageButton5)
    Button imageButton5;
    @BindView(R.id.imageButton8)
    Button imageButton8;

    int id;
    boolean isReversed;
    private QuickAction quickAction;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private User user;
    private Calendar calendar;
    private SharedPreferences resvPref;
    private View v;
    public static int currV;
    public static String selectedFloor="Floor 1";
    public static String selectedRoom;
    private int btnId;
    private boolean isReserved;

    public ParkingFragment() {
        // Required empty public constructor
    }


    public static ParkingFragment getInstance(User user) {
        ParkingFragment fragment = new ParkingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        fragment.setArguments(bundle);
        return fragment;

    }


    public void reversedPlace(View view) {

        quickAction.show(view);
    }

    public void bookingPark(final Button imageButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle("Reservation Dialog");
        builder.setMessage("Are you sure you want to reserve this room?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                imageButton.setBackgroundResource(R.drawable.green);
                btnId = imageButton.getId();
                Log.i("BtnId",btnId+"");
                selectedRoom = imageButton.getText().toString();
                Date date = calendar.getTime();
                startReservation(id, date);
                isReversed =true;


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void startReservation(int id, Date date) {
        resvPref.edit().putBoolean("isReserved",true).commit();
        ProgressFragment.getInstance(user);
        Toast.makeText(getActivity(), "Your reservation ends after 30 minutes", Toast.LENGTH_LONG).show();
        Reservation reservation = new Reservation(id, date, "reserved" ,0, selectedFloor , selectedRoom);
        databaseRef.child(mAuth.getCurrentUser().getUid()).child(user.getCarNumber()).child(date.toString()).setValue(reservation);
        currV = 31;
        resvPref.edit().putInt(user.getCarNumber(), id).commit();
    }


    @OnClick({R.id.imageButton11, R.id.imageButton5, R.id.imageButton8})
    public void selectPlace(View view) {
      isReversed = resvPref.getBoolean("isReserved",false);

        if (!isReversed) {
            Drawable bg = view.getBackground();
            if (bg.getConstantState().equals(
                    ContextCompat.getDrawable(getActivity(), R.drawable.blank).getConstantState())) {
                Button imageButton = (Button) view;
                imageButton.setClickable(true);
                bookingPark(imageButton);

            } else {
                reversedPlace(view);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_parking, container, false);
        resvPref = getActivity().getSharedPreferences("reservation", MODE_PRIVATE);
        isReversed = resvPref.getBoolean("isReserved",false);


        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);
        imageButton.setClickable(false);
        imageButton4.setClickable(false);
        imageButton10.setClickable(false);

        final String[] floors = {"Floor 1", "Floor 2", "Floor 3"};
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, floors);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(spAdapter);
        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 selectedFloor = floors[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();


        calendar = Calendar.getInstance();
        RelativeLayout view = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.popup_custom_layout, null);
        if(v.findViewById(id)!=null)
            v.findViewById(id).setBackgroundResource(R.drawable.green);
        if (!isReversed) {


            Log.i("what", btnId + "");
        }


        quickAction = new QuickAction
                (getActivity(), R.style.PopupAnimation, view, view); // in this case, layout identical
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        Bundle bundle = getArguments();
        if (bundle != null) {
            user = bundle.getParcelable("user");
            Log.i("user", user==null?"y":"n");

            if (user == null) {
                user = getSavedUser();
                Log.i("user2", user==null?"y":"n");

            }
            SharedPreferences resvPref = getActivity().getSharedPreferences("reservation", MODE_PRIVATE);
            isReserved = resvPref.getBoolean("isReserved", false);
            if(!isReserved){
                imageButton11.setClickable(true);
                imageButton5.setClickable(true);
                imageButton8.setClickable(true);
            }
            id = resvPref.getInt(user.getCarNumber(), 0);
                if (id != 0) {
                    isReversed = true;
                }
//
                if (isReversed) {
                    if(v.findViewById(id)!=null)
                    v.findViewById(id).setBackgroundResource(R.drawable.green);
                }
            }
        }




    private User getSavedUser() {
        Gson gson = new Gson();
        SharedPreferences pref = getActivity().getSharedPreferences("userReg",MODE_PRIVATE);

        return  gson.fromJson(pref.getString("user",null),User.class);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.floor, menu);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
         isReserved = false;
        if(getActivity()!=null) {
            SharedPreferences resvPref = getActivity().getSharedPreferences("reservation", MODE_PRIVATE);
             isReserved = resvPref.getBoolean("isReserved", false);
        }
        if (isVisibleToUser && !isReserved && btnId!=0){
           Button btn =  (Button)getActivity().findViewById(btnId);
                Log.i("active",getActivity()==null?"y":"n");
                Log.i("text",selectedRoom);
                if(btn.getText().toString().equals(selectedRoom)){
                    btn.setBackgroundResource(R.drawable.blank);
                    btn.setClickable(true);

            }
            imageButton11.setClickable(true);
            imageButton5.setClickable(true);
            imageButton8.setClickable(true);

            return;


        }if (isVisibleToUser && isReserved && btnId!=0){
            Button btn =  (Button)getActivity().findViewById(btnId);

            if(btn.getText().toString().equals(selectedRoom)){
                btn.setBackgroundResource(R.drawable.green);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCheckOutDialoge((Button) view);
                    }
                });

            }
            isReversed = false;

        }
        else
            Log.d("MyFragment", "Fragment is not visible.");


    }

    private void showCheckOutDialoge(final Button btn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setTitle("Check Out Dialog");
        builder.setMessage("Are you sure you want to check out?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resvPref.edit().putBoolean("isReserved",false).commit();

                btn.setBackgroundResource(R.drawable.blank);
                isReversed = false;
                imageButton11.setClickable(true);
                imageButton5.setClickable(true);
                imageButton8.setClickable(true);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    }

