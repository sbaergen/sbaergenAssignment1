package com.sbaergen.sbaergenassignment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditExpense extends Activity {

	private EditText expNameEdit;
	private EditText expFromEdit;
	private EditText costEdit;
	private EditText expDesEdit;
	private ExpensesObject expEdit;
	private DatePickerDialog dateEdit;
	private Spinner currenciesEdit;
	private Spinner catEdit;
	private Button expSaveEdit;
	private Button expDeleteEdit;
	private ArrayList<ExpensesObject> expList;
	private int id;
	private Intent goBackEdit;
	/*
	 * from http://developer.android.com/guide/topics/ui/controls/spinner.html
	 * accessed 2015-01-27 variable name changed to currenciesEdit from spinner
	 * and adapterSpinner to adapterSpinnerEdit.
	 */
	private ArrayAdapter<CharSequence> adapterSpinnerEdit;
	private ArrayAdapter<CharSequence> adapterSpinnerEditCat;


	// end of cited material

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		expNameEdit = (EditText) findViewById(R.id.expNameEdit);
		expFromEdit = (EditText) findViewById(R.id.expFromEdit);
		costEdit = (EditText) findViewById(R.id.costEdit);
		expDesEdit = (EditText)findViewById(R.id.expDesEdit);
		expSaveEdit = (Button) findViewById(R.id.expSaveEdit);
		expDeleteEdit = (Button) findViewById(R.id.expDeleteEdit);
		dateEdit = DateHandler.datePickerInit(this, dateEdit, expFromEdit);
		DateHandler.editTextOnClickInit(dateEdit, expFromEdit);
		currenciesEdit = (Spinner) findViewById(R.id.currencyEdit);
		catEdit = (Spinner) findViewById(R.id.catEdit);
		goBackEdit = new Intent(this, Expenses.class);

		/*
		 * from
		 * http://developer.android.com/guide/topics/ui/controls/spinner.html
		 * accessed 2015-01-27 variable names changed
		 */
		// Currency Spinner
		adapterSpinnerEdit = ArrayAdapter.createFromResource(this,
				R.array.currencies, android.R.layout.simple_spinner_item);
		adapterSpinnerEdit
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currenciesEdit.setAdapter(adapterSpinnerEdit);
		
		// Category Spinner
		adapterSpinnerEditCat = ArrayAdapter.createFromResource(this,
				R.array.categories, android.R.layout.simple_spinner_item);
		adapterSpinnerEditCat
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		catEdit.setAdapter(adapterSpinnerEditCat);
		// end of cited material

		expSaveEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					expEdit.name = expNameEdit.getText().toString();
					expEdit.description = expDesEdit.getText().toString();
					expEdit.cost = Double.parseDouble(costEdit.getText()
							.toString());
					expEdit.setCostString(expEdit.cost);
					expEdit.currency = currenciesEdit.getSelectedItem()
							.toString();
					expEdit.category = catEdit.getSelectedItem().toString();
					numDate(expFromEdit, expEdit);
					expList.remove(id);
					Iterator<ExpensesObject> insert = expList.iterator();
					ExpensesObject compare;
					int index = -1;
					while (insert.hasNext()){
						index++;
						compare = insert.next();
						if (compare.year<expEdit.year)
							continue;
						if (compare.year>expEdit.year)
							break;
						if (compare.month<expEdit.month)
							continue;
						if (compare.month>expEdit.month)
							break;
						if (compare.day<expEdit.day)
							continue;
						if (compare.day>expEdit.day)
							break;
					}
					if (!insert.hasNext())
						index++;
					expList.add(index, expEdit);
					saveInFile();
					startActivity(goBackEdit);
				} catch (Throwable e) {
					Toast.makeText(getApplicationContext(), "Items missing", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		expDeleteEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				expList.remove(id);
				saveInFile();
				startActivity(goBackEdit);
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Intent edit = getIntent();
		expList = (ArrayList<ExpensesObject>) edit
				.getSerializableExtra("expList");
		id = edit.getIntExtra("expId", -2);
		if (id != -2) {
			expEdit = expList.get(id);
			expNameEdit.setText(expEdit.name);
			expFromEdit.setText(expEdit.dayString + "-" + expEdit.monthString
					+ "-" + expEdit.year);
			costEdit.setText(expEdit.costString);
			expDesEdit.setText(expEdit.description);

		// from
		// http://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position
		// accessed 30-01-2015, variable names changed
		// Currency Spinner
		int spinnerPostion = adapterSpinnerEdit.getPosition(expEdit.currency);
		currenciesEdit.setSelection(spinnerPostion);
		
		// Category Spinner
		int spinnerPostionCat = adapterSpinnerEditCat.getPosition(expEdit.category);
		catEdit.setSelection(spinnerPostionCat);

		// end of cited material
		}
	}

	public void numDate(EditText txt, ExpensesObject obj) {
		String word = txt.getText().toString();
		obj.day = Integer.parseInt(word.substring(0, 2));
		obj.month = Integer.parseInt(word.substring(3, 5));
		obj.year = Integer.parseInt(word.substring(6));
		obj.setDayString(obj.day);
		obj.setMonthString(obj.month);
	}

	private void saveInFile() {
		Gson gson = new Gson();
		try {
			FileOutputStream fos = openFileOutput(File.getExpfile(), 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(expList, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_expense, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
