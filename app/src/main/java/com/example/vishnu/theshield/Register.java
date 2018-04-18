package com.example.vishnu.theshield;

import android.content.Context;
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
            Toast toast = Toast.makeText(ct, s, Toast.LENGTH_SHORT);
            toast.show();
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


        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            System.out.println("The read failed: " + error.getCode());
        }
    });
}

public void setcount()
{
    DatabaseReference ctRef = database.getReference("user_count");
    ctRef.setValue(count.toString());
}
    public void sub_details(View view) {
        // Write a message to the database
        try {

            user = new Profile(name.getText().toString(), dob.getText().toString(),
                    phonenum.getText().toString(), emailid.getText().toString(), pwd.getText().toString());
            DatabaseReference writeRef = database.getReference("User_Profiles");
            if(user.isEmptyall()){
                Toast toast = Toast.makeText(ct, "Fill all fields", Toast.LENGTH_SHORT);
                toast.show();

            }
            else if(pwd.getText().toString().equals(cnfpwd.getText().toString()))
            {
                count = count + 1;
                user.Password=pwd.getText().toString();
                writeRef.child("User_" + count).setValue(user);
                setcount();
                Toast toast = Toast.makeText(ct, "Successfully Registered", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(ct, "Passwords do not match", Toast.LENGTH_SHORT);
                toast.show();
            }

            writeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String p = dataSnapshot.child("User_1").child("Password").getValue(String.class);

                    // hm = dataSnapshot.getValue(HashMap.class);


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    System.out.println("The read failed: " + error.getCode());
                }
            });

        }
        catch(Exception e)
        {
            Toast toast = Toast.makeText(ct, "Establishing connection. Please wait.", Toast.LENGTH_SHORT);
            toast.show();
        }




            //   myRef.setValue("Hello, World!");


// Write a message to the database

    }
}

