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

                //if no pass entered
                if(isPasswordEmpty(pass) || isPasswordEmpty(pass2)){
                    Toast.makeText(CreatePasswordActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }

                else {
                    //if passwords match
                    if(passwordsMatch(pass, pass2)){
                        //hash password using BCrypt
                        bcryptHashStringPass = BCrypt.withDefaults().hashToString(12, pass.toCharArray());

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

    //check if passwords match
    public static boolean passwordsMatch(String pass1, String pass2){
        return pass1.equals(pass2);
    }

    //check if password entered
    public static boolean isPasswordEmpty(String pass){
        return pass.trim().isEmpty();
    }


}
