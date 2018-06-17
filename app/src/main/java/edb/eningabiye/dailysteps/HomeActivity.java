package edb.eningabiye.dailysteps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btn = findViewById(R.id.btnlogin);
        btn.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        });
    }
}
