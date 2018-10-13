package com.example.sargam.vacationplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    Button btn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String name,img,gender_str,quarter_str;
    ImageView profilepic;
    TextView Username;
    Spinner gender,quarter;




    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);




        btn=findViewById(R.id.logout);
        profilepic=findViewById(R.id.imageView);
        Username=findViewById(R.id.textView);
        gender=findViewById(R.id.gender);
        quarter=findViewById(R.id.quarter);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender_str=parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.quarter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarter.setAdapter(adapter2);

        quarter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quarter_str=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mAuth=FirebaseAuth.getInstance();

        Bundle b=getIntent().getExtras();
        name=b.getString("name");
        img=b.getString("img");




        Username.setText(name);
        //profilepic.setImageResource();


        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser()==null)
                {
                    Intent i=new Intent(SecondActivity.this,MainActivity.class);
                    startActivity(i);
                }

            }
        };


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mAuth.signOut();


            }
        });



    }


}
