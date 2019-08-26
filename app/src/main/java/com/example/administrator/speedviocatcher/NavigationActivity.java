package com.example.administrator.speedviocatcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String emailnav2= "";
    public static String lisensya2= "";
    public static String TAG= "";
    TextView emailnav, unamenav;
    Button welcome;
    //ListView simpleList;
    ArrayAdapter arrayAdapter;
    String countryList[] = {"ESPRESSO","ESPRESSO DOPPIO","ESPRESSO MACCHIATO","ESPRESSO CONPANNA","BREWED COFFEE", "HOT CAFE AMERICANO", "HOT CAFE LATTE", "HOT LATTE MACCHIATO", "HOT CAPPUCCINO", "HOT CAFE MOCHA"};

    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    Firebase fName;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("/Drivers_Info/");
    ////==================================================================
/*    private String name;
    private Bluetooth b;
    private boolean registered=false;*/
    ////==================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

////==================================
 /*       b = new Bluetooth(this);
        b.enableBluetooth();
        b.setCommunicationCallback(mCallback);

        int pos = getIntent().getExtras().getInt("pos");
        name = b.getPairedDevices().get(pos).getName();

        Display("Connecting...");
        b.connectToDevice(b.getPairedDevices().get(pos));

        IntentFilter filters = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver, filters);
        registered=true;*/
        ////==================================
        /*LocalRecordFragment fragment=new LocalRecordFragment();*/
        MainFragment fragment=new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Speed VioCatcher");
        setSupportActionBar(toolbar);
        if (AppStatus.getInstance(this).isOnline(this)) {

            //Toast.makeText(getBaseContext(),
                    //"Internet connection available\n (POWERED BY LEXDARK)", Toast.LENGTH_LONG).show();
            // do your stuff
        }
        else
        {
            //Toast.makeText(getBaseContext(),
                    //"No Internet connection available\n(POWERED BY LEXDARK)", Toast.LENGTH_LONG).show();
        }

        /*arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_listviewtext, R.id.listviewtext, countryList);
        simpleList=(ListView)findViewById(R.id.simpleListView);
        simpleList.setAdapter(arrayAdapter);*/

        Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        //SAVE DATA ON intent
        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        emailnav2 = prefs.getString("email", "UNKNOWN");
        /*SharedPreferences prefs2 = getSharedPreferences("MyApp", MODE_PRIVATE);
        unamenav2 = prefs2.getString("uname", "UNKNOWN");*/
        //sAVE DATA ON TOAST

        //
        //fab
        //

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);
        emailnav = (TextView) header.findViewById(R.id.emailnav);
        unamenav = (TextView) header.findViewById(R.id.unamenav);
        emailnav.setText(emailnav2);

        //for searching via UID
        FirebaseUser user =  mAuth.getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Driver/"+uid).child("License");

        myRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String usern = dataSnapshot.getValue(String.class);
                unamenav.setText(usern);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
                Toast.makeText(NavigationActivity.this, " "+databaseError.toException().getMessage() , Toast.LENGTH_LONG).show();
            }
        });

        SharedPreferences lisensyas = getSharedPreferences("MyApp", MODE_PRIVATE);
        lisensya2=lisensyas.getString("lisensya",  "UNKNOWN");
        unamenav.setText(lisensya2);
        /*fName = new Firebase("https://viocatcher.firebaseio.com/AdminFile/" + uid + "/Username");

        fName.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String usern = dataSnapshot.getValue(String.class);
                unamenav.setText(usern);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
        //for searching via UID
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            //LoginManager.getInstance().logOut();
            toolbar.setTitle("Speed VioCatcher");
            Intent i = new Intent(this, Login.class);
            startActivity(i);

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //displaySelectedScreen(item.getItemId());
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            /*toolbar.setTitle("Home");*/

           /* MainFragment fragment=new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();*/

            Intent pairing = new Intent (NavigationActivity.this,SelectVehicle.class);
            pairing.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(pairing);
            finish();
        } else if (id == R.id.nav_viewdriver) {
            /*toolbar.setTitle("Drivers Profile");*/
            //Toast.makeText(this, "View Student is under construction!", Toast.LENGTH_SHORT).show();

           /* Intent profile = new Intent (NavigationActivity.this,ProfileActivity.class);
            startActivity(profile);*/

            MainFragment fragment=new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        }/*else if (id == R.id.nav_local) {
            toolbar.setTitle("Pending Report");
            LocalRecordFragment fragment=new LocalRecordFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        }*/
        else if (id == R.id.nav_selectvehicle) {
            /*toolbar.setTitle("Speedometer View");*/
           // Toast.makeText(this, "Please input ID number!", Toast.LENGTH_SHORT).show();
            /*DeleteFragment fragment=new DeleteFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();*/
            //Intent map = new Intent (NavigationActivity.this,SelectVehicle.class);
            Toast.makeText(this, "Coming soon.", Toast.LENGTH_SHORT).show();
            //startActivity(map);

        }else if (id == R.id.nav_violation) {
            /*toolbar.setTitle("Violation Record");*/
            //Toast.makeText(this, "View Student is under construction!", Toast.LENGTH_SHORT).show();
            Intent viorec = new Intent (NavigationActivity.this,CardViewActivity.class);
            startActivity(viorec);
        }
        else if (id == R.id.nav_settled) {
            Intent viosettled = new Intent (NavigationActivity.this,SettledViolationActivity.class);
            startActivity( viosettled );
            //finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //========================bluetooth
    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
    }
    public void Display(final String s){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
    private Bluetooth.CommunicationCallback mCallback = new Bluetooth.CommunicationCallback() {
        @Override
        public void onConnect(BluetoothDevice device) {
            Display("Connected to " + device.getName() + " - " + device.getAddress());
            //name
            //devName=name;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String car="DRIVING: "+name;

                }
            });
        }

        @Override
        public void onDisconnect(BluetoothDevice device, String message) {
            Display("Disconnected!");
            Display("Connecting again...");
            b.connectToDevice(device);
        }

        @Override
        public void onMessage(final String message) {
            Display(name + ": " + message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        @Override
        public void onError(String message) {
            Display("Error: " + message);
        }

        @Override
        public void onConnectError(final BluetoothDevice device, String message) {
            //Display("Error: " + message + ". Trying again in 3 sec...");
            Display("Error while connecting" + ". Trying again in 3 sec...");
            //Display("Trying again in 3 sec...");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            b.connectToDevice(device);
                        }
                    }, 2000);
                }
            });
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(NavigationActivity.this, SelectVehicle.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };*/

    //========================bluetooth
}
