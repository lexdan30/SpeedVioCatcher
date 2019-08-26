package com.example.administrator.speedviocatcher;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.aflak.bluetooth.Bluetooth;

import static com.example.administrator.speedviocatcher.AppStatus.context;

/**
 * Created by Administrator on 1/14/2018.
 */

public class SpeedOfflineMonitoring extends AppCompatActivity {


    public static String lisensya2= "";
    public static String defaultLimit= "40";
    public static String  staticSpeedLimit= "";
    //public static String datetimeL="";
    //public static String licenseL="";
    //public static String speedL="";
    //public static String vehicleL="";
    View view;
    private String name;
    private Bluetooth b;
    private boolean registered=false;
    boolean active;
    int tnum=0;
    ImageButton sync,map;
    ImageButton notif;
    int speed;
    int speedlimit;
    private String userId;
    private int counts = 0;
    //blueTooth-plate number
    //blueKph-digitalview
    //userslicense-license
    TextView textspeed,textspeedunit,textmax,textmaxunit,textvehicle,textlicense;
    TextView userslicense,blueTooth, blueKph,time,limit,count;

    SQLiteDatabase dbase;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        DBHelperTwo helper=new DBHelperTwo(getApplication(),"VioFile",1);
        dbase = helper.getWritableDatabase();


        //addViolation();
        rFIDs();
        rFIDsText();


        /*if (txtView .getVisibility() == View.VISIBLE)
            txtView.setVisibility(View.INVISIBLE);
        else
            txtView.setVisibility(View.VISIBLE);*/
        time.setVisibility(View.INVISIBLE);
        count.setVisibility(View.INVISIBLE);


        setTitle("Speed Offline Monitoring");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences lisensyas = getSharedPreferences("MyApp", MODE_PRIVATE);
        lisensya2=lisensyas.getString("lisensya",  "UNKNOWN");
        userslicense.setText(lisensya2);

        SharedPreferences prefstatic = getSharedPreferences("MyApp", MODE_PRIVATE);
        staticSpeedLimit =prefstatic.getString("staticlimit", "");

        limit.setText(staticSpeedLimit);

        if(limit.getText().toString().equals(""))
        {
            limit.setText(defaultLimit);
        }
        //========================bluetooth
        b = new Bluetooth(SpeedOfflineMonitoring.this);
        b.enableBluetooth();

        b.setCommunicationCallback(mCallback);

        int pos = getIntent().getExtras().getInt("pos");
        name = b.getPairedDevices().get(pos).getName();

