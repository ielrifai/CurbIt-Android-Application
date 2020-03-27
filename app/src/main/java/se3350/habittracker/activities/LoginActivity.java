package se3350.habittracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import at.favre.lib.crypto.bcrypt.BCrypt;
import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;

public class LoginActivity extends AppCompatActivity {

    EditText loginPassword;
    Button loginButton;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences settings = getSharedPreferences("PREFS",0);
        password = settings.getString("password", "");


        loginPassword = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pass = loginPassword.getText().toString();
                //static check function for testing*********************************************************
                checkEmptyPass(pass);
                checkNullPass(pass);
                //compare hashed password in saved preferences to password being entered
                BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(), password);
                //Log.v("verify", result.toString());
                //correct password entered
                if(result.verified == true){
                    //enter main app
                    AppDatabase.setPassword(password);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //check that password is entered -- false if not
    public static boolean checkNullPass(String pass){
        if(pass == null){
            return false;
        }
        else{
            return true;
        }
    }

    //check if pass is empty string -- false if is
    public static boolean checkEmptyPass(String pass){
        if(pass.equals("")){
            return false;
        }
        else{
            return true;
        }
    }


}
