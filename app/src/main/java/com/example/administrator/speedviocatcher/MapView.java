package com.example.administrator.speedviocatcher;

/**
 * Created by Administrator on 12/13/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import com.squareup.picasso.Target;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import me.aflak.bluetooth.Bluetooth;

import static android.app.Service.START_STICKY;
import static com.example.administrator.speedviocatcher.AppStatus.context;
import static com.example.administrator.speedviocatcher.R.layout.dialog;

public class MapView extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    //protected MyApplication app;

    //==============Markerpic
    private final int ORANGE = 0xffffa500;//0xFFFF3300;
    public static String driversID = "";
    public static String fName = "";
    public static String lName = "";
    FirebaseDatabase dbaccount = FirebaseDatabase.getInstance();
    DatabaseReference myAcctRef = dbaccount.getReference("/Drivers_Info/");
    private static final String TAG = "MapView";
    //==============Markerpic
    //========================bluetooth
    private String name;
    private Bluetooth b;
    int tnum;
    int tnums;
    int vticket;
    //int vticket;String payment;
    Button blueKph, limit;
    FloatingActionButton fab,fab2;
    //FloatingActionButton center;
    TextView blueTooth, vehicle, time, restric, count, profname,load;
    ImageButton notif;
    CircleImageView profpic;
    //private Bluetooth.CommunicationCallback mCallback;
    private boolean registered = false;
    boolean active;
    boolean animbounce;
    boolean success;
    boolean explore;
    boolean minimum;
    public static String lisensya2 = "";
    private String payment;
    int speed;
    int speedlimit;
    //========================bluetooth
    private GoogleMap mMap;

    //=============db0
    double baniladlat;//Latitude
    double baniladlong;//Longitude
    String baniladLimit;
    int banilad1;
    //=============db1
    double banilad2lat;//Latitude
    double banilad2long;//Longitude
    String banilad2Limit;
    //=============db2
    double mabololat;//Latitude
    double mabololong;//Longitude
    String maboloLimit;
    //=============db3
    double mabolo2lat;//Latitude
    double mabolo2long;//Longitude
    String mabolo2Limit;
    //=============db4
    double smlat;//Latitude
    double smlong;//Longitude
    String smLimit;
    //=============db5
    double sm2lat;//Latitude
    double sm2long;//Longitude
    String sm2Limit;
    //=============db6
    double sm3lat;//Latitude
    double sm3long;//Longitude
    String sm3Limit;
    //=============db7
    double sergiolat;//Latitude
    double sergiolong;//Longitude
    String sergioLimit;
    //=============db8
    double sergio2lat;//Latitude
    double sergio2long;//Longitude
    String sergio2Limit;
    //=============db9
    double tunnellat;//Latitude
    double tunnellong;//Longitude
    String tunnelLimit;
    //=============db10
    double tunnel2lat;//Latitude
    double tunnel2long;//Longitude
    String tunnel2Limit;
    //=============db11
    double cscrlat;//Latitude
    double cscrlong;//Longitude
    String cscrLimit;
    //=============db12
    double cscr2lat;//Latitude
    double cscr2long;//Longitude
    String cscr2Limit;
    //=============db14
    double cscr3lat;//Latitude
    double cscr3long;//Longitude
    String cscr3Limit;
    //=============db15
    double cscr4lat;//Latitude
    double cscr4long;//Longitude
    String cscr4Limit;
    //=============db16
    double cscr5lat;//Latitude
    double cscr5long;//Longitude
    String cscr5Limit;
    //=============db13
    double rizallat;//Latitude
    double rizallong;//Longitude
    String rizalLimit;

    private static final LatLng THIRTYKPH = new LatLng(10.345268, 123.907426);
    private Marker paseostr;
    /*private static final LatLng SIXTYKPH = new LatLng(latitude, longitude);//10.339012, 123.911545
    private Marker baniladstr;*/
    private static final LatLng FOURTYKPH = new LatLng(10.323275, 123.909172);
    private Marker mabolostr;


    //10.327146, 123.981011 aspac////10.348958, 123.903147
    private static final LatLng TWENTYKPH = new LatLng(10.348958, 123.903147);
    private Marker mepszone;

    private LocationManager manager;
    private LocationListener locationListener;
    private String userId, userIds, userIdNotif,userPos;
    private Context mContext;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Ref = database.getReference("/Ticket/");
    FirebaseDatabase databases = FirebaseDatabase.getInstance();
    DatabaseReference myRefs, myNotifRef,myRefPos;
    //=================anims
    private final Handler mHandler;
    private Runnable mAnimation;
    private Marker mLastShownInfoWindowMarker = null;
    private PolylineOptions polyline;
    int vioCount=0;
    Marker driver_marker;
    Marker marker;
    LatLng pos;
    //LocationManager lm;
    LatLng latLng;
    private ProgressBar mProgressBar;
    private android.app.AlertDialog progressDialogs;
    Marker banilad2str;

    String distanceSignage;
    int warningTimer,ticketSlip;
    double range;

    //============Penalty
    String firstPenalty,secondPenalty,thirdPenalty ,fourthPenalty;
    //============Penalty
    //=====polyline
    private ArrayList<LatLng> points; //added
    Polyline line; //added
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    private static final float SMALLEST_DISPLACEMENT = 0.25F; //quarter of a meter
    private LocationRequest mLocationRequest;
    //=================
    BitmapDescriptor icon;

    public MapView() {
        mHandler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Get the application instance
        //app = (MyApplication)getApplication();
        points = new ArrayList<LatLng>();
        //createLocationRequest();
        rFIDs();
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#FF7F27'>Location Services Not Active</font>"));

            builder.setIcon(R.drawable.map7);
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>OK</font>"), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            //CheckBlueToothState();
        }
        //================second permission
        /*fn_permission();*/
        //================second permission

        SharedPreferences lisensyas = getSharedPreferences("MyApp", MODE_PRIVATE);
        lisensya2 = lisensyas.getString("lisensya", "UNKNOWN");
        myRefs = databases.getReference("Violation_Record").child(lisensya2);


        //=============
        FirebaseUser userpos = FirebaseAuth.getInstance().getCurrentUser();
        String uids = userpos.getUid();
        //FirebaseDatabase databas = FirebaseDatabase.getInstance();

        myRefPos = database.getReference().child("User/" + uids);
        //==========


        myNotifRef = databases.getReference("Overspeeding_Notification");
        //progressDialogs = new SpotsDialog(mContext, R.style.Custom);
        //progressDialogs.show();
        time.setVisibility(View.INVISIBLE);
        count.setVisibility(View.INVISIBLE);
        fab = (FloatingActionButton) findViewById(R.id.recenter);
        fab2 = (FloatingActionButton) findViewById(R.id.explore);
        fab.setEnabled(false);
        fab2.setEnabled(false);

        if (AppStatus.getInstance(this).isOnline(this)) {
            //==============profilepic
            myAcctRef.orderByChild("License_No").equalTo(lisensya2).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean flag = false;

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Log.d(TAG, "PARENT: " + childDataSnapshot.getKey());
                        Log.d(TAG, "" + childDataSnapshot.child("License_No").getValue());

                        driversID = childDataSnapshot.child("Drivers_ID").getValue().toString();
                        fName = childDataSnapshot.child("First_Name").getValue().toString();
                        lName = childDataSnapshot.child("Last_Name").getValue().toString();
                        profname.setText(fName+" "+lName);
                        if(!driversID.equals("default")) {
                            Picasso.with(MapView.this).load(driversID).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.svclogo)
                                    .into(profpic, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            final Animation myAnim = AnimationUtils.loadAnimation(MapView.this, R.anim.bounce);
                                            // Use bounce interpolator with amplitude 0.2 and frequency 20
                                            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                                            myAnim.setInterpolator(interpolator);

                                            profpic.startAnimation(myAnim);
                                        }

                                        @Override
                                        public void onError() {
                                            Picasso.with(MapView.this).load(driversID).placeholder(R.drawable.svclogo).into(profpic);
                                        }
                                    });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //==============profilepic
           /* Toast.makeText(getBaseContext(),
                    "Internet connection available\n (POWERED BY LEXDARK)", Toast.LENGTH_LONG).show();*/
            //===============================firebase==================================//

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("/Speed_Signage/");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //String baniladlats="",baniladlongs="";
                    String address = "";
                    int counter = 0;
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        address = areaSnapshot.child("Address").getValue().toString();
                        String key = areaSnapshot.getKey();
                        //if(address.equals("Banilad"))
                        if (key.equals("0")) {
                            /*baniladlats = areaSnapshot.child("latit").getValue().toString();
                            baniladlongs = areaSnapshot.child("longit").getValue().toString();*/
                            baniladlat = areaSnapshot.child("latit").getValue(Double.class);
                            baniladlong = areaSnapshot.child("longit").getValue(Double.class);
                            baniladLimit = areaSnapshot.child("Speed_Limit").getValue().toString();
                            banilad1 = Integer.parseInt(baniladLimit);
                        } else if (key.equals("1")) {
                            banilad2lat = areaSnapshot.child("latit").getValue(Double.class);
                            banilad2long = areaSnapshot.child("longit").getValue(Double.class);
                            banilad2Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("2")) {
                            mabololat = areaSnapshot.child("latit").getValue(Double.class);
                            mabololong = areaSnapshot.child("longit").getValue(Double.class);
                            maboloLimit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("3")) {
                            mabolo2lat = areaSnapshot.child("latit").getValue(Double.class);
                            mabolo2long = areaSnapshot.child("longit").getValue(Double.class);
                            mabolo2Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("9")) {
                            tunnellat = areaSnapshot.child("latit").getValue(Double.class);
                            tunnellong = areaSnapshot.child("longit").getValue(Double.class);
                            tunnelLimit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("10")) {
                            tunnel2lat = areaSnapshot.child("latit").getValue(Double.class);
                            tunnel2long = areaSnapshot.child("longit").getValue(Double.class);
                            tunnel2Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("4")) {
                            smlat = areaSnapshot.child("latit").getValue(Double.class);
                            smlong = areaSnapshot.child("longit").getValue(Double.class);
                            smLimit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("5")) {
                            sm2lat = areaSnapshot.child("latit").getValue(Double.class);
                            sm2long = areaSnapshot.child("longit").getValue(Double.class);
                            sm2Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("6")) {
                            sm3lat = areaSnapshot.child("latit").getValue(Double.class);
                            sm3long = areaSnapshot.child("longit").getValue(Double.class);
                            sm3Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("7")) {
                            sergiolat = areaSnapshot.child("latit").getValue(Double.class);
                            sergiolong = areaSnapshot.child("longit").getValue(Double.class);
                            sergioLimit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("8")) {
                            sergio2lat = areaSnapshot.child("latit").getValue(Double.class);
                            sergio2long = areaSnapshot.child("longit").getValue(Double.class);
                            sergio2Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("11")) {
                            cscrlat = areaSnapshot.child("latit").getValue(Double.class);
                            cscrlong = areaSnapshot.child("longit").getValue(Double.class);
                            cscrLimit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("12")) {
                            cscr2lat = areaSnapshot.child("latit").getValue(Double.class);
                            cscr2long = areaSnapshot.child("longit").getValue(Double.class);
                            cscr2Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("13")) {
                            rizallat = areaSnapshot.child("latit").getValue(Double.class);
                            rizallong = areaSnapshot.child("longit").getValue(Double.class);
                            rizalLimit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("14")) {
                            cscr3lat = areaSnapshot.child("latit").getValue(Double.class);
                            cscr3long = areaSnapshot.child("longit").getValue(Double.class);
                            cscr3Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("15")) {
                            cscr4lat = areaSnapshot.child("latit").getValue(Double.class);
                            cscr4long = areaSnapshot.child("longit").getValue(Double.class);
                            cscr4Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        } else if (key.equals("16")) {
                            cscr5lat = areaSnapshot.child("latit").getValue(Double.class);
                            cscr5long = areaSnapshot.child("longit").getValue(Double.class);
                            cscr5Limit = areaSnapshot.child("Speed_Limit").getValue().toString();
                        }
                        address = "";
                        counter++;
                    }
                    /*Toast.makeText(getBaseContext(),
                            "lONGLAT IS "+baniladlat+","+baniladlong, Toast.LENGTH_LONG).show();*/
                    /*Toast.makeText(getBaseContext(),
                            "counter is "+counter, Toast.LENGTH_LONG).show();*/
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Toast.makeText(getBaseContext(),lisensya2, Toast.LENGTH_LONG).show();

            //===============================firebase==================================//
            FirebaseDatabase datab = FirebaseDatabase.getInstance();
            DatabaseReference adjustmentRef = datab.getReference("/Map_Adjustment/");
            adjustmentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        String keys = areaSnapshot.getKey();
                    if (keys.equals("1")) {
                        //distanceSignage =dataSnapshot.child("distance_signage").getValue().toString();
                        //range = Integer.parseInt(distanceSignage);
                        //range = areaSnapshot.child("distance_signage").getValue(Double.class);
                        String Srange = areaSnapshot.child("distance_signage").getValue().toString();
                        range=Double.parseDouble(Srange);
                        String partialTimer = areaSnapshot.child("warning_timer").getValue().toString();
                        warningTimer = Integer.parseInt(partialTimer);
                        String ticketNumbering = areaSnapshot.child("ticketNo_update").getValue().toString();
                        ticketSlip = Integer.parseInt(ticketNumbering);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            //FirebaseDatabase data = FirebaseDatabase.getInstance();
            DatabaseReference penaltyRef = database.getReference("/Penalty_Record/");
            penaltyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        String keys = areaSnapshot.getKey();
                        if (keys.equals("1")) {
                            //distanceSignage =dataSnapshot.child("distance_signage").getValue().toString();
                            //range = Integer.parseInt(distanceSignage);
                            firstPenalty = areaSnapshot.child("first_penalty").getValue().toString();
                            secondPenalty = areaSnapshot.child("second_penalty").getValue().toString();
                            thirdPenalty = areaSnapshot.child("third_penalty").getValue().toString();
                            fourthPenalty = areaSnapshot.child("fourth_penalty").getValue().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });

            // do your stuff
            // Get Location Manager and check for GPS & Network location services

        } else {
           /* Toast.makeText(getBaseContext(),
                    "No Internet connection available\n(POWERED BY LEXDARK)", Toast.LENGTH_LONG).show();*/
        }
       /* this.getSupportActionBar().setTitle("Map View");*/
        setTitle("Speed Online Monitoring");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //=====loading
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        //get the location service
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //request the location update thru location manager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //get the latitude and longitude from the location
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                ArrayList<LatLng> list = new ArrayList<LatLng>();
                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(getApplicationContext());
                /*CameraUpdate center =
                        CameraUpdateFactory.newLatLng(new LatLng(10.339012, 123.911545));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);

                mMap.moveCamera(center);
                mMap.animateCamera(zoom);*/
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getSubLocality() + ":";
                    result += addresses.get(0).getLocality() + ":";
                    result += addresses.get(0).getCountryCode();
                    latLng = new LatLng(latitude, longitude);
                    pos=latLng;
                    //Marker.remove();
                    mMap.clear();

                    points.add(latLng); //added

                    if(active)
                    {
                        latitLong(pos);
                    }
                    redrawLine(); //added

                    /*Polyline line = mMap.addPolysline(new PolylineOptions()
                            .add(pos)
                            .width(5)
                            .color(Color.RED));*/

                    //===============memarker
                    /*mMap.addMarker(new MarkerOptions().position(latLng).title(result).icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.memarker)));*/



                    //list.add(latLng);
                    //drawPolyLineOnMap(list);
                   /* PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    for (int z = 0; z < list.size(); z++) {
                        LatLng point = list.get(z);
                        options.add(point);
                    }
                    polyline = mMap.addPolyline(options);*/

                    //polylines.setPoints(list);

                    //PolylineOptions polylines = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true).add(list);
                    //mMap.setOnPolylineClickListener(this);
                    //=====================
                    /*polyline = new PolylineOptions();
                    polyline.color(ORANGE);
                    polyline.width(5);
                    list.add(latLng);
                    polyline.addAll(list);
                    //mMap.addPolyline(polyline);
                    mMap.addPolyline(polyline);*/
                    //=====================


                    //==========
                    profileMarker(latLng);
                    //===============
                    //mMap.setMaxZoomPreference(18);


                    //Toast.makeText(getBaseContext(),
                            //range+"", Toast.LENGTH_LONG).show();
                    CircleOptions circleOptions = new CircleOptions()
                            .center(latLng)
                            .radius(range)
                            .strokeWidth(2)
                            .strokeColor(ORANGE)
                            .fillColor(Color.parseColor("#500084d3"));  //
                    // Supported formats are: #RRGGBB #AARRGGBB
                    //   #AA is the alpha, or amount of transparency

                    mMap.addCircle(circleOptions);

                    //----------setMapUI();
                    //=============//mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLng, 0));
                    //====================================================rotunda


                    /*Location mepzLocation = new Location("mepzlocation");
                    mepzLocation.setLatitude(10.348958);//10.348958, 123.903147
                    mepzLocation.setLongitude(123.903147);
                    mepszone = mMap.addMarker(new MarkerOptions()
                            .position(TWENTYKPH)
                            .title("20KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.twentyk)));

                    mepszone.setTag(0);

                    Location paseoLocation = new Location("paseolocation");
                    paseoLocation.setLatitude(10.345268);
                    paseoLocation.setLongitude(123.907426);
                    paseostr = mMap.addMarker(new MarkerOptions()
                            .position(THIRTYKPH)
                            .title("30KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.thirtyk)));

                    paseostr.setTag(0);
*/                  if(!explore)
                    {
                        resetCamera();
                    }

                    //==============================
                    final LatLng baniladFOURTYKPH = new LatLng(baniladlat, baniladlong);//10.339012, 123.911545//baniladlat, baniladlong
                    Marker baniladstr;

                    Location baniladLocation = new Location("baniladlocation");
                    baniladLocation.setLatitude(baniladlat);
                    baniladLocation.setLongitude(baniladlong);

                    baniladstr = mMap.addMarker(new MarkerOptions()
                            .position(baniladFOURTYKPH)
                            .title(baniladLimit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(baniladLimit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.fourtyk)));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    baniladstr.setTag(0);

                    //=================================
                    final LatLng banilad2FOURTYKPH = new LatLng(banilad2lat, banilad2long);//10.339012, 123.911545//baniladlat, baniladlong
                    //Marker banilad2str;

                    Location banilad2Location = new Location("banilad2location");
                    banilad2Location.setLatitude(banilad2lat);
                    banilad2Location.setLongitude(banilad2long);

                    banilad2str = mMap.addMarker(new MarkerOptions()
                            .position(banilad2FOURTYKPH)
                            .title(banilad2Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(banilad2Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    banilad2str.setTag(0);

                    //=================================
                    final LatLng maboloFOURTYKPH = new LatLng(mabololat, mabololong);//10.339012, 123.911545//baniladlat, baniladlong
                    Marker mabolostr;

                    Location maboloLocation = new Location("mabololocation");
                    maboloLocation.setLatitude(mabololat);
                    maboloLocation.setLongitude(mabololong);

                    mabolostr = mMap.addMarker(new MarkerOptions()
                            .position(maboloFOURTYKPH)
                            .title(maboloLimit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(maboloLimit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    mabolostr.setTag(0);
                    //=================================
                    final LatLng mabolo2TWENTYKPH = new LatLng(mabolo2lat, mabolo2long);//10.339012, 123.911545//baniladlat, baniladlong
                    Marker mabolo2str;

                    Location mabolo2Location = new Location("mabolo2location");
                    mabolo2Location.setLatitude(mabolo2lat);
                    mabolo2Location.setLongitude(mabolo2long);

                    mabolo2str = mMap.addMarker(new MarkerOptions()
                            .position(mabolo2TWENTYKPH)
                            .title(mabolo2Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(mabolo2Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    mabolo2str.setTag(0);
                    //=================================
                    final LatLng smFOURTYKPH = new LatLng(smlat, smlong);//10.339012, 123.911545//baniladlat, baniladlong
                    Marker smstr;

                    Location smLocation = new Location("smlocation");
                    smLocation.setLatitude(smlat);
                    smLocation.setLongitude(smlong);

                    smstr = mMap.addMarker(new MarkerOptions()
                            .position(smFOURTYKPH)
                            .title(smLimit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(smLimit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    smstr.setTag(0);
                    //=================================
                    final LatLng sm2FOURTYKPH = new LatLng(sm2lat, sm2long);//10.339012, 123.911545//baniladlat, baniladlong
                    Marker sm2str;

                    Location sm2Location = new Location("sm2location");
                    sm2Location.setLatitude(sm2lat);
                    sm2Location.setLongitude(sm2long);

                    sm2str = mMap.addMarker(new MarkerOptions()
                            .position(sm2FOURTYKPH)
                            .title(sm2Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(sm2Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    sm2str.setTag(0);
                    //=================================
                    final LatLng sm3FOURTYKPH = new LatLng(sm3lat, sm3long);//10.339012, 123.911545//baniladlat, baniladlong
                    Marker sm3str;

                    Location sm3Location = new Location("sm3location");
                    sm3Location.setLatitude(sm3lat);
                    sm3Location.setLongitude(sm3long);

                    sm3str = mMap.addMarker(new MarkerOptions()
                            .position(sm3FOURTYKPH)
                            .title(sm3Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(sm3Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    sm3str.setTag(0);
                    //=================================
                    final LatLng sergioFOURTYKPH = new LatLng(sergiolat, sergiolong);//10.339012, 123.911545//baniladlat, baniladlong
                    Marker sergiostr;

                    Location sergioLocation = new Location("sergiolocation");
                    sergioLocation.setLatitude(sergiolat);
                    sergioLocation.setLongitude(sergiolong);

                    sergiostr = mMap.addMarker(new MarkerOptions()
                            .position(sergioFOURTYKPH)
                            .title(sergioLimit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(sergioLimit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    sergiostr.setTag(0);
                    //=================================
                    final LatLng sergio2FOURTYKPH = new LatLng(sergio2lat, sergio2long);//10.339012, 123.911545//baniladlat, baniladlong
                    Marker sergio2str;

                    Location sergio2Location = new Location("sergio2location");
                    sergio2Location.setLatitude(sergio2lat);
                    sergio2Location.setLongitude(sergio2long);

                    sergio2str = mMap.addMarker(new MarkerOptions()
                            .position(sergio2FOURTYKPH)
                            .title(sergio2Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(sergio2Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    sergio2str.setTag(0);
                    //=================================
                    final LatLng tunnelMinSixty = new LatLng(tunnellat, tunnellong);
                    Marker tunnelstr;

                    Location tunnelLocation = new Location("tunnellocation");
                    tunnelLocation.setLatitude(tunnellat);
                    tunnelLocation.setLongitude(tunnellong);

                    tunnelstr = mMap.addMarker(new MarkerOptions()
                            .position(tunnelMinSixty)
                            .title(tunnelLimit+"KPH")
                            .snippet("minimum \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitchGreen(tunnelLimit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    tunnelstr.setTag(0);
                    //=================================
                    final LatLng tunnel2MinSixty = new LatLng(tunnel2lat, tunnel2long);
                    Marker tunnel2str;

                    Location tunnel2Location = new Location("tunnel2location");
                    tunnel2Location.setLatitude(tunnel2lat);
                    tunnel2Location.setLongitude(tunnel2long);

                    tunnel2str = mMap.addMarker(new MarkerOptions()
                            .position(tunnel2MinSixty)
                            .title(tunnel2Limit+"KPH")
                            .snippet("minimum \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitchGreen(tunnel2Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    tunnel2str.setTag(0);
                    //=================================
                    final LatLng cscrMinSixty = new LatLng(cscrlat, cscrlong);
                    Marker cscrstr;

                    Location cscrLocation = new Location("cscrlocation");
                    cscrLocation.setLatitude(cscrlat);
                    cscrLocation.setLongitude(cscrlong);

                    cscrstr = mMap.addMarker(new MarkerOptions()
                            .position(cscrMinSixty)
                            .title(cscrLimit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(cscrLimit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    cscrstr.setTag(0);
                    //=================================
                    final LatLng cscr2MinSixty = new LatLng(cscr2lat, cscr2long);
                    Marker cscr2str;

                    Location cscr2Location = new Location("cscr2location");
                    cscr2Location.setLatitude(cscr2lat);
                    cscr2Location.setLongitude(cscr2long);

                    cscr2str = mMap.addMarker(new MarkerOptions()
                            .position(cscr2MinSixty)
                            .title(cscr2Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(cscr2Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    cscr2str.setTag(0);
                    //=================================
                    final LatLng cscr3MinSixty = new LatLng(cscr3lat, cscr3long);
                    Marker cscr3str;

                    Location cscr3Location = new Location("cscr3location");
                    cscr3Location.setLatitude(cscr3lat);
                    cscr3Location.setLongitude(cscr3long);

                    cscr3str = mMap.addMarker(new MarkerOptions()
                            .position(cscr3MinSixty)
                            .title(cscr3Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(cscr3Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    cscr3str.setTag(0);
                    //=================================
                    final LatLng cscr4MinSixty = new LatLng(cscr4lat, cscr4long);
                    Marker cscr4str;

                    Location cscr4Location = new Location("cscr4location");
                    cscr4Location.setLatitude(cscr4lat);
                    cscr4Location.setLongitude(cscr4long);

                    cscr4str = mMap.addMarker(new MarkerOptions()
                            .position(cscr4MinSixty)
                            .title(cscr4Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(cscr4Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    cscr4str.setTag(0);
                    //=================================
                    final LatLng cscr5MinSixty = new LatLng(cscr5lat, cscr5long);
                    Marker cscr5str;

                    Location cscr5Location = new Location("cscr5location");
                    cscr5Location.setLatitude(cscr5lat);
                    cscr5Location.setLongitude(cscr5long);

                    cscr5str = mMap.addMarker(new MarkerOptions()
                            .position(cscr5MinSixty)
                            .title(cscr5Limit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(cscr5Limit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    cscr5str.setTag(0);
                    //=================================
                    final LatLng rizalMinSixty = new LatLng(rizallat, rizallong);
                    Marker rizalstr;

                    Location rizalLocation = new Location("rizallocation");
                    rizalLocation.setLatitude(rizallat);
                    rizalLocation.setLongitude(rizallong);

                    rizalstr = mMap.addMarker(new MarkerOptions()
                            .position(rizalMinSixty)
                            .title(rizalLimit+"KPH")
                            .snippet("allowable \nspeed limit")
                            .icon(BitmapDescriptorFactory.fromResource(getIconUsingSwitch(rizalLimit))));
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
                    rizalstr.setTag(0);


                    /*if (location.distanceTo(paseoLocation) < range) {
                        minimum=false;
                        marker=paseostr;
                        //Toast.makeText(MapView.this, "Max speed is 30KPH", Toast.LENGTH_LONG).show();
                        limit.setText(baniladLimit);
                        paseostr.showInfoWindow();
                        mLastShownInfoWindowMarker = paseostr;
                        success=false;
                        click(paseostr);
                    } else if (location.distanceTo(mepzLocation) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "Paghinay sa pangpang!...", Toast.LENGTH_LONG).show();
                        limit.setText("20");
                        marker=mepszone;
                        mepszone.showInfoWindow();
                        mLastShownInfoWindowMarker = mepszone;
                        success=false;
                        click(mepszone);
                    } else*/
                    if (location.distanceTo(baniladLocation) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        success=false;
                        limit.setText(baniladLimit);
                        marker=baniladstr;
                        baniladstr.showInfoWindow();
                        mLastShownInfoWindowMarker =baniladstr;
                        click(baniladstr);
                    } else if (location.distanceTo(banilad2Location) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        limit.setText(banilad2Limit);
                        success=false;
                        marker=banilad2str;
                        banilad2str.showInfoWindow();
                        mLastShownInfoWindowMarker = banilad2str;
                        click(banilad2str);
                    } else if (location.distanceTo(maboloLocation) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        limit.setText(maboloLimit);
                        success=false;
                        marker= mabolostr;
                        mabolostr.showInfoWindow();
                        mLastShownInfoWindowMarker = mabolostr;
                        click(mabolostr);
                    } else if (location.distanceTo(mabolo2Location) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        limit.setText(mabolo2Limit);
                        success=false;
                        marker= mabolo2str;
                        mabolo2str.showInfoWindow();
                        mLastShownInfoWindowMarker = mabolo2str;
                        click(mabolo2str);
                    } else if (location.distanceTo(smLocation) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        limit.setText(smLimit);
                        success=false;
                        marker= smstr;
                        smstr.showInfoWindow();
                        mLastShownInfoWindowMarker = smstr;
                        click(smstr);
                    } else if (location.distanceTo(sm2Location) < range) {
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        limit.setText(sm2Limit);
                        success=false;
                        marker= sm2str;
                        sm2str.showInfoWindow();
                        mLastShownInfoWindowMarker = sm2str;
                        click(sm2str);
                    } else if (location.distanceTo(sm3Location) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        limit.setText(sm3Limit);
                        success=false;
                        marker= sm3str;
                        sm3str.showInfoWindow();
                        mLastShownInfoWindowMarker = sm3str;
                        click(sm3str);
                    } else if (location.distanceTo(sergioLocation) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        limit.setText(sergioLimit);
                        success=false;
                        marker= sergiostr;
                        sergiostr.showInfoWindow();
                        mLastShownInfoWindowMarker = sergiostr;
                        click(sergiostr);
                    } else if (location.distanceTo(sergio2Location) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "You are in banilad...", Toast.LENGTH_LONG).show();
                        limit.setText(sergio2Limit);
                        success=false;
                        marker= sergio2str;
                        sergio2str.showInfoWindow();
                        mLastShownInfoWindowMarker = sergio2str;
                        click(sergio2str);
                    } else if (location.distanceTo(tunnelLocation) < range) {
                        minimum=true;
                        limit.setText(tunnelLimit);
                        success=false;
                        marker= tunnelstr;
                        tunnelstr.showInfoWindow();
                        mLastShownInfoWindowMarker = tunnelstr;
                        click(tunnelstr);
                        //if below 60kph
                        if (location.distanceTo(tunnelLocation) < range)
                        {
                            String speedo = blueKph.getText().toString();
                            speed = Integer.parseInt(speedo);
                            String limitation = limit.getText().toString();
                            speedlimit = Integer.parseInt(limitation);
                            if(speed<speedlimit){
                            /*LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast,
                                    (ViewGroup) findViewById(R.id.toast_layout_root));

                            ImageView image = (ImageView) layout.findViewById(R.id.image);
                            image.setImageResource(R.drawable.sixtygreen);
                            TextView text = (TextView) layout.findViewById(R.id.text);
                            text.setText("Speed must be minimum of 60KPH!");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();*/

                                Context context=getApplicationContext();
                                LayoutInflater inflater=getLayoutInflater();

                                View customToastroot =inflater.inflate(R.layout.toast, null);

                                Toast customtoast=new Toast(context);

                                customtoast.setView(customToastroot);
                                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
                                customtoast.setDuration(Toast.LENGTH_LONG);
                                customtoast.show();
                            }
                        }
                    } else if (location.distanceTo(tunnel2Location) < range) {
                        minimum=true;
                        limit.setText(tunnel2Limit);
                        success=false;
                        marker= tunnel2str;
                        tunnel2str.showInfoWindow();
                        mLastShownInfoWindowMarker = tunnel2str;
                        click(tunnel2str);
                        //if below 60kph
                        if (location.distanceTo(tunnel2Location) < range)
                        {
                            String speedo = blueKph.getText().toString();
                            speed = Integer.parseInt(speedo);
                            String limitation = limit.getText().toString();
                            speedlimit = Integer.parseInt(limitation);
                            if(speed<speedlimit)
                            {
                            /*LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast,
                                    (ViewGroup) findViewById(R.id.toast_layout_root));

                            ImageView image = (ImageView) layout.findViewById(R.id.image);
                            image.setImageResource(R.drawable.sixtygreen);
                            TextView text = (TextView) layout.findViewById(R.id.text);
                            text.setText("Speed must be minimum of 60KPH!");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();*/

                                Context context=getApplicationContext();
                                LayoutInflater inflater=getLayoutInflater();

                                View customToastroot =inflater.inflate(R.layout.toast, null);

                                Toast customtoast=new Toast(context);

                                customtoast.setView(customToastroot);
                                customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
                                customtoast.setDuration(Toast.LENGTH_LONG);
                                customtoast.show();
                            }
                        }
                    } else if (location.distanceTo(cscrLocation) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "Minimum of 60kph speed.", Toast.LENGTH_LONG).show();
                        limit.setText(cscrLimit);
                        success=false;
                        marker= cscrstr;
                        cscrstr.showInfoWindow();
                        mLastShownInfoWindowMarker = cscrstr;
                        click(cscrstr);
                    } else if (location.distanceTo(cscr2Location) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "Minimum of 60kph speed.", Toast.LENGTH_LONG).show();
                        limit.setText(cscr2Limit);
                        success=false;
                        marker= cscr2str;
                        cscr2str.showInfoWindow();
                        mLastShownInfoWindowMarker = cscr2str;
                        click(cscr2str);
                    } else if (location.distanceTo(cscr3Location) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "Minimum of 60kph speed.", Toast.LENGTH_LONG).show();
                        limit.setText(cscr3Limit);
                        success=false;
                        marker= cscr3str;
                        cscr3str.showInfoWindow();
                        mLastShownInfoWindowMarker = cscr3str;
                        click(cscr3str);
                    } else if (location.distanceTo(cscr4Location) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "Minimum of 60kph speed.", Toast.LENGTH_LONG).show();
                        limit.setText(cscr4Limit);
                        success=false;
                        marker= cscr4str;
                        cscr4str.showInfoWindow();
                        mLastShownInfoWindowMarker = cscr4str;
                        click(cscr4str);
                    } else if (location.distanceTo(cscr5Location) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "Minimum of 60kph speed.", Toast.LENGTH_LONG).show();
                        limit.setText(cscr5Limit);
                        success=false;
                        marker= cscr5str;
                        cscr5str.showInfoWindow();
                        mLastShownInfoWindowMarker = cscr5str;
                        click(cscr5str);
                    } else if (location.distanceTo(rizalLocation) < range) {
                        minimum=false;
                        //Toast.makeText(MapView.this, "Minimum of 60kph speed.", Toast.LENGTH_LONG).show();
                        limit.setText(rizalLimit);
                        success=false;
                        marker= rizalstr;
                        rizalstr.showInfoWindow();
                        mLastShownInfoWindowMarker = rizalstr;
                        click(rizalstr);
                    } else {
                        minimum=false;
                        //successs=false;
                        /*marker= driver_marker;
                        //successs=false;
                        while (!successs) {
                            try {
                                Timer timer = new Timer();
                                TimerTask updateProfile = new CustomTimerTask(MapView.this);
                                timer.scheduleAtFixedRate(updateProfile, 10,5000);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            successs = true;
                        }*/
                        //Toast.makeText(MapView.this, "NONE SPEEDLIMIT ZONE...", Toast.LENGTH_LONG).show();
                        //limit.setText("0");
                        if (mLastShownInfoWindowMarker != null && mLastShownInfoWindowMarker.isInfoWindowShown()) {
                            mLastShownInfoWindowMarker.hideInfoWindow();
                        }
                    }

                }catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (!success) {
                    try {
                        fab.setEnabled(true);
                        fab2.setEnabled(true);
                        //progressDialogs.show();
                        /*final Animation myAnim = AnimationUtils.loadAnimation(MapView.this, R.anim.bounce);
                        // Use bounce interpolator with amplitude 0.2 and frequency 20
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                        myAnim.setInterpolator(interpolator);

                        fab.startAnimation(myAnim);*/
                        resetCamera();
                        success = true;
                        mProgressBar.setVisibility(View.GONE);
                        load.setVisibility(View.GONE);
                       /* click(paseostr);
                        click(mepszone);
                        click(baniladstr);
                        click(banilad2str);
                        click(mabolostr);
                        click(mabolo2str);
                        click(smstr);
                        click(sm2str);
                        click(sm3str);
                        click(sergiostr);
                        click(sergio2str);
                        click(tunnelstr);
                        click(tunnel2str);
                        click(cscrstr);
                        click(cscr2str);
                        click(cscr3str);
                        click(cscr4str);
                        click(cscr5str);
                        click(rizalstr);*/
                        //progressDialogs.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        //========================bluetooth
        b = new Bluetooth(MapView.this);
        b.enableBluetooth();

        b.setCommunicationCallback(mCallback);

        int pos = getIntent().getExtras().getInt("pos");
        name = b.getPairedDevices().get(pos).getName();

        Display("Connecting...");
        b.connectToDevice(b.getPairedDevices().get(pos));

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered = true;
        //========================bluetooth
        Notif();

        /*if (mMap != null) {
            final Animation myAnim = AnimationUtils.loadAnimation(MapView.this, R.anim.bounce);
            // Use bounce interpolator with amplitude 0.2 and frequency 20
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);

            fab.startAnimation(myAnim);
            fab.setEnabled(true);
            //Toast.makeText(MapView.this, "Tracking your location.", Toast.LENGTH_LONG).show();
        }else{
            //mMap.setOnMapLoadedCallback(this);
            Toast.makeText(MapView.this, "Please wait, trying to load location.", Toast.LENGTH_LONG).show();
        }*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(MapView.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                fab.startAnimation(myAnim);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    public void onMapLoaded() {
                        if (mMap != null) {
                            resetCamera();
                            explore=false;
                            Toast.makeText(MapView.this, "Center mode.", Toast.LENGTH_LONG).show();
                            //Toast.makeText(MapView.this, "Tracking your location.", Toast.LENGTH_LONG).show();
                        }else{
                            //mMap.setOnMapLoadedCallback(this);
                            Toast.makeText(MapView.this, "Please wait, trying to load location.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(MapView.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                fab2.startAnimation(myAnim);
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    public void onMapLoaded() {
                        if (mMap != null) {
                            explore=true;
                            Toast.makeText(MapView.this, "Explore mode.", Toast.LENGTH_LONG).show();
                            //Toast.makeText(MapView.this, "Tracking your location.", Toast.LENGTH_LONG).show();
                        }else{
                            //mMap.setOnMapLoadedCallback(this);
                            Toast.makeText(MapView.this, "Please wait, trying to load location.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
    public void resetCamera()
    {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                //.zoom(18)
                .bearing(90)
                .tilt(60)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setMinZoomPreference(4.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        mMap.animateCamera(update);
    }
    private void Recenter() {
        resetCamera();
    }
    private void Notif() {
        notif.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapView.this);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF7F27'>Confirm Exit...</font>"));
                // Icon Of Alert Dialog
                alertDialogBuilder.setIcon(R.drawable.map7);
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage("Are you sure, you want to exit?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MapView.this, "Vehicle Disconnected.", Toast.LENGTH_LONG).show();
                        // onBackPressed();
                        b.removeCommunicationCallback();
                        b.disconnect();
                        Intent intent = new Intent(MapView.this, CardViewActivity.class);
                        //======timan.e
                        //b.disableBluetooth();
                        //=======timan.e
                        startActivity(intent);
                        finish();
                    }
                });

                alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>No</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ///Toast.makeText(MainActivity.this,"You clicked over No",Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setNeutralButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                try {
                    Resources resources = alertDialog.getContext().getResources();
                    int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
                    TextView alertTitle = (TextView) alertDialog.getWindow().getDecorView().findViewById(alertTitleId);
                    alertTitle.setTextColor(Color.MAGENTA); // change title text color

                    int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
                    View titleDivider = alertDialog.getWindow().getDecorView().findViewById(titleDividerId);
                    titleDivider.setBackgroundColor(Color.YELLOW); // change divider color
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                //Set negative button background
                //nbutton.setBackgroundColor(Color.MAGENTA);
                //Set negative button text color
                nbutton.setTextColor(Color.BLACK);
                Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                //Set positive button background
                //pbutton.setBackgroundColor(Color.YELLOW);
                //Set positive button text color
                pbutton.setTextColor(Color.BLACK);
                Button cbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                //Set positive button background
                //pbutton.setBackgroundColor(Color.YELLOW);
                //Set positive button text color
                cbutton.setTextColor(Color.BLACK);


                //=================================original
                /*b.removeCommunicationCallback();
                b.disconnect();
                Intent record = new Intent(MapView.this, CardViewActivity.class);
                startActivity(record);*/
            }
        });

    }

    //========================bluetooth
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registered) {
            unregisterReceiver(mReceiver);
            registered = false;
        }
    }

    public void Display(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Toast.makeText(Chat.this, s + "\n", Toast.LENGTH_LONG).show();


                /*LayoutInflater inflater = getLayoutInflater();

                View layout = inflater.inflate(R.layout.activity_custom, (ViewGroup)findViewById(R.id.root));

                TextView text_toast = (TextView) layout.findViewById(R.id.textView1);
                text_toast.setText(s + "\n");

                Toast toast = new Toast(getApplicationContext());

                toast.setGravity(Gravity.TOP, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();*/


                //=====https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
                //======isNumeric
                if (!android.text.TextUtils.isDigitsOnly(s)) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.mapview), s + "\n", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();//findViewById(android.R.id.content)
                } else {
                    speed = Integer.parseInt(s);
                    String limitation = limit.getText().toString();
                    speedlimit = Integer.parseInt(limitation);
                    if (speedlimit != 0 && !minimum) {
                        if (speed > speedlimit) {
                            if (!active) {
                                Ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        tnums = 0;
                                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                            tnums++;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                myRefs.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        tnum = 0;
                                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                            tnum++;
                                        }
                                        vioCount=tnum;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                //Toast.makeText(MapView.this, "You are overspeeding", Toast.LENGTH_LONG).show();
                                new CountDownTimer(warningTimer, 1000) {

                                    public void onTick(long millisUntilFinished) {
                                        active = true;
                                        time.setVisibility(View.VISIBLE);
                                        time.setText(String.valueOf(millisUntilFinished / 1000));
                                    }

                                    public void onFinish() {
                                        active = false;
                                        time.setText("");
                                        String sms = "Violation Reported.";
                                        if (speed > speedlimit) {
                                            Toast.makeText(MapView.this, sms, Toast.LENGTH_LONG).show();
                                            count.setVisibility(View.VISIBLE);
                                            int counter = Integer.parseInt(count.getText().toString());
                                            counter = counter + 1;
                                            count.setText(String.valueOf(counter));

                                            final Animation myAnim = AnimationUtils.loadAnimation(MapView.this, R.anim.bounce);
                                            // Use bounce interpolator with amplitude 0.2 and frequency 20
                                            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                                            myAnim.setInterpolator(interpolator);

                                            count.startAnimation(myAnim);
                                            /*Date date = new Date();
                                            Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                                            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                                            String stringdate = dt.format(newDate);*/
                                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                            Date date = new Date();
                                            String strDate = dateFormat.format(date).toString();
                                            //myRef.child("datetime").setValue(strDate);

                                            ticketRecord(tnums);
                                            vioRecord(vioCount, strDate, pos);
                                        } else {
                                            Toast.makeText(MapView.this, "Thank you for maintaining speed limit.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.start();

                                time.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
            }
        });

    }

    private Bluetooth.CommunicationCallback mCallback = new Bluetooth.CommunicationCallback() {
        @Override
        public void onConnect(BluetoothDevice device) {
            //Display("Connected to " + device.getName() + " - " + device.getAddress());
            Display("Vehicle Connected!");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String car = "DRIVING: " + name;
                    vehicle.setText(car);
                    SharedPreferences wheels = getSharedPreferences("MyApp", MODE_PRIVATE);
                    wheels.edit().putString("vehicle", name).apply();
                }
            });
        }

        @Override
        public void onDisconnect(BluetoothDevice device, String message) {
            Display("Disconnected!");
            Display("Connecting again...");
            b.connectToDevice(device);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    blueKph.setText("0");
                    String car = "Vehicle disconnected.";
                    vehicle.setText(car);
                }
            });

        }

        @Override
        public void onMessage(final String message) {
            Display(message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    blueKph.setText(message);
                }
            });
        }

        @Override
        public void onError(String message) {
            Display("Error: " + message);
            //==Display("47");
        }

        @Override
        public void onConnectError(final BluetoothDevice device, String message) {
            //Display("Error: " + message + ". Trying again in 3 sec...");
            Display("Error while connecting" + ". Trying again in 3 sec...");
            //==Display("47");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    blueKph.setText("0");
                    String car = "Vehicle not found.";
                    vehicle.setText(car);
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
                Intent intent1 = new Intent(MapView.this, SelectVehicle.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if (registered) {
                            unregisterReceiver(mReceiver);
                            registered = false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if (registered) {
                            unregisterReceiver(mReceiver);
                            registered = false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };

    //========================bluetooth
    private void setMapUI() {
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setTiltGesturesEnabled(false);
        mUiSettings.setZoomControlsEnabled(true);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this.getApplicationContext(), R.raw.maps_night);
        mMap.setMapStyle(style);

        /*cmall=mMap.addMarker(new MarkerOptions()
                .position(CMALL)
                .title("CMALL")
                .snippet("Population: 4,627,300"));
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.lexicon)));
        cmall.setTag(0);*/


       /*//add a marker in Sydney and move the camera
        LatLng myfav = new LatLng(10.3395125, 123.9110532);//(-34, 151);
        mMap.addMarker(new MarkerOptions().position(myfav).title("My Favorite Mall"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myfav));
        //Zooming mode
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update=CameraUpdateFactory.newLatLngZoom(myfav, 18);
        mMap.animateCamera(update);*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.removeUpdates(locationListener);
        //lm.removeUpdates(locationListener);
        Log.i("onPause...", "paused");
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF7F27'>Confirm Exit...</font>"));
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.map7);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure, you want to exit?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                int id = item.getItemId();
                if (id == android.R.id.home) ;
                {
                    //Toast.makeText(MapView.this, "Vehicle Disconnected!\n See you in our next drive!", Toast.LENGTH_LONG).show();
                    // onBackPressed();
                    b.removeCommunicationCallback();
                    b.disconnect();
                    Intent intent = new Intent(MapView.this, SelectVehicle.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>No</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ///Toast.makeText(MainActivity.this,"You clicked over No",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        try {
            Resources resources = alertDialog.getContext().getResources();
            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) alertDialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(Color.MAGENTA); // change title text color

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
            View titleDivider = alertDialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(Color.YELLOW); // change divider color
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        //Set negative button background
        //nbutton.setBackgroundColor(Color.MAGENTA);
        //Set negative button text color
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //Set positive button background
        //pbutton.setBackgroundColor(Color.YELLOW);
        //Set positive button text color
        pbutton.setTextColor(Color.BLACK);
        Button cbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        //Set positive button background
        //pbutton.setBackgroundColor(Color.YELLOW);
        //Set positive button text color
        cbutton.setTextColor(Color.BLACK);


        return super.onOptionsItemSelected(item);
    }

    public void rFIDs() {
        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(), "DS-DIGIB.TTF");
        blueKph = (Button) findViewById(R.id.btnSpeed);
        blueKph.setTypeface(myTypeface);
        blueTooth = (TextView) findViewById(R.id.speedo);
        blueTooth.setTypeface(myTypeface);

        restric = (TextView) findViewById(R.id.restriction);
        restric.setTypeface(myTypeface);

        vehicle = (TextView) findViewById(R.id.connectedTo);
        vehicle.setTypeface(myTypeface);
        limit = (Button) findViewById(R.id.maxlimit);
        limit.setTypeface(myTypeface);
        time = (TextView) findViewById(R.id.timer);
        time.setTypeface(myTypeface);
        count = (TextView) findViewById(R.id.counter);
        count.setTypeface(myTypeface);
        notif = (ImageButton) findViewById(R.id.tab4_icon);
        profname=(TextView) findViewById(R.id.profilename);
        load=(TextView) findViewById(R.id.loading);
        profpic = (CircleImageView) findViewById(R.id.profilepic);
        //center = (FloatingActionButton) findViewById(R.id.recenter);
    }

    public void ticketRecord(int tnums) {
        vticket = ticketSlip + tnums;
        userIds = Ref.push().getKey();
        Ref.child(userIds).child("Ticket_No").setValue(vticket);
    }
    public void latitLong(LatLng userpos) {
        //myRefPos.child(userPos).child("User_Location").setValue(userpos);
        myRefPos.child("User_Location").setValue(userpos);
    }

    public void vioRecord(int vioCount, String strDate, LatLng pos) {
        if (vioCount == 0) {
            payment = firstPenalty;
        } else if (vioCount == 1) {
            payment = secondPenalty;
        } else if (vioCount == 2) {
            payment = thirdPenalty;
        } else {
            payment = fourthPenalty;
        }

        userId = myRefs.push().getKey();
        String stats = "Unsettled";
        myRefs.child(userId).child("Date_Time").setValue(strDate);
        myRefs.child(userId).child("License").setValue(lisensya2);
        myRefs.child(userId).child("Speed").setValue(speed);
        myRefs.child(userId).child("Vehicle").setValue(vehicle.getText());
        myRefs.child(userId).child("Ticket_No").setValue(vticket);
        myRefs.child(userId).child("Penalty").setValue(payment);
        myRefs.child(userId).child("Status").setValue(stats);
        myRefs.child(userId).child("Lat_Lng").setValue(pos);
        //====Overspeeding Notification
        userIdNotif=myNotifRef.push().getKey();
        myNotifRef.child(userId).child("Date_Time").setValue(strDate);
        myNotifRef.child(userId).child("Speed").setValue(speed);
        myNotifRef.child(userId).child("Vehicle").setValue(vehicle.getText());
        myNotifRef.child(userId).child("Ticket_No").setValue(vticket);
        myNotifRef.child(userId).child("Lat_Lng").setValue(pos);
    }

    public void profileMarker(final LatLng latLng) {
        Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                driver_marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .title("Driver: "+fName)
                        .snippet("License: " + lisensya2)
                );
            }


            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("picasso", "onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                /*mMap.addMarker(new MarkerOptions().position(latLng).title("My Location").icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.memarker)));*/
                //Picasso.with(MapView.this).load(driversID).resize(200,200).centerCrop().transform(new RoundedTransformation(50, 4)).error(context.getResources().getDrawable(R.drawable.login_viologo));
                int height = 80;
                int width = 100;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.svclogo);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Driver: "+fName)
                        .snippet(lisensya2)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                );
            }
        };
        if (!driversID.equals("")) {
            Picasso.with(MapView.this).load(driversID).resize(200, 200).centerCrop().transform(new RoundedTransformation(50, 4)).placeholder(R.drawable.svclogo).error(context.getResources().getDrawable(R.drawable.svclogo)).into(mTarget);

        } else {
            Picasso.with(MapView.this).load(R.drawable.svclogo).resize(200, 200).centerCrop()
                    .transform(new RoundedTransformation(50, 4))
                    .placeholder(R.drawable.svclogo)
                    .onlyScaleDown()
                    .into(mTarget);
        }
       /* mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                //Remove previous center if it exists

                CameraPosition test = mMap.getCameraPosition();
                //Assign mCenterMarker reference:
                driver_marker.setPosition(mMap.getCameraPosition().target);
                Log.d(TAG, "Map Coordinate: " + String.valueOf(test));
         }
        });*/

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // This causes the marker at Perth to bounce into position when it is clicked.
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;

        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation);

        // Starts the bounce animation
        mAnimation = new MapView.BounceAnimation(start, duration, marker, mHandler);
        mHandler.post(mAnimation);
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return true;
    }

    public void click(final Marker marker) {

        // This causes the marker at Perth to bounce into position when it is clicked.
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;
        animbounce=true;


        //if(!animbounce){
        // animbounce=true;
        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation);
        // Starts the bounce animation
        mAnimation = new MapView.BounceAnimation(start, duration, marker, mHandler);
        mHandler.post(mAnimation);
        //}
        // Cancels the previous animation
        //mHandler.removeCallbacks(mAnimation);

        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        //return false;
        //return true;
    }

    /**
     * Performs a bounce animation on a {@link Marker}.
     */
    private static class BounceAnimation implements Runnable {

        private final long mStart, mDuration;
        private final Interpolator mInterpolator;
        private final Marker mMarker;
        private final Handler mHandler;
        /* final long mStart = SystemClock.uptimeMillis();
         final long mDuration = 1500;*/
        //
        private BounceAnimation(long start, long duration,Marker marker, Handler handler) {
            mStart = start;
            mDuration = duration;
            mMarker = marker;
            mHandler = handler;
            mInterpolator = new BounceInterpolator();
        }

        @Override
        public void run() {

            long elapsed = SystemClock.uptimeMillis() - mStart;
            float t = Math.max(1 - mInterpolator.getInterpolation((float) elapsed / mDuration), 0f);
            mMarker.setAnchor(0.5f, 1.0f + 1.2f * t);

            if (t > 0.0) {
                // Post again 16ms later.
                mHandler.postDelayed(this, 16L);
                //animbounce=false;
            }
        }
    }
    //=================animation 15secs
     /*Timer timer = new Timer();
        TimerTask updateProfile = new CustomTimerTask(MapView.this);
        timer.scheduleAtFixedRate(updateProfile, 10,5000);*/
    //===============================class below
    class CustomTimerTask extends TimerTask {
        private Context context;
        private Handler mHandlers = new Handler();

        // Write Custom Constructor to pass Context
        public CustomTimerTask(Context con) {
            this.context = con;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandlers.post(new Runnable() {
                        @Override
                        public void run() {
                            final Handler handlers = new Handler();
                            final long start = SystemClock.uptimeMillis();
                            final long duration = 1500;

                            final Interpolator interpolator = new BounceInterpolator();

                            handlers.post(new Runnable() {
                                @Override
                                public void run() {
                                    long elapsed = SystemClock.uptimeMillis() - start;
                                    float t = Math.max(
                                            1 - interpolator.getInterpolation((float) elapsed
                                                    / duration), 0);
                                    marker.setAnchor(0.5f, 1.0f + 2 * t);

                                    if (t > 0.0) {
                                        // Post again 16ms later.
                                        handlers.postDelayed(this, 16);
                                    }
                                }
                            });
                        }
                    });
                }
            }).start();

        }

    }
    private void redrawLine(){

        PolylineOptions options = new PolylineOptions().width(5).color(ORANGE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        line = mMap.addPolyline(options); //add Polyline
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); //added
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    // Draw polyline on map
    public void drawPolyLineOnMap(List<LatLng> list) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(5);
        polyOptions.addAll(list);

        mMap.clear();
        mMap.addPolyline(polyOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }

        //final LatLngBounds bounds = builder.build();

        //BOUND_PADDING is an int to specify padding of bound.. try 100.
        //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, BOUND_PADDING);
        //mMap.animateCamera(cu);
    }
    private int getIconUsingSwitch(String campoCategoria) {
        switch (campoCategoria) {
            case "20":
                return R.drawable.twenty;
            //break;
            case "25":
                return R.drawable.twentyfive;
            //break;
            case "30":
                return R.drawable.thirty;
            //break;
            case "35":
                return R.drawable.thirtyfive;
            //break;
            case "40":
                return R.drawable.fourty;
            //break;
            case "45":
                return R.drawable.fortyfive;
            //break;
            case "50":
                return R.drawable.fifty;
            //break;
            case "55":
                return R.drawable.fiftyfive;
            //break;
            case "60":
                return R.drawable.sixty;
            //break;
            case "65":
                return R.drawable.sixtyfive;
            //break;
            case "70":
                return R.drawable.seventy;
            //break;
            case "75":
                return R.drawable.seventyfive;
            //break;
            case "80":
                return R.drawable.eighty;
            //break;
            default:
                return R.drawable.zero;
        }
        /*if(campoCategoria==40){
            return R.drawable.twokph;
        }
        return R.drawable.twokph;*/
    }
    private int getIconUsingSwitchGreen(String campoCategoriaGreen) {
        switch (campoCategoriaGreen) {
            case "20":
                return R.drawable.twentymin;
            //break;
            case "25":
                return R.drawable.twentyfivemin;
            //break;
            case "30":
                return R.drawable.thirtymin;
            //break;
            case "35":
                return R.drawable.thirtyfivemin;
            //break;
            case "40":
                return R.drawable.fortymin;
            //break;
            case "45":
                return R.drawable.fortyfivemin;
            //break;
            case "50":
                return R.drawable.fiftymin;
            //break;
            case "55":
                return R.drawable.fiftyfivemin;
            //break;
            case "60":
                return R.drawable.sixtymin;
            //break;
            case "65":
                return R.drawable.sixtyfivemin;
            //break;
            case "70":
                return R.drawable.seventymin;
            //break;
            case "75":
                return R.drawable.seventyfivemin;
            //break;
            case "80":
                return R.drawable.eightymin;
            //break;
            default:
                return R.drawable.zero;
        }
        /*if(campoCategoria==40){
            return R.drawable.twokph;
        }
        return R.drawable.twokph;*/
    }
    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED))
        {
            if((ActivityCompat.shouldShowRequestPermissionRationale(MapView.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) &&  (ActivityCompat.shouldShowRequestPermissionRationale(MapView.this,
                    Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(MapView.this, new   String[]{ Manifest.permission.ACCESS_FINE_LOCATION
                        }, 200);//REQUEST_PERMISSIONS
            }
        }else {

        }
    }
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
