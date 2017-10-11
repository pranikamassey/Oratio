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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.darklightning.partycatrers.Constants.MyConstants;
import com.darklightning.partycatrers.MainActivity;
import com.darklightning.partycatrers.R;
import com.darklightning.partycatrers.Authentication.RegisterUserFolder.RegisterPhoneActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import custom_font.MyEditText;
import custom_font.MyTextView;

/**
 * Created by rikki on 6/24/17.
 */

public class EmailFragment extends Fragment implements View.OnClickListener
{
    private MyEditText signUpEmailText,signUpPasswordText;
    private MyTextView signUpButton,forgotPassword;
    private String userEmailId,userPassword;
    private String callingKey;
    private Context mContext;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener  mAuthListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_email,container,false);

        mContext = getActivity();
        callingKey = getArguments().getString(MyConstants.CALLING_KEY);
        mAuth=FirebaseAuth.getInstance();
        forgotPassword = (MyTextView) v.findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(this);
        if(callingKey.equals(MyConstants.CALLED_FOR_REGISTER))
        {
            forgotPassword.setVisibility(View.GONE);
        }
        signUpEmailText = (MyEditText) v.findViewById(R.id.fragment_email_sign_up_email_text);
        signUpPasswordText = (MyEditText) v.findViewById(R.id.fragment_email_sign_up_password_text);
        signUpButton = (MyTextView) v.findViewById(R.id.fragment_email_sign_up_button);
        if(MyConstants.CALLED_FOR_LOGIN.equals(callingKey)==true)
        {
            signUpButton.setText("Sign in");
        }
        signUpButton.setOnClickListener(this);
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

    private void createAccountWithEmail()
    {
        userEmailId = signUpEmailText.getText().toString();
        userPassword = signUpPasswordText.getText().toString();
        if(userEmailId!=null&&!userEmailId.isEmpty()) {
            if (userEmailId.substring(userEmailId.length() - 4).equalsIgnoreCase(".com")) {
                if (userPassword == null || userPassword.isEmpty()) {
                    Toast.makeText(mContext, "Enter a valid password!!!", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(userEmailId, userPassword)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Email Account Created", Toast.LENGTH_SHORT).show();
                                    sendVerificationEmail();
                                    startPhoneRegisteration();
                                } else {
                                    Toast.makeText(getActivity(), "Account already created!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else
            {
                Toast.makeText(mContext,"Please enter a valid email", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(mContext,"Please enter an email",Toast.LENGTH_LONG).show();
        }
    }
    private void signInAccountWithEmail()
    {
        userEmailId = signUpEmailText.getText().toString();
        userPassword = signUpPasswordText.getText().toString();
        if(userEmailId!=null&&!userEmailId.isEmpty()) {
            if(userEmailId.substring(userEmailId.length()-4).equalsIgnoreCase(".com")) {
                if (userPassword == null || userPassword.isEmpty()) {
                    Toast.makeText(mContext, "Enter a valid password!!!", Toast.LENGTH_LONG).show();
                    return;
                }
                AuthCredential credential = EmailAuthProvider.getCredential(userEmailId, userPassword);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (checkIfPhoneExist()) {
                                        startMainActivity();
                                    } else {
                                        Toast.makeText(getActivity(), "Phone num not registered with this account", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    Toast.makeText(getActivity(),"Email or Password incorrect", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
            else
            {
                Toast.makeText(mContext,"Please enter a valid email", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(mContext,"Please enter an email",Toast.LENGTH_LONG).show();
        }
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
    private void logout()
    {
        FirebaseAuth.getInstance().signOut();
    }
    private void deleteCurrentUser()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete();
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
                            Toast.makeText(mContext, "email has not been sent", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void changeCurrentPassword() {
        userEmailId = signUpEmailText.getText().toString();
        if(userEmailId!=null&&!userEmailId.isEmpty()) {
            mAuth.sendPasswordResetEmail(userEmailId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(mContext," Password recovery mail has been \nsent to the entered mail",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(mContext," Email account not registered",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(mContext,"Please enter an email",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fragment_email_sign_up_button :
                if(MyConstants.CALLED_FOR_REGISTER.equals(callingKey))
                {
                    createAccountWithEmail();

                }
                else if(MyConstants.CALLED_FOR_LOGIN.equals(callingKey))
                {
                    signInAccountWithEmail();
                }
                break;
            case R.id.forgot_password :
                changeCurrentPassword();
        }
    }


    private void startMainActivity()
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MyConstants.SIGNIN_METHOD,MyConstants.EMAIL_SIGNIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
    private void startPhoneRegisteration()
    {
        Intent intent = new Intent(getActivity(), RegisterPhoneActivity.class);
        intent.putExtra(MyConstants.SIGNIN_METHOD,MyConstants.EMAIL_SIGNIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();

    }
}
