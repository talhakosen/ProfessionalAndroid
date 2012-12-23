package com.paad.intentcamera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CameraActivity extends Activity {
  
  private static final int TAKE_PICTURE = 0;
  
  private Uri outputFileUri;  
  private ImageView imageView;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    imageView = (ImageView)findViewById(R.id.imageView1);
    
    Button fullPhotoButton = (Button)findViewById(R.id.buttonFullPicture);
    fullPhotoButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        /**
         * Listing 15-23: Requesting a full-size picture using an Intent
         */
        // Create an output file.
        File file = new File(Environment.getExternalStorageDirectory(),
                             "test.jpg");
        Uri outputFileUri = Uri.fromFile(file);
   
        // Generate the Intent.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
   
        // Launch the camera app.
        startActivityForResult(intent, TAKE_PICTURE);
      }
    });
    
    Button photoButton = (Button)findViewById(R.id.buttonPicture);
    photoButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startActivityForResult(
            new Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PICTURE);
      }
    }); 
  }
    
  /**
   * Listing 15-24: Receiving pictures from an Intent
   */
  @Override
  protected void onActivityResult(int requestCode,
                                  int resultCode, Intent data) {
    if (requestCode == TAKE_PICTURE) {
      // Check if the result includes a thumbnail Bitmap
      if (data != null) {
        if (data.hasExtra("data")) {
          Bitmap thumbnail = data.getParcelableExtra("data");
          imageView.setImageBitmap(thumbnail);
        }
      } else {
        // If there is no thumbnail image data, the image
        // will have been stored in the target output URI.

        // Resize the full image to fit in out image view.
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        
        BitmapFactory.Options factoryOptions = new 
          BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(outputFileUri.getPath(), 
                                factoryOptions);
          
        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;
        
        // Determine how much to scale down the image
        int scaleFactor = Math.min(imageWidth/width, 
                                   imageHeight/height);
        
        // Decode the image file into a Bitmap sized to fill the View
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;
        
        Bitmap bitmap = 
          BitmapFactory.decodeFile(outputFileUri.getPath(),
                                   factoryOptions);
          
        imageView.setImageBitmap(bitmap); 
      }
    }
  }

}