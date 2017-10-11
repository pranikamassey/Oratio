package com.darklightning.partycatrers.Authentication.AuthenticationFragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.darklightning.partycatrers.Constants.MyConstants;
import com.darklightning.partycatrers.MainActivity;
import com.darklightning.partycatrers.R;
import com.darklightning.partycatrers.Authentication.RegisterUserFolder.RegisterPhoneActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by rikki on 6/24/17.
 */

public class GoogleFragment extends Fragment implements View.OnClickListener
{
    private SignInButton googleSignUpButton;
    private GoogleApiClient mGoogleApiClient;
    private String googleIdToken;
    private static final int RC_SIGN_IN = 1 ;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener  mAuthListener;
    private String callingKey;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_google,container,false);
//        CallingKey = getString(R.string.calling_key);
//        CalledForRegister = getString(R.string.called_for_register);
        callingKey = getArguments().getString(MyConstants.CALLING_KEY);
        mAuth= FirebaseAuth.getInstance();
        googleSignUpButton = (SignInButton) v.findViewById(R.id.fragment_google_signup_button);

        googleSignUpButton.setOnClickListener(this);


        /*
            changing google login  button attributes below
         */
        ///////////////////////////////////////////////////////////////////
        TextView textView = (TextView) googleSignUpButton.getChildAt(0);
        textView.setText("");
        textView.setBackgroundResource(R.drawable.google);
        ///////////////////////////////////////////////////////////////////



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

        return v;

    }
    public void googleSignIn()
    {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .enableAutoManage((FragmentActivity) getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getActivity(), "unable to Google Sign in", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);

    }
    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.stopAutoManage((FragmentActivity) getActivity());
        mGoogleApiClient.disconnect();
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
//                if(MyConstants.CALLED_FOR_REGISTER.equals(CallingMode))
//                {
//                    linkAcountWithGoogle();
//                }
//                else
                {


                        signInWithGoogle();

                }
//                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    private void signInWithGoogle()
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            if (callingKey == MyConstants.CALLED_FOR_LOGIN) {
                                if (checkIfPhoneExist()) {
                                    Toast.makeText(getActivity(), "" + mAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                                    startMainActivity();
                                    Toast.makeText(getActivity(), "Signing in with google", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Account does not exist !!!", Toast.LENGTH_LONG).show();
                                }

                            } else if (callingKey == MyConstants.CALLED_FOR_REGISTER) {
                                if(mAuth.getCurrentUser().getPhoneNumber()==null)
                                {
                                    // sendVerificationEmail();
                                    startPhoneRegisteration();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Account already created!!!",Toast.LENGTH_LONG).show();
                                    logout();
                                }
                            } else {
                                Toast.makeText(getActivity(), "calling key not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }


    private void linkAcountWithGoogle()
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startMainActivity();
                            sendVerificationEmail();
                            Toast.makeText(getActivity(), "google acount Linked!!!", Toast.LENGTH_SHORT).show();

                        } else {

                        }


                    }
                });
    }


    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "email has been sent", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "email has not been sent", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private boolean checkIfPhoneExist()
    {
        if(mAuth.getCurrentUser().getPhoneNumber()==null)
        {

            Toast.makeText(getActivity(), "Phone Num is null", Toast.LENGTH_SHORT).show();
            deleteCurrentUser();
            logout();
            return false;
        }
        return true;
    }
    private void logout() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();
                if(mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess())
                            {
                                Toast.makeText(getActivity(),"Account does'nt exist !!!" , Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        }
                    });
                }
            }
            @Override
            public void onConnectionSuspended(int i) {
                Log.d("TAG", "Google API Client Connection Suspended");
            }
        });
    }

    private void deleteCurrentUser()
    {
        mAuth.getCurrentUser().delete();
    }
    private void startMainActivity()
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MyConstants.SIGNIN_METHOD,MyConstants.GOOGLE_SIGNIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
    private void startPhoneRegisteration()
    {
        Intent intent = new Intent(getActivity(), RegisterPhoneActivity.class);
        intent.putExtra(MyConstants.SIGNIN_METHOD,MyConstants.GOOGLE_SIGNIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fragment_google_signup_button :
                googleSignIn();
                break;
        }
    }
}
