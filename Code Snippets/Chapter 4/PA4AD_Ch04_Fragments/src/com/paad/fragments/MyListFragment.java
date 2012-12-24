package com.paad.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyListFragment extends Fragment {

	public MyListFragment() {
	}

	// Called once the Fragment has been created in order for it to
	// create its user interface.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("MyListFragment.onCreateView()");

		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("MyListFragment.onCreate()");
	}

	// Called when the Fragment is attached to its parent Activity.
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		System.out.println("MyListFragment.onAttach()");
	}

	// Called once the parent Activity and the Fragment's UI have
	// been created.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Complete the Fragment initialization Ð particularly anything
		// that requires the parent Activity to be initialized or the
		// Fragment's view to be fully inflated.
		System.out.println("MyListFragment.onActivityCreated()");
	}

	// Called at the start of the visible lifetime.
	@Override
	public void onStart() {
		super.onStart();
		// Apply any required UI change now that the Fragment is visible.
		System.out.println("MyListFragment.onStart()");
	}

	// Called at the start of the active lifetime.
	@Override
	public void onResume() {
		super.onResume();
		// Resume any paused UI updates, threads, or processes required
		// by the Fragment but suspended when it became inactive.
		System.out.println("MyListFragment.onResume()");
	}

	// Called at the end of the active lifetime.
	@Override
	public void onPause() {
		// Suspend UI updates, threads, or CPU intensive processes
		// that don't need to be updated when the Activity isn't
		// the active foreground activity.
		// Persist all edits or state changes
		// as after this call the process is likely to be killed.
		super.onPause();
		System.out.println("MyListFragment.onPause()");
	}

	// Called to save UI state changes at the
	// end of the active lifecycle.
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate, onCreateView, and
		// onCreateView if the parent Activity is killed and restarted.
		super.onSaveInstanceState(savedInstanceState);
		System.out.println("MyListFragment.onSaveInstanceState()");
	}

	// Called at the end of the visible lifetime.
	@Override
	public void onStop() {
		// Suspend remaining UI updates, threads, or processing
		// that aren't required when the Fragment isn't visible.
		super.onStop();
		System.out.println("MyListFragment.onStop()");
	}

	// Called when the Fragment's View has been detached.
	@Override
	public void onDestroyView() {
		// Clean up resources related to the View.
		super.onDestroyView();
		System.out.println("MyListFragment.onDestroyView()");
	}

	// Called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		// Clean up any resources including ending threads,
		// closing database connections etc.
		super.onDestroy();
		System.out.println("MyListFragment.onDestroy()");
	}

	// Called when the Fragment has been detached from its parent Activity.
	@Override
	public void onDetach() {
		super.onDetach();
		System.out.println("MyListFragment.onDetach()");
	}

}