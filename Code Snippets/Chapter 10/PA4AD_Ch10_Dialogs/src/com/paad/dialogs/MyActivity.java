package com.paad.dialogs;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }
  
  private void eatenByGrue() {}
  
  private void displayNewDialog() {
    /**
     * Listing 10-22: Creating a new dialog using the Dialog class
     */
    // Create the new Dialog.
    Dialog dialog = new Dialog(MyActivity.this);

    // Set the title.
    dialog.setTitle("Dialog Title");

    // Inflate the layout.
    dialog.setContentView(R.layout.dialog_view);

    // Update the Dialog's contents.
    TextView text = (TextView)dialog.findViewById(R.id.dialog_text_view);
    text.setText("This is the text in my dialog");

    // Display the Dialog.
    dialog.show();
  }
  
  private void displayAlertDialog() {
    /**
     * Listing 10-23: Configuring an Alert Dialog
     */
    Context context = MyActivity.this;
    String title = "It is Pitch Black";
    String message = "You are likely to be eaten by a Grue.";
    String button1String = "Go Back";
    String button2String = "Move Forward";

    AlertDialog.Builder ad = new AlertDialog.Builder(context);
    ad.setTitle(title);
    ad.setMessage(message);

    ad.setPositiveButton(
      button1String,
      new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int arg1) {
          eatenByGrue();
        }
      }
    );

    ad.setNegativeButton(
      button2String,
      new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int arg1) {
          // do nothing
        }
      }
    );

    // 
    ad.show();
  }
  
  private void showDialogFragment() {
    String dateString = "April 1 2012";
    
    /**
     * Listing 10-25: Displaying a Dialog Fragment
     */
    String tag = "my_dialog";
    DialogFragment myFragment = 
      MyDialogFragment.newInstance(dateString);

    myFragment.show(getFragmentManager(), tag);
  }
  
  /**
   * Listing 10-27: Using the On Create Dialog event handler
   */
  static final private int TIME_DIALOG = 1;

  @Override
  public Dialog onCreateDialog(int id) {
    switch(id) {
      case (TIME_DIALOG) :
        AlertDialog.Builder timeDialog = new AlertDialog.Builder(this);
        timeDialog.setTitle("The Current Time Is...");
        timeDialog.setMessage("Now");
        return timeDialog.create();
    }
    return null;
  }
  
  /**
   * Listing 10-28: Using the On Prepare Dialog event handler
   */
  @Override
  public void onPrepareDialog(int id, Dialog dialog) {
    switch(id) {
      case (TIME_DIALOG) :
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = 
          new Date(java.lang.System.currentTimeMillis());
        String dateString = sdf.format(currentTime);
        AlertDialog timeDialog = (AlertDialog)dialog;
        timeDialog.setMessage(dateString);

        break;
    }
  }
  
  //
  private void showPreparedDialog() {
    showDialog(TIME_DIALOG);
  }
  
  private void displayToast() {
    /**
     * Listing 10-29: Displaying a Toast
     */
    Context context = this;
    String msg = "To health and happiness!";
    int duration = Toast.LENGTH_SHORT;

    Toast toast = Toast.makeText(context, msg, duration);
    toast.show();
  }
  
  private void displayCustomToast() {
    /**
     * Listing 10-30: Aligning Toast text
     */
    Context context = this;
    String msg = "To the bride and groom!";
    int duration = Toast.LENGTH_SHORT;
    Toast toast = Toast.makeText(context, msg, duration);
    int offsetX = 0;
    int offsetY = 0;

    toast.setGravity(Gravity.BOTTOM, offsetX, offsetY);
    toast.show();
  }

  private void displayCustomViewToast() {
    /**
     * Listing 10-31: Using Views to customize a Toast
     */
    Context context = getApplicationContext();
    String msg = "Cheers!";
    int duration = Toast.LENGTH_LONG;
    Toast toast = Toast.makeText(context, msg, duration);
    toast.setGravity(Gravity.TOP, 0, 0);

    LinearLayout ll = new LinearLayout(context);
    ll.setOrientation(LinearLayout.VERTICAL);

    TextView myTextView = new TextView(context);
    CompassView cv = new CompassView(context);

    myTextView.setText(msg);

    int lHeight = LinearLayout.LayoutParams.FILL_PARENT;
    int lWidth = LinearLayout.LayoutParams.WRAP_CONTENT;

    ll.addView(cv, new LinearLayout.LayoutParams(lHeight, lWidth));
    ll.addView(myTextView, new LinearLayout.LayoutParams(lHeight, lWidth));

    ll.setPadding(40, 50, 0, 50);

    toast.setView(ll);
    toast.show();
  }
  
  /**
   * Listing 10-32: Opening a Toast on the GUI thread
   */
  Handler handler = new Handler();

  private void mainProcessing() {
    Thread thread = new Thread(null, doBackgroundThreadProcessing,
                               "Background");
    thread.start();
  }

  private Runnable doBackgroundThreadProcessing = new Runnable() {
    public void run() {
      backgroundThreadProcessing();
    }
  };

  private void backgroundThreadProcessing() {
    handler.post(doUpdateGUI);
  }

  // Runnable that executes the update GUI method.
  private Runnable doUpdateGUI = new Runnable() {
    public void run() {
      Context context = getApplicationContext();
      String msg = "To open mobile development!";
      int duration = Toast.LENGTH_SHORT;
      Toast.makeText(context, msg, duration).show();
    }
  };

}