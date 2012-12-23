package com.paad.linkify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.widget.TextView;

public class MyActivty extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    final TextView myTextView = (TextView)findViewById(R.id.text_view);
    
    /**
     * Listing 5-7: Creating custom link strings in Linkify 
     */
     // Define the base URI.
     String baseUri = "content://com.paad.earthquake/earthquakes/";
    
     // Contruct an Intent to test if there is an Activity capable of 
     // viewing the content you are Linkifying. Use the Package Manager
     // to perform the test.
     PackageManager pm = getPackageManager();
     Intent testIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUri));
     boolean activityExists = testIntent.resolveActivity(pm) != null;
    
     // If there is an Activity capable of viewing the content
     // Linkify the text.
     if (activityExists) {
       int flags = Pattern.CASE_INSENSITIVE;
       Pattern p = Pattern.compile("\\bquake[\\s]?[0-9]+\\b", flags);
       Linkify.addLinks(myTextView, p, baseUri);
     }
 
     // Uncomment to apply the match and transform filters
//     if (activityExists) {
//       int flags = Pattern.CASE_INSENSITIVE;
//       Pattern p = Pattern.compile("\\bquake[\\s]?[0-9]+\\b", flags);
//       Linkify.addLinks(myTextView, p, baseUri,
//           new MyMatchFilter(), new MyTransformFilter());
//     }
  }
  
  /**
   * Listing 5-8: Using a Linkify Match Filter 
   */
  class MyMatchFilter implements MatchFilter {
    public boolean acceptMatch(CharSequence s, int start, int end) {
      return (start == 0 || s.charAt(start-1) != '!');
    }
  }
  
  /**
   * Listing 5-9: Using a Linkify Transform Filter
   * 
   */
  class MyTransformFilter implements TransformFilter {
    public String transformUrl(Matcher match, String url) {
      return url.toLowerCase().replace(" ", "");
    }
  }
}