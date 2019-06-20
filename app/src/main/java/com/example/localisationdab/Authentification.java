package com.example.localisationdab;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Authentification extends AppCompatActivity {


    DatabaseAccess databaseAccess;
    Button btnCnt;
    EditText textLogin , textPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        databaseAccess=DatabaseAccess.getInstance(this);


        btnCnt=findViewById(R.id.idbtnCn);
        textLogin=findViewById(R.id.idEmail);
        textPass=findViewById(R.id.idPassw);

        btnCnt.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                String email=textLogin.getText().toString();
                String pass=textPass.getText().toString();

                databaseAccess.open();

                Cursor cursor =databaseAccess.getAdmin(email,pass);

                if(cursor!= null){
                    if(cursor.getCount()>0){
                        Toast.makeText(getApplicationContext(),"Bienvenue",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Authentification.this,BankList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),"votre E-mail ou Mot de passe n'est pas correct",Toast.LENGTH_LONG).show();

                    }
                }

            }


            });
        databaseAccess.close();

    }
}
