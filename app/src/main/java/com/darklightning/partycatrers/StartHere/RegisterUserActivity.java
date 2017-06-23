package com.darklightning.partycatrers.StartHere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.darklightning.partycatrers.MainActivity;
import com.darklightning.partycatrers.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;



public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener
{

    private static final int RC_SIGN_IN = 1 ;
    private SignInButton googleSignInButton;
    public GoogleApiClient mGoogleApiClient;

    EditText phoneNumText,otpText,emailIdText,userPassword;
    Button sendOtpButton,registerButton,resendOtpButton,loginInsteadButton;

    EditText otp1,otp2,otp3,otp4,otp5,otp6;

    private String mVerificationId,googleIdToken;

    private PhoneAuthProvider.ForceResendingToken mToken;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener  mAuthListener;
    Context mContext;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.register_user_activity);

        googleSignInButton = (SignInButton) findViewById(R.id.google_signin_button);

        phoneNumText = (EditText)findViewById(R.id.phone_number_edittext);
        otpText = (EditText)findViewById(R.id.otp_edittext);
        emailIdText = (EditText)findViewById(R.id.email_id);
        userPassword = (EditText) findViewById(R.id.user_password);

        otp1 = (EditText) findViewById(R.id.otp_et_1);
        otp2 = (EditText) findViewById(R.id.otp_et_2);
        otp3 = (EditText) findViewById(R.id.otp_et_3);
        otp4 = (EditText) findViewById(R.id.otp_et_4);
        otp5 = (EditText) findViewById(R.id.otp_et_5);
        otp6 = (EditText) findViewById(R.id.otp_et_6);

        sendOtpButton = (Button) findViewById(R.id.send_otp);
        registerButton = (Button) findViewById(R.id.register_button);
        resendOtpButton = (Button) findViewById(R.id.resend_otp);
        loginInsteadButton = (Button) findViewById(R.id.login_button);


        loginInsteadButton.setOnClickListener(this);
        sendOtpButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        resendOtpButton.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);

        otp1.addTextChangedListener(new GenricTextWatcher(otp1));
        otp2.addTextChangedListener(new GenricTextWatcher(otp2));
        otp3.addTextChangedListener(new GenricTextWatcher(otp3));
        otp4.addTextChangedListener(new GenricTextWatcher(otp4));
        otp5.addTextChangedListener(new GenricTextWatcher(otp5));
        otp6.addTextChangedListener(new GenricTextWatcher(otp6));


        mAuth = FirebaseAuth.getInstance();

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    phoneNumText.setError("Invalid phone number.");
                }
                else if(e instanceof FirebaseTooManyRequestsException)
                {
                    Toast.makeText(mContext,"Quota exceeded",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                mToken = forceResendingToken;
            }
        };

        //for email verification
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {


                } else
                {
                }

            }
        };

        // Configure Google Sign In


    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(mContext, "unable to Google Sign in", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);

    }



    public void googleSignIn()
    {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                googleIdToken = account.getIdToken();
                Toast.makeText(mContext, "token is "+googleIdToken , Toast.LENGTH_SHORT).show();
//                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        // ...
//                    }
//                });
//        Intent intent = new Intent(mContext,MainActivity.class);
//        startActivity(intent);
//    }


    //sending otp
    public void startPhoneNumberVerification(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,60, java.util.concurrent.TimeUnit.SECONDS,this,mCallBacks);
    }
    //sending otp again
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                java.util.concurrent.TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    //verifying phone number
    private void verifyPhoneNumberWithCode(String verificationId,String code)
    {
        PhoneAuthCredential credential = new PhoneAuthCredential(verificationId,code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential phoneAuthCredential)
    {

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = task.getResult().getUser();
                    //linking phone auth with email auth

                        linkAcountWithGoogle();

//                    else
//                    {
//                        linkAccountWithEmail();
//                    }


                    Toast.makeText(mContext, "you have been registered", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(mContext,"Invalid Code",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void linkAccountWithEmail()
    {
        Log.e("link","entered the linked function");
        String email = emailIdText.getText().toString();
        String password = userPassword.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("link","account has been linked");
                            sendVerificationEmail();
                            Intent intent  = new Intent(mContext,MainActivity.class);
                            startActivity(intent);

                        } else {

                        }


                    }
                });
    }

    private void linkAcountWithGoogle()
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("link","account has been linked");
                            sendVerificationEmail();
                            Intent intent  = new Intent(mContext,MainActivity.class);
                            startActivity(intent);

                        } else {

                        }


                    }
                });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.send_otp:
                if(phoneNumText.getText()==null)
                {
                    phoneNumText.setError("phone number field cant be empty");
                    return;
                }
                startPhoneNumberVerification(phoneNumText.getText().toString());

                break;
            case R.id.register_button :
                String code = otpText.getText().toString();
                verifyPhoneNumberWithCode(mVerificationId,code);
                break;
            case R.id.resend_otp :
                resendVerificationCode(phoneNumText.getText().toString(),mToken);
                break;
            case R.id.login_button  :
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.google_signin_button:
                googleSignIn();
                break;
        }
    }


    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "email has been sent", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(mContext, "nope", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public class GenricTextWatcher implements TextWatcher
    {

        private View view;
        private GenricTextWatcher(View view)
        {
            this.view = view;
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.otp_et_1:
                    if(text.length()==1)
                        otp2.requestFocus();
                    break;
                case R.id.otp_et_2:
                    if(text.length()==1)
                        otp3.requestFocus();
                    break;
                case R.id.otp_et_3:
                    if(text.length()==1)
                        otp4.requestFocus();
                    break;
                case R.id.otp_et_4:
                    if(text.length()==1)
                        otp5.requestFocus();
                    break;
                case R.id.otp_et_5:
                    if(text.length()==1)
                        otp6.requestFocus();
                    break;
                case R.id.otp_et_6:
                    break;
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


    }



}
