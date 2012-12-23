package com.paad.actionbar;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class ActionBarTabActivity extends Activity {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    ActionBar actionBar = getActionBar();
    
    /**
     * Listing 10-7: Enabling Action Bar navigation tabs
     */
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    actionBar.setDisplayShowTitleEnabled(false);
    
    /**
     * Listing 10-8: Adding Action Bar navigation tabs
     */
    Tab tabOne = actionBar.newTab();

    tabOne.setText("First Tab")
          .setIcon(R.drawable.ic_launcher)
          .setContentDescription("Tab the First")
          .setTabListener(
             new TabListener<MyFragment>
               (this, R.id.fragmentContainer, MyFragment.class));

    actionBar.addTab(tabOne);

  }

  /**
   * Listing 10-9: Handling Action Bar tab switching
   */
  public static class TabListener<T extends Fragment> 
    implements ActionBar.TabListener {
    
    private MyFragment fragment;
    private Activity activity;
    private Class<T> fragmentClass;
    private int fragmentContainer;

    public TabListener(Activity activity, int fragmentContainer, 
                       Class<T> fragmentClass) {

      this.activity = activity;
      this.fragmentContainer = fragmentContainer;
      this.fragmentClass = fragmentClass;
    }

    // Called when a new tab has been selected
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
      if (fragment == null) {
        String fragmentName = fragmentClass.getName();
        fragment =
          (MyFragment)Fragment.instantiate(activity, fragmentName);
        ft.add(fragmentContainer, fragment, null);
        fragment.setFragmentText(tab.getText());
      } else {
        ft.attach(fragment);
      }
    }

    // Called on the currently selected tab when a different tag is
    // selected. 
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
      if (fragment != null) {
        ft.detach(fragment);
      }
    } 

    // Called when the selected tab is selected.
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
      // TODO React to a selected tab being selected again.
    }
  }
}