package com.example.administrator.speedviocatcher;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
/*import android.support.v7.app.NotificationCompat;*/
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import com.firebase.client.core.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;




//for phone auth
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;
//phoneAuth

import dmax.dialog.SpotsDialog;

import static android.media.audiofx.Visualizer.STATE_INITIALIZED;

public class Registration extends AppCompatActivity implements View.OnClickListener{


    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    public boolean valid;
    private Context mContext;
    Button bsignup;

    EditText license,pword,pword2,email;
    TextView tvlogin;
    private AlertDialog progressDialogs;
    private static final String TAG = "Registration";
    private FirebaseAuth mAuth, mmAuth;
    String number;
    private boolean mVerificationInProgress = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    View rootView;
    /*final AlertDialog.Builder dialog = new AlertDialog.Builder(Registration.this);

    LayoutInflater factory=LayoutInflater.from(Registration.this);
    final View textEntryView=factory.inflate(R.layout.customalert,null);*/
    AlertDialog.Builder dialog;
    LayoutInflater factory;
    View textEntryView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("/Drivers_Info/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        //setTitle("Registration");

        mAuth = FirebaseAuth.getInstance();
        mmAuth = FirebaseAuth.getInstance();
        mContext=this;
        dialog = new AlertDialog.Builder(Registration.this);

        LayoutInflater factory=LayoutInflater.from(Registration.this);
        textEntryView=factory.inflate(R.layout.customalert,null);
        RFIDs();




        bsignup.setOnClickListener(this);
        tvlogin.setOnClickListener(this);
// Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential

                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]

                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }*/

    public void RFIDs()
    {

        rootView =(View)findViewById(R.id.rootView);
        bsignup = (Button) findViewById(R.id.btnSigUp);
        tvlogin = (TextView) findViewById(R.id.link_login);

        email = (EditText) findViewById(R.id.etEmail);
        email.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(email, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


        license = (EditText) findViewById(R.id.etLicense);

        // TextWatcher would let us check validation error on the fly
        license.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(license);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        pword = (EditText) findViewById(R.id.etPassword);
        pword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(pword);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        pword2 = (EditText) findViewById(R.id.etPassword2);
        pword2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(pword2);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSigUp:

                String slicence=license.getText().toString();
                String email2=email.getText().toString();
                String password=pword.getText().toString();
                String confirmPassword=pword2.getText().toString();








                // check if any of the fields are vaccant
                if(email2.equals("")||password.equals("")||confirmPassword.equals(""))
                {
                    Toast.makeText(Registration.this, "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if(!password.equals(confirmPassword))
                {
                    Toast.makeText(Registration.this, "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    progressDialogs = new SpotsDialog(mContext, R.style.Custom);
                    progressDialogs.show();
                    //================verify
                    myRef.orderByChild("License_No").equalTo(slicence).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean flag=false;

                            for (com.google.firebase.database.DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                                Log.d(TAG, "PARENT: " + childDataSnapshot.getKey());
                                number=childDataSnapshot.child("Mobile_No").getValue().toString();
                                flag=true;

                                //if true then save with number
                                startPhoneNumberVerification(number);
                                progressDialogs.dismiss();
                                if ( checkValidation () ) {

                                    //progressDialogs = new SpotsDialog(mContext, R.style.Custom);
                                    if (mContext != null) {
                                        // final ProgressDialog mProgressDialog = ProgressDialog.show(mContext, "Please wait...", "Connecting...\n(Powered by LexDark)", true);
                                        //progressDialogs.show();
                                        //finallogo ProgressDialog prog=new ProgressDialog.show(Registration.this,"Please wait...","Connecting...", true);




                                        Toast.makeText(Registration.this, "The SMS verification code has been sent to the registered phone number.", Toast.LENGTH_LONG).show();


                                                            //Toast.makeText(Registration.this, "Realtime Database Updated!", Toast.LENGTH_LONG).show();
                                                            //realtimedatabase
                                                            //=======================================dialog



                                                            dialog.setView(textEntryView).setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                                                public void onClick(DialogInterface dialog,int id){
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    //LoginManager.getInstance().logOut();
                                                                    //Intent i = new Intent(Registration.this, Login.class);
                                                                    //startActivity(i);
                                                                    //finish();
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                            dialog.setView(textEntryView).setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                                                public void onClick(DialogInterface dialog,int id){
                                                                    EditText code=(EditText)textEntryView.findViewById(R.id.txtCode);

                                                                    /*String textCode=code.getText().toString();
                                                                    if(textCode.equals(""))
                                                                    {
                                                                        code.setError("Invalid code.");
                                                                        //code.setError("Invalid code.");
                                                                        //Toast.makeText(Registration.this, "You have no record in the system.", Toast.LENGTH_LONG).show();
                                                                    }else{
                                                                    verifyPhoneNumberWithCode(mVerificationId, textCode);
                                                                    }
                                                                    code.setError("Invalid code.");*/

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

                                                            Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                                                            b.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {

                                                                    EditText code=(EditText)textEntryView.findViewById(R.id.txtCode);
                                                                    String textCode=code.getText().toString();
                                                                    if(textCode.equals(""))
                                                                    {
                                                                        code.setError("Invalid code.");
                                                                        //code.setError("Invalid code.");
                                                                        //Toast.makeText(Registration.this, "You have no record in the system.", Toast.LENGTH_LONG).show();
                                                                    }else{
                                                                        //progressDialogs = new SpotsDialog(mContext, R.style.Custom);
                                                                        //progressDialogs.show();
                                                                        verifyPhoneNumberWithCode(mVerificationId, textCode);
                                                                       // progressDialogs.dismiss();

                                                                       /* if(!getFlag(valid)) {
                                                                            *//*code.setError("Invalid code.");*//*

                                                                        }else{

                                                                        }*/
                                                                       /* Boolean valid=null;
                                                                        getFlag(valid);*/


                                                                    }

                                                                }
                                                            });
                                                            //=====================================dialog
                                    }

                                }else {
                                    Toast.makeText(Registration.this, "Form contains error", Toast.LENGTH_LONG).show();
                                }



                            }
                            if(!flag) {
                                progressDialogs.dismiss();
                                license.setError("No record");
                                Toast.makeText(Registration.this, "You have no record in the system.", Toast.LENGTH_LONG).show();


                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });
                    //================verify



                    /*if ( checkValidation () ) {

                        progressDialogs = new SpotsDialog(mContext, R.style.Custom);
                        if (mContext != null) {
                           // final ProgressDialog mProgressDialog = ProgressDialog.show(mContext, "Please wait...", "Connecting...\n(Powered by LexDark)", true);
                            progressDialogs.show();
                            //finallogo ProgressDialog prog=new ProgressDialog.show(Registration.this,"Please wait...","Connecting...", true);
                            mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pword.getText().toString().trim())
                                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //mProgressDialog.dismiss();
                                            progressDialogs.dismiss();
                               *//* if(!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthWeakPasswordException e) {
                                        password.setError(getString(R.string.error_weak_password));
                                        mTxtPassword.requestFocus();
                                    } catch(FirebaseAuthInvalidCredentialsException e) {
                                        mTxtEmail.setError(getString(R.string.error_invalid_email));
                                        mTxtEmail.requestFocus();
                                    } catch(FirebaseAuthUserCollisionException e) {
                                        mTxtEmail.setError(getString(R.string.error_user_exists));
                                        mTxtEmail.requestFocus();
                                    } catch(Exception e) {
                                        Log.e(TAG, e.getMessage());
                                    }
                                }*//*

                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                //Log.d(TAG, "createUserWithEmail:success");
                                                Toast.makeText(Registration.this, "Registration successful!",
                                                        Toast.LENGTH_SHORT).show();
                                   *//* FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);*//*
                                                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                                                prefs.edit().putString("email", email.getText().toString()).apply();

                                           *//* SharedPreferences prefs2 = getSharedPreferences("MyApp", MODE_PRIVATE);
                                            prefs2.edit().putString("uname", uname.getText().toString()).apply();*//*

                                                //realtimedatabase
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                String uid = user.getUid();
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                                DatabaseReference myRef = database.getReference().child("Driver/" + uid);
                                                myRef.child("Username").setValue(license.getText().toString());
                                                myRef.child("Email").setValue(email.getText().toString());
                                                Toast.makeText(Registration.this, "Realtime Database Updated!", Toast.LENGTH_LONG).show();
                                                //realtimedatabase

                                                Intent i = new Intent(Registration.this, SyncActivity.class);
                                                //i.putExtra("Email", mAuth.getCurrentUser().getEmail());
                                                startActivity(i);
                                                finish();

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("signInWithEmail:failure", task.getException().toString());
                                                Toast.makeText(Registration.this, task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                                //updateUI(null);
                                            }
                                        }
                                    });
                        }
                    *//*
                    String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);
                    // check if the Stored password matches with Password entered by user
                    if(password.equals(storedPassword))
                    {
                        Toast.makeText(Registration.this, "Account already exist!\nPlease try again...", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //Save personal name
                        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                        prefs.edit().putString("username", userName).apply();

                        // Save the Data in Database
                        loginDataBaseAdapter.insertEntry(userName, password);
                        Toast.makeText(Registration.this, "Account Successfully Created ", Toast.LENGTH_LONG).show();
                        finish();

                        //intent
                        Intent i=new Intent(this, MainActivity.class);
                        startActivity(i);
                    }*//*
                    }else {
                        Toast.makeText(Registration.this, "Form contains error", Toast.LENGTH_LONG).show();
                    }*/
                }



                break;
            case R.id.link_login:
                Intent login = new Intent(Registration.this, Login.class);
                startActivity(login);
                finish();
                break;
        }
    }
    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(license)) ret = false;
        //if (!Validation.hasText(pword)) ret = false;
        if (!Validation.hasText(pword) || pword.length() < 6 || pword.length() > 10) {
            pword.setError("between 6 and 10 alphanumeric characters");
            ret = false;
        } else {
            pword.setError(null);
        }
        if (!Validation.hasText(pword2) || pword2.length() < 6 || pword2.length() > 10) {
            pword2.setError("between 6 and 10 alphanumeric characters");
            ret = false;
        } else {
            pword2.setError(null);
        }
        if (!Validation.isEmailAddress(email, true)) ret = false;

        return ret;
    }



    //PhoneAuth

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    // [START sign_in_with_phone]
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mmAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialogs = new SpotsDialog(mContext, R.style.Custom);
                        progressDialogs.show();

                        if (task.isSuccessful()) {
                            EditText code=(EditText)textEntryView.findViewById(R.id.txtCode);
                            //code.setTextColor(Color.BLACK);
                            code.setError(null);
                            /*valid = true;
                            getFlag(valid);*/

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //valid=true;
                            mmAuth.signOut();
                            mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pword.getText().toString().trim())
                                    .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //mProgressDialog.dismiss();

                                            progressDialogs.dismiss();

                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                //Log.d(TAG, "createUserWithEmail:success");
                                                Toast.makeText(Registration.this, "Registration successful!",
                                                        Toast.LENGTH_SHORT).show();

                                                SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                                                prefs.edit().putString("email", email.getText().toString()).apply();

                                                SharedPreferences lisensyas = getSharedPreferences("MyApp", MODE_PRIVATE);
                                                lisensyas.edit().putString("lisensya", license.getText().toString()).apply();

                                           /* SharedPreferences prefs2 = getSharedPreferences("MyApp", MODE_PRIVATE);
                                            prefs2.edit().putString("uname", uname.getText().toString()).apply();*/

                                                //realtimedatabase
                                                FirebaseUser usern = FirebaseAuth.getInstance().getCurrentUser();
                                                String uid = usern.getUid();
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                                final DatabaseReference myRef = database.getReference().child("User/" + uid);
                                                //final DatabaseReference myRef = database.getReference().child("Driver/" + uid);
                                                myRef.child("License").setValue(license.getText().toString());
                                                myRef.child("Email").setValue(email.getText().toString());
                                                myRef.child("Mobile_No").setValue(number);

                                                myRef.child("Verify_Status").setValue("Verified");
                                                //code.setText(number);
                                                //if code is auth(register the number value)
                                                //ibutang dre
                                                Intent i = new Intent(Registration.this, SelectVehicle.class);
                                                //i.putExtra("Email", mAuth.getCurrentUser().getEmail());
                                                startActivity(i);
                                                Toast.makeText(Registration.this, "Registration Successful.", Toast.LENGTH_LONG).show();
                                                finish();

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("signInWithEmail:failure", task.getException().toString());
                                                Toast.makeText(Registration.this, task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                                //updateUI(null);
                                            }
                                        }
                                    });
                            //FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            // [END_EXCLUDE]

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progressDialogs.dismiss();
                                // getFlag();
                                //==============================
                                //Snackbar snackbar = Snackbar
                                // .make(rootView, "The verification code entered was invalid.", Snackbar.LENGTH_LONG);
                                        /*.setAction("RESEND", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                resendVerificationCode(number, mResendToken);
                                            }
                                        });
                                // Changing message text color
                                snackbar.setActionTextColor(Color.RED);

// Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();*/
                                // snackbar.show();
                                //==============================
                                EditText code=(EditText)textEntryView.findViewById(R.id.txtCode);
                                Toast.makeText(Registration.this, "False", Toast.LENGTH_LONG).show();
                                Snackbar snackbar = Snackbar
                                        .make(textEntryView, "The verification code entered was invalid.", Snackbar.LENGTH_LONG)
                                        .setAction("RESEND", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                resendVerificationCode(number, mResendToken);
                                                Toast.makeText(Registration.this, "Resending code.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                // Changing message text color
                                snackbar.setActionTextColor(Color.RED);

                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                //code.setTextColor(Color.RED);
                                code.setError("Invalid code.");
                                snackbar.show();
                                /*valid = false;
                                getFlag(valid);*/
                                Toast.makeText(Registration.this, "Invalid code.", Toast.LENGTH_LONG).show();
                                // [START_EXCLUDE silent]
                                //valid=false;
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            // [END_EXCLUDE]
                        }
                        }
                });


    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    // [END sign_in_with_phone]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    public Boolean getFlag(Boolean valid){ return  valid;}

}
