/*Copyright 2015 Sean Baergen
*Licensed under the Apache License, Version 2.0 (the "License");
*you may not use this file except in compliance with the License.
*You may obtain a copy of the License at
*http://www.apache.org/licenses/LICENSE-2.0
*Unless required by applicable law or agreed to in writing, software
*distributed under the License is distributed on an "AS IS" BASIS,
*WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*See the License for the specific language governing permissions and
*limitations under the License.
*/
package com.sbaergen.sbaergenassignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NewExpense extends Activity {

	private DatePickerDialog date;
	private Spinner currencies;
	private Spinner category;
	private EditText expFrom;
	private EditText expName;
	private EditText cost;
	private EditText description;
	private ExpensesObject newExpense;
	private ArrayList<ExpensesObject> expList;
	private Intent goBackNew;
	/*
	 * from http://developer.android.com/guide/topics/ui/controls/spinner.html
	 * accessed 2015-01-27 variable name changed to currencies from spinner
	 */
	private ArrayAdapter<CharSequence> adapterSpinner;
	private ArrayAdapter<CharSequence> adapterSpinnerCat;

	// end of cited material
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_expense);
		expName = (EditText) findViewById(R.id.expName);
		cost = (EditText) findViewById(R.id.cost);
		currencies = (Spinner) findViewById(R.id.currency);
		category = (Spinner) findViewById(R.id.expCat);
		expFrom = (EditText) findViewById(R.id.expFrom);
		description = (EditText) findViewById(R.id.description);
		date = DateHandler.datePickerInit(this, date, expFrom);
		DateHandler.editTextOnClickInit(date, expFrom);
		newExpense = new ExpensesObject();
		goBackNew = new Intent(this, Expenses.class);

		/*
		 * from
		 * http://developer.android.com/guide/topics/ui/controls/spinner.html
		 * accessed 2015-01-27 variable name changed to currencies from spinner
		 */
		// Currency Spinner
		adapterSpinner = ArrayAdapter.createFromResource(this,
				R.array.currencies, android.R.layout.simple_spinner_item);
		adapterSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencies.setAdapter(adapterSpinner);
		
		// Category Spinner
		adapterSpinnerCat = ArrayAdapter.createFromResource(this,
				R.array.categories, android.R.layout.simple_spinner_item);
		adapterSpinnerCat
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category.setAdapter(adapterSpinnerCat);
		// end of cited material
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		expList = loadFromFile();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			numDate(expFrom, newExpense);
			newExpense.name = expName.getText().toString();
			if (newExpense.name == "")
				throw new RuntimeException();
			newExpense.cost = Double.parseDouble(cost.getText().toString());
			newExpense.setCostString(newExpense.cost);
			newExpense.currency = currencies.getSelectedItem().toString();
			newExpense.category = category.getSelectedItem().toString();
			newExpense.description = description.getText().toString();
			if (newExpense.description == "")
				throw new RuntimeException();
			if (newExpense.id == -1) {
				newExpense.id = Counters.expCount;
				Counters.expCount++;
				Iterator<ExpensesObject> insert = expList.iterator();
				ExpensesObject compare;
				int index = -1;
				while (insert.hasNext()){
					index++;
					compare = insert.next();
					if (compare.year<newExpense.year)
						continue;
					if (compare.year>newExpense.year)
						break;
					if (compare.month<newExpense.month)
						continue;
					if (compare.month>newExpense.month)
						break;
					if (compare.day<newExpense.day)
						continue;
					if (compare.day>newExpense.day)
						break;
				}
				if (!insert.hasNext())
					index++;
				expList.add(index, newExpense);
			}
			saveInFile();
			startActivity(goBackNew);
		} catch(Throwable e) {
			Toast.makeText(this, "Items missing", Toast.LENGTH_LONG).show();
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

	// From LonelyTwitterActivity by Abram Hindle
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

	// From LonelyTwitterActivity by Abram Hindle
	private ArrayList<ExpensesObject> loadFromFile() {
		Gson gson = new Gson();
		expList = new ArrayList<ExpensesObject>();
		try {
			FileInputStream fis = openFileInput(File.getExpfile());
			// https://sites.google.com/site/gson/gson-user-guide 2015-01-21
			Type dataType = new TypeToken<ArrayList<ExpensesObject>>() {
			}.getType();
			InputStreamReader isr = new InputStreamReader(fis);
			expList = gson.fromJson(isr, dataType);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (expList == null) {
			expList = new ArrayList<ExpensesObject>();

		}
		return expList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_expense, menu);
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
