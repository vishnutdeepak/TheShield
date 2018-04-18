package com.example.vishnu.theshield;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class Register extends AppCompatActivity {
    EditText name;
    EditText dob;
    EditText phonenum;
    EditText emailid;
    EditText pwd;
    EditText cnfpwd;
    Button submit;
    Context ct;
    Long value;
    HashMap hm;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Integer count;
    Profile user;
    DatabaseReference writeRef;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user_count");
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        phonenum = findViewById(R.id.phonenum);
        emailid = findViewById(R.id.emailid);
        pwd = findViewById(R.id.pwd);
        cnfpwd = findViewById(R.id.conpwd);
        submit = findViewById(R.id.submitbtn);
        ct = this.getApplicationContext();
        DatabaseReference writeRef;
        getcount();

// Read from the database

    }

public void getcount()
{
    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            String s = dataSnapshot.getValue(String.class);
         //   Toast toast = Toast.makeText(ct, s, Toast.LENGTH_SHORT);
    //        toast.show();
            // hm = dataSnapshot.getValue(HashMap.class);
            try {
                if (s.equals("null")) {


                }
            } catch (Exception e) {


                myRef.setValue("0");
                count = 0;
                s = "0";

            }

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
Integer i;
    Boolean flag;

public void checkavailable(){
flag=Boolean.TRUE;
    if(count ==0)
    {
        return;
    }
    final DatabaseReference checkRef = database.getReference("User_Profiles");





        checkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(i=1;i<=count;i++) {
                String s = dataSnapshot.child("User_" + i).child("email").getValue(String.class);

                if (user.email.equals(s)) {

                flag=Boolean.FALSE;
                    Toast tv = Toast.makeText(ct, "User already exists with registered email!", Toast.LENGTH_SHORT);
                    tv.show();
                    break;
                }
                //                hm = dataSnapshot.getValue(HashMap.class);
            }
            checkRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


}
        public void setcount()
{
    DatabaseReference ctRef = database.getReference("user_count");
    ctRef.setValue(count.toString());
}
    public void sub_details(View view) {
    getcount();
        // Write a message to the database
       writeRef = database.getReference("User_Profiles");
        try {

            user = new Profile(name.getText().toString(), dob.getText().toString(),
                    phonenum.getText().toString(), emailid.getText().toString(), pwd.getText().toString());



            if(user.isEmptyall()){
                Toast toast = Toast.makeText(ct, "Fill all fields", Toast.LENGTH_SHORT);
                toast.show();

            }
            else if(pwd.getText().toString().equals(cnfpwd.getText().toString()))
            {flag=Boolean.TRUE;
                checkavailable();
                mHandler.postDelayed(mUpdateTimeTask, 1000);

            }
            else{
                Toast toast = Toast.makeText(ct, "Passwords do not match", Toast.LENGTH_SHORT);
                toast.show();
            }



        }
        catch(Exception e)
        {
            Toast toast = Toast.makeText(ct, "Establishing connection. Please wait.", Toast.LENGTH_SHORT);
            toast.show();
        }




            //   myRef.setValue("Hello, World!");


// Write a message to the database

    }




    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {
            writeRef = database.getReference("User_Profiles");
            if(flag) {
                count = count + 1;
                user.Password = pwd.getText().toString();
                writeRef.child("User_" + count).setValue(user);
                setcount();
                Toast toast = Toast.makeText(ct, "Successfully Registered", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent (ct,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }

    };
}

