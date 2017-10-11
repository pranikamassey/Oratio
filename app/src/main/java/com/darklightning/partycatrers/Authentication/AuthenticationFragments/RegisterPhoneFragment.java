package com.darklightning.partycatrers.Authentication.AuthenticationFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.darklightning.partycatrers.Constants.MyConstants;
import com.darklightning.partycatrers.MainActivity;
import com.darklightning.partycatrers.R;
import com.darklightning.partycatrers.Authentication.LoginUser.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import custom_font.MyEditText;
import custom_font.MyTextView;


public class RegisterPhoneFragment extends Fragment implements View.OnClickListener
{
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener  mAuthListener;
    Context mContext;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private MyEditText phoneNumText,userNameText;
    private MyTextView sendOtpView,resendOtpView;
    private PhoneAuthProvider.ForceResendingToken mToken;
    EditText otp1,otp2,otp3,otp4,otp5,otp6;
    private String mVerificationId;
    private int signInMethod;



    public RegisterPhoneFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_phone_signup,container,false);

        signInMethod = getArguments().getInt(MyConstants.SIGNIN_METHOD);
        mContext = getActivity();

        otp1 = (EditText) v.findViewById(R.id.otp_et_1);
        otp2 = (EditText) v.findViewById(R.id.otp_et_2);
        otp3 = (EditText) v.findViewById(R.id.otp_et_3);
        otp4 = (EditText) v.findViewById(R.id.otp_et_4);
        otp5 = (EditText) v.findViewById(R.id.otp_et_5);
        otp6 = (EditText) v.findViewById(R.id.otp_et_6);
        phoneNumText = (MyEditText) v.findViewById(R.id.register_phone_number);
        userNameText = (MyEditText) v.findViewById(R.id.register_user_name);
        sendOtpView  = (MyTextView) v.findViewById(R.id.sendOTP_register_fragment);
        resendOtpView = (MyTextView) v.findViewById(R.id.resend_otp_mytextview);


        otp1.addTextChangedListener(new RegisterPhoneFragment.GenricTextWatcher(otp1));
        otp2.addTextChangedListener(new RegisterPhoneFragment.GenricTextWatcher(otp2));
        otp3.addTextChangedListener(new RegisterPhoneFragment.GenricTextWatcher(otp3));
        otp4.addTextChangedListener(new RegisterPhoneFragment.GenricTextWatcher(otp4));
        otp5.addTextChangedListener(new RegisterPhoneFragment.GenricTextWatcher(otp5));
        otp6.addTextChangedListener(new RegisterPhoneFragment.GenricTextWatcher(otp6));

        sendOtpView.setOnClickListener(this);
        resendOtpView.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                linkAccountWithPhoneAuthCredential(phoneAuthCredential);
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
        return v;
    }

    //sending otp
    public void startPhoneNumberVerification(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,60, java.util.concurrent.TimeUnit.SECONDS,getActivity(),mCallBacks);
    }
    //sending otp again
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                java.util.concurrent.TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallBacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    //verifying phone number
    private void verifyPhoneNumberWithCode(String verificationId,String code)
    {
        PhoneAuthCredential credential = new PhoneAuthCredential(verificationId,code);
        linkAccountWithPhoneAuthCredential(credential);
    }


    private void linkAccountWithPhoneAuthCredential(final PhoneAuthCredential phoneAuthCredential)
    {
        if(mAuth.getCurrentUser()!=null)
        {
            mAuth.getCurrentUser().linkWithCredential(phoneAuthCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
//                        if(mAuth.getCurrentUser().getPhoneNumber().equals("+91"+phoneNumText.toString()))
                        {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra(MyConstants.SIGNIN_METHOD, signInMethod);
                            startActivity(intent);
                        }

                    }
                    else
                    {
                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            Toast.makeText(mContext,"Invalid Code",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(mContext,"Phone number already registered",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
        else
        {
            Toast.makeText(mContext,"Phone number already registered",Toast.LENGTH_LONG).show();
        }
    }
//    private boolean checkIfAlreadyRegistered()
//    {
//        if(mAuth.getCurrentUser().getEmail()==null)
//        {
//            return true;
//        }
//        Toast.makeText(mContext,"Account already registered.\n\t\t\t\t   Try Signing in", Toast.LENGTH_SHORT).show();
//        mAuth.signOut();
//        return false;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sendOTP_register_fragment :
                startPhoneNumberVerification(phoneNumText.getText().toString());
                break;
            case R.id.resend_otp_mytextview :
                resendVerificationCode(phoneNumText.getText().toString(),mToken);
                break;
        }
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
                    String code = getCode();
                    verifyPhoneNumberWithCode(mVerificationId,code);
                    break;
            }
        }
        public String getCode()
        {
            String mcode = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
            return mcode;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


    }


}
