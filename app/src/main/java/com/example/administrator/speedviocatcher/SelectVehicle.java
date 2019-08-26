package com.example.administrator.speedviocatcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Callback;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.pulltorefresh.PullToRefresh;


public class SelectVehicle extends Activity implements PullToRefresh.OnRefreshListener {
    private Bluetooth bt;
    private ListView listView;
    private Button not_found;
    private TextView drivername;
    private List<BluetoothDevice> paired;
    private PullToRefresh pull_to_refresh;
    private boolean registered=false;
    private AlertDialog progressDialogs;
    private Context mContext;
    int count=0;
    int x=0;
    int loadx=0;
    int tnum;
    int tnums;
    int vticket;
    int vioCount=0;
    int ticketSlip;
    private String userId, userIds, userIdNotif;
    String firstPenalty,secondPenalty,thirdPenalty ,fourthPenalty;
    private String payment;
    //int arr[]=new int[1];

    private CircleImageView disp_profilepic;
    public static String lisensya2= "";
    public static String lastFirebaseDate="";
    public static String lastDate="";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("/Drivers_Info/");
    private static final String TAG = "Selection";
    public static String staticSpeedLimit= "";
    SQLiteDatabase dbase;
    DatabaseReference Ref = database.getReference("/Ticket/");
    FirebaseDatabase databases = FirebaseDatabase.getInstance();
    DatabaseReference myRefs, myNotifRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        setTitle("Welcome Alex");
        mContext=this;
        DBHelperTwo helper=new DBHelperTwo(getApplication(),"VioFile",1);
        dbase=helper.getWritableDatabase();
        SharedPreferences lisensyas = getSharedPreferences("MyApp", MODE_PRIVATE);
        lisensya2=lisensyas.getString("lisensya",  "UNKNOWN");
        myRefs = databases.getReference("Violation_Record").child(lisensya2);
        myNotifRef = databases.getReference("Overspeeding_Notification");

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
        //FirebaseDatabase datab = FirebaseDatabase.getInstance();
        DatabaseReference adjustmentRef = database.getReference("/Map_Adjustment/");
        adjustmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String keys = areaSnapshot.getKey();
                    if (keys.equals("1")) {
                        String ticketNumbering = areaSnapshot.child("ticketNo_update").getValue().toString();
                        ticketSlip = Integer.parseInt(ticketNumbering);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        //======
        Cursor rsCursor;

        String[] rsFields = {"vioid", "datetime", "license", "speed", "vehicle"};
        rsCursor = dbase.query("VioFile", rsFields, null, null, null, null, null, null);
        rsCursor.moveToFirst();

        Integer c = rsCursor.getCount();

        final int sqlArr[]=new int[c];
        if (!rsCursor.isAfterLast()) {
            Integer number = 0;
            while (number<c) {
                String dblicense = rsCursor.getString(2);
                String lisc = lisensya2;

                if (dblicense.equals(lisc)) {

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
                    //===insert to firebase
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String strDate = dateFormat.format(date).toString();
                    //myRef.child("datetime").setValue(strDate);
                    int vionum=rsCursor.getInt(0);
                    String strLdate=rsCursor.getString(1);
                    String strLisc=rsCursor.getString(2);
                    String strUploadedDate=strDate;
                    String strLspeed=rsCursor.getString(4);
                    String strLvhicle=rsCursor.getString(3);
                    ticketRecord(tnums);
                    vioRecord(vioCount, strDate,strLisc,strLdate,strLspeed,strLvhicle,strUploadedDate);
                    sqlArr[loadx]=vionum;
                    loadx++;

                    lastDate=strLdate;
                    if(number==(c-1))
                    {
                        FirebaseDatabase databasesss = FirebaseDatabase.getInstance();
                        DatabaseReference myRefs = databasesss.getReference("Violation_Record").child(lisensya2);
                        myRefs.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean found=false;
                                for (com.google.firebase.database.DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                    //areaSnapshot.child("ID Number").getValue(String.class);
                                    lastFirebaseDate=childDataSnapshot.child("Date_Time").getValue().toString();
                                    found=true;
                                }
                                if(found)
                                {
                                //for (com.google.firebase.database.DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                if(lastFirebaseDate.equals(lastDate))
                                {
                                    Cursor rsCursor;

                                    String[] rsFields = {"vioid", "datetime", "license", "speed", "vehicle"};
                                    for(int i=0;i<loadx;i++) {
                                        rsCursor = dbase.query("VioFile", rsFields, "vioid = " + sqlArr[i], null, null, null, null, null);
                                        rsCursor.moveToFirst();
                                        if (!rsCursor.isAfterLast()) {
                                            dbase.delete("VioFile", "vioid = " + sqlArr[i], null);
                                        }
                                        rsCursor.close();
                                    }
                        /*if(lastDate.equals("")){
                        Toast.makeText(SelectVehicle.this, "Offline Violation Reported!", Toast.LENGTH_LONG).show();
                        }*/
                                }
                                }
                                // }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
                rsCursor.moveToNext();
                number++;
            }
        }
        rsCursor.close();


        //==============Delete

        //====
        /*rsCursor = dbase.query("VioFile", rsFields, null, null, null, null, null, null);
        rsCursor.moveToFirst();

        if (!rsCursor.isAfterLast()) {
            Integer number = 0;
            while (number<c) {
                if (number==(c-1)) {
                    lastDate=rsCursor.getString(1);
                }
                rsCursor.moveToNext();
                number++;
            }
        }
        rsCursor.close();*/
        //==============
        //==============firebase

        //==============firebase


        //======Bluetooth
        bt = new Bluetooth(this);
        bt.enableBluetooth();
        disp_profilepic = (CircleImageView)findViewById(R.id.profilepic);
       drivername = (TextView)findViewById(R.id.name);
        disp_profilepic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                disp_profilepic.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                disp_profilepic.playSoundEffect(SoundEffectConstants.CLICK);

                final Animation myAnim = AnimationUtils.loadAnimation(SelectVehicle.this, R.anim.bounce);

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);

                disp_profilepic.startAnimation(myAnim);
                //Create a popup and specify which button will be active
                PopupMenu popup = new PopupMenu(SelectVehicle.this,disp_profilepic);

                //Let's inflate. Our popup xml file
                popup.getMenuInflater().inflate(R.menu.menu_profile, popup.getMenu());

                //What happens when you click on menu items
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if(item.getTitle().equals("MY ACCOUNT")){
                            progressDialogs.show();
                            //Toast.makeText(SelectVehicle.this, "MY ACCOUNT", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SelectVehicle.this, NavigationActivity.class);
                            startActivity(i);
                            bt.disableBluetooth();
                            progressDialogs.dismiss();
                            finish();

                        }else if(item.getTitle().equals("SIGN OUT")){
                            progressDialogs.show();
                            Toast.makeText(SelectVehicle.this, "SIGN OUT", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            //LoginManager.getInstance().logOut();
                            bt.disableBluetooth();
                            Intent i = new Intent(SelectVehicle.this, Login.class);
                            startActivity(i);
                            progressDialogs.dismiss();
                            finish();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


        myRef.orderByChild("License_No").equalTo(lisensya2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag=false;

                for (com.google.firebase.database.DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "PARENT: " + childDataSnapshot.getKey());
                    Log.d(TAG, "" + childDataSnapshot.child("License_No").getValue());
                    flag=true;

                    final String driversID = childDataSnapshot.child("Drivers_ID").getValue().toString();


                    if(!driversID.equals("default")) {
                        Picasso.with(SelectVehicle.this).load(driversID).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.svclogo)
                                .into(disp_profilepic, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        final Animation myAnim = AnimationUtils.loadAnimation(SelectVehicle.this, R.anim.bounce);
                                        // Use bounce interpolator with amplitude 0.2 and frequency 20
                                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                                        myAnim.setInterpolator(interpolator);

                                        disp_profilepic.startAnimation(myAnim);
                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(SelectVehicle.this).load(driversID).placeholder(R.drawable.svclogo).into(disp_profilepic);
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
                Toast.makeText(SelectVehicle.this, " "+databaseError.toException().getMessage() , Toast.LENGTH_LONG).show();
            }
        });
        String driferhandler="lic. #: "+lisensya2;
        drivername.setText(driferhandler);
        final Animation myAnim = AnimationUtils.loadAnimation(SelectVehicle.this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        drivername.startAnimation(myAnim);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;
        progressDialogs = new SpotsDialog(mContext, R.style.Custom);


        //Toast.makeText(SelectVehicle.this, "Bluetooth is connected.", Toast.LENGTH_SHORT).show();
        pull_to_refresh = (PullToRefresh)findViewById(R.id.pull_to_refresh);
        listView =  (ListView)findViewById(R.id.list);
        not_found =  (Button) findViewById(R.id.not_in_list);

        pull_to_refresh.setListView(listView);
        pull_to_refresh.setOnRefreshListener(this);
        pull_to_refresh.setSlide(500);




        not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogs.show();
                Intent i = new Intent(SelectVehicle.this, ScanningDevice.class);
                startActivity(i);
                progressDialogs.dismiss();
            }
        });
        progressDialogs.show();
        addDevicesToList();
        progressDialogs.dismiss();


        Snackbar snackbar = Snackbar
                .make(not_found, "Info: Vehicle is not in the list? \nPull to refresh or press pair.", Snackbar.LENGTH_LONG)
                .setAction("Pair", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentOpenBluetoothSettings = new Intent();
                        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivity(intentOpenBluetoothSettings);
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        //sbView.setBackgroundColor(Color.TRANSPARENT);
        //code.setTextColor(Color.RED);
        //not_found.setError("Vehicle not found?\nclick me..");
        snackbar.show();
        FirebaseDatabase datas = FirebaseDatabase.getInstance();
        DatabaseReference penaltyRefs = datas.getReference("/Map_Adjustment/");
        penaltyRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String keys = areaSnapshot.getKey();
                    if (keys.equals("1")) {
                        //distanceSignage =dataSnapshot.child("distance_signage").getValue().toString();
                        //range = Integer.parseInt(distanceSignage);
                        staticSpeedLimit = areaSnapshot.child("static_speedlimit").getValue().toString();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(!staticSpeedLimit.equals(""))
        {
            SharedPreferences prefstatic = getSharedPreferences("MyApp", MODE_PRIVATE);
            prefstatic.edit().putString("staticlimit", staticSpeedLimit).apply();
        }

        //SQLite
        /*Cursor cnCursor;

        String  [] cnFields={"vioid","datetime","license","speed","vehicle"};
        cnCursor=dbase.query("VioFile",cnFields,null,null,null,null,null,null);
        cnCursor.moveToFirst();

        Integer c=cnCursor.getCount();
        if(!cnCursor.isAfterLast())
        {
            //===============
            Integer number=0;
            while(number<c){
                String dblicense=cnCursor.getString(2);
                String lisc=lisensya2;
                if(dblicense.equals(lisc))
                {
                    final Dialog dialog=new Dialog(SelectVehicle.this);
                    //Toast.makeText(Listview.this," "+text,Toast.LENGTH_LONG).show();

                    dialog.setContentView(R.layout.dialog);
                    dialog.setTitle("Violation Information");

                    TextView texts=(TextView) dialog.findViewById(R.id.dialogText);
                    texts.setText("ID:"+" "+cnCursor.getString(1)+"\nLicense:"+" "+cnCursor.getString(2));
                    //dialog.setIcon(R.drawable.ic_launcher);
                    dialog.show();

                    Button submit=(Button)dialog.findViewById(R.id.dialogSubmit);
                    submit.setOnClickListener(new 	View.OnClickListener()
                    {
                        public void onClick(View view){
                            dialog.dismiss();
                        }
                    });
                }
                cnCursor.moveToNext();
                number++;
            }
        }
        cnCursor.close();*/


    }

    @Override
    public void onRefresh() {
        /*List<String> names = new ArrayList<String>();
        for (BluetoothDevice d : bt.getPairedDevices()){
            names.add(d.getName());
        }*/
        String FourWheelerVehicleFilter = "AV";
        String TwoWheelerVehicleFilter = "MV";
        int i=0;

        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : paired){
            if(d.getName().toString().contains("VH")){
                count++;
            }
        }
        final int arr[]=new int[count];
        for (BluetoothDevice d : paired){
            //names.add(d.getName());
            if(d.getName().toString().toString().contains(TwoWheelerVehicleFilter)) {
                //ImageView imageView = findViewById(R.id.logo);
                //imageView.setImageResource(R.drawable.motorvehicle);
                names.add(d.getName());
                arr[x]=i;

                x++;
            }
            else if(d.getName().toString().contains(FourWheelerVehicleFilter)) {
                //ImageView imageView = findViewById(R.id.logo);
                //imageView.setImageResource(R.drawable.autovehicle);
                names.add(d.getName());
                arr[x]=i;
                x++;
            }
            else if(d.getName().toString().contains("VH"))//ANZA MOTOR --- STILL USBUNON ANG IYAHA BT module CONFIGURATIONS
            {
                //ImageView imageView = findViewById(R.id.logo);
                //imageView.setImageResource(R.drawable.motorvehicle);
                names.add(d.getName());
                arr[x]=i;
                x++;
            }
            i++;
        }

        String[] array = names.toArray(new String[names.size()]);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, array);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.removeViews(0, listView.getCount());
                listView.setAdapter(adapter);
                paired = bt.getPairedDevices();
            }
        });
        pull_to_refresh.refreshComplete();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
    }

    private void addDevicesToList(){
        paired = bt.getPairedDevices();
        String FourWheelerVehicleFilter = "AV";
        String TwoWheelerVehicleFilter = "MV";
        int i=0;

        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : paired){
            if(d.getName().toString().contains("VH")){
                count++;
            }
        }
        final int arr[]=new int[count];
        for (BluetoothDevice d : paired){
            //names.add(d.getName());
            if(d.getName().toString().toString().contains(TwoWheelerVehicleFilter)) {
                //ImageView imageView = findViewById(R.id.logo);
                //imageView.setImageResource(R.drawable.motorvehicle);
                names.add(d.getName());
                arr[x]=i;

                x++;
            }
            else if(d.getName().toString().contains(FourWheelerVehicleFilter)) {
                //ImageView imageView = findViewById(R.id.logo);
                //imageView.setImageResource(R.drawable.autovehicle);
                names.add(d.getName());
                arr[x]=i;
                x++;
            }
            else if(d.getName().toString().contains("VH"))//ANZA MOTOR --- STILL USBUNON ANG IYAHA BT module CONFIGURATIONS
            {
                //ImageView imageView = findViewById(R.id.logo);
                //imageView.setImageResource(R.drawable.motorvehicle);
                names.add(d.getName());
                arr[x]=i;
                x++;
            }
            i++;
        }

        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //progressDialogs.show();

                /*listView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {*/

                //Create a popup and specify which button will be active
                PopupMenu popup = new PopupMenu(SelectVehicle.this,listView);

                //Let's inflate. Our popup xml file
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

                //What happens when you click on menu items
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if(item.getTitle().equals("GO OFFLINE")){
                            progressDialogs.show();
                            Intent i = new Intent(SelectVehicle.this, SpeedOfflineMonitoring.class);
                            i.putExtra("pos", arr[position]);

                            if(registered) {
                                unregisterReceiver(mReceiver);
                                registered=false;
                            }
                            progressDialogs.dismiss();
                            startActivity(i);
                            finish();

                        }else if(item.getTitle().equals("GO ONLINE")){
                            progressDialogs.show();
                            Intent i = new Intent(SelectVehicle.this, MapView.class);
                            i.putExtra("pos", arr[position]);

                            if(registered) {
                                unregisterReceiver(mReceiver);
                                registered=false;
                            }
                            progressDialogs.dismiss();
                            startActivity(i);
                            finish();

                        }
                        return true;
                    }
                });
                popup.show();
                   /* }
                });*/
            }
        });


        //


        String[] array = names.toArray(new String[names.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, array);

        listView.setAdapter(adapter);

        not_found.setEnabled(true);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setEnabled(false);
                            }
                        });
                        //Toast.makeText(SelectVehicle.this, "Turn on bluetooth", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addDevicesToList();
                                listView.setEnabled(true);
                            }
                        });
                        break;
                }
            }
        }
    };
    private void errorUnfound() {
        Toast.makeText(this,
                "No Record Found", Toast.LENGTH_LONG).show();
    }
    public void ticketRecord(int tnums) {
        vticket = ticketSlip + tnums;
        userIds = Ref.push().getKey();
        Ref.child(userIds).child("Ticket_No").setValue(vticket);
    }

    public void vioRecord(final int vioCount,final String strDate,final String strLisc,final String strLdate,final String strLspeed,final String strLvhicle,final String strUploadedDate) {

        FirebaseDatabase databases = FirebaseDatabase.getInstance();
        DatabaseReference myRefss = databases.getReference("Violation_Record").child(lisensya2);
        myRefss.orderByChild("Date_Time").equalTo(strLdate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag=false;

                for (com.google.firebase.database.DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "PARENT: " + childDataSnapshot.getKey());
                    Log.d(TAG, "" + childDataSnapshot.child("Date_Time").getValue());
                    flag=true;
                }
                if(!flag) {
                    add(vioCount, strDate,strLisc,strLdate,strLspeed,strLvhicle,strUploadedDate);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void add(int vioCount,final String strDate,final String strLisc, String strLdate,final String strLspeed,final String strLvhicle,final String strUploadedDate) {
        if (vioCount == 0) {
            payment = firstPenalty;
        } else if (vioCount == 1) {
            payment = secondPenalty;
        } else if (vioCount == 2) {
            payment = thirdPenalty;
        } else {
            payment = fourthPenalty;
        }
        //String DateOff=strLdate;

        userId = myRefs.push().getKey();
        String stats = "Unsettled/Offline";
        String notAvailable="N/A";
        myRefs.child(userId).child("Date_Time").setValue(strLdate);
        myRefs.child(userId).child("License").setValue(strLisc);
        myRefs.child(userId).child("Date_Uploaded").setValue(strUploadedDate);
        myRefs.child(userId).child("Speed").setValue(strLspeed);
        myRefs.child(userId).child("Vehicle").setValue("DRIVING: " +strLvhicle);
        myRefs.child(userId).child("Ticket_No").setValue(notAvailable);//vticket
        myRefs.child(userId).child("Penalty").setValue(payment);
        myRefs.child(userId).child("Status").setValue(stats);
                    /*myRefs.child(userId).child("Offline_Date").setValue(strLdate);*/
        myRefs.child(userId).child("Penalty").setValue(notAvailable);
        myRefs.child(userId).child("Lat_Lng").setValue(notAvailable);

    }
    /*@Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
    }*/
}

