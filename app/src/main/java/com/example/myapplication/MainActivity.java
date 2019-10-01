package com.example.myapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.service.BeaconManager;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.*;


import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity
{

    private static final Map<String, List<String>> PLACES_BY_BEACONS;

    TextView textView;
    TextView textView2;
    TextView textView3;
    AlertDialog dialog;
    EditText editText;



    private BeaconManager beaconManager;
    private BeaconRegion region;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = (TextView) findViewById(R.id.textView);
        dialog = new AlertDialog.Builder(this).create();
        editText = new EditText(this);

        dialog.setTitle("Beacon Message");
        dialog.setView(editText);

        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);


        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    Log.d("Forestry", "Nearest places: " + places);
                    String str = places.toString();
                    //textView.setText(str);

                    String str1 = "";
                    double power1 = 0.0;
                    double RSII1 = 0.0;
                    double distance1 = 100.0;


                    String str2 = "";
                    double power2 = 0.0;
                    double RSII2 = 0.0;
                    double distance2 = 100.0;

                    for(int i = 0; i <list.size();i++)
                    {

                        if(i == 0)
                        {

                            // str2 = str2 + list.get(i).toString();
                            power1 = list.get(i).getMeasuredPower();
                            RSII1 = list.get(i).getRssi();
                            distance1 = Math.pow(10, (power1 - RSII1)/20);

                            DecimalFormat df = new DecimalFormat("#.00");
                            String angleFormated1 = df.format(distance1);

                            str1 = "Beacon " + (i + 1) + " is " + angleFormated1 + "m away.";

                        }
                        else
                        {

                            power2 = list.get(i).getMeasuredPower();
                            RSII2 = list.get(i).getRssi();
                            distance2 = Math.pow(10, (power2 - RSII2)/20);

                            DecimalFormat df = new DecimalFormat("#.00");
                            String angleFormated2 = df.format(distance2);

                            str2 = "Beacon " + (i + 1) + " is " + angleFormated2 + "m away.";

                        }

                    }

                    if (distance1 < 5 && distance2 < 5)
                    {
                        textView.setText("DANGER!!!");
                    }
                    else if (distance1 < 8 && distance2 < 5 || distance1 < 5 && distance2 < 8)
                    {
                        textView.setText("Warning");
                    }
                    else
                    {
                        textView.setText("Safe");
                    }

                    textView2.setText(str1);
                    textView3.setText(str2);
                }
                else
                {
                    textView.setText(null);
                    textView2.setText(null);

                }
            }
        });


        region = new BeaconRegion("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback()
        {
            @Override
            public void onServiceReady()
            {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause()
    {
        beaconManager.stopRanging(region);

        super.onPause();
    }


    static
    {
        Map<String, List<String>> placesByBeacons = new HashMap<>();

        placesByBeacons.put("8805:31055", new ArrayList<String>() {{ add("Beacon 1"); }});
        placesByBeacons.put("29056:22750", new ArrayList<String>() {{ add("Beacon 2"); }});

        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon)
    {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }



}
