package com.sbaergen.sbaergenassignment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class DateHandler extends NewExpense {
	
	//private DatePickerDialog picker;
	private static SimpleDateFormat dateFormatter;
	static DatePickerDialog pickerHold;

	public static DatePickerDialog datePickerInit(Context context, DatePickerDialog picker, final EditText text) {
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		picker = new DatePickerDialog(context, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				text.setText(dateFormatter.format(newDate.getTime()));
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		return picker;
	}
	
	public static void editTextOnClickInit(final DatePickerDialog picker, EditText text) {
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picker.show();
			}
		});
	}
}
