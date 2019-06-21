package com.example.localisationdab;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Map";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int PERMISSION_REQUEST_ENABLE_GPS = 9001;
    private static final float DEFAULT_ZOOM = 15f;

    private EditText mSearchText;
    private ImageView mGps,mMapType;
    private Spinner spinner;

    DatabaseAccess database;
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    FusedLocationProviderClient mLocationProviderClient;
    ArrayAdapter<String> adapter;
    List<String> insList;
    Marker mMarker;




    /*
    *infowindow user
    * distance
    * type de map
    */

    @Override
    public void onMapReady(GoogleMap googleMap) {


        try {

            Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onMapReady: Map is ready ");
            mMap = googleMap;


            if (mLocationPermissionGranted) {
                getCurrentLocation();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                init();


                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);



            }


            mMap.setInfoWindowAdapter(new InfoWindowAdapter(Map.this));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    clearMarkers();
                    String item = parent.getItemAtPosition(position).toString();
                    if ( item.equals("Toutes les Banques") || item.equals("-BANK-")) {
                        showAllMarkers();
                    } else {
                        getSomeMarkers(item);
                        Toast.makeText(Map.this, "" + item, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    clearMarkers();
                    showAllMarkers();
                    Toast.makeText(Map.this, "Tous les DAB", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (SecurityException e){

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        database=DatabaseAccess.getInstance(this);

        spinner=findViewById(R.id.spinner);
        gettingInsData();


        mGps=findViewById(R.id.ic_gps);
        mMapType=findViewById(R.id.mapType);


        getLocationPermission();
        isMapEnable();

    }


    public void clearMarkers(){

        mMap.clear();
    }


    public void gettingInsData(){

        database.open();

        insList=database.getInsData();
        insList.add("Toutes les Banques");
        insList.add("Banques");



        adapter=new ArrayAdapter<String>(Map.this,android.R.layout.simple_list_item_1,insList){
            @Override
            public int getCount() {
                return super.getCount()-1;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

    }


    public  void getSomeMarkers(String insName){


        database.open();
        mMap.setInfoWindowAdapter(new InfoWindowAdapter(Map.this));
        List<Dab> dabs= database.getInsDab(insName);
        for(Dab dab: dabs){
            if(dab != null){
                try{

                    String info="Adresse: "+dab.getAdresse()+"\n" +"Etat: "+dab.getEtat()+"\n";
                    LatLng location = new LatLng(dab.getLatitude(),dab.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(location).title(dab.getNomInstitut()).snippet(info);
                    mMap.addMarker(markerOptions);

                }catch(NullPointerException e){
                    Log.e(TAG, "getSomeMarkers: "+ e.getMessage() );
                }

            }
        }
        database.close();



    }
    public void showAllMarkers(){


        database.open();

        List<Dab> dabs= database.getDab();
        for(Dab dab: dabs){
            String info="Adresse: "+dab.getAdresse()+"\n" +"Etat: "+dab.getEtat()+"\n";
            LatLng location = new LatLng(dab.getLatitude(),dab.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(location).title(dab.getNomInstitut()).snippet(info);

            mMarker= mMap.addMarker(markerOptions);

        }
        database.close();
    }

    private void moveCamera(LatLng latLng){

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Map.DEFAULT_ZOOM));


    }



    // search bar nerej3oulou be3deyn
//    private void geoLocate(){
//        Log.d(TAG, "geoLocate: geolocating");
//
//        String searchString =mSearchText.getText().toString();
//        Geocoder geocoder =new Geocoder(Map.this);
//        List<Address> list=new ArrayList<>();
//
//        try{
//            list=geocoder.getFromLocationName(searchString,1);
//        }
//        catch(IOException e){
//            Log.d(TAG, "geoLocate:IOException "+ e.getMessage());
//        }
//        if(list.size()>0){
//            Address address=list.get(0);
//            Log.d(TAG, "geoLocate: adresse: "+address.toString());
//            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM);
//            drawMarker(new LatLng(address.getLatitude(),address.getLongitude()),address.getAddressLine(0));
//        }
//
//    }

    private void getCurrentLocation() {
        Log.d(TAG, "getLastKnownLocation: called");
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
              getLocationPermission();
            if(mLocationPermissionGranted){
                final Task location=mLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if(location.isSuccessful()){

                                Location currentLocation = (Location) task.getResult();
                                if(currentLocation != null) {
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

                                }else{
                                    Toast.makeText(Map.this, "location indisponible", Toast.LENGTH_SHORT).show();
                                    buildAlertMsgNoGPS();
                                }
                        }
                        else{
                            Toast.makeText(Map.this, "unable to get current location", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }
        catch (SecurityException e){
            Log.e(TAG, "getCurrentLocation: security exception"+ e.getMessage() );
        }

    }




    private boolean isMapEnable() {

        Log.d(TAG, "getDeviceLocation: getting divice current location");
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMsgNoGPS();
            return false;
        }

        return true;
    }

    private void init(){

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();

            }
        });
        mMapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });


    }


    private void buildAlertMsgNoGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("L'app a besoin de GPS pour fonctioner ,voulez vous l'activer?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, PERMISSION_REQUEST_ENABLE_GPS);


                    }

                });

        final AlertDialog alert = builder.create();
        alert.show();

    }



    private void initMap(){

        Log.d(TAG, "initMap: inisializing map");
        if(mMap== null){
            SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(Map.this);
        }
    }


    private  void getLocationPermission(){

        Log.d(TAG, "getLocationPermission: getting location permission");
         String [] permissions ={Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

         if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

             if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                 mLocationPermissionGranted=true;
                 initMap();
             }
             else {
                 ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);

             }
         }
         else {
             ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);

         }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted=false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE :{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted=true;
                    initMap();
                }
            }

        }

    }




}
