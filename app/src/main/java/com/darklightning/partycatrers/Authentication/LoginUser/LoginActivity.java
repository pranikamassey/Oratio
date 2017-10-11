package com.darklightning.partycatrers.Authentication.LoginUser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.darklightning.partycatrers.Constants.MyConstants;
import com.darklightning.partycatrers.R;
import com.darklightning.partycatrers.Authentication.AuthenticationFragments.EmailFragment;
import com.darklightning.partycatrers.Authentication.AuthenticationFragments.FacebookFragment;
import com.darklightning.partycatrers.Authentication.AuthenticationFragments.GoogleFragment;
import com.darklightning.partycatrers.Authentication.AuthenticationFragments.TwitterFragment;
import com.darklightning.partycatrers.Authentication.RegisterUserFolder.RegisterUserActivity;
import com.google.firebase.auth.FirebaseAuth;

import custom_font.MyTextView;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private String Mode;
    private String CallingKey;
    private MyTextView registerInstead;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mContext = this;
        registerInstead = (MyTextView) findViewById(R.id.register_account_intead);
        registerInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RegisterUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            Log.e("FirebaseAuthLogin",mAuth.getCurrentUser().getUid()+"");
        }
        Bundle bundle = new Bundle();
        bundle.putString(MyConstants.CALLING_KEY,MyConstants.CALLED_FOR_LOGIN);

        GoogleFragment googleFragment = new GoogleFragment();
        googleFragment.setArguments(bundle);
        EmailFragment emailFragment = new EmailFragment();
        emailFragment.setArguments(bundle);
        FacebookFragment facebookFragment = new FacebookFragment();
        facebookFragment.setArguments(bundle);
        TwitterFragment twitterFragment = new TwitterFragment();
        twitterFragment.setArguments(bundle);

        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.google_sign_in_container, googleFragment);
        transaction.replace(R.id.email_sign_in_container, emailFragment);
        transaction.replace(R.id.facebook_sign_in_container, facebookFragment);
        transaction.replace(R.id.twitter_sign_in_container, twitterFragment,"twittertag_signin").commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("twittertag_signin");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
