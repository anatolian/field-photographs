package org.archaeology.surveyphotography;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends Activity {

    public final static String SU_YEAR = "org.archaeology.surveyphotography.SU_YEAR";
    public final static String SU_SEQNUM = "org.archaeology.surveyphotography.SU_SEQNUM";
    public final static String FIELDPHOTONUMBER = "org.archaeology.surveyphotography.FIELDPHOTONUMBER";
    public final static String FIELDORBAG = "org.archaeology.surveyphotography.FIELDORBAG";
    public final static String FIELD = "field";
    public final static String BAG = "bag";
    public final static String THUMBNAIL = "thumbnail";
    public final static String PHOTOSAVEPATH = "photoSavePath";
    public final static int BAGCODE = 101;
    public final static int FIELDCODE = 100;
    public final static String DEFAULTSAVEPATH = "/SUPhotos/";

    public  String getSave_path() {
        return this.save_path;
    }

    public void setSave_path(String save_path) {
       this.save_path = save_path;
        Log.v("Survey App", "set save path: " + this.save_path);
    }

    private String save_path = DEFAULTSAVEPATH;

    public boolean isThumbnailCreation() {
        return isThumbnailCreation;
    }

    public void setThumbnailCreation(boolean isThumbnailCreation) {
        this.isThumbnailCreation = isThumbnailCreation;
    }

    private boolean isThumbnailCreation;

    public ArrayList<String> photoList =  new ArrayList<>();

    public String[] getRelevantYears(int currentYear) {
        int initialYear = currentYear - 3;
        ArrayList<String> years = new ArrayList<>();
        for(int i=0; i < 6; i++) {
            years.add((initialYear + i) + "");
        }

        return years.toArray(new String[years.size()]);
    }

    public int getCurrentYear()
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner sp = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> spa = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, getRelevantYears(getCurrentYear()));
        sp.setAdapter(spa);
        sp.setSelection(3);

        setThumbnailCreation(getIntent().getBooleanExtra("thumbnailState", true));
        if(getIntent().hasExtra(PHOTOSAVEPATH)) {
            Log.v("Survey App", "has extra" + getIntent().getStringExtra(PHOTOSAVEPATH));
            setSave_path(getIntent().getStringExtra(PHOTOSAVEPATH));
        } else {
            setSave_path(DEFAULTSAVEPATH);
        }



        final EditText et;


        et = (EditText) findViewById(R.id.suSeqNum2);


        et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Nothing
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EditText fieldPhotoNumberTxt = (EditText) findViewById(R.id.fieldPhotoNumber2);
                fieldPhotoNumberTxt.setText("1");
                clearImages();
            }
        });


    }



    public void goToSettings(View view) {
        Intent myIntent = new Intent(this, Settings.class);
        myIntent.putExtra(THUMBNAIL, isThumbnailCreation());
        myIntent.putExtra(PHOTOSAVEPATH, getSave_path());
        startActivity(myIntent);
        Log.v("Survey App", "Settings button clicked");
    }

    /** Called when the user clicks the Send button */
    public void takeFieldPhoto(View view) {
        // Do something in response to button
        Log.v("Survey App", "takeFieldPhoto clicked");

        Spinner mySpinner=(Spinner) findViewById(R.id.spinner2);
        String suYearTxt = mySpinner.getSelectedItem().toString();
        EditText suSeqNumTxt = (EditText) findViewById(R.id.suSeqNum2);
        EditText fieldPhotoNumberTxt = (EditText) findViewById(R.id.fieldPhotoNumber2);

        Intent intent = new Intent(this, DisplayMessageActivity.class);


        intent.putExtra(SU_YEAR, suYearTxt);
        intent.putExtra(SU_SEQNUM, suSeqNumTxt.getText().toString());
        intent.putExtra(FIELDPHOTONUMBER, fieldPhotoNumberTxt.getText().toString());
        intent.putExtra(FIELDORBAG, FIELD);
        intent.putExtra(PHOTOSAVEPATH, getSave_path());

        startActivityForResult(intent, FIELDCODE);


        //}
    }


    /** Called when the user clicks the Send button */
    public void takeBagPhoto(View view) {

        Log.v("Survey App", "takeBagPhoto clicked");

        // Do something in response to button
        Spinner mySpinner=(Spinner) findViewById(R.id.spinner2);
        String suYearTxt = mySpinner.getSelectedItem().toString();
        EditText suSeqNumTxt = (EditText) findViewById(R.id.suSeqNum2);
        EditText fieldPhotoNumberTxt = (EditText) findViewById(R.id.fieldPhotoNumber2);

        Intent intent = new Intent(this, DisplayMessageActivity.class);

        intent.putExtra(SU_YEAR, suYearTxt);
        intent.putExtra(SU_SEQNUM, suSeqNumTxt.getText().toString());
        intent.putExtra(FIELDPHOTONUMBER, fieldPhotoNumberTxt.getText().toString());
        intent.putExtra(FIELDORBAG, BAG);
        intent.putExtra(PHOTOSAVEPATH, getSave_path());

        startActivityForResult(intent, BAGCODE);


        //}
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //	boolean result = data.getBooleanExtra("result", false);
        Spinner mySpinner=(Spinner) findViewById(R.id.spinner2);
        String suYearTxt = mySpinner.getSelectedItem().toString();
        EditText suSeqNumTxt = (EditText) findViewById(R.id.suSeqNum2);
        EditText fieldPhotoNumberTxt = (EditText) findViewById(R.id.fieldPhotoNumber2);



        if (resultCode == RESULT_OK)
        {
            if (requestCode == FIELDCODE)
            {
                Log.v("ActivityResulty", getSave_path());
                String path = Environment.getExternalStorageDirectory().getPath().toString() + getSave_path() + suYearTxt + "/" +  suSeqNumTxt.getText().toString() + "/fld/" + "1_pic_" + fieldPhotoNumberTxt.getText().toString() + ".jpg";
                String thumbPath = Environment.getExternalStorageDirectory().getPath().toString() + getSave_path() + "tmb/" + suYearTxt + "/" +  suSeqNumTxt.getText().toString() + "/fld";
                File thumbFile = new File(thumbPath, "1_pic_" + fieldPhotoNumberTxt.getText().toString() + ".jpg");

                if(isThumbnailCreation()) {
                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 300, 225);

                    try {
                        FileOutputStream fos = new FileOutputStream(thumbFile);
                        ThumbImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.close();
                    } catch (Exception ex) {
                        Log.d("Error", "Error Message: " + ex.getMessage()); //e.getMessage is returning a null value
                        ex.printStackTrace();
                    }

                }
                String photoNum = fieldPhotoNumberTxt.getText().toString();
                int fieldPhotoNumber = Integer.parseInt(photoNum);


                ImageView imageView = (ImageView) findViewById(R.id.picture1);

                if (fieldPhotoNumber == 1)
                {
                    ++fieldPhotoNumber;
                    fieldPhotoNumberTxt.setText(String.valueOf(fieldPhotoNumber));
                } else if (fieldPhotoNumberTxt.getText().toString().equals("2"))
                {
                    imageView = (ImageView) findViewById(R.id.picture2);
                    fieldPhotoNumberTxt.setText("3");
                }else if (fieldPhotoNumberTxt.getText().toString().equals("3"))
                {
                    imageView = (ImageView) findViewById(R.id.picture3);
                    fieldPhotoNumberTxt.setText("4");
                }else if (fieldPhotoNumberTxt.getText().toString().equals("4"))
                {
                    imageView = (ImageView) findViewById(R.id.picture4);
                    fieldPhotoNumberTxt.setText("5");
                }else if (fieldPhotoNumberTxt.getText().toString().equals("5"))
                {
                    imageView = (ImageView) findViewById(R.id.picture5);
                    fieldPhotoNumberTxt.setText("6");
                }


                if (fieldPhotoNumber > 5)
                {
                    ++fieldPhotoNumber;
                    fieldPhotoNumberTxt.setText(String.valueOf(fieldPhotoNumber));
                }
                else
                {
                    if(isThumbnailCreation()) {
                        imageView.setImageURI(Uri.fromFile(thumbFile));
                    }
                }
            } else if (requestCode == BAGCODE) {
                Log.v("ActivityResulty", getSave_path());
                String path = Environment.getExternalStorageDirectory().getPath().toString() + getSave_path() + suYearTxt + "/" +  suSeqNumTxt.getText().toString() + "/fnd/" + "1_bag_1.jpg";
                String thumbPath = Environment.getExternalStorageDirectory().getPath().toString() + getSave_path() + "tmb/" + suYearTxt + "/" +  suSeqNumTxt.getText().toString() + "/fnd";
                File thumbFile = new File(thumbPath, "1_bag_1.jpg");


                if(isThumbnailCreation()) {
                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 300, 225);
                    try {
                        FileOutputStream fos = new FileOutputStream(thumbFile);
                        ThumbImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                        fos.close();
                    } catch (Exception ex) {
                        Log.d("Error", "Error Message: " + ex.getMessage()); //e.getMessage is returning a null value
                        ex.printStackTrace();
                    }
                }

                ImageView imageView = (ImageView) findViewById(R.id.pictureBag);
                if(isThumbnailCreation()) {
                    imageView.setImageURI(Uri.fromFile(thumbFile));
                }

            }

        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                goToSettings(findViewById(R.id.action_settings));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearImages()
    {
        ImageView imageView = (ImageView) findViewById(R.id.picture1);
        imageView.setImageDrawable(null);
        imageView = (ImageView) findViewById(R.id.picture2);
        imageView.setImageDrawable(null);
        imageView = (ImageView) findViewById(R.id.picture3);
        imageView.setImageDrawable(null);
        imageView = (ImageView) findViewById(R.id.picture4);
        imageView.setImageDrawable(null);
        imageView = (ImageView) findViewById(R.id.picture5);
        imageView.setImageDrawable(null);
        imageView = (ImageView) findViewById(R.id.pictureBag);
        imageView.setImageDrawable(null);
    }


}
