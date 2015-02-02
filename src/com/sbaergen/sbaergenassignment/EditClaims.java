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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EditClaims extends Activity {

	private EditText claimNameEdit;
	private EditText claimFromEdit;
	private EditText claimToEdit;
	private EditText claimDesEdit;
	private Button saveEdit;
	private Button submit;
	private ClaimsObject claimEdit;
	private ArrayList<ClaimsObject> claimEditList;
	private ArrayList<ExpensesObject> sumExp;
	private ArrayList<ExpensesObject> expList;
	private DatePickerDialog toDatePickerEdit;
	private DatePickerDialog fromDatePickerEdit;
	private boolean validInput;
	private int claimId;
	private Intent goBackEditClaim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claims);
		sumExp = new ArrayList<ExpensesObject>();
		claimNameEdit = (EditText) findViewById(R.id.claimNameEdit);
		claimFromEdit = (EditText) findViewById(R.id.claimFromEdit);
		claimToEdit = (EditText) findViewById(R.id.claimToEdit);
		claimDesEdit = (EditText) findViewById(R.id.claimDesEdit);
		goBackEditClaim = new Intent(this, Claims.class);
		saveEdit = (Button) findViewById(R.id.saveEdit);
		submit = (Button) findViewById(R.id.submit);
		toDatePickerEdit = DateHandler.datePickerInit(this, toDatePickerEdit,
				claimToEdit);
		DateHandler.editTextOnClickInit(toDatePickerEdit, claimToEdit);
		fromDatePickerEdit = DateHandler.datePickerInit(this,
				fromDatePickerEdit, claimFromEdit);
		DateHandler.editTextOnClickInit(fromDatePickerEdit, claimFromEdit);
		saveEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validInput = true;
				try {
					claimEdit.name = claimNameEdit.getText().toString();
					claimEdit.claimDes = claimDesEdit.getText().toString();
					if (claimEdit.name.equals("")
							|| claimEdit.claimDes.equals(""))
						numFrom(claimFromEdit, claimEdit);
					numTo(claimToEdit, claimEdit);
					claimEdit.setFromDayString(claimEdit.fromDay);
					claimEdit.setFromMonthString(claimEdit.fromMonth);
					claimEdit.setToDayString(claimEdit.toDay);
					claimEdit.setToMonthString(claimEdit.toMonth);
					if (claimEdit.id == -1) {
						claimEdit.id = Counters.claimCount;
						Counters.claimCount++;
					}
					if (claimEdit.fromYear > claimEdit.toYear) {
						Toast.makeText(getApplicationContext(),
								"invalid date selection", Toast.LENGTH_LONG)
								.show();
						validInput = false;
					} else if (claimEdit.fromYear == claimEdit.toYear) {
						if (claimEdit.fromMonth > claimEdit.toMonth) {
							Toast.makeText(getApplicationContext(),
									"invalid date selection", Toast.LENGTH_LONG)
									.show();
							validInput = false;
						} else if (claimEdit.fromMonth == claimEdit.toMonth
								&& claimEdit.fromDay > claimEdit.toDay) {
							Toast.makeText(getApplicationContext(),
									"invalid date selection", Toast.LENGTH_LONG)
									.show();
							validInput = false;
						}
					}
					if (validInput) {
						Iterator<ClaimsObject> insert = claimEditList.iterator();
						ClaimsObject compare;
						int index = -1;
						while (insert.hasNext()){
							index++;
							compare = insert.next();
							if (compare.fromYear<claimEdit.fromYear)
								continue;
							if (compare.fromYear>claimEdit.fromYear)
								break;
							if (compare.fromMonth<claimEdit.fromMonth)
								continue;
							if (compare.fromMonth>claimEdit.fromMonth)
								break;
							if (compare.fromDay<claimEdit.fromDay)
								continue;
							if (compare.fromDay>claimEdit.fromDay)
								break;
						}
						if (!insert.hasNext())
							index++;
						claimEditList.set(claimId, claimEdit);
						saveClaimInFile();
						startActivity(goBackEditClaim);
					}
				} catch (Throwable e) {
					Toast.makeText(getApplicationContext(), "missing items",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				claimEdit.status = "Submitted";
				
				// From
				// https://stackoverflow.com/questions/8284706/send-email-via-gmail
				// accessed 01-02-2015
				Intent send = new Intent(Intent.ACTION_SENDTO);
				String uriText = "mailto:"
						+ Uri.encode("")
						+ "?subject="
						+ Uri.encode("New Travel Claim")
						+ "&body="
						+ Uri.encode(submitClaimSummary(claimEdit)
								+ "\nExpenses:\n" + submitClaimExpenses(sumExp));
				Uri uri = Uri.parse(uriText);

				send.setData(uri);
				startActivity(goBackEditClaim);
				startActivity(Intent.createChooser(send, "Send mail..."));
				saveClaimInFile();
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		claimEditList = loadFromFile();
		claimId = getIntent().getIntExtra("claimId", -2);
		claimEdit = claimEditList.get(claimId);
		sumExp = getExpenses(claimEdit);
		claimNameEdit.setText(claimEdit.name);
		claimFromEdit.setText(claimEdit.fromDayString + "-"
				+ claimEdit.fromMonthString + "-" + claimEdit.fromYear);
		claimToEdit.setText(claimEdit.toDayString + "-"
				+ claimEdit.toMonthString + "-" + claimEdit.toYear);
		claimDesEdit.setText(claimEdit.claimDes);
	}

	private ArrayList<ClaimsObject> loadFromFile() {
		Gson gson = new Gson();
		ArrayList<ClaimsObject> claimList = new ArrayList<ClaimsObject>();
		try {
			FileInputStream fis = openFileInput(File.getClaimfile());
			// https://sites.google.com/site/gson/gson-user-guide 2015-01-21
			Type dataType = new TypeToken<ArrayList<ClaimsObject>>() {
			}.getType();
			InputStreamReader isr = new InputStreamReader(fis);
			claimList = gson.fromJson(isr, dataType);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (claimList == null) {
			claimList = new ArrayList<ClaimsObject>();

		}
		return claimList;
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
	private void saveClaimInFile() {
		Gson gson = new Gson();
		try {
			FileOutputStream fos = openFileOutput(File.getClaimfile(), 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(claimEditList, osw);
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

	private ArrayList<ExpensesObject> getExpenses(ClaimsObject claim) {
		// TODO Auto-generated method stub
		expList = loadExpFromFile();
		ExpensesObject exp = new ExpensesObject();
		int fromDay, fromMonth, fromYear, toDay, toMonth, toYear;
		fromDay = claim.fromDay;
		fromMonth = claim.fromMonth;
		fromYear = claim.fromYear;
		toDay = claim.toDay;
		toMonth = claim.toMonth;
		toYear = claim.toYear;
		Iterator<ExpensesObject> it = expList.iterator();
		while (it.hasNext()) {
			exp = it.next();
			if (exp.year > toYear)
				break;
			if (exp.year < fromYear)
				break;
			if (exp.year == toYear) {
				if (exp.month > toMonth)
					break;
				if (exp.month == toMonth)
					if (exp.day > toDay)
						break;
				if (exp.month < fromMonth)
					break;
				if (exp.month == fromMonth)
					if (exp.day < fromDay)
						break;
			}
			sumExp.add(exp);
		}
		return sumExp;
	}

	private String submitClaimSummary(ClaimsObject claim) {
		return claim.toString() + "\n" + "$" + claim.CAD + " CAD" + "\n" + "$"
				+ claim.USD + " USD" + "\n" + "€" + claim.EUR + " EUR" + "\n"
				+ "£" + claim.GBP + " GBP" + "\n" + claim.claimDes + "\n";
	}

	private String submitClaimExpenses(ArrayList<ExpensesObject> sumExp) {
		String output = "";
		ExpensesObject exp = new ExpensesObject();
		Iterator<ExpensesObject> it = sumExp.iterator();
		while (it.hasNext()) {
			exp = it.next();
			output += exp.toString() + "\n" + exp.category + "\n"
					+ exp.description + "\n" + "-----------------" + "\n";
		}
		return output;
	}

	private ArrayList<ExpensesObject> loadExpFromFile() {
		Gson gson = new Gson();
		ArrayList<ExpensesObject> expList = new ArrayList<ExpensesObject>();
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
		getMenuInflater().inflate(R.menu.edit_claims, menu);
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
