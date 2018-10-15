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
    private Button signOutButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String name, img, gender_str, quarter_str;
    private ImageView profilepic;
    private TextView username;
    private Spinner gender, quarter;
    private FirebaseUser firebaseUser;
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
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        cur = cr.query(uri, CALENDAR_PROJECTION, CalendarContract.Calendars.VISIBLE + " = 1", null, CalendarContract.Calendars._ID + " ASC");
        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            Log.d(TAG, "inside while loop");
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            Log.d(TAG, String.valueOf(calID));
            if (accountName.contains("gmail.com")) {
                // got Google account, mostly synced to Google calendar
                this.calendarId = String.valueOf(calID);
                Log.d(TAG, accountName);
                break;
            }
        }

        uri = CalendarContract.Events.CONTENT_URI;
        /*String selection = CalendarContract.Calendars._ID + " = ? AND " + CalendarContract.Events.DTSTART + " = ? AND "
        + CalendarContract.Events.DTEND + " = ? ";
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 10, 12);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2018, 10, 12);
        String selectionArgs[] = {calendarId, String.valueOf(startDate.getTimeInMillis()), String.valueOf(endDate.getTimeInMillis())};*/
        String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
        };
        cur = cr.query(uri, EVENT_PROJECTION, CalendarContract.Events.VISIBLE + " = 1", null, null);
        while (cur.moveToNext()) {
            // getting query results
            String dtstart = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTSTART));
            String dtend = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTEND));
            Log.d(TAG + "DT Start", dtstart);
            Log.d(TAG + "DT End", dtend);
        }
        signOutButton =findViewById(R.id.logout);
        profilepic=findViewById(R.id.imageView);
        username =findViewById(R.id.textView);
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
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        username.setText(firebaseUser.getDisplayName());
        profilepic.setImageURI(firebaseUser.getPhotoUrl());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    Intent i=new Intent(SecondActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }
        };

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mAuth.signOut();
            }
        });
    }
}
