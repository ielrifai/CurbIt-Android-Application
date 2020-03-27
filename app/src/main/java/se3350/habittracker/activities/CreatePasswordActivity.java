package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import at.favre.lib.crypto.bcrypt.*;

public class CreatePasswordActivity extends AppCompatActivity {

    EditText inputPassword, inputPassword2;
    Button savePasswordButton;
    String bcryptHashStringPass;
    String pass, pass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputPassword2 = (EditText) findViewById(R.id.inputPassword2);
        savePasswordButton = (Button) findViewById(R.id.savePassword);


        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = inputPassword.getText().toString();
                pass2 = inputPassword2.getText().toString();
                //hash password using BCrypt
                bcryptHashStringPass = BCrypt.withDefaults().hashToString(12, pass.toCharArray());
                //Log.v("hash pass",bcryptHashStringPass);
                // compare pass2 to pass1 hash
                //static check function for testing*********************************************************
                checkEncryption(bcryptHashStringPass, pass);
                BCrypt.Result result = BCrypt.verifyer().verify(pass2.toCharArray(), bcryptHashStringPass);
                //if no pass entered
                //static check function for testing*********************************************************
                checkEmptyPass(pass,pass2);
                //static check function for testing*********************************************************
                checkNullPass(pass,pass2);
                if(pass.equals("") || pass2.equals("")){
                    Toast.makeText(CreatePasswordActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
                else {
                    //if passwords match
                    if(result.verified == true){
                        //save password
                        SharedPreferences settings = getSharedPreferences("PREFS",0);
                        SharedPreferences.Editor editor = settings.edit();
                        //saved hashed pass to preferences
                        editor.putString("password", bcryptHashStringPass).apply();

                        //enter main app
                        AppDatabase.setPassword(bcryptHashStringPass);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        //inputs dont match
                        Toast.makeText(CreatePasswordActivity.this, "Passwords dont match", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });


    }
    //check that password is encrypted -- false if not
    public static boolean checkEncryption(String encryptedPass, String pass){

        if(encryptedPass.equals(pass)){
            return true;
        }
        else{
            return false;
        }
    }
    //check that password is entered -- false if not
    public static boolean checkNullPass(String pass, String pass2){
        if(pass == null || pass2 == null){
            return false;
        }
        else{
            return true;
        }
    }
    //check if pass is empty string -- false if is
    public static boolean checkEmptyPass(String pass, String pass2){
        if(pass.equals("") || pass2.equals("")){
            return false;
        }
        else{
            return true;
        }
    }

}
