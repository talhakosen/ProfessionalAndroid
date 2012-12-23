package com.paad.views;

import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

public class SeasonView extends View{

  private Season season;
  
  public SeasonView(Context context) {
    super(context);
  }

  /**
   * Listing 4-19: Broadcasting Accessibility Events
   */
  public void setSeason(Season _season) {
    season = _season;
    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
  } 

  /**
   * Listing 4-20: Customizing Accessibility Event properties
   */
  @Override
  public boolean dispatchPopulateAccessibilityEvent(final 
    AccessibilityEvent event) {
    
    super.dispatchPopulateAccessibilityEvent(event);
    if (isShown()) {
      String seasonStr = Season.valueOf(season);
      if (seasonStr.length() > AccessibilityEvent.MAX_TEXT_LENGTH)
        seasonStr = seasonStr.substring(0, AccessibilityEvent.MAX_TEXT_LENGTH-1);
      
      event.getText().add(seasonStr);
      return true;
    }
    else
      return false;
  }
  
}
