package org.archaeology.surveyphotography;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class DisplayMessageActivity extends Activity {

	private Uri fileUri;
    private String photoSavePath = "";
    public static int PHOTOCODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String suYearTxt = getIntent().getStringExtra(MainActivity.SU_YEAR);
		String suSeqNumTxt = getIntent().getStringExtra(MainActivity.SU_SEQNUM);
		String fieldPhotoNumberTxt = getIntent().getStringExtra(MainActivity.FIELDPHOTONUMBER);
		String fieldOrBag = getIntent().getStringExtra(MainActivity.FIELDORBAG);
        photoSavePath = getIntent().getStringExtra((MainActivity.PHOTOSAVEPATH));


		getIntent().putExtra("result", false);

		
		TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    
    	if (suYearTxt == null |  suYearTxt.length() == 0 )
    	{
    	    textView.setText("wrong year");
    	    setContentView(textView);
    	    return;
    	}
    	
    	if ( Integer.parseInt(suYearTxt) < 2005 | Integer.parseInt(suYearTxt) > 2050)
    	{
    	    textView.setText("wrong year");
    	    setContentView(textView);
    	    return;
    	}
  	
    	if (suSeqNumTxt == null |  suSeqNumTxt.length() == 0 )
    	{
    		textView.setText("wrong SU Number");
    		setContentView(textView);
    	    return;
    	}
 	
    	if (Integer.parseInt(suSeqNumTxt) < 1 | Integer.parseInt(suSeqNumTxt) > 9999)
    	{
    		textView.setText("wrong SU Number");
    		setContentView(textView);
    	    return;
    	}
    	
    	if (fieldPhotoNumberTxt == null |  fieldPhotoNumberTxt.length() == 0 )
    	{	
    	    textView.setText("wrong Field Photo number");
    	    setContentView(textView);
    	    return;
    	}
		
    	if ( Integer.parseInt(fieldPhotoNumberTxt) < 1 | Integer.parseInt(fieldPhotoNumberTxt) > 50)
    	{	
    	    textView.setText("wrong Field Photo number");
    	    setContentView(textView);
    	    return;
    	}
	
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    fileUri = getOutputMediaFileUri(fieldOrBag);  // create a file to save the video
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
	    // start the image capture Intent
	    startActivityForResult(intent, PHOTOCODE);

		
		
		//String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		//Toast.makeText(this, Boolean.toString(new File("/storage/extSdCard").exists()), Toast.LENGTH_LONG).show();
	    // Create the text view
	    //TextView textView = new TextView(this);
	    //textView.setTextSize(40);
	    //textView.setText(message);

	    // Set the text view as the activity layout
	    //setContentView(textView);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      

		if (requestCode == PHOTOCODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Image saved to:\n" + fileUri.getPath(), Toast.LENGTH_LONG).show();
	            
	           // getIntent().putExtra("result", true);
	            this.setResult(RESULT_OK);
	    		
	        	//= (EditText)  findViewById("imageView1");

	        } else if (resultCode == RESULT_CANCELED) {
	        	
	        	this.setResult(RESULT_CANCELED);
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        	this.setResult(RESULT_CANCELED);
	        }
	    }
		
		finish();
	}

	
	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(String fieldOrBag){
	      return Uri.fromFile(getOutputMediaFile(fieldOrBag));
	}

	/** Create a File for saving an image or video */
	private File getOutputMediaFile(String fieldOrBag){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
	
		String suYear = getIntent().getStringExtra(MainActivity.SU_YEAR);
		String suSeqNum = getIntent().getStringExtra(MainActivity.SU_SEQNUM);
		String fieldPhotoNumber = getIntent().getStringExtra(MainActivity.FIELDPHOTONUMBER);
		
		File mediaStorageDir = null;
		File thumbMediaStorageDir = null;
		
		if (fieldOrBag.equals(MainActivity.FIELD))
		{
			mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath().toString() + photoSavePath + suYear + "/" + suSeqNum + "/fld");
			thumbMediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath().toString() + photoSavePath + "tmb/" + suYear + "/" + suSeqNum + "/fld");
		} else if (fieldOrBag.equals(MainActivity.BAG))
		{
			mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath().toString() + photoSavePath + suYear + "/" + suSeqNum + "/fnd");
			thumbMediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath().toString() + photoSavePath + "/tmb/" + suYear + "/" + suSeqNum + "/fnd");
		}
	    
	    //Toast.makeText(this, mediaStorageDir.toString(), Toast.LENGTH_LONG).show();
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }
	    if (! thumbMediaStorageDir.exists()){
	        if (! thumbMediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create thumb directory");
	            return null;
	        }
	    }

	    // Create a media file name
	//    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile = null;

		if (fieldOrBag.equals(MainActivity.FIELD))
		{

			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"1_pic_" + fieldPhotoNumber + ".jpg");
		} else if (fieldOrBag.equals(MainActivity.BAG))
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"1_bag_1.jpg");
		}
	    return mediaFile;
	}
}
