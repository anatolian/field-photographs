package org.archaeology.surveyphotography;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText savePathInput = (EditText) findViewById(R.id.savePathInput);
        savePathInput.setText(getIntent().getStringExtra(MainActivity.PHOTOSAVEPATH));

        CheckBox thumbnailCheckBox = (CheckBox) findViewById(R.id.checkBox);


        thumbnailCheckBox.setChecked(getIntent().getBooleanExtra(MainActivity.THUMBNAIL, true));

        thumbnailCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    Log.v("Survey App", "Thumbnail setting checked");

                } else {
                    Log.v("Survey App", "Thumbnail setting unchecked");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    public void saveAndReturn(View view) {
        Intent tempIntent = new Intent(this, MainActivity.class);
        CheckBox thumbnailCheckBox = (CheckBox) findViewById(R.id.checkBox);
        EditText savePathInput = (EditText) findViewById(R.id.savePathInput);
        tempIntent.putExtra("thumbnailState", thumbnailCheckBox.isChecked());
        tempIntent.putExtra("photoSavePath", savePathInput.getText().toString().trim());
        startActivity(tempIntent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
