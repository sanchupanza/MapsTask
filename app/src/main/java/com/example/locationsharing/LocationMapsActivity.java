package com.example.locationsharing;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.WindowManager;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    private TextView l1,l2,l3;
    public int locationNo=0;
    public boolean firstLocation=true;
    public Double lat1,lon1,lat2,lon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_maps);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        l1 = (TextView)findViewById(R.id.location1);
        l2 = (TextView)findViewById(R.id.location2);
        l3 = (TextView)findViewById(R.id.location3);



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point)
            {

                if(firstLocation)
                {

                    mMap.clear();
                    l2.setText("");
                    l3.setText("");
                    mMap.addMarker(new MarkerOptions().position(point).title("location number 1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    l1.setText("("+String.valueOf(point.latitude)+")  ("+String.valueOf(point.longitude)+")");
                    lat1=point.latitude;
                    lon1=point.longitude;
                    locationNo=1;
                    firstLocation=false;

                }else if(!firstLocation)
                {
                    mMap.addMarker(new MarkerOptions().position(point).title("location number 2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    l2.setText("("+String.valueOf(point.latitude)+")  ("+String.valueOf(point.longitude)+")");
                    lat2=point.latitude;
                    lon2=point.longitude;

                    firstLocation=true;

                    findMiddleLocation(lat1,lon1,lat2,lon2);
                }
            }
        });
        mMap.setMyLocationEnabled(true);


    }

    private void findMiddleLocation(Double lat1, Double lon1, Double lat2, Double lon2)
    {




        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);


        l3.setText("("+String.valueOf(Math.toDegrees(lat3))+")  ("+String.valueOf(Math.toDegrees(lon3))+")");




        LatLng MiddlePoint = new LatLng(Math.toDegrees(lat3),Math.toDegrees(lon3));
        mMap.addMarker(new MarkerOptions().position(MiddlePoint).title("Middle Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MiddlePoint));
    }


    private void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


    }
}
