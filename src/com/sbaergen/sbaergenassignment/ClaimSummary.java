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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClaimSummary extends Activity {

	private TextView sumTitle;
	private TextView sumFrom;
	private TextView sumTo;
	private TextView sumCAD;
	private TextView sumUSD;
	private TextView sumEUR;
	private TextView sumGBP;
	private Button sumEdit;
	private Button sumBack;
	private Button delClaim;
	private ListView sumListView;
	private Intent editClaim;
	private Intent subClaim;
	private Intent editExp;
	private Intent sumBackInt;
	private ArrayList<ClaimsObject> claimList;
	private ArrayList<ExpensesObject> expList; // All Expenses
	private ArrayList<ExpensesObject> sumExp; // Only Expenses in claim
	private ArrayAdapter<ExpensesObject> expAdapter;
	private ClaimsObject claim;
	private int id;
	private double CAD;
	private double USD;
	private double EUR;
	private double GBP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_summary);
		sumTitle = (TextView) findViewById(R.id.sumTitle);
		sumFrom = (TextView) findViewById(R.id.sumFrom);
		sumTo = (TextView) findViewById(R.id.sumTo);
		sumCAD = (TextView) findViewById(R.id.sumCAD);
		sumUSD = (TextView) findViewById(R.id.sumUSD);
		sumEUR = (TextView) findViewById(R.id.sumEUR);
		sumGBP = (TextView) findViewById(R.id.sumGBP);
		sumEdit = (Button) findViewById(R.id.sumEdit);
		sumBack = (Button) findViewById(R.id.sumBack);
		delClaim = (Button) findViewById(R.id.delClaim);
		sumListView = (ListView) findViewById(R.id.sumListView);
		editClaim = new Intent(this, EditClaims.class);
		sumBackInt = new Intent(this, Claims.class);
		subClaim = new Intent(this, SubmittedClaim.class);
		editExp = new Intent(this, EditExpense.class);
		delClaim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Deletes claim from claimList, saves claimList, and goes to Claims
				claimList.remove(id);
				saveClaimInFile();
				startActivity(sumBackInt);
			}
		});
		sumBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Goes back to Claims
				startActivity(sumBackInt);
			}
		});

		sumEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// If editable, go to EditClaim.class
				if (claim.status.equals("In Progress")
						|| claim.status.equals("Returned")) {
					editClaim.putExtra("claimId", claim.id);
					startActivity(editClaim);
				}
				// If it is approved don't go anywhere
				else if (claim.status.equals("Approved"))
					Toast.makeText(getApplicationContext(), "Cannot edit Approved Claim", Toast.LENGTH_SHORT).show();
				// If it is Submitted go to subClaim
				else{
					subClaim.putExtra("claim", claim);
					startActivity(subClaim);
				}
			}
		});

		sumListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// Goes to EditExpense by sending sumExp and the position of the Expense
				Bundle bundle = new Bundle();
				bundle.putSerializable("expList", sumExp);
				editExp.putExtras(bundle);
				editExp.putExtra("expId", position);
				startActivity(editExp);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Load list from previous intent
		Intent sum = getIntent();
		claimList = (ArrayList<ClaimsObject>) sum
				.getSerializableExtra("sumList");
		id = sum.getIntExtra("claimId", -2);
		claim = claimList.get(id);
		// Gets all expenses in date range
		getExpenses(claim);
		claim.id = id;
		claimList.set(id, claim);
		sumTitle.setText(claim.name);
		sumCAD.setText("$" + CAD + "CAD");
		sumUSD.setText("$" + USD + "USD");
		sumEUR.setText("€" + EUR + "EUR");
		sumGBP.setText("£" + GBP + "GBP");
		;
		sumFrom.setText(claim.fromDayString + "-" + claim.fromMonthString + "-"
				+ claim.fromYear);
		sumTo.setText(claim.toDayString + "-" + claim.toMonthString + "-"
				+ claim.toYear);
		if (sumExp != null) {
			expAdapter = new ArrayAdapter<ExpensesObject>(this,
					R.layout.claim_view, sumExp);
			sumListView.setAdapter(expAdapter);
		}
		saveClaimInFile();
	}

	// Gets expenses in date range of Claim
	private ArrayList<ExpensesObject> getExpenses(ClaimsObject claim) {
		// TODO Auto-generated method stub
		expList = loadExpFromFile();
		ExpensesObject exp = new ExpensesObject();
		sumExp = new ArrayList<ExpensesObject>();
		CAD = 0;
		USD = 0;
		EUR = 0;
		GBP = 0;
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
			Log.v("cur", "\\" + exp.currency + "//");
			if (exp.currency.equals("CAD"))
				CAD += exp.cost;
			else if (exp.currency.equals("USD"))
				USD += exp.cost;
			else if (exp.currency.equals("EUR"))
				EUR += exp.cost;
			else
				GBP += exp.cost;

			sumExp.add(exp);
		}
		claim.CAD = CAD;
		claim.USD = USD;
		claim.EUR = EUR;
		claim.GBP = GBP;
		return sumExp;
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

	// From LonelyTwitterActivity by Abram Hindle
	private void saveClaimInFile() {
		Gson gson = new Gson();
		try {
			FileOutputStream fos = openFileOutput(File.getClaimfile(), 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(claimList, osw);
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
		getMenuInflater().inflate(R.menu.claim_summary, menu);
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
