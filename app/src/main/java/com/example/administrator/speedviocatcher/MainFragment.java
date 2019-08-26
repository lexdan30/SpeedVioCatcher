package com.example.administrator.speedviocatcher;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothProfile;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.IntentFilter;

        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.location.Location;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.provider.Settings;
        import android.support.annotation.Nullable;
        import android.support.design.widget.NavigationView;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentTransaction;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.google.android.gms.common.api.GoogleApiClient;


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

        import de.hdodenhof.circleimageview.CircleImageView;

        import static android.content.Context.MODE_PRIVATE;


public class MainFragment extends Fragment {

    TextView  textplate, textcr,textmake,textseries,textyrmodel, textnationality,textaddress2,textBdate, textSex, textHT, textWT,textlicense_No,textlicense,textaddress,textdriver_name,textrestriction,textres_code,textagency,textexpiry;
    private CircleImageView disp_profilepic;
    public static String lisensya2= "";
    public static String plateno="";
    public static String platevehicle="";

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference("/Drivers_Info/");

    private static final String TAG = "Search";

public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_profile, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("SpeedViocatcher");
        rFIDs();

        SharedPreferences lisensyas = getActivity().getSharedPreferences("MyApp", MODE_PRIVATE);
        lisensya2=lisensyas.getString("lisensya",  "UNKNOWN");

        //Toast.makeText(getActivity(), "license number is "+ lisensya2, Toast.LENGTH_LONG).show();
        //search
        if (AppStatus.getInstance(getActivity()).isOnline(getActivity())) {
            textlicense_No.setText(lisensya2);
            String lis="LICENSE NO: "+lisensya2;
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
                        String addr="Address: "+childDataSnapshot.child("Address").getValue(String.class);
                        //textaddress2.setText(addr);
                        String nation="NATIONALITY: "+childDataSnapshot.child("Citizenship").getValue(String.class);
                        textnationality.setText(nation);

                        String resCode="RESTRICTION CODE: "+childDataSnapshot.child("Restriction_Code").getValue(String.class);
                        String resAgency="ACY: "+childDataSnapshot.child("Agency").getValue(String.class);
                        String resExpiry="EXPIRY: "+childDataSnapshot.child("Expiry_Date").getValue(String.class);
                        textres_code.setText(resCode);
                        textagency.setText(resAgency);
                        textexpiry.setText(resExpiry);



                        if(!driversID.equals("default")) {
                            Picasso.with(getActivity()).load(driversID).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.svclogo)
                                    .into(disp_profilepic, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Picasso.with(getActivity()).load(driversID).placeholder(R.drawable.svclogo).into(disp_profilepic);
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
                    Toast.makeText(getActivity(), " "+databaseError.toException().getMessage() , Toast.LENGTH_LONG).show();
                }
            });
        } else {
            /*Toast.makeText(getActivity(),
                    "No Internet connection available\n(POWERED BY LEXDARK)", Toast.LENGTH_LONG).show();*/
        }

        //=========================Vehicle firebase ver1
        FirebaseDatabase databases = FirebaseDatabase.getInstance();
        DatabaseReference myRefs = databases.getReference("Vehicle_Info");
        SharedPreferences wheels = getActivity().getSharedPreferences("MyApp", MODE_PRIVATE);
        plateno ="PLATE NO: "+ wheels.getString("vehicle", "UNKNOWN");
        platevehicle ="Driving: "+ wheels.getString("vehicle", "UNKNOWN");
        String car=wheels.getString("vehicle", "UNKNOWN");
        textplate.setText(plateno);
        myRefs.orderByChild("Plate_No").equalTo(car).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flags=false;

                for (com.google.firebase.database.DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                    //Log.d(TAG, "PARENT: " + areaSnapshot.getKey());
                    Log.d(TAG, "" + areaSnapshot.child("Plate_No").getValue());
                    flags=true;

                    String crn="CR NO: "+areaSnapshot.child("CR_No").getValue().toString();
                    String mk="MAKE: "+areaSnapshot.child("Make").getValue().toString();
                    String ser="SERIES: "+areaSnapshot.child("Series").getValue().toString();
                    String year="YEAR MODEL: "+areaSnapshot.child("Year_Model").getValue().toString();
                    textcr.setText(crn);
                    textmake.setText(mk);
                    textseries.setText(ser);
                    textyrmodel.setText(year);
                }
                if(!flags) {
                    errorUnfoundvh();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
                Toast.makeText(getActivity(), " "+databaseError.toException().getMessage() , Toast.LENGTH_LONG).show();
            }
        });
        //===============================firebase ver2==================================//
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Vehicle");
        SharedPreferences wheels = getActivity().getSharedPreferences("MyApp", MODE_PRIVATE);
        plateno ="Plate No: "+ wheels.getString("vehicle", "UNKNOWN");
        final String car=wheels.getString("vehicle", "UNKNOWN");
        textplate.setText(plateno);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flags=false;
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String key=areaSnapshot.getKey();

                    if(key.equals(car)) {
                        flags=true;
                        String crn="CR No: "+areaSnapshot.child("CR_No").getValue().toString();
                        String mk="Make: "+areaSnapshot.child("Make").getValue().toString();
                        String ser="Series: "+areaSnapshot.child("Series").getValue().toString();
                        String year="Year Model: "+areaSnapshot.child("Year_Model").getValue().toString();
                        textcr.setText(crn);
                        textmake.setText(mk);
                        textseries.setText(ser);
                        textyrmodel.setText(year);
                    }
                }
                if(!flags) {
                    errorUnfoundvh();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        Refresh();
    }
    public void Refresh()
    {
        textdriver_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RefreshTwo();
            }
        });
    }
    public void RefreshTwo()
    {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if bluetooth is on or off
    }

    public void rFIDs(){
        textdriver_name=(TextView)getActivity().findViewById(R.id.driver_name);
        textlicense_No=(TextView)getActivity().findViewById(R.id.license_No);
        textrestriction=(TextView)getActivity().findViewById(R.id.restriction);
        textlicense=(TextView)getActivity().findViewById(R.id.license);
        textaddress=(TextView)getActivity().findViewById(R.id.address);
        textres_code=(TextView)getActivity().findViewById(R.id.res_code);
        textexpiry=(TextView)getActivity().findViewById(R.id.expiry);
        textagency=(TextView)getActivity().findViewById(R.id.agency);
        textBdate=(TextView)getActivity().findViewById(R.id.txtDate);
        textSex=(TextView)getActivity().findViewById(R.id.txtSex);
        textHT=(TextView)getActivity().findViewById(R.id.txtHt);
        textWT=(TextView)getActivity().findViewById(R.id.txtWt);
        disp_profilepic = (CircleImageView)getActivity().findViewById(R.id.profilepic);
        //textaddress2=(TextView)getActivity().findViewById(R.id.addr);
        textnationality=(TextView)getActivity().findViewById(R.id.nationality);

        textplate=(TextView)getActivity().findViewById(R.id.plate_no);
        textcr=(TextView)getActivity().findViewById(R.id.cr_no);
        textmake=(TextView)getActivity().findViewById(R.id.make);
        textseries=(TextView)getActivity().findViewById(R.id.series);
        textyrmodel=(TextView)getActivity().findViewById(R.id.year_model);
        //refresh=(TextView)getActivity().findViewById(R.id.driver_name);
    }
    private void errorUnfound() {
        Toast.makeText(getActivity(),
                "No Record Found", Toast.LENGTH_LONG).show();
    }
    private void errorUnfoundvh() {
        Toast.makeText(getActivity(),
                "No record found in your vehicle.", Toast.LENGTH_LONG).show();
    }
}
