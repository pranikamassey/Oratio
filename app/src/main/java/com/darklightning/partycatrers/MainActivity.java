package com.darklightning.partycatrers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.darklightning.partycatrers.Caterers.CaterersList;

import com.darklightning.partycatrers.Constants.MyConstants;
import com.darklightning.partycatrers.Authentication.LoginUser.LoginActivity;
import com.darklightning.partycatrers.Fab.TakePictureActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FloatingActionButton postButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userName,userEmailId,userPhone,uId;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private int signinMethod;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.main_menu:
                    fragment = CaterersList.newInstance();
                    break;
                case R.id.main_map:
                    break;
            }
            if(fragment!=null)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content,fragment);
                fragmentTransaction.commit();
            }
            return true;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_resourses,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.sign_out)
        {
            if (signinMethod == MyConstants.FACEBOOK_SIGNIN) {
                Log.e("facebooklogout","yep");
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                gotoLoginActivity();
            }

            if (signinMethod == MyConstants.EMAIL_SIGNIN || signinMethod == MyConstants.TWITTER_SIGNIN)
            {
                Log.e("twitterlogout","yep");
                FirebaseAuth.getInstance().signOut();
                gotoLoginActivity();
            }

            if(signinMethod==MyConstants.GOOGLE_SIGNIN)
            {
                Log.e("googlelogout","yep");
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                FirebaseAuth.getInstance().signOut();
                               gotoLoginActivity();
                            }
                        });
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void gotoLoginActivity()
    {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
        finish();
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signinMethod = getIntent().getExtras().getInt(MyConstants.SIGNIN_METHOD);
        postButton = (FloatingActionButton) findViewById(R.id.float_post_action_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,TakePictureActivity.class);
                startActivity(intent);

            }
        });
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mContext=this;

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
            if(user!=null)
            {
                userName = user.getDisplayName();
                userEmailId = user.getEmail();
                userPhone = user.getPhoneNumber();
                uId = user.getUid();
                Toast.makeText(this, "user id:"+uId+"email:"+userEmailId, Toast.LENGTH_LONG).show();
            }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(0);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
