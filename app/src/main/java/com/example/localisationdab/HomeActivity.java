package com.example.localisationdab;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST=9001;


    Button btnCnt ,btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isServicesOK()) {
            init();
        }

        btnCnt=findViewById(R.id.idbtnCnMain);
        btnCnt.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeActivity.this,Authentification.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


    }


    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: cheking google services");
        int valable= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this);

        if (valable== ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google services is working");
            return true;

        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(valable)) {

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, valable, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    private void init(){
        btnMap =findViewById(R.id.idbtnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,Map.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }



}

