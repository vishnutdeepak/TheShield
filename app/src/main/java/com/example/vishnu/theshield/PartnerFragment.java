package com.example.vishnu.theshield;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PartnerFragment extends Fragment {
    String[] infoArray ={"abc"} ;
    String number;
    FirebaseDatabase database;
    Integer count;
    DatabaseReference myRef;
    SharedPreferences sharedPref;
    DatabaseReference checkRef;
    DatabaseReference locRef;
    double latitude,longitude,distance;
    double ele = 0;
    Handler mHandler = new Handler();
    String name;
    ImageButton nw;
    ProgressBar pb;

    public PartnerFragment() {
        // Required empty public constructor
    }

    ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        sharedPref = getActivity().getSharedPreferences("the-shield",getActivity().MODE_PRIVATE);

        getcount();


        final View rootView = inflater.inflate(R.layout.fragment_partner,
                container, false);
        lv = rootView.findViewById(R.id.lv);
       // Toast.makeText(this.getContext(),infoArray[0],Toast.LENGTH_SHORT).show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
                try {
                    startActivity(intent);
                }catch(SecurityException e){
                    Toast.makeText(getContext(), "Please provide calling permission", Toast.LENGTH_SHORT).show();
                }


            }
        });

         nw = rootView.findViewById(R.id.imageButton);
        nw.setOnClickListener(new ImageButton.OnClickListener() {

            public void onClick(View v){
                pb.setVisibility(View.VISIBLE);
                name= sharedPref.getString("Name","");

                mHandler.postDelayed(mUpdateTimeTask, 300);

            }

        });
pb = rootView.findViewById(R.id.pbar);
        // Inflate the layout for this fragment
        return rootView;
    }
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            populate();
            // do what you need to do here after the delay
            if(distance<1000&&distance>0.0){
            CustomListAdapter whatever = new CustomListAdapter(getActivity(), infoArray);

            lv.setAdapter(whatever);
                pb.setVisibility(View.GONE);}
            else{
                nw.performClick();
            }
         //   Toast.makeText(getActivity(),"dist : " +   new DecimalFormat("#.##").format(distance),Toast.LENGTH_LONG).show();
        }

    };


    Integer i;
    public void getcount() {

        myRef = database.getReference("user_count");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String s = dataSnapshot.getValue(String.class);

                // hm = dataSnapshot.getValue(HashMap.class);
                try {
                    if (s.equals("null")) {


                    }
                } catch (Exception e) {


                    myRef.setValue("0");
                    count = 0;
                    s = "0";


                }
                //Toast toast = Toast.makeText(ct, s, Toast.LENGTH_SHORT);
                //     toast.show();
                count = Integer.parseInt(s);

                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }
Integer j;

    public void getloc() {

        locRef = database.getReference("User_Profiles");
        locRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (j = 1; j <= count; j++) {
                    String s = dataSnapshot.child("User_" + j).child("name").getValue(String.class);
                    if(s.equals(name)) {
                        latitude = dataSnapshot.child("User_" + j).child("latitude").getValue(Double.class);
                        longitude = dataSnapshot.child("User_" + j).child("longitude").getValue(Double.class);

                        break;
                    }
                    //                hm = dataSnapshot.getValue(HashMap.class);
                }
                //Toast toast = Toast.makeText(ct, s, Toast.LENGTH_SHORT);
                //     toast.show();

               locRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }



    public void populate() {







        getcount();
       getloc();

        myRef = database.getReference("User_Profiles");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (i = 1; i <= count; i++) {
                   String s = dataSnapshot.child("User_" + i).child("name").getValue(String.class);
                    number = dataSnapshot.child("User_" + i).child("phonenumber").getValue(String.class);
                    if(!s.equals(name))
                        infoArray[0]=s;
                        Double lati1  = dataSnapshot.child("User_" + i).child("latitude").getValue(Double.class);
                    Double longi1  = dataSnapshot.child("User_" + i).child("longitude").getValue(Double.class);
                   distance =  distance(lati1,latitude,longi1,longitude,ele,ele);
                   infoArray[0]+= ", " + new DecimalFormat("#.##").format(distance) + "m away";

                    //                hm = dataSnapshot.getValue(HashMap.class);
                }

                myRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
    public void refresh(View view) {
        populate();
    }
}
