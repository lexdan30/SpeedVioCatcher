package com.example.administrator.speedviocatcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class SettledViolationActivity extends AppCompatActivity implements View.OnClickListener{

    //ArrayList<Person> violations = new ArrayList<Person>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    private static final String TAG = "View";
    public static String lisensya2= "";
    int counter=0;
    public static String key="";
    String num="";
    String ticket="";
    String penalty="";
    String status="";
    ImageView click;

    private static HashMap<String, SettledViolationActivity> violationRecords = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_viewtwo);
        /*this.getSupportActionBar().setTitle("Violation Record");*/
        setTitle("Settled Violation");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        click=(ImageView) findViewById(R.id.clickme);
        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
        Toast.makeText(SettledViolationActivity.this, "Click image to refresh..", Toast.LENGTH_LONG).show();
        click.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.clickme:
                click.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                click.playSoundEffect(SoundEffectConstants.CLICK);
                //Toast.makeText(CardViewActivity.this, "You clicked me : ", Toast.LENGTH_LONG).show();
                mRecyclerView.setAdapter(mAdapter);
                final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                click.startAnimation(myAnim);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //String text=(String) parent.getAdapter().getItem(position);
                Log.i(LOG_TAG, " Clicked on Item " + position);
                //Toast.makeText(CardViewActivity.this, "Clicked on Item"+ position, Toast.LENGTH_LONG).show();

            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
         final ArrayList results = new ArrayList<DataObject>();

        SharedPreferences lisensyas = getSharedPreferences("MyApp", MODE_PRIVATE);
        lisensya2=lisensyas.getString("lisensya",  "UNKNOWN");

                FirebaseDatabase databases = FirebaseDatabase.getInstance();
                DatabaseReference myRefs = databases.getReference("Settled_Violation").child(lisensya2);
                myRefs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int index = 0;
                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            counter++;
                        }
                        int i=0;
                        String arr[]=new String[counter];
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            //areaSnapshot.child("ID Number").getValue(String.class);
                            arr[i]=childDataSnapshot.child("Ticket_No").getValue().toString();
                            i++;
                            DataObject obj = new DataObject("Ticket No: " + childDataSnapshot.child("Ticket_No").getValue().toString(),"Settled Date: " +childDataSnapshot.child("Date_Settled").getValue().toString(),"Offense: " + childDataSnapshot.child("Offense").getValue().toString()," " +childDataSnapshot.child("Vehicle").getValue().toString()," Status: " + childDataSnapshot.child("Status").getValue().toString()," Penalty Paid: " + childDataSnapshot.child("Penalty_Paid").getValue().toString());

                            results.add(index, obj);
                            index++;
                        }
                        /*for(int m=0;m<counter;m++) {
                            Toast.makeText(CardViewActivity.this, "Ticket Number : " + arr[m], Toast.LENGTH_LONG).show();
                        }*/
                        if(counter==0)
                        {
                            Toast.makeText(SettledViolationActivity.this, "No violation record to display. ", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //===========================
        /*DataObject obj = new DataObject("Violation No: " + areaSnapshot.child("Violation Count").getValue(String.class),
                "Ticket No: " + areaSnapshot.child("Violation Ticket").getValue(String.class),"Penalty: " + areaSnapshot.child("Violation Penalty").getValue(String.class),"Status: " + areaSnapshot.child("Violation Status").getValue(String.class));

        results.add(index, obj);
        index++;*/
        return results;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();
        if(id==android.R.id.home);
        {
            // onBackPressed();
            Intent intent = new Intent(SettledViolationActivity.this, NavigationActivity.class);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
/*    public void didTapButton(View view) {
        click=(ImageButton) findViewById(R.id.clickme);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        click.startAnimation(myAnim);
    }*/
}
