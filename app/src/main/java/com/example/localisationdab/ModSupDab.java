package com.example.localisationdab;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
//import android.app.AlertDialog;
import android.view.Window;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static java.lang.Double.parseDouble;

public class ModSupDab extends AppCompatActivity {

    private static final String TAG = "ModSupDab ";
    EditText lat,lng,adrs,etat,insN;
    Button btnSup,btnMod;
    long selectedId;
    double selectedLat,selectedLng ;
    String selectedAdr,selectedEtat,selectedInst;
    DatabaseAccess databaseAccess;
    Toolbar toolbar;





    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        MenuItem mSearch = menu.findItem(R.id.searchView);
        SearchView mSearchView = (SearchView) mSearch.getActionView();

        menu.removeItem(mSearchView.getId());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.id_dx:{
                Intent intent =new Intent(ModSupDab.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modsup);

        setTitle("Modifier/Supprimer");

        try {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            databaseAccess = DatabaseAccess.getInstance(this);

            btnMod = findViewById(R.id.btnMod);
            btnSup = findViewById(R.id.btmSup);

            lat = findViewById(R.id.modsup_lat);
            lng = findViewById(R.id.modsup_lng);
            adrs = findViewById(R.id.modsup_adr);
            etat = findViewById(R.id.modsup_etat);
            insN = findViewById(R.id.modsup_inst);

            Intent receivedIntent = getIntent();
            selectedId = receivedIntent.getLongExtra("id", -1);
            selectedLat = receivedIntent.getDoubleExtra("latitude", 1);
            selectedLng = receivedIntent.getDoubleExtra("longitude", 1);
            selectedAdr = receivedIntent.getStringExtra("adresse");
            selectedEtat = receivedIntent.getStringExtra("etat");
            selectedInst = receivedIntent.getStringExtra("nom institut");

            //set the text to show the current selected name
            lat.setText("" + selectedLat);
            lng.setText("" + selectedLng);
            adrs.setText("" + selectedAdr);
            etat.setText("" + selectedEtat);
            insN.setText("" + selectedInst);



        btnMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String latTex=lat.getText().toString();
                String lngTex =lng.getText().toString();
                String adresTex=adrs.getText().toString();
                String etatTex=etat.getText().toString();
                String insText=insN.getText().toString();

                if(!(latTex.equals("") && lngTex.equals("") && adresTex.equals("") && etatTex.equals("") && insText.equals(""))){

                    double latD=parseDouble(latTex);
                    double lngD=parseDouble(lngTex);

                    databaseAccess.open();
                    databaseAccess.updateDAB(selectedId,latD,lngD,adresTex,etatTex,insText);

                    Toast.makeText(ModSupDab.this, "La modification a été effectuer ", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ModSupDab.this,BankList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else{
                    Toast.makeText(ModSupDab.this, "Les données sont manquantes", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final AlertDialog.Builder builder = new AlertDialog.Builder(ModSupDab.this);
                builder.setMessage("Vous voulez vraiment supprimer cette DAB?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseAccess.open();
                                databaseAccess.deleteDAB(selectedId);


                                Toast.makeText(ModSupDab.this, "Cette DAB a été supprimer", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ModSupDab.this,BankList.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                final AlertDialog alert = builder.create();
                alert.show();



            }
        });

        databaseAccess.close();
        }catch(Exception e){

            Log.e(TAG, "onCreate: ",e);
        }

        }
}
