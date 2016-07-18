package com.wilsong.tumble;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Gerard on 10/07/2016.
 */
public class MainScreenActivity extends AppCompatActivity {

    private Button callButton;
    String contact = "07703520914";
    String sms;
    String mapURL;
    Location currentLocation;
    String body;
    TextView textLatitude;
    TextView textLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        textLatitude = (TextView) findViewById(R.id.tvLatitude);
        textLongitude = (TextView) findViewById(R.id.tvLongitude);

        getCurrentLocation();
        callButton = (Button) findViewById(R.id.callButton);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Calling Yer Man", Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_CALL);

                callIntent.setData(Uri.parse("tel:" + contact));
                if (ActivityCompat.checkSelfPermission(MainScreenActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
                AudioManager audioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);


                sendSMS(contact);

            }
        });
    }

    /**
     * Method sends a SMS to the chosen contact, alerting them that there has been a possible fall.
     * @param contact
     */
    private void sendSMS(String contact) {
        sms = "Alert, possible fall";

       // String smsMap = getCurrentLocation();
        //Location currentLocation = new Location("");
        // double latitude = currentLocation.getLatitude();
        // Double longitude = currentLocation.getLongitude();
        //String stringLAT = Double.toString(currentLocation.getLatitude());
        //String stringLONG = Double.toString(currentLocation.getLatitude());

        //String mapURL = "maps.google.com/maps?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        textLatitude.setText(mapURL);
        //textLongitude.setText(stringLONG);


        try {
            SmsManager smsManager = SmsManager.getDefault();
            StringBuffer smsBody = new StringBuffer();
            //smsBody.append("maps.google.com/maps?q=");
            //smsBody.append(currentLocation.getLatitude());
            //smsBody.append(",");
            //smsBody.append(currentLocation.getLatitude());
            smsBody.append(Uri.parse(mapURL));
            body = smsBody.toString();
            //smsManager.sendTextMessage(contact, null, sms + " " + body, null, null);
            smsManager.sendTextMessage(contact, null, body, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent!" + mapURL, Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {

            Toast.makeText(getApplicationContext(), "SMS failed " + mapURL, Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        }
    }

    public String getCurrentLocation() {

        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);
        String error = "Something wrong";
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            return error;
        }

        locationManager.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
            @Override
            public  void onStatusChanged(String provider, int status, Bundle extras){

            }
            @Override
            public  void onProviderEnabled(String provider){

            }
            @Override
            public void onProviderDisabled(String provider){

            }
            @Override
            public void onLocationChanged(Location location){
                if (location != null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    if (latitude != 0.0 && longitude != 0.0){

                        String stringLAT = Double.toString(location.getLatitude());
                        String stringLONG = Double.toString(location.getLongitude());
                        mapURL = "maps.google.com/maps?q=" + stringLAT + "," + stringLONG;
                        textLatitude.setText(mapURL);
                        //textLongitude.setText(stringLONG);
                    }
                }
            }
        });
        return mapURL;
    }


}
