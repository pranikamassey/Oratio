package com.darklightning.partycatrers.Authentication.RegisterUserFolder;




import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.util.Log;

import com.darklightning.partycatrers.Authentication.AuthenticationFragments.RegisterPhoneFragment;
import com.darklightning.partycatrers.Constants.MyConstants;
import com.darklightning.partycatrers.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPhoneActivity extends AppCompatActivity {
    int signinMethod;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_phone_activity);

        signinMethod = getIntent().getExtras().getInt(MyConstants.SIGNIN_METHOD);
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.SIGNIN_METHOD,signinMethod);

        RegisterPhoneFragment rpf = new RegisterPhoneFragment();
        rpf.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.replae_container,rpf,"signupfragment").commit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("signupfragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    protected void onStart() {
        if(signinMethod==MyConstants.GOOGLE_SIGNIN)
        {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (signinMethod == MyConstants.FACEBOOK_SIGNIN) {
            Log.e("facebooklogout","yep");
            LoginManager.getInstance().logOut();
        }

        if (signinMethod == MyConstants.EMAIL_SIGNIN || signinMethod == MyConstants.TWITTER_SIGNIN)
        {
            Log.e("twitterlogout","yep");
        }

        if(signinMethod==MyConstants.GOOGLE_SIGNIN)
        {
            Log.e("googlelogout","yep");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                        }
                    });
        }
        FirebaseAuth.getInstance().getCurrentUser().delete();
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
    }
}

