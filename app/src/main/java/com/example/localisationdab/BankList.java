package com.example.localisationdab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class BankList extends AppCompatActivity {

    DatabaseAccess databaseAccess;
    ArrayAdapter<String> adapter;
    ListView listView;
    Button btnAj;
    Toolbar toolbar;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        MenuItem mSearch = menu.findItem(R.id.searchView);
        SearchView mSearchView = (SearchView) mSearch.getActionView();


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return  false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                (BankList.this).adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.id_dx:{
                Intent intent =new Intent(BankList.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


try {


    this.listView = findViewById(R.id.listview2);
    btnAj = findViewById(R.id.ic_btnAjout2);

    btnAj.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(BankList.this, AjouterDab.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    });

    databaseAccess = DatabaseAccess.getInstance(this);
    databaseAccess.open();

    final List<String> dabs = databaseAccess.getInsData();

    adapter = new ArrayAdapter<>(this,R.layout.list_item,R.id.textView2, dabs);
    listView.setAdapter(adapter);


    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String inst = adapter.getItem(position);

            databaseAccess.open();
            List<Dab> list = databaseAccess.getInsDab(inst);

            Intent editScreenintent = new Intent(BankList.this, GestionDAB.class);
            editScreenintent.putExtra("institut", inst);
            editScreenintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(editScreenintent);
        }
    });

     }catch (Exception e){

     }
    }
}
