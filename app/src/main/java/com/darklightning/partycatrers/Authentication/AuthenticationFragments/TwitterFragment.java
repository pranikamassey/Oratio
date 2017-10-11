package com.darklightning.partycatrers.Authentication.AuthenticationFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.darklightning.partycatrers.Constants.MyConstants;
import com.darklightning.partycatrers.MainActivity;
import com.darklightning.partycatrers.R;
import com.darklightning.partycatrers.Authentication.RegisterUserFolder.RegisterPhoneActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;



/**
 * Created by rikki on 6/24/17.
 */

public class TwitterFragment extends Fragment
{
    public TwitterLoginButton mLoginButton;
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String callingKey;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_twitter,container,false);
        callingKey = getArguments().getString(MyConstants.CALLING_KEY);

        mContext = getActivity();

        mAuth = FirebaseAuth.getInstance();
        mLoginButton = (TwitterLoginButton) v.findViewById(R.id.fragment_twitter_signup_button);


        /*
            changing twitter login  button attributes below
         */
        ////////////////////////////////////////////////////////
        mLoginButton.setPadding(10,0,0,0);
        mLoginButton.setText("");
        mLoginButton.setBackgroundResource(R.drawable.twiter);
        //////////////////////////////////////////////////////

        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e("twitter", "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("twitter", "twitterLogin:failure", exception);
            }
        });
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the Twitter login button.
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.e("twitter", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

            signInWithTwitter(credential);

    }

    private void signInWithTwitter(AuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (callingKey == MyConstants.CALLED_FOR_LOGIN)
                        {
                            if (checkIfPhoneExist())
                            {
                                Toast.makeText(getActivity(), "" + mAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                                startMainActivity();
                                Toast.makeText(getActivity(), "Signing in with Twitter", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Account does not exist !!!", Toast.LENGTH_LONG).show();
                            }

                        }
                        else if (callingKey == MyConstants.CALLED_FOR_REGISTER)
                        {
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

                        }
                        else
                        {
                            Toast.makeText(getActivity(), "calling key not registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean checkIfPhoneExist()
    {
        if(mAuth.getCurrentUser().getPhoneNumber()==null)
        {
            deleteCurrentUser();
            Toast.makeText(getActivity(), "Account does'nt exist", Toast.LENGTH_SHORT).show();
            logout();
            return false;
        }
        return true;
    }
    private void logout()
    {
        mAuth.signOut();
    }
    private void deleteCurrentUser()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete();
    }
    private void startMainActivity()
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MyConstants.SIGNIN_METHOD,MyConstants.TWITTER_SIGNIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
    private void startPhoneRegisteration()
    {
        Intent intent = new Intent(getActivity(), RegisterPhoneActivity.class);
        intent.putExtra(MyConstants.SIGNIN_METHOD,MyConstants.TWITTER_SIGNIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}

