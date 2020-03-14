package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePasswordActivity extends AppCompatActivity {

    EditText inputPassword, inputPassword2;
    Button savePasswordButton;

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
                String pass = inputPassword.getText().toString();
                String pass2 = inputPassword2.getText().toString();
                //if no pass entered
                if(pass.equals("") || pass2.equals("")){
                    Toast.makeText(CreatePasswordActivity.this, "No password entered", Toast.LENGTH_SHORT).show();
                }
                else {
                    //if passwords match
                    if(pass.equals(pass2)){
                        //save password
                        SharedPreferences settings = getSharedPreferences("PREFS",0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password", pass).apply();

                        //enter main app
                        AppDatabase.setPassword(pass);
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
}
