package com.darklightning.partycatrers.Authentication.RegisterUserFolder;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.darklightning.partycatrers.Authentication.LoginUser.LoginActivity;
import com.darklightning.partycatrers.Constants.MyConstants;
import com.darklightning.partycatrers.R;
import com.darklightning.partycatrers.Authentication.AuthenticationFragments.EmailFragment;
import com.darklightning.partycatrers.Authentication.AuthenticationFragments.FacebookFragment;
import com.darklightning.partycatrers.Authentication.AuthenticationFragments.GoogleFragment;
import com.darklightning.partycatrers.Authentication.AuthenticationFragments.TwitterFragment;
import com.google.firebase.auth.FirebaseAuth;

import custom_font.MyTextView;


public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener
{
    FirebaseAuth mAuth;
    private MyTextView signinInstead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user_activity);
        Bundle bundle = new Bundle();
        bundle.putString(MyConstants.CALLING_KEY,MyConstants.CALLED_FOR_REGISTER);
        signinInstead = (MyTextView) findViewById(R.id.sign_in_instead);
        signinInstead.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            Log.e("FirebaseAuthRegister",mAuth.getCurrentUser().getUid()+"");
        }
        GoogleFragment googleFragment = new GoogleFragment();
        googleFragment.setArguments(bundle);
        EmailFragment emailFragment = new EmailFragment();
        emailFragment.setArguments(bundle);
        FacebookFragment facebookFragment = new FacebookFragment();
        facebookFragment.setArguments(bundle);
        TwitterFragment twitterFragment = new TwitterFragment();
        twitterFragment.setArguments(bundle);
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.google_signip_container, googleFragment);
        transaction.replace(R.id.email_signup_container, emailFragment);
        transaction.replace(R.id.facebook_signup_container, facebookFragment);
        transaction.replace(R.id.twitter_signup_container, twitterFragment,"twittertag").commit();


        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result to the fragment, which will then pass the result to the login
            // button.
            android.app.Fragment fragment = getFragmentManager().findFragmentByTag("twittertag");
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sign_in_instead :
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }
    }
}



