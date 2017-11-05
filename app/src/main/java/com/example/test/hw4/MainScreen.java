package com.example.test.hw4;

import android.content.Intent;
import android.media.audiofx.BassBoost;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;

public class MainScreen extends AppCompatActivity {

    public static final int SELECTION = 1;

    public String distVar = "Kilometers";
    public String bearVar = "Degrees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Text fields
        EditText latP1 = (EditText) findViewById(R.id.LatP1);
        EditText latP2 = (EditText) findViewById(R.id.LatP2);
        EditText lonP1 = (EditText) findViewById(R.id.LonP1);
        EditText lonP2 = (EditText) findViewById(R.id.LonP2);

        // Buttons
        Button calcButton = (Button) findViewById(R.id.CalculateButton);
        Button clearButton = (Button) findViewById(R.id.ClearButton);

        // Labels
        TextView dist = (TextView) findViewById(R.id.DistanceLabel);
        TextView bear = (TextView) findViewById(R.id.BearingLabel);

        calcButton.setOnClickListener(v -> {
            if(latP1.length() != 0 && latP2.length() != 0
                    && lonP1.length() != 0 && lonP2.length() != 0) {

                double radius = 6371000.0;      // Earth's radius (m)
                double p1lt = Double.parseDouble(latP1.getText().toString());
                double p2lt = Double.parseDouble(latP2.getText().toString());
                double p1ln = Double.parseDouble(lonP1.getText().toString());
                double p2ln = Double.parseDouble(lonP2.getText().toString());
                float[] arr = new float[2];

                Location.distanceBetween(p1lt, p1ln, p2lt, p2ln, arr);

                float distConvert = 1.0f;
                float bearConvert = 1.0f;

                // Distance coefficient
                if(distVar.equals("Miles"))
                    distConvert = 0.621371f;
                else
                    distConvert = 1.0f;

                // Bearing coefficient
                if(bearVar.equals("Mils"))
                    bearConvert = 17.777778f;
                else
                    bearConvert = 1.0f;

                dist.setText("Distance: " + String.format("%.2f", arr[0] / 1000.0 * distConvert)
                        + " " + distVar, TextView.BufferType.EDITABLE);
                bear.setText("Bearing: " + String.format("%.2f", arr[1] * bearConvert) + " "
                        + bearVar, TextView.BufferType.EDITABLE);
            }
        });

        clearButton.setOnClickListener(v -> {
            latP1.setText("", TextView.BufferType.EDITABLE);
            latP2.setText("", TextView.BufferType.EDITABLE);
            lonP1.setText("", TextView.BufferType.EDITABLE);
            lonP2.setText("", TextView.BufferType.EDITABLE);
            dist.setText("Distance: ", TextView.BufferType.EDITABLE);
            bear.setText("Bearing: ", TextView.BufferType.EDITABLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == SELECTION){
            distVar = data.getStringExtra("distChoice");
            bearVar = data.getStringExtra("bearChoice");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.dropSettings) {
            Intent intent = new Intent(MainScreen.this, SettingsScreen.class);
            startActivityForResult(intent, SELECTION);
        }
        return super.onOptionsItemSelected(item);
    }
}
