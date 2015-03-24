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
import android.widget.RelativeLayout;
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
    public final static int PHOTOCOUNTERDEFAULT = 300;
    public final static int BAGPHOTOID = 299;


    private String save_path = DEFAULTSAVEPATH;
    DrawView bagPhoto = null;
    public ArrayList<ImageView> photoList =  new ArrayList<>();
    RelativeLayout mainLayout;
    private int photoIdCounter = PHOTOCOUNTERDEFAULT;
    private boolean isThumbnailCreation;



    public boolean isThumbnailCreation() {
        return isThumbnailCreation;
    }

    public void setThumbnailCreation(boolean isThumbnailCreation) {
        this.isThumbnailCreation = isThumbnailCreation;
    }
    public  String getSave_path() {
        return this.save_path;
    }

    public void setSave_path(String save_path) {
        this.save_path = save_path;
        Log.v("Survey App", "set save path: " + this.save_path);
    }

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
                photoList = clearPhotos(mainLayout, photoList);
            }
        });

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

    }


    public void addPhotoToList(ImageView aPhoto, ArrayList<ImageView> aPhotoList) {
        aPhoto.setId(photoIdCounter++);
        aPhotoList.add(aPhoto);
    }

    public View addPhotoToEnd(RelativeLayout aLayout, ImageView anImage, View previousImage) {
        RelativeLayout.LayoutParams imageDetails = new RelativeLayout.LayoutParams(
          340, 255
        );
        imageDetails.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageDetails.addRule(RelativeLayout.BELOW, previousImage.getId());
        imageDetails.setMargins(0,20,0,0);

        aLayout.addView(anImage, imageDetails);
        return anImage;
    }

    public void arrangePhotoListInLayout(RelativeLayout aLayout, ArrayList<ImageView> images, View topAttachedView) {
        images.trimToSize();
        Log.v("Survey App", "number of photos : " + images.size());

        for(ImageView aPhoto : images) {
            aLayout.removeView(aPhoto);
        }
        if(bagPhoto != null) {
            aLayout.removeView(bagPhoto);
            addPhotoToEnd(aLayout, bagPhoto, topAttachedView);
            topAttachedView = bagPhoto;
        }

        for(int i = 0; i < images.size(); i++) {
            if(i == 0) {
                addPhotoToEnd(aLayout, images.get(0), topAttachedView);
            } else {
                addPhotoToEnd(aLayout, images.get(i), images.get(i - 1));
            }
        }
    }

    public void clearBagPhoto(RelativeLayout aLayout) {
        if(bagPhoto != null) {
            aLayout.removeView(bagPhoto);
            bagPhoto = null;
        }
    }

    public ArrayList<ImageView> clearPhotos(RelativeLayout aLayout, ArrayList<ImageView> photos) {
        for(ImageView aPhoto : photos) {
            aLayout.removeView(aPhoto);
            aPhoto = null;
        }

        clearBagPhoto(aLayout);

        photoIdCounter = PHOTOCOUNTERDEFAULT;
        return new ArrayList<>();
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

        Intent intent = new Intent(this, TakePhotographActivity.class);


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

        Intent intent = new Intent(this, TakePhotographActivity.class);

        intent.putExtra(SU_YEAR, suYearTxt);
        intent.putExtra(SU_SEQNUM, suSeqNumTxt.getText().toString());
        intent.putExtra(FIELDPHOTONUMBER, fieldPhotoNumberTxt.getText().toString());
        intent.putExtra(FIELDORBAG, BAG);
        intent.putExtra(PHOTOSAVEPATH, getSave_path());

        startActivityForResult(intent, BAGCODE);


        //}
    }

    private void createOrReplaceBagPhoto(Uri anUri) {
        clearBagPhoto(mainLayout);
        bagPhoto = new DrawView(this);
        bagPhoto.setImageURI(anUri);
        bagPhoto.setId(BAGPHOTOID);
    }

    private int increaseFieldPhotoNumberAndReturnOldNumber(EditText anEditText) {
        int tempNumber =  Integer.parseInt(anEditText.getText().toString().trim());
        anEditText.setText((tempNumber+1) + "");
        return tempNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Spinner mySpinner=(Spinner) findViewById(R.id.spinner2);
        String suYearTxt = mySpinner.getSelectedItem().toString();
        EditText suSeqNumTxt = (EditText) findViewById(R.id.suSeqNum2);
        EditText fieldPhotoNumberTxt = (EditText) findViewById(R.id.fieldPhotoNumber2);
        Log.v("ActivityResult" , "asdfsda");
        if (resultCode == RESULT_OK) {
            Log.v("ActivityResult" , "RESULT_OK " + resultCode + "edfasdf");

            if(requestCode == FIELDCODE) { // field photo
                Log.v("ActivityResulty", getSave_path());
                int fieldPhotoNumber = increaseFieldPhotoNumberAndReturnOldNumber(fieldPhotoNumberTxt);
                String path = Environment.getExternalStorageDirectory().getPath().toString() + getSave_path() + suYearTxt + "/" +  suSeqNumTxt.getText().toString() + "/fld/" + "1_pic_" + fieldPhotoNumber + ".jpg";
                String thumbPath = Environment.getExternalStorageDirectory().getPath().toString() + getSave_path() + "tmb/" + suYearTxt + "/" +  suSeqNumTxt.getText().toString() + "/fld";
                File thumbFile = new File(thumbPath, "1_pic_" + fieldPhotoNumber + ".jpg");

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
                ImageView aPhoto = new ImageView(this);
                if(isThumbnailCreation()) {
                    aPhoto.setImageURI(Uri.fromFile(thumbFile));
                } else {
                    aPhoto.setImageURI(Uri.parse(path));
                }



                addPhotoToList(aPhoto, photoList);
                arrangePhotoListInLayout(mainLayout, photoList, findViewById(R.id.space3));

            } else if(requestCode == BAGCODE) { // bag photo
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

                if(isThumbnailCreation()) {
                    createOrReplaceBagPhoto(Uri.fromFile(thumbFile));
                } else {
                    createOrReplaceBagPhoto(Uri.parse(path));
                }
                arrangePhotoListInLayout(mainLayout, photoList, findViewById(R.id.space3));
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




}
