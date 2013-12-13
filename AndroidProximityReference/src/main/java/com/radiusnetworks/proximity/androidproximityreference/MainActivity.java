package com.radiusnetworks.proximity.androidproximityreference;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconData;
import com.radiusnetworks.ibeacon.IBeaconDataNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;
import com.radiusnetworks.ibeacon.client.DataProviderException;
import com.radiusnetworks.proximity.ibeacon.IBeaconManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements IBeaconConsumer, RangeNotifier, IBeaconDataNotifier
{
    public static final String TAG = "MainActivity";

    IBeaconManager iBeaconManager;
    Map<String,TableRow> rowMap = new HashMap<String,TableRow>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iBeaconManager = IBeaconManager.getInstanceForApplication(this.getApplicationContext());
        iBeaconManager.bind(this);
    }

    @Override
    public void onIBeaconServiceConnect() {
        Region region = new Region("MainActivityRanging", null, null, null);
        try {
            iBeaconManager.startRangingBeaconsInRegion(region);
            iBeaconManager.setRangeNotifier(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iBeaconManager.unBind(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
        for (IBeacon iBeacon: iBeacons) {
            iBeacon.requestData(this);
            Log.d(TAG, "I see an iBeacon: "+iBeacon.getProximityUuid()+","+iBeacon.getMajor()+","+iBeacon.getMinor());
            String displayString = iBeacon.getProximityUuid()+" "+iBeacon.getMajor()+" "+iBeacon.getMinor()+"\n";
            displayTableRow(iBeacon, displayString, false);
        }
    }

    @Override
    public void iBeaconDataUpdate(IBeacon iBeacon, IBeaconData iBeaconData, DataProviderException e) {
        if (e != null) {
            Log.d(TAG, "data fetch error:"+e);
        }
        if (iBeaconData != null) {
            Log.d(TAG, "I have an iBeacon with data: uuid="+iBeacon.getProximityUuid()+" major="+iBeacon.getMajor()+" minor="+iBeacon.getMinor()+" ScavengerhuntLocation="+iBeaconData.get("ScavengerHunt"));
            String displayString = iBeacon.getProximityUuid()+" "+iBeacon.getMajor()+" "+iBeacon.getMinor()+"\n"+"ScavengerHuntLocation:"+iBeaconData.get("scavengerHuntLocation");
            displayTableRow(iBeacon, displayString, true);
        }
    }
    private void displayTableRow(final IBeacon iBeacon, final String displayString, final boolean updateIfExists) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TableLayout table = (TableLayout) findViewById(R.id.beacon_table);
                String key = iBeacon.getProximity() + "-" + iBeacon.getMajor() + "-" + iBeacon.getMinor();
                TableRow tr = (TableRow) rowMap.get(key);
                if (tr == null) {
                    tr = new TableRow(MainActivity.this);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    rowMap.put(key, tr);
                    table.addView(tr);
                }
                else {
                    if (updateIfExists == false) {
                        return;
                    }
                }
                tr.removeAllViews();
                TextView textView=new TextView(MainActivity.this);
                textView.setText(displayString);
                tr.addView(textView);

            }
        });

    }


}
