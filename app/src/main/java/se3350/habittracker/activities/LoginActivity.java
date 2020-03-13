package se3350.habittracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

                //correct password entered
                if(pass.equals(password)){
                    //enter main app
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

}
