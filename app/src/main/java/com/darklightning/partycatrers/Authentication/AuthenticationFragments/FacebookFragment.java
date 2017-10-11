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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by rikki on 6/24/17.
 */

public class FacebookFragment extends Fragment
{

    LoginButton loginButton;
    FirebaseAuth mAuth;
    Context mContext;
    private CallbackManager mCallbackManager;
    private String callingKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_facebook,container,false);
        callingKey = getArguments().getString(MyConstants.CALLING_KEY);
        mContext = getActivity();

        loginButton = (LoginButton) v.findViewById(R.id.fragment_fb_signup_button);
        loginButton.setFragment(this);

        /*
            changing facebook login  button attributes below
         */
        ////////////////////////////////////////////////
        loginButton.setText("");
        loginButton.setBackgroundResource(R.drawable.fb);
        ////////////////////////////////////////////////

        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("FB", "facebook:onSuccess:" + loginResult);
                        signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e("FB", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FB", "facebook:onError", error);
                // ...
            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void signInWithFacebook(AccessToken token)
    {
        Log.e("FB", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (callingKey == MyConstants.CALLED_FOR_LOGIN)
                            {
                                if (checkIfPhoneExist())
                                {
                                    startMainActivity();
                                    Toast.makeText(getActivity(), "Signing in with Facebook", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), "calling key not identified", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("FB", "signInWithCredential:failure");
                            Toast.makeText(mContext, "Authentication failed.",Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private boolean checkIfPhoneExist()
    {
        if(mAuth.getCurrentUser().getPhoneNumber()==null)
        {
            deleteCurrentUser();
            Toast.makeText(getActivity(), "Account does not exist!!!", Toast.LENGTH_SHORT).show();
            logout();
            return false;
        }
        return true;
    }
    private void logout()
    {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }
    private void deleteCurrentUser()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete();
    }
    private void startMainActivity()
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MyConstants.SIGNIN_METHOD,MyConstants.FACEBOOK_SIGNIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
    private void startPhoneRegisteration()
    {
        Intent intent = new Intent(getActivity(), RegisterPhoneActivity.class);
        intent.putExtra(MyConstants.SIGNIN_METHOD,MyConstants.FACEBOOK_SIGNIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