        Display("Connecting...");
        b.connectToDevice(b.getPairedDevices().get(pos));

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;
        //========================bluetooth

    }

    public void rFIDs(){
        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),"DS-DIGIB.TTF");
        sync = (ImageButton)findViewById(R.id.btnsync);
        //map = (ImageButton)findViewById(R.id.btnmap);
        blueKph=(TextView)findViewById(R.id.digitalView);
        blueKph.setTypeface(myTypeface);
        limit=(TextView)findViewById(R.id.maxValue);
        limit.setTypeface(myTypeface);
        blueTooth=(TextView)findViewById(R.id.vehicle);
        blueTooth.setTypeface(myTypeface);
        userslicense=(TextView)findViewById(R.id.lis_no);
        userslicense.setTypeface(myTypeface);
        time=(TextView)findViewById(R.id.timer);
        time.setTypeface(myTypeface);
        //Map();
    }
    public void rFIDsText(){
        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),"DS-DIGIB.TTF");

        textspeed=(TextView)findViewById(R.id.speedText);
        textspeed.setTypeface(myTypeface);
        textspeedunit=(TextView)findViewById(R.id.speedunit);
        textspeedunit.setTypeface(myTypeface);

        textmax=(TextView)findViewById(R.id.maxText);
        textmax.setTypeface(myTypeface);
        textmaxunit=(TextView)findViewById(R.id.maxUnit);
        textmaxunit.setTypeface(myTypeface);

        textvehicle=(TextView)findViewById(R.id.vehicletext);
        textvehicle.setTypeface(myTypeface);
        textlicense=(TextView)findViewById(R.id.licenseText);
        textlicense.setTypeface(myTypeface);
        count=(TextView)findViewById(R.id.counter);
        count.setTypeface(myTypeface);
        notif=(ImageButton)findViewById(R.id.tab4_icon);

        //Map();
        //Notif();
    }
    //==============counter2
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);

        MenuItem menuItem = menu.findItem(R.id.testAction);
        menuItem.setIcon(buildCounterDrawable(counts, R.drawable.notif_icon));

        return true;
    }
    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            *//*final Animation myAnim = AnimationUtils.loadAnimation(SpeedOfflineMonitoring.this, R.anim.bounce);
            // Use bounce interpolator with amplitude 0.2 and frequency 20
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            textView.startAnimation(myAnim);*//*
            textView.setText("" + counts);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void doIncrease() {
        counts++;
        invalidateOptionsMenu();
    }*/
    //==============counter2
    private void Notif() {

        notif.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent record = new Intent(SpeedOfflineMonitoring.this, NavigationActivity.class);
                startActivity(record);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SpeedOfflineMonitoring.this);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF7F27'>Confirm Exit...</font>"));
                // Icon Of Alert Dialog
                alertDialogBuilder.setIcon(R.drawable.db);
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage("Are you sure, you want to exit?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(SpeedOfflineMonitoring.this, "Vehicle Disconnected!", Toast.LENGTH_LONG).show();
                        // onBackPressed();
                        b.removeCommunicationCallback();
                        b.disconnect();
                        Intent intent = new Intent(SpeedOfflineMonitoring.this, CardViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            }
        });
    }
    /*private void Map() {
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mapView = new Intent(SpeedOfflineMonitoring.this, SelectVehicle.class);
                Toast.makeText(SpeedOfflineMonitoring.this, "Connect to vehicle and select GO ONLINE.", Toast.LENGTH_SHORT).show();
                startActivity(mapView);
            }
        });
    }*/
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id= item.getItemId();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF7F27'>Confirm Exit...</font>"));
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.db);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure, you want to exit?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if(id==android.R.id.home);
                {
                    //Toast.makeText(SpeedOfflineMonitoring.this, "Vehicle Disconnected!\n See you in our next drive!", Toast.LENGTH_LONG).show();
                    // onBackPressed();
                    b.removeCommunicationCallback();
                    b.disconnect();
                    Intent intent = new Intent(SpeedOfflineMonitoring.this, SelectVehicle.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    //========================bluetooth
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }

        /*Intent svc = new Intent(this, OverlayShowingService.class);
        checkPermissionOverlay();
        startService(svc);
        finish();*/
    }
    /*@TargetApi(Build.VERSION_CODES.M)
    public void checkPermissionOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intentSettings = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(intentSettings, OVERLAY_PERMISSION_REQ_CODE);
        }
    }*/
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
    public void Display(final String s){
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
                if(!android.text.TextUtils.isDigitsOnly(s)) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.bottomtoolbarmain), s + "\n", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();//findViewById(android.R.id.content)
                }else{
                    speed=Integer.parseInt(s);
                    String limitation=limit.getText().toString();
                    speedlimit=Integer.parseInt(limitation);

                    if(speed>speedlimit)
                    {
                        if(!active) {

                            //Toast.makeText(SpeedOfflineMonitoring.this, "You are overspeeding", Toast.LENGTH_LONG).show();
                            new CountDownTimer(10000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    active=true;
                                    /*if (time .getVisibility() == View.VISIBLE)
                                        time.setVisibility(View.INVISIBLE);
                                    else*/
                                    time.setVisibility(View.VISIBLE);
                                    time.setText(String.valueOf(millisUntilFinished / 1000));

                                }

                                public void onFinish() {
                                    active=false;

                                    time.setText("");
                                    String sms = "Violation Reported!";
                                    if(speed>speedlimit) {
                                        //Toast.makeText(SpeedOfflineMonitoring.this, sms, Toast.LENGTH_LONG).show();
                                        count.setVisibility(View.VISIBLE);
                                        int counter=Integer.parseInt(count.getText().toString());
                                        counter=counter+1;
                                        count.setText(String.valueOf(counter));

                                        final Animation myAnim = AnimationUtils.loadAnimation(SpeedOfflineMonitoring.this, R.anim.bounce);
                                        // Use bounce interpolator with amplitude 0.2 and frequency 20
                                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                                        myAnim.setInterpolator(interpolator);
                                        //==============counter2
                                        //doIncrease();
                                        //==============counter2
                                        count.startAnimation(myAnim);

                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                        Date date = new Date();
                                        String datetime = dateFormat.format(date).toString();
                                        String license=lisensya2;
                                        String vehicle=blueTooth.getText().toString();
                                        String speed=blueKph.getText().toString();
                                        int sample=12;
                                        //===============insertSQLite
                                        int id=15;
                                       /* String fname="asdasdasd";
                                        String lname="asdasda";
                                        String course="Asdasdasd";
                                        String year="Asdasd";*/


                                        addVioRecord(datetime,license,vehicle,speed);
                                        //addVioRecord(sample,datetime,license,vehicle,speed);
                                        //Toast.makeText(SpeedOfflineMonitoring.this, sample+" "+datetimeL+" "+licenseL+" "+vehicleL+" "+speedL, Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(SpeedOfflineMonitoring.this, "Thank you for maintaining speed limit.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }.start();
                            time.setVisibility(View.INVISIBLE);


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
                    String car=name;
                    blueTooth.setText(car);
                    SharedPreferences wheels = getSharedPreferences("MyApp", MODE_PRIVATE);
                    wheels.edit().putString("vehicle", car).apply();
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
                    String car="Vehicle disconnected.";
                    blueTooth.setText(car);
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
            //Display("45");
        }

        @Override
        public void onConnectError(final BluetoothDevice device, String message) {
            //Display("Error: " + message + ". Trying again in 3 sec...");
            Display("Error while connecting" + ". Trying again in 3 sec...");
            //Display("45");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //0
                    blueKph.setText("0");
                    String car="Vehicle not found.";
                    blueTooth.setText(car);
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
                Intent intent1 = new Intent(SpeedOfflineMonitoring.this, SelectVehicle.class);

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
    };
    //========================bluetooth
    /*public void addVioRecord(int id,String fname,String lname,String course,String year) {
        // TODO Auto-generated method stub
        ContentValues rsValues=new ContentValues();
        Cursor rsCursor;
        long x;

        String  [] rsFields={"StudID","StudFirstName","StudLastName","StudCourse","StudYear"};
        rsCursor=dbase.query("ViolationFile",rsFields,"StudID="+id,null,null,null,null,null);
        rsCursor.moveToFirst();
        if(!rsCursor.isAfterLast())
        {
            Toast.makeText(SpeedOfflineMonitoring.this,"Violation time Exist!",Toast.LENGTH_LONG).show();

        }else{
            rsValues.put("StudID", id);
            rsValues.put("StudFirstName", fname);
            rsValues.put("StudLastName", lname);
            rsValues.put("StudCourse", course);
            rsValues.put("StudYear", year);

            x=dbase.insert("ViolationFile", null, rsValues);
            if(x!=-1)
            {   rsCursor.close();
                Toast.makeText(SpeedOfflineMonitoring.this,"Violation Recorded!",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(SpeedOfflineMonitoring.this,"Error!",Toast.LENGTH_LONG).show();
            }
        }
    }*/
    /*public void addVioRecord(int sample,String datetime,String license,String vehicle,String speed) {
        // TODO Auto-generated method stub
        ContentValues rsValues=new ContentValues();
        Cursor rsCursor;
        long x;

        String  [] rsFields={"VioCount","LocalDateTime","LocalLicense","LocalSpeed","LocalVehicle"};
        rsCursor=dbase.query("DriverViolation",rsFields,"VioCount="+sample,null,null,null,null,null);

        rsCursor.moveToFirst();

        if(!rsCursor.isAfterLast())
        {
            Toast.makeText(SpeedOfflineMonitoring.this,"Violation time Exist!",Toast.LENGTH_LONG).show();

        }else{
            rsValues.put("VioCount", sample);
            rsValues.put("LocalDateTime", datetime);
            rsValues.put("LocalLicense", license);
            rsValues.put("LocalSpeed",   vehicle);
            rsValues.put("LocalVehicle", speed);

            x=dbase.insert("DriverViolation", null, rsValues);
            if(x!=-1)
            {
                rsCursor.close();
                Toast.makeText(SpeedOfflineMonitoring.this,"Violation Reported!",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(SpeedOfflineMonitoring.this,"Error!",Toast.LENGTH_LONG).show();
            }
        }
    }*/
    public void addVioRecord(String date,String license,String speed,String vehicle)
    {
        int lastnum=0;
        //====
        Cursor rsCursor;

        String[] rsFields = {"vioid", "datetime", "license", "speed", "vehicle"};
        rsCursor = dbase.query("VioFile", rsFields, null, null, null, null, null, null);
        rsCursor.moveToFirst();

        Integer c = rsCursor.getCount();

        if (!rsCursor.isAfterLast()) {
            Integer number = 0;
            while (number<c) {
                if (number==(c-1)) {
                    lastnum=rsCursor.getInt(0);
                }
                rsCursor.moveToNext();
                number++;
            }
        }
        rsCursor.close();

        //====

        Cursor cnCursor;

        String  [] cnFields={"vioid","datetime","license","speed","vehicle"};
        cnCursor=dbase.query("VioFile",cnFields,null,null,null,null,null,null);
        cnCursor.moveToFirst();

        //int c=cnCursor.getCount();

            int counter = lastnum + 1;
            cnCursor.close();


        // TODO Auto-generated method stub
        ContentValues rsValues=new ContentValues();
        //Cursor rsCursor;
        long x;

        //String  [] rsFields={"vioid","datetime","license","speed","vehicle"};
        rsCursor=dbase.query("VioFile",rsFields,"vioid="+counter,null,null,null,null,null);
        rsCursor.moveToFirst();
        if(rsCursor.isAfterLast())
        {
            rsValues.put("vioid", counter);
            rsValues.put("datetime", date);
            rsValues.put("license", license);
            rsValues.put("speed", speed);
            rsValues.put("vehicle", vehicle);

            x=dbase.insert("VioFile", null, rsValues);
            if(x!=-1)
            {   rsCursor.close();
                Toast.makeText(SpeedOfflineMonitoring.this,"Violation Recorded.",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(SpeedOfflineMonitoring.this,"Error.",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(SpeedOfflineMonitoring.this,"Data already Exist.",Toast.LENGTH_LONG).show();

        }
    }
    //=================================
    class HUDView extends ViewGroup {
        private Paint mLoadPaint;

        public HUDView(Context context) {
            super(context);
            Toast.makeText(getContext(),"HUDView", Toast.LENGTH_LONG).show();

            mLoadPaint = new Paint();
            mLoadPaint.setAntiAlias(true);
            mLoadPaint.setTextSize(10);
            mLoadPaint.setARGB(255, 255, 0, 0);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawText("Hello World", 5, 15, mLoadPaint);
        }

        @Override
        protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //return super.onTouchEvent(event);
            Toast.makeText(getContext(),"onTouchEvent", Toast.LENGTH_LONG).show();
            return true;
        }
}
}
