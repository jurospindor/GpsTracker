package jspindor.gpstracker.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jspindor.gpstracker.Data;
import jspindor.gpstracker.MainActivity;
import jspindor.gpstracker.models.Home;

public class GpsService extends Service implements LocationListener {
    private static final String TAG = "GpsService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0f;
    DatabaseReference database;
    DatabaseReference databaseImsi;
    private int dataId;
    private int mLocationInterval;

    String imsi;


    Location mLastLocation;
    GpsService mGpsLocationListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    GpsService mNetworkLocationListener;


    public GpsService() {
    }

    public GpsService(String provider, String aImsi, List<Home> homeLocations) {
        Log.e(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
        imsi = aImsi;
    }

    private void saveData(Data data) {
        database = FirebaseDatabase.getInstance().getReference();
        databaseImsi = database.child("users").child(imsi);
        databaseImsi.removeValue();
        //databaseImsi.child(String.valueOf(data.getId())).setValue(data);
        databaseImsi.push().setValue(data);
        dataId++;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        String positionStr = "long:" + String.valueOf(location.getLongitude())
                + "lat:" + String.valueOf(location.getLatitude())
                + "time:" + new Date(location.getTime()).toString();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "0");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "location changed");
        bundle.putString(FirebaseAnalytics.Param.VALUE, positionStr);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);
        Double longitude = Double.valueOf(location.getLongitude());
        Double latitude = Double.valueOf(location.getLatitude());
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy   ");
        String timestamp = ft.format(date).toString();
        Data data = new Data(dataId,longitude,latitude,timestamp);
        if(longitude != null)
            saveData(data);
        checkHome();
    }

    private void checkHome() {

    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }
    @Override
    public void onProviderEnabled(String provider)
    {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

        Log.e(TAG, "onStatusChanged: " + provider);
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        Bundle bundle = intent.getExtras();
        imsi = bundle.getString("imsi");
        mLocationInterval = bundle.getInt("interval");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        /*if(imsi.equals("231061900736521"))
            database.child("users").child(imsi).removeValue();*/
        //database.child("users").child(imsi).setValue("no data");
        //databaseImsi.setValue(null);

        database.child("users").child("123456").setValue("data");

        List<Home> homeLocations = new ArrayList<Home>();
        Home myHome = new Home("231061900736521","juraj dom",new LatLng(48.17414495959551,17.105091435983294),100l);
        homeLocations.add(myHome);
        GpsService mGpsLocationListener =  new GpsService(LocationManager.GPS_PROVIDER, imsi, homeLocations);
        //GpsService mNetworkLocationListener = new GpsService(LocationManager.NETWORK_PROVIDER, imsi);

        initializeLocationManager();
        /*try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, mLocationInterval, LOCATION_DISTANCE,
                    mNetworkLocationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }*/
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, mLocationInterval, LOCATION_DISTANCE,
                    mGpsLocationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        //service is destroyed with app
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e(TAG, "onCreate");
        //in onCreate of your service
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock cpuWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "gps_service");
        cpuWakeLock.acquire();

        // Release in onDestroy of your service
        if (cpuWakeLock.isHeld())
            cpuWakeLock.release();
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mGpsLocationListener);
                mLocationManager.removeUpdates(mNetworkLocationListener);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listner, ignore", ex);
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}