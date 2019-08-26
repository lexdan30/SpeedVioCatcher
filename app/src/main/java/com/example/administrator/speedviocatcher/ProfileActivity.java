
package com.example.administrator.speedviocatcher;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView  textplate,textcr,textmake,textseries,textyrmodel,textBdate, textSex, textHT, textWT,textlicense_No,textlicense,textaddress,textdriver_name,textrestriction,textres_code,textagency,textexpiry;
    private CircleImageView disp_profilepic;
    public static String lisensya2= "";
    public static String car= "";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("/Drivers_Info/");
    DatabaseReference myRefs = database.getReference("/Vehicle_Info/");
    private static final String TAG = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*this.getSupportActionBar().setTitle("Profile");*/
        setTitle("Profile");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refIDs();

        SharedPreferences lisensyas = getSharedPreferences("MyApp", MODE_PRIVATE);
        lisensya2=lisensyas.getString("lisensya",  "UNKNOWN");

        Toast.makeText(ProfileActivity.this, "license number is "+ lisensya2, Toast.LENGTH_LONG).show();
        //search
        if (AppStatus.getInstance(this).isOnline(this)) {
            textlicense_No.setText(lisensya2);
            String lis="License No: "+lisensya2;
            textlicense.setText(lis);
            myRef.orderByChild("License_No").equalTo(lisensya2).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean flag=false;

                    for (com.google.firebase.database.DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Log.d(TAG, "PARENT: " + childDataSnapshot.getKey());
                        Log.d(TAG, "" + childDataSnapshot.child("License_No").getValue());
                        flag=true;

                        final String driversID = childDataSnapshot.child("Drivers_ID").getValue().toString();

                        String fname=childDataSnapshot.child("First_Name").getValue(String.class);
                        String mname=childDataSnapshot.child("Middle_Name").getValue(String.class);
                        String lname=childDataSnapshot.child("Last_Name").getValue(String.class);
                        textdriver_name.setText(fname+" "+mname+" "+lname);
                        textrestriction.setText(childDataSnapshot.child("Restriction_Name").getValue(String.class));
                        textaddress.setText(childDataSnapshot.child("Address").getValue(String.class));
                        textBdate.setText(childDataSnapshot.child("Date_of_Birth").getValue(String.class));
                        textSex.setText(childDataSnapshot.child("Gender").getValue(String.class));
                        textHT.setText(childDataSnapshot.child("Height").getValue(String.class));
                        textWT.setText(childDataSnapshot.child("Weight").getValue(String.class));
                        String resCode="Restriction_Code: "+childDataSnapshot.child("Restriction_Code").getValue(String.class);
                        String resAgency=childDataSnapshot.child("Agency").getValue(String.class);
                        String resExpiry=childDataSnapshot.child("Expiry_Date").getValue(String.class);
                        textres_code.setText(resCode);
                        textagency.setText(resAgency);
                        textexpiry.setText(resExpiry);
                        if(!driversID.equals("default")) {
                            Picasso.with(ProfileActivity.this).load(driversID).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.svclogo)
                                    .into(disp_profilepic, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Picasso.with(ProfileActivity.this).load(driversID).placeholder(R.drawable.svclogo).into(disp_profilepic);
                                        }
                                    });
                        }
                    }
                    if(!flag) {
                        errorUnfound();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                    Toast.makeText(ProfileActivity.this, " "+databaseError.toException().getMessage() , Toast.LENGTH_LONG).show();
                }
            });
            //=========================Vehicle

            SharedPreferences wheels = getSharedPreferences("MyApp", MODE_PRIVATE);
            car=wheels.getString("vehicle",  "UNKNOWN");
            String vehicle="Plate No: "+car;
            textplate.setText(vehicle);
            myRefs.orderByChild("Plate_No").equalTo(car).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean flag=false;

                    for (com.google.firebase.database.DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Log.d(TAG, "PARENT: " + childDataSnapshot.getKey());
                        Log.d(TAG, "" + childDataSnapshot.child("Plate_No").getValue());
                        flag=true;
                        String crn="CR No: "+childDataSnapshot.child("CR_No").getValue(String.class);
                        String mk="Make: "+childDataSnapshot.child("Make").getValue(String.class);
                        String ser="Series: "+childDataSnapshot.child("Series").getValue(String.class);
                        String year="Year Model: "+childDataSnapshot.child("Year_Model").getValue(String.class);
                        textcr.setText(crn);
                        textmake.setText(mk);
                        textseries.setText(ser);
                        textyrmodel.setText(year);
                    }
                    if(!flag) {
                        errorUnfound();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                    Toast.makeText(ProfileActivity.this, " "+databaseError.toException().getMessage() , Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(this,
                    "No Internet connection available\n(POWERED BY LEXDARK)", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();
        if(id==android.R.id.home);
        {
           // onBackPressed();
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void refIDs()
    {
        textdriver_name=(TextView)findViewById(R.id.driver_name);
        textlicense_No=(TextView)findViewById(R.id.license_No);
        textrestriction=(TextView)findViewById(R.id.restriction);
        textlicense=(TextView)findViewById(R.id.license);
        textaddress=(TextView)findViewById(R.id.address);
        textres_code=(TextView)findViewById(R.id.res_code);
        textexpiry=(TextView)findViewById(R.id.expiry);
        textagency=(TextView)findViewById(R.id.agency);
        textBdate=(TextView)findViewById(R.id.txtDate);
        textSex=(TextView)findViewById(R.id.txtSex);
        textHT=(TextView)findViewById(R.id.txtHt);
        textWT=(TextView)findViewById(R.id.txtWt);
        disp_profilepic = (CircleImageView)findViewById(R.id.profilepic);
        textplate=(TextView)findViewById(R.id.plate_no);
        textcr=(TextView)findViewById(R.id.cr_no);
        textmake=(TextView)findViewById(R.id.make);
        textseries=(TextView)findViewById(R.id.series);
        textyrmodel=(TextView)findViewById(R.id.year_model);
    }


        private void errorUnfound() {
        Toast.makeText(this,
        "No Record Found", Toast.LENGTH_LONG).show();
        }
}
