package com.example.whereami.whereami;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.B_Validate);
        button.setOnClickListener(myHandler);

        Button buttonCamera = (Button) findViewById(R.id.B_Camera);
        buttonCamera.setOnClickListener(myHandlerCamera);
    }

    View.OnClickListener myHandlerCamera = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener myHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            EditText edittext = (EditText) findViewById(R.id.TB_RoomName);
            intent.putExtra("NamePosition", edittext.getText().toString());
            startActivity(intent);
            //finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
