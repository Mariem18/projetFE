package com.example.localisationdab;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GestionDAB extends AppCompatActivity {


   final  int first=Menu.FIRST;
   final int secnd=Menu.FIRST+1;

    ListView listView;
    Button btnAj;
    DatabaseAccess databaseAccess ;
    ListDabAdapter adapter;
    Toolbar toolbar;

    String selectedInst;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        MenuItem mSearch = menu.findItem(R.id.searchView);
        SearchView mSearchView = (SearchView) mSearch.getActionView();

        menu.removeItem(mSearchView.getId());
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                 return  false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                (GestionDAB.this).adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_dx: {
                Intent intent = new Intent(GestionDAB.this, HomeActivity.class);
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
        setContentView(R.layout.activity_gestion_dab);


        showList();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getMenu().removeItem(first);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnAj = findViewById(R.id.ic_btnAjout);
        btnAj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionDAB.this, AjouterDab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



    }
    public void showList(){

        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        Intent receivedIntent = getIntent();
        selectedInst = receivedIntent.getStringExtra("institut");

        listView = findViewById(R.id.listview);
        List<Dab> dab=databaseAccess.getInsDab(selectedInst);

        adapter=new ListDabAdapter(this,dab);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

             Long idDab = adapter.getItemId(position);

               databaseAccess.open();
               Cursor data = databaseAccess.getItemByID(idDab);
               double lat=0,lng=0;

               String adr="",etat="",inst="";

                 while (data.moveToNext()){
                   lat =data.getDouble(1);
                   lng =data.getDouble(2);
                   adr =data.getString(3);
                   etat=data.getString(4);
                   inst=data.getString(5);
                 }

                   Intent editScreenIntent = new Intent(GestionDAB.this,ModSupDab.class);
                    editScreenIntent.putExtra("id",idDab);
                    editScreenIntent.putExtra("latitude",lat);
                    editScreenIntent.putExtra("longitude",lng);
                    editScreenIntent.putExtra("adresse",adr);
                    editScreenIntent.putExtra("etat",etat);
                    editScreenIntent.putExtra("nom institut",inst);
                    editScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(editScreenIntent);

             }

        }
        );

        databaseAccess.close();
}
}





