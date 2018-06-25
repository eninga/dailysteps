package edb.eningabiye.dailysteps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences("user",Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        Intent intent = getIntent();
        if(username != null && !intent.getAction().equals("CHANGE_USER")){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText username = findViewById(R.id.login);
        Button btn = findViewById(R.id.btnlogin);

            btn.setOnClickListener(view -> {
                if(!username.getText().toString().equals("")) {
                    SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", username.getText().toString());
                    editor.apply();
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(this, "Veillez saisir votre nom d'utilisateur", Toast.LENGTH_SHORT).show();
                }
            });


    }
}
