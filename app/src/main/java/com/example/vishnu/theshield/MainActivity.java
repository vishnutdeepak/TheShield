package com.example.vishnu.theshield;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import android.Manifest; // correct
import static android.Manifest.permission.READ_CONTACTS;
public class MainActivity extends AppCompatActivity implements LocationListener {
    ViewPager viewPager;
    TabLayout tabLayout;
    double latitude;
    double longitude;
    GPSTracker gpsTracker;
    LocationManager locationManager;
    private static final int REQUEST_READ_CONTACTS = 0;
    Button location,call,alert;
    private Timer myTimer;
    SharedPreferences sharedPref;
    MediaPlayer mp;
    Uri alertTone;
    public AlertDialog.Builder safetyCheck;
    public AlertDialog dialog;
    Notification n=null;
    NotificationManager notificationManager;
    String whatsAppmessage;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        alertTone = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(),RingtoneManager.TYPE_RINGTONE);
        mp = MediaPlayer.create(MainActivity.this,alertTone);
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 101);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},101);
        }
        }
public void callalert(View view)
{
    if(mp!=null){
        if(mp.isPlaying()){
            mp.stop();
            mp.release();
            mp = null;
        } else{
            if(!sharedPref.getString("alertTone","").isEmpty())
                alertTone = Uri.parse(sharedPref.getString("alertTone",""));
            else
                alertTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mp = MediaPlayer.create(MainActivity.this,alertTone);
            mp.start();
        }
    }else{
        if(!sharedPref.getString("alertTone","").isEmpty())
            alertTone = Uri.parse(sharedPref.getString("alertTone",""));
        else
            alertTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mp = MediaPlayer.create(MainActivity.this,alertTone);
        mp.start();
    }
}

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == 1 && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = this.getContentResolver().query(contactUri, projection,
                    null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                // Do something with the phone number
                sharedPref = getSharedPreferences("the-shield",this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("emergency", number);
                editor.apply();

            }
            assert cursor != null;
            cursor.close();
        }
        if (reqCode == 2 && resultCode == RESULT_OK) {
//            Ringtone defaultRingtone = RingtoneManager.getRingtone(this,
            //  Settings.System.DEFAULT_RINGTONE_URI);
            //Uri currentRintoneUri = RingtoneManager.getValidRingtoneUri(this);
            Uri currentRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(MainActivity.this, RingtoneManager.TYPE_RINGTONE);
            sharedPref = getSharedPreferences("the-shield",this.MODE_PRIVATE);
            Toast.makeText(MainActivity.this,currentRintoneUri.toString(),Toast.LENGTH_LONG).show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("alertTone", currentRintoneUri.toString());
            editor.apply();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.alertTone:
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select alert tone:");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALL);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                startActivityForResult(intent,2);
                return true;
            case R.id.contact:
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, 1);
                return true;
            case R.id.profile:
                Toast.makeText(MainActivity.this,"Opens new activity",Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                Toast.makeText(MainActivity.this,"Logs out",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


   public void sendLocationViaWhatsapp(View view) {
        String smsNumber = "7200847674";
        getLocation();
        whatsAppmessage = "http://maps.google.com/maps?saddar=" + latitude +","+ longitude;
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {
            try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppmessage);
                sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            this.startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = this.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
    public void callEmergency(View view){
        sharedPref = getSharedPreferences("the-shield",getApplicationContext().MODE_PRIVATE);
        String phone = sharedPref.getString("emergency","");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        try {
            startActivity(intent);
        }catch(SecurityException e){
            Toast.makeText(MainActivity.this, "Please provide calling permission", Toast.LENGTH_SHORT).show();
        }

    }

    public void setTimer(View v){

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 30000);
    }

    private void TimerMethod()
    {

        this.runOnUiThread(DisplaySafetyChecker);
    }
    private Runnable DisplaySafetyChecker = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            //Do something to the UI thread here
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if(n==null) {
                n = new Notification.Builder(getApplicationContext())
                        .setContentTitle("TheShield wants to check on you")
                        .setContentText("Are you okay?")
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentIntent(pIntent)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.FLAG_AUTO_CANCEL)
                        .setAutoCancel(false).build();
                notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            }
            notificationManager.notify(0, n);
            displayDialog();
            setSafetyTimer();

        }
    };
    public void displayDialog(){
        safetyCheck = new android.support.v7.app.AlertDialog.Builder(this);

        safetyCheck.setTitle("Confirm your security status")
                .setMessage("Are you sure you are safe?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //safetyAlert.hide();
                        notificationManager.cancelAll();
                        Log.i("noti",n.toString());

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //sendEmergencySMS();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        dialog = safetyCheck.create();
        dialog.show();
    }
    public void setSafetyTimer(){

        new Timer().schedule(new TimerTask(){
            public void run() {
                if(dialog.isShowing())
                    Toast.makeText(getApplicationContext(),"Will send SMS",Toast.LENGTH_SHORT).show();            }
        }, 10000);
//        myTimer = new Timer();
//        myTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if(dialog.isShowing())
//                    Toast.makeText(getApplicationContext(),"Will send SMS",Toast.LENGTH_SHORT).show();
//                //sendEmergencySMS();
//            }
//
//        }, 0, 10000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}

