package com.example.vishnu.theshield;

public class Profile {

    String name;
    String dob;
    String phonenumber;
            String email;
            String Password;

            Profile (String n,String d, String p, String e, String pw)
            {
                name = n;
                dob=d;
                phonenumber=p;
                email=e;
                Password = pw;
            }
            public boolean isEmptyall(){
                if(name.isEmpty()||dob.isEmpty()||phonenumber.isEmpty()||email.isEmpty()||Password.isEmpty())
                    return Boolean.TRUE;

                else return Boolean.FALSE;
            }
}
