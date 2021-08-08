package com.example.digitalshop.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.digitalshop.Interfaces.LocationListener;
import com.example.digitalshop.R;
import com.example.digitalshop.Utils.LocationProvider;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker marker;
    LatLng mylatLong ;
    TextView txtAddress;
    ImageView imgback;
    ImageView imgMyLocation;
    String CurrentAddress="";
    LatLng newLatLong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imgback=findViewById(R.id.imgBack);
        imgMyLocation=findViewById(R.id.imgMyLocation);

        txtAddress=findViewById(R.id.txtAddress);
        txtAddress.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtAddress.setSelected(true);




        imgback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        imgMyLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pickCurrentPlace();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions markerOptions = new MarkerOptions();

        if(mylatLong!=null)
        {
            marker=mMap.addMarker(markerOptions.position(mylatLong));
            marker.setTitle("Selected Location");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylatLong,15));
        }


        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);


        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
        {
            @Override
            public void onCameraIdle()
            {
                CameraPosition cameraPosition = googleMap.getCameraPosition();
                newLatLong = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                loadAddressOnBackGround(newLatLong);
            }
        });
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
        {
            @Override
            public void onCameraMove()
            {
                CameraPosition cameraPosition = googleMap.getCameraPosition();
                newLatLong = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);

               if(marker==null)
               {
                   marker=mMap.addMarker(markerOptions.position(newLatLong));
               }
                marker.setPosition(newLatLong);
                txtAddress.setText("Loading........");
            }
        });

    }

    private void loadAddressOnBackGround(LatLng newLatLong)
    {


        new AsyncTask<String, String, String>()

        {

            @Override
            protected String doInBackground(String... strings)
            {
                String address = getAddress(newLatLong.latitude, newLatLong.longitude);
                return address;
            }

            @Override
            protected void onPostExecute(String string)
            {

                CurrentAddress=string;
                txtAddress.setText(CurrentAddress);
                super.onPostExecute(string);
            }
        }.execute();


    }


    public String getAddress(double lat, double lng)   //This Function Returns Full Address In Text Format If We Provide it Lat And Long Of Some Palce
    {
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try
        {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if(addresses.size()>=1)
            {
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                System.out.println("ADDRESS : "+obj.getLocality());
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();

                return  obj.getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return  "";
    }


    private void pickCurrentPlace()
    {
        if (mMap == null)
        {
            return;
        }

        if(mylatLong!=null)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylatLong,15));
            MarkerOptions markerOptions = new MarkerOptions();
            if(marker==null)
              marker=mMap.addMarker(markerOptions.position(mylatLong));

            marker.setPosition(mylatLong);
        }
        else
        {
            loadLocation();
        }




    }


    private void loadLocation()
    {


        AlertDialog alertDialog=ProgressDialogManager.getProgressDialog(this);
        alertDialog.show();
        Util.loadLocation(this , new LocationListener()
        {
            @Override
            public void onLocationLoad (LatLng latLng) {

                mylatLong=latLng;
                if(alertDialog.isShowing())
                {
                    alertDialog.dismiss();
                    pickCurrentPlace();
                }
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

    }


    @SuppressLint("CommitPrefEdits")
    public void onClickDone (View view)
    {
        if(!CurrentAddress.isEmpty())
        {

            Intent returnIntent = new Intent();
            returnIntent.putExtra("Lat",newLatLong.latitude);
            returnIntent.putExtra("Long",newLatLong.longitude);
            returnIntent.putExtra("Address",CurrentAddress);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else
        {
            Snackbar.make(findViewById(android.R.id.content), "Please Select Valid Location", BaseTransientBottomBar.LENGTH_LONG).show();
        }


    }
}