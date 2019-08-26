package com.example.administrator.speedviocatcher;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
//import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
/*import android.support.v7.app.NotificationCompat;*/
import android.support.v7.app.AppCompatActivity;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.speedviocatcher.NavigationActivity;
import com.example.administrator.speedviocatcher.R;
import com.example.administrator.speedviocatcher.Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

import static com.example.administrator.speedviocatcher.AppStatus.context;
import static com.example.administrator.speedviocatcher.R.drawable.view;

/**
 * Created by Alyssa on 3/5/2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    //private Firebase fElement, fNames;
    //int increement = 0;
    private Context mContext;
    private FirebaseAuth mAuth;
    private AlertDialog progressDialogs;

    private static final String TAG = "LoginPage";
    TextView tvSighUpS,tvForgetpw;
    Button log;
    /**Button sbsignup;*/
    EditText email,pword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        //setTitle("Login");
        //Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();

        rFIDs();

        //HTML CODE
        //Spanned createacct = Html.fromHtml("Do not have an account? <b>Create one</b>");
        //tvSighUpS.setText(Html.fromHtml("Do not have an account? <b>Create one</b>"));
        //Spanned fromHtml = HtmlCompat.fromHtml(context, "Do not have an account? <b>Create one</b>", 0);
        // You may want to provide an ImageGetter, TagHandler and SpanCallback:
        //Spanned fromHtml = HtmlCompat.fromHtml(context, source, 0,
        //        imageGetter, tagHandler, spanCallback);
        //tvSighUpS.setMovementMethod(LinkMovementMethod.getInstance());
        //tvSighUpS.setText(fromHtml);
        if (Build.VERSION.SDK_INT >= 24)
        {
            tvSighUpS.setText(Html.fromHtml("Do not have an account? "+"<b>" + "Create one." + "</b>",Html.FROM_HTML_MODE_LEGACY));
        }
        //HTMLCODING


        tvSighUpS.setOnClickListener(this);
        //tvForgetpw.setOnClickListener(this);
        log.setOnClickListener(this);
        mContext = this;
       /* fElement = new Firebase("https://account-login-4dc0a.firebaseio.com/" + increement + "/name");

        fElement.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                String fname = dataSnapshot.getValue(String.class);
                Toast.makeText(Login.this, "First Element is: "+fname, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

            if (currentUser != null) {
                if (mContext != null) {
                    final ProgressDialog mProgressDialog = ProgressDialog.show(mContext, "Please wait...", "\n(...PoweredByLexDark...)", true);

                    Intent profile = new Intent(Login.this, SelectVehicle.class);
                    startActivity(profile);
                    mProgressDialog.dismiss();
            }
        }
        /*String userid=mAuth.getCurrentUser().toString();
        Toast.makeText(Login.this, "User: "+ userid , Toast.LENGTH_LONG).show();*/
    }

    public void rFIDs(){
        tvSighUpS = (TextView) findViewById(R.id.tvSignup);
       /* tvForgetpw = (TextView) findViewById(R.id.tvForgetpass);*/
        log = (Button) findViewById(R.id.btnLogin);
        email = (EditText) findViewById(R.id.edtEmail);
        pword = (EditText) findViewById(R.id.edtPassword);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvSignup:

                Intent signup = new Intent(Login.this, Registration.class);
                startActivity(signup);
                finish();
                break;
            //case R.id.tvForgetpass:

                //Toast.makeText(Login.this, "Verification through email not yet ready!", Toast.LENGTH_LONG).show();


                /*Snackbar snackbar = Snackbar
                        .make(tvForgetpw, "Email verification not yet ready!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(),Login.class);
                                PendingIntent pIntent=PendingIntent.getActivity(getApplicationContext(),(int) System.currentTimeMillis(),intent,0);
                                Resources r=getResources();
                                Notification noti=null;
                                //if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.JELLY_BEAN){
                                noti=new NotificationCompat.Builder(Login.this).addAction(R.drawable.delete,"Back", pIntent).addAction(R.drawable.view2,"Stare", pIntent).setAutoCancel(true).setTicker(r.getString(R.string.app_name)).setContentTitle("HELLO MASTER!").setContentText("Are you calling me?").setSmallIcon(R.drawable.add).setContentIntent(pIntent).build();

                                //}
                                NotificationManager notiManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                assert noti!=null;
                                noti.flags|=Notification.FLAG_AUTO_CANCEL;

                                notiManager.notify(0,noti);
                            }
                        });

// Changing message text color
                snackbar.setActionTextColor(Color.RED);

// Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();*/
                //break;

            case R.id.btnLogin:
                String email2= email.getText().toString();
                String password= pword.getText().toString();
                if(email2.equals("")||password.equals(""))
                {
                    Toast.makeText(Login.this, "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                /*
                String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);
                // check if the Stored password matches with Password entered by user
                if(password.equals(storedPassword))
                {
                    SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                    prefs.edit().putString("username", userName).apply();

                    finish();

                    Intent login = new Intent(this, MainActivity.class);
                    startActivity(login);

                    Toast.makeText(Login.this, "Login Successfull!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Login.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }*/

                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                prefs.edit().putString("email", email.getText().toString()).apply();
                progressDialogs = new SpotsDialog(mContext, R.style.Custom);
                if (mContext != null) {

                    //final ProgressDialog mProgressDialog = ProgressDialog.show(mContext,"Please wait...","Connecting...\n(Powered by LexDark)", true);
                    progressDialogs.show();

                //finallogo ProgressDialog prog=new ProgressDialog.show(Login.this,"Please wait...","Connecting...", true);
                (mAuth.signInWithEmailAndPassword(email2, password))
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //mProgressDialog.dismiss();
                                progressDialogs.dismiss();
                                if (task.isSuccessful()) {

                                    //check verification
                                    FirebaseUser user =  mAuth.getCurrentUser();
                                    final String uid = user.getUid();
                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("Driver/"+uid).child("License");

                                    myRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                        @Override
                                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                            String liceno = dataSnapshot.getValue(String.class);

                                                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                                                prefs.edit().putString("email", email.getText().toString()).apply();

                                                SharedPreferences lisensyas = getSharedPreferences("MyApp", MODE_PRIVATE);
                                                lisensyas.edit().putString("lisensya", liceno).apply();

                                                Intent login = new Intent(Login.this, SelectVehicle.class);
                                                startActivity(login);
                                                finish();

                                           /* }else{
                                                //=======================================dialog
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);

                                                LayoutInflater factory=LayoutInflater.from(Login.this);
                                                final View textEntryView=factory.inflate(R.layout.customalert,null);


                                                dialog.setView(textEntryView).setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                                    public void onClick(DialogInterface dialog,int id){
                                                        FirebaseAuth.getInstance().signOut();
                                                        //LoginManager.getInstance().logOut();

                                                        dialog.cancel();
                                                    }
                                                });
                                                dialog.setView(textEntryView).setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                                    public void onClick(DialogInterface dialog,int id){
                                                        EditText code=(EditText)textEntryView.findViewById(R.id.txtCode);

                                                        DatabaseReference myRef = database.getReference("Driver/"+uid);
                                                        String textCode=code.getText().toString();
                                                        myRef.child("Verify Status").setValue("Verified");
                                                        //code.setText(number);
                                                        //if code is auth(register the number value)
                                                        Intent i = new Intent(Login.this, SyncActivity.class);
                                                        //i.putExtra("Email", mAuth.getCurrentUser().getEmail());
                                                        startActivity(i);
                                                        finish();

                                                    }
                                                });
                                                AlertDialog alert=dialog.create();
                                                alert.setTitle("Enter Verification Code.");
                                                alert.setIcon(R.drawable.add);
                                                alert.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                                                alert.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.add);
                                                //dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.view2);
                                                //colorheader
                                                alert.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
                                                alert.show();
                                                //=====================================dialog
                                                Toast.makeText(Login.this, "Please verify your account.", Toast.LENGTH_LONG).show();
                                            }*/
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.w(TAG, "Failed to read value.", databaseError.toException());
                                            Toast.makeText(Login.this, " "+databaseError.toException().getMessage() , Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    //check verification


                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "signInWithEmail:success");
                                    /*=======SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                                    prefs.edit().putString("email", email.getText().toString()).apply();



                                    Intent login = new Intent(Login.this, SyncActivity.class);
                                    startActivity(login);
                                    finish();==============*/
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w("signInWithEmail:failure", task.getException().toString());
                                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    //updateUI(null);
                                }
                            }
                        });
                }
                break;
        }
    }
}
