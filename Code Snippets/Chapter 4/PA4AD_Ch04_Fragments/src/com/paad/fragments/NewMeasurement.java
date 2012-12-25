package com.paad.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class NewMeasurement extends DialogFragment {

	public interface OnMeasurementSetListener {
		public abstract void onMeasurementSet(Measurement measurement);
	}

	private OnMeasurementSetListener onMeasurementSetListener;
	private Measurement currentMeasurement;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			Measurement measurement = new Measurement();
			measurement.s = "fragment";
			onMeasurementSetListener = (OnMeasurementSetListener) activity;
			setMeasurement(measurement);

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement onMeasurementSetListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Fragment Dialog");
		builder.setIconAttribute(R.drawable.ic_launcher);
		builder.setPositiveButton(R.string.app_name, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.setNegativeButton(R.string.app_name, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		return builder.create();
	}

	private void setMeasurement(Measurement measurement) {
		currentMeasurement = measurement;
		onMeasurementSetListener.onMeasurementSet(measurement);
	}

}
