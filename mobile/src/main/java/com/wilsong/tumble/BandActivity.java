package com.wilsong.tumble;

//imports

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;

import java.lang.ref.WeakReference;

/**
 * Created by Gerard on 23/07/2016.
 */
public class BandActivity extends AppCompatActivity {


    // firstly get a listed of bands that are paired with the device
    private BandInfo[] pairedBands = null;
    //create a BandClient
    private BandClient bandClient = null;

    private Button btnStart;
    private Button btnConsent;
    private TextView txtStatus;

    /**
     * Method creates a Band Heart Rate Event Listener and shows the heart rate updates on screen
     */
    private BandHeartRateEventListener heartRateEventListener = new BandHeartRateEventListener() {
        @Override
        public void onBandHeartRateChanged(BandHeartRateEvent hrEvent) {
            if (hrEvent != null) {
                //update screen
                txtStatus.setText(String.format("Heart Rate = %d beats per minute\n" + "Quality = %s\n", hrEvent.getHeartRate(), hrEvent.getQuality()));
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band);

        // set heart rate
        txtStatus = (TextView) findViewById(R.id.txtStatus);

        //set consent
        btnConsent = (Button) findViewById(R.id.btnConsent);

        //set start heart rate listener
        btnStart = (Button) findViewById(R.id.btnStart);

        final WeakReference<Activity> reference = new WeakReference<Activity>(this);

        btnConsent.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View view) {

               // new HeartRateConsentTask().execute(reference);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HeartRateTask().execute();
                txtStatus.setText("");
    }
});
    }

    /**
     * Method to connect to a paired microsoft band via Bluetooth
     *
     * @return is there a connection to a band
     * @throws InterruptedException
     * @throws BandException
     */
    private boolean getConnectBandClient() throws InterruptedException, BandException {
        if (bandClient == null) {
            //find a paired band
            pairedBands = BandClientManager.getInstance().getPairedBands();
            // if there are no bands connected
            if (pairedBands.length == 0) {
                // send message to the user
                return false;
            }
            //connect the Band Client to a paired band
            bandClient = BandClientManager.getInstance().create(getBaseContext(), pairedBands[0]);
            //already made a connection to a band previously
        } else if (ConnectionState.CONNECTED == bandClient.getConnectionState()) {
            return true;
        }
        return true;
    }

    // Method starts the Heart Rate Listener in the background
    private class HeartRateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String exceptionMessage="";
            try {
                if (getConnectBandClient()) {
                    //if (bandClient.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                        bandClient.getSensorManager().registerHeartRateEventListener(heartRateEventListener);
                   // } else {
                        // message to user consent not given

                        Toast.makeText(BandActivity.this, "User Consent required", Toast.LENGTH_SHORT).show();
                    }
               // } else {
                    // message to user that
               // }
            } catch (BandException bE) {
                exceptionMessage="Unknown Error";

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }





    private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
        @Override
        protected Void doInBackground(WeakReference<Activity>... params) {
            try {
                if (getConnectBandClient()) {

                    if (params[0].get() != null) {
                        bandClient.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                            @Override
                            public void userAccepted(boolean consentGiven) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(BandActivity.this, "Band isn't connected. Please make sure bluetooth is on and the band is in range", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                String exceptionMessage = "";
            }
            return null;
        }

    }
    }
}
