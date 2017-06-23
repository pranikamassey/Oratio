package com.darklightning.partycatrers.StartHere;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.darklightning.partycatrers.MainActivity;
import com.darklightning.partycatrers.R;
import com.darklightning.partycatrers.StartHere.RegisterUserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText emailIdText,passwordText;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button loginButton,forgotPasswordButton,registerInsteadButton;
    Context mContext;
    String emailIdOfUser,passwordOfUser;
    boolean flag=true;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mAuth = FirebaseAuth.getInstance();


        emailIdText = (EditText) findViewById(R.id.emai_id_login);
        passwordText = (EditText) findViewById(R.id.user_password_login);

        loginButton = (Button) findViewById(R.id.login_please_button);
        forgotPasswordButton = (Button) findViewById(R.id.forgot_password_login);
        registerInsteadButton = (Button) findViewById(R.id.register_instead_button);

        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        registerInsteadButton.setOnClickListener(this);


    }




    private void signInWithEmailId()
    {
        emailIdOfUser = emailIdText.getText().toString();
        passwordOfUser = passwordText.getText().toString();
        mAuth.signInWithEmailAndPassword(emailIdOfUser, passwordOfUser)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                                Intent intent = new Intent(mContext,MainActivity.class);
                                startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void sendPasswordChangeMail()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        emailIdOfUser = emailIdText.getText().toString();
        auth.sendPasswordResetEmail(emailIdOfUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "mail sent to ur emailid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_please_button :
                signInWithEmailId();
                break;
            case R.id.forgot_password_login :
                sendPasswordChangeMail();
                break;
            case R.id.register_instead_button :
                Intent intent1 = new Intent(mContext,RegisterUserActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
