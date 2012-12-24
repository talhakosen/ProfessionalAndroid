package com.paad.weatherstation;

import com.paad.fragments.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyListFragment extends Fragment {

	Activity activity;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
	}

	public MyListFragment() {
		System.out.println("MyListFragment.MyListFragment()");
	}

	// Called once the Fragment has been created in order for it to
	// create its user interface.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		new Activity().runOnUiThread(new Runnable() {			
			public void run() {
				System.out.println("MyListFragment.onCreateView(...).new Runnable() {...}.run()");
			}
		});
		
		
		
		System.out.println("MyListFragment.onCreateView()");
		
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
}