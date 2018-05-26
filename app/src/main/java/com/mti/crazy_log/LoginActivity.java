package com.mti.crazy_log;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mti.facebook_api.R;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static int APP_REQUEST_CODE = 1;
    EditText loginText;
    LoginButton mLoginButton;
    Button accountkitButton;
    CallbackManager mCallbackManager;
    AppEventsLogger logger; //optional
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        logger = AppEventsLogger.newLogger(this);//optional

        loginText = (EditText) findViewById(R.id.login_text);
        accountkitButton = (Button) findViewById(R.id.accountkit_button);
        accountkitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String login = loginText.getText().toString();
                if (login.contains("@")) {
                    logger.logEvent("emailLogin");
                    onAccountKitLogin(login, LoginType.EMAIL);
                } else {
                    logger.logEvent("phoneLogin");
                    onAccountKitLogin(login, LoginType.PHONE);
                }
            }
        });


        mLoginButton = findViewById(R.id.facebook_login_button);
        mLoginButton.setReadPermissions("email");

        //Login Button callback registration
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            //   launchAccountActivity();
                launchMainActivity(); //if you want to open the main Activity
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                //display error

                Toast.makeText(LoginActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //check for an existing access token
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();

        if (accessToken != null || loginToken != null) {
            //if previously logged in, proceed to the acount activity
            //launchAccountActivity();
            launchMainActivity();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Forward result to the callback manager for login Button
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


        //confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null) {
                String ToastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, "" + ToastMessage, Toast.LENGTH_SHORT).show();

            } else if (loginResult.getAccessToken() != null) {

                //on succesful login proceed to the account Activity
               // launchAccountActivity();
                launchMainActivity();
            }
        }
    }



    private void onAccountKitLogin(final String login, final LoginType loginType) {
        // create intent for the Account Kit activity
        final Intent intent = new Intent(this, AccountKitActivity.class);

        // configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        if (loginType == LoginType.EMAIL) {
            configurationBuilder.setInitialEmail(login);
        }
        else {
            PhoneNumber phoneNumber = new PhoneNumber(Locale.getDefault().getCountry(), login, null);
            configurationBuilder.setInitialPhoneNumber(phoneNumber);
        }
        final AccountKitConfiguration configuration = configurationBuilder.build();

        // launch the Account Kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }




    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


   /* private void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }*/
}
