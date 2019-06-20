package com.example.localisationdab;

import android.content.Intent;
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
import android.widget.Toast;

import static java.lang.Double.parseDouble;

public class AjouterDab extends AppCompatActivity {

    private static final String TAG = "AjouterDab";
    Button ajoutBtn;
    EditText lat,lng,adrs,etat,insNom;
    DatabaseAccess databaseAccess;
    Toolbar toolbar;


// type double edittext

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        MenuItem mSearch = menu.findItem(R.id.searchView);
        SearchView mSearchView = (SearchView) mSearch.getActionView();

        menu.removeItem(mSearchView.getId());
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.id_dx:{
                Intent intent =new Intent(AjouterDab.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_dab);
        setTitle("Nouveau DAB");
    toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        databaseAccess=DatabaseAccess.getInstance(this);


        lat=findViewById(R.id.txtLat);
        lng=findViewById(R.id.txtLng);
        adrs=findViewById(R.id.txtAdrs);
        etat=findViewById(R.id.txtEtat);
        insNom=findViewById(R.id.txtNInst);


        ajoutBtn=findViewById(R.id.btAjout);
        ajoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String latTxt=lat.getText().toString();
                String lngText=lng.getText().toString();
                String adrss=adrs.getText().toString();
                String etatt=etat.getText().toString();
                String nomIns=insNom.getText().toString();

                if (!(latTxt .isEmpty()) && !adrss.equals("") && !(lngText.equals("")) && !etatt.equals("") && !nomIns.equals(""))
                {
                    try{

                        databaseAccess.open();
                    double latD=parseDouble(latTxt);
                    double lngD=parseDouble(lngText);
                    databaseAccess.insertDAB(latD,lngD,adrss,etatt,nomIns);

                    Toast.makeText(AjouterDab.this, "bien ajouter", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AjouterDab.this,BankList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    }catch(Exception e){

                        Log.e(TAG, "onCreate: ",e);
                    }
                }else{
                    Toast.makeText(AjouterDab.this, "Les données sont manquantes", Toast.LENGTH_SHORT).show();
                }


            }
        });
        databaseAccess.close();



    }
}
