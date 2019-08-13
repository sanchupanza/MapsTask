package com.example.locationsharing;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;


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
    public Double lat1,lon1,lat2,lon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_maps);
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

                if(locationNo==0)
                {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(point).title("location number 1").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                  //  Toast.makeText(LocationMapsActivity.this, ""+point.latitude+point.latitude, Toast.LENGTH_SHORT).show();
                    l1.setText("("+String.valueOf(point.latitude)+")  ("+String.valueOf(point.latitude)+")");
                    lat1=point.latitude;
                    lon1=point.longitude;
                    locationNo=1;

                }else if(locationNo==1)
                {
                    mMap.addMarker(new MarkerOptions().position(point).title("location number 2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                 //   Toast.makeText(LocationMapsActivity.this, ""+point.latitude+point.latitude, Toast.LENGTH_SHORT).show();
                    l2.setText("("+String.valueOf(point.latitude)+")  ("+String.valueOf(point.latitude)+")");
                    lat2=point.latitude;
                    lon2=point.longitude;
                    locationNo=0;

                    findMiddleLocation(lat1,lon1,lat2,lon2);
                }
            }
        });
   //     mMap.setMyLocationEnabled(true);



       /* // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    private void findMiddleLocation(Double lat1, Double lon1, Double lat2, Double lon2)
    {

     /*   lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon2);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        double r = 6371;*/

        double dLon = Math.toRadians(lon2-lon1);
        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1)+Math.sin(lat2),Math.sqrt( (Math.cos(lat1)+Bx)*(Math.cos(lat1)+Bx) + By*By) );
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        Toast.makeText(this, ""+lat3+"  "+lon3, Toast.LENGTH_SHORT).show();
        l3.setText("("+String.valueOf(lat3)+")  ("+String.valueOf(lon3)+")");




        LatLng MiddlePoint = new LatLng(lat3, lon3);
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
