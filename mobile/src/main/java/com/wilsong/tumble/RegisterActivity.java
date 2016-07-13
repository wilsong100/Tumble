package com.wilsong.tumble;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    //variables for the edit text fields on the activity_register.xml.xml
    private EditText etEmail;
    private EditText etName;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private boolean emailOK, nameOK, passwordOK, confirmPasswordOK;
    private String registrationError = "Please fill all fields correctly";

    // variable for the register button on activity_register.xml.xml
    private Button registerButton;
    // url variable to contain the url to the php script for registering a user and
    // entering their data into the database
    private static final String REGISTER_URL = "http://wilsong.esy.es/registration/myRegister.php";
    public boolean success = false;
    private String result ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        //call the validation methods to validate each field
        registerEmailValidation();
        registerNameValidation();
        registerPasswordValidation();
        registerPasswordsMatchValidation();





        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this);

    }

    /**
     * Method takes the user input and checks it against the relevant validation methods
     * from the Validation class
     */
    private void registerEmailValidation(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        // TextWatcher allows validation to be checked straight away
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                emailOK = Validation.isEmailValid(etEmail, true);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
        });
    }
    /**
     * Method takes the user input for name and checks it against the relevant validation methods
     * from the Validation class
     */
    private void registerNameValidation(){
        etName = (EditText) findViewById(R.id.etName);
        // TextWatcher allows validation to be checked straight away
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

                nameOK =Validation.hasText(etName);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
        });
    }
    /**
     * Method takes the user input for name and checks it against the relevant validation methods
     * from the Validation class
     */
    private void registerPasswordValidation(){

        // TextWatcher allows validation to be checked straight away
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                passwordOK = Validation.passwordLengthValid(etPassword);

            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
        });
    }
    /**
     * Method takes the user input for name and checks it against the relevant validation methods
     * from the Validation class
     */
    private void registerPasswordsMatchValidation(){


        // TextWatcher allows validation to be checked straight away
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

                confirmPasswordOK = Validation.passwordsMatch(etPassword, etConfirmPassword);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
        });
    }

    public static boolean allFieldsValid(boolean emailOK, boolean nameOK, boolean passwordOK, boolean confirmPasswordOK ){

        if (!emailOK || !nameOK || !passwordOK || !confirmPasswordOK){

            return false;
        }
        return true;
    }
    @Override
    public void onClick(View view) {
        if (view == registerButton && allFieldsValid(emailOK, nameOK, passwordOK, confirmPasswordOK)) {

            registerUser();
        } else {
            Toast.makeText(RegisterActivity.this, registrationError, Toast.LENGTH_LONG).show();
        }
    }


    private void registerUser() {
        String email = etEmail.getText().toString().trim().toLowerCase();
        String name = etName.getText().toString().trim().toLowerCase();
        String password = etPassword.getText().toString().trim().toLowerCase();

        register(email,name,password);
    }

    private void register(String email, String name, String password) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass registerUserClass = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
                if(string.equals("Error Registering")){
                    Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                } else {
                    handleLoginIntent();
                    //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                }

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("email", params[0]);
                data.put("name", params[1]);
                data.put("password", params[2]);

                result = registerUserClass.sendPostRequest(REGISTER_URL, data);

                return result;
            }
        }
            RegisterUser ru = new RegisterUser();
            ru.execute(email,name,password);
    }

    /**
     *  Move from the RegisterActivity to the LoginActivity
     */
    private void handleLoginIntent() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        //start the intent
        RegisterActivity.this.startActivity(loginIntent);
    }


}
