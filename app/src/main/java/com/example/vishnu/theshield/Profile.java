package com.example.vishnu.theshield;

public class Profile {

    String name;
    String dob;
    String phonenumber;
            String email;
            String Password;
double latitude;
double  longitude;
            Profile (String n,String d, String p, String e, String pw)
            {
                name = n;
                dob=d;
                phonenumber=p;
                email=e;

                Password = pw;
                setloc(0.0,0.0);
            }
            public boolean isEmptyall(){
                if(name.isEmpty()||dob.isEmpty()||phonenumber.isEmpty()||email.isEmpty()||Password.isEmpty()){
                    return Boolean.TRUE;
                }
                else return Boolean.FALSE;
            }

public void setloc(double lat,double lon){
                latitude = lat;
                longitude = lon;
        }
}
