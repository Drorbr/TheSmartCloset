package com.morandror.scclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                TextView text = findViewById(R.id.)
                Toast.makeText(getApplicationContext(), "Stam", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
