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
import java.util.Calendar;
import java.util.Iterator;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NewClaim extends Activity {

	private EditText claimTo;
	private EditText claimFrom;
	private EditText claimName;
	private EditText claimDes;
	// private ListView claimView;
	private ArrayList<ClaimsObject> claimsList;
	// private ArrayAdapter<String> adapter;
	private DatePickerDialog toDatePicker;
	private DatePickerDialog fromDatePicker;
	private Button save;
	private ClaimsObject newClaim;
	private Intent goBackNewClaim;
	private boolean validInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_claim);
		Calendar.getInstance();
		// claimView = (ListView)findViewById(R.id.listView1);
		goBackNewClaim = new Intent(this, Claims.class);
		claimsList = new ArrayList<ClaimsObject>();
		claimTo = (EditText) findViewById(R.id.claimTo);
		claimFrom = (EditText) findViewById(R.id.claimFrom);
		claimName = (EditText) findViewById(R.id.claimName);
		claimDes = (EditText) findViewById(R.id.claimDes);
		save = (Button) findViewById(R.id.save);
		toDatePicker = DateHandler.datePickerInit(this, toDatePicker, claimTo);
		DateHandler.editTextOnClickInit(toDatePicker, claimTo);
		fromDatePicker = DateHandler.datePickerInit(this, fromDatePicker,
				claimFrom);
		DateHandler.editTextOnClickInit(fromDatePicker, claimFrom);
		newClaim = new ClaimsObject();
		buttonListener();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		claimsList = loadFromFile();
	}

	public void buttonListener() {
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validInput = true;
				try {
					newClaim.name = claimName.getText().toString();
					newClaim.claimDes = claimDes.getText().toString();
					if (newClaim.name == "" || newClaim.claimDes == "")
						throw new RuntimeException();
					numFrom(claimFrom, newClaim);
					numTo(claimTo, newClaim);
					newClaim.setFromDayString(newClaim.fromDay);
					newClaim.setFromMonthString(newClaim.fromMonth);
					newClaim.setToDayString(newClaim.toDay);
					newClaim.setToMonthString(newClaim.toMonth);
					if (newClaim.id == -1) {
						newClaim.id = Counters.claimCount;
						Counters.claimCount++;
					}
					if (newClaim.fromYear > newClaim.toYear) {

						Toast.makeText(getApplicationContext(),
								"invalid date selection", Toast.LENGTH_LONG)
								.show();
						validInput = false;
					} else if (newClaim.fromYear == newClaim.toYear) {

						if (newClaim.fromMonth > newClaim.toMonth) {
							Toast.makeText(getApplicationContext(),
									"invalid date selection", Toast.LENGTH_LONG)
									.show();
							validInput = false;
						} else if (newClaim.fromMonth == newClaim.toMonth
								&& newClaim.fromDay > newClaim.toDay) {
							Toast.makeText(getApplicationContext(),
									"invalid date selection", Toast.LENGTH_LONG)
									.show();
							validInput = false;
						}
					}
					if (validInput) {
						Iterator<ClaimsObject> insert = claimsList.iterator();
						ClaimsObject compare;
						int index = -1;
						while (insert.hasNext()){
							index++;
							compare = insert.next();
							if (compare.fromYear<newClaim.fromYear)
								continue;
							if (compare.fromYear>newClaim.fromYear)
								break;
							if (compare.fromMonth<newClaim.fromMonth)
								continue;
							if (compare.fromMonth>newClaim.fromMonth)
								break;
							if (compare.fromDay<newClaim.fromDay)
								continue;
							if (compare.fromDay>newClaim.fromDay)
								break;
						}
						if (!insert.hasNext())
							index++;
						claimsList.add(index, newClaim);
						saveInFile();
						startActivity(goBackNewClaim);
					}
				} catch (Throwable e) {
					Toast.makeText(getApplicationContext(), "missing items",
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	public void numFrom(EditText txt, ClaimsObject obj) {
		String word = txt.getText().toString();
		obj.fromDay = Integer.parseInt(word.substring(0, 2));
		obj.fromMonth = Integer.parseInt(word.substring(3, 5));
		obj.fromYear = Integer.parseInt(word.substring(6));
	}

	public void numTo(EditText txt, ClaimsObject obj) {
		String word = txt.getText().toString();
		obj.toDay = Integer.parseInt(word.substring(0, 2));
		obj.toMonth = Integer.parseInt(word.substring(3, 5));
		obj.toYear = Integer.parseInt(word.substring(6));
	}
	
	// From LonelyTwitterActivity by Abram Hindle
	private ArrayList<ClaimsObject> loadFromFile() {
		Gson gson = new Gson();
		claimsList = new ArrayList<ClaimsObject>();
		try {
			FileInputStream fis = openFileInput(File.getClaimfile());
			// https://sites.google.com/site/gson/gson-user-guide 2015-01-21
			Type dataType = new TypeToken<ArrayList<ClaimsObject>>() {
			}.getType();
			InputStreamReader isr = new InputStreamReader(fis);
			claimsList = gson.fromJson(isr, dataType);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (claimsList == null) {
			claimsList = new ArrayList<ClaimsObject>();
		}
		return claimsList;
	}

	// From LonelyTwitterActivity by Abram Hindle
	private void saveInFile() {
		Gson gson = new Gson();
		try {
			FileOutputStream fos = openFileOutput(File.getClaimfile(), 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(claimsList, osw);
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
		getMenuInflater().inflate(R.menu.new_claim, menu);
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
