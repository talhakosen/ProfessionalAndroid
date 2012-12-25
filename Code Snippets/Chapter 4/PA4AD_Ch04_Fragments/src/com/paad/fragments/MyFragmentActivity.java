package com.paad.fragments;

import com.paad.fragments.R.id;
import com.paad.fragments.SeasonFragment.OnSeasonSelectedListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

public class MyFragmentActivity extends Activity implements
		OnSeasonSelectedListener {
	TextView txt;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate the layout containing the Fragment containers
		setContentView(R.layout.fragment_container_layout);
		txt = (TextView) findViewById(id.textView1);
		
		FragmentManager fm = getFragmentManager();

		// Check to see if the Fragment back stack has been populated
		// If not, create and populate the layout.
		DetailsFragment detailsFragment = (DetailsFragment) fm.findFragmentById(R.id.details_container);

		System.out.println("MyFragmentActivity.onCreate()");

		if (detailsFragment == null) {
			FragmentTransaction ft = fm.beginTransaction();			
			ft.add(new SeasonFragment(),"sF");
			ft.replace(R.id.details_container, new DetailsFragment());
			ft.replace(R.id.ui_container, new MyListFragment());
			ft.commit();
		}
	}

	public void onSeasonSelected(Season season) {		
		txt.setText(season.s);
	}
}
