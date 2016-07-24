package com.wilsong.tumble;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Gerard on 08/07/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    private static final String LOGIN_URL = "http://wilsong.esy.es/registration/myLogin.php";

    private EditText etEmail;
    private EditText etPassword;
    private Button loginButton;
    private TextView registerClick;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        registerClick = (TextView) findViewById(R.id.registerClick);
        registerClick.setOnClickListener(this);
        registerEmailValidation();
        registerPasswordValidation();

    }

    private void registerEmailValidation(){
        //etEmail = (EditText) findViewById(R.id.etEmail);
        // TextWatcher allows validation to be checked straight away
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                //emailOK =
                Validation.isEmailValid(etEmail, true);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
        });
    }

    private void registerPasswordValidation(){

        // TextWatcher allows validation to be checked straight away
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
               // passwordOK =
                Validation.passwordLengthValid(etPassword);

            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
        });
    }

    private void login(){
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        userLogin(email, password);
    }

    private void userLogin(String email, String password) {
        class UserLoginAttempt extends AsyncTask<String,Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                loading.dismiss();
                if (string.equalsIgnoreCase("success")) {
                    handleMainIntent();
                } else {
                   Toast.makeText(LoginActivity.this, string, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("email", params[0]);
                data.put("password", params[1]);

                RegisterUserClass registerUserClass = new RegisterUserClass();
                String result = registerUserClass.sendPostRequest(LOGIN_URL, data);

                return result;
            }
        }
            UserLoginAttempt userLoginAttempt = new UserLoginAttempt();
            userLoginAttempt.execute(email, password);

    }

    @Override
    public void onClick(View view){
        if(view == loginButton){
            login();
        }
        if(view == registerClick){
            registerLink();
        }
    }

    private void handleMainIntent() {
        Intent mainScreenIntent = new Intent(LoginActivity.this, HeartRateActivity.class);
       // Intent mainScreenIntent = new Intent(LoginActivity.this, SensorActivity.class);
        //start the intent
        LoginActivity.this.startActivity(mainScreenIntent);

    }

    private void registerLink() {
       Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);

        //start the intent
       LoginActivity.this.startActivity(registerIntent);
    }


}
