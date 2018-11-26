package com.example.sargam.vacationplanner;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;
public class SecondActivity extends AppCompatActivity {
    Button btn,submit;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String name,img,gender_str,quarter_str,budget_str;
    ImageView profilepic;
    TextView Username, budget;
    Spinner gender,quarter;
    private final String TAG = getClass().getSimpleName();
    private String calendarId = null;

    private static final String[] CALENDAR_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private String[] userDetails = new String[3];

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
        submit=findViewById(R.id.Submit);
        profilepic=findViewById(R.id.imageView);
        Username=findViewById(R.id.textView);
        gender=findViewById(R.id.gender);
        quarter=findViewById(R.id.quarter);
        budget=findViewById(R.id.budget);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budget_str = budget.getText().toString();
                Log.d(TAG, budget_str);
                Intent i=new Intent(SecondActivity.this,ResultActivity.class);
                i.putExtra("params", new String[]{gender_str, quarter_str, budget_str});
                startActivity(i);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender_str=parent.getItemAtPosition(position).toString();
                Log.d(TAG, gender_str);
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
                Log.d(TAG, quarter_str);
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