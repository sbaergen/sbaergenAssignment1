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
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Expenses extends Activity {

	private Button newitem;
	private Button expBack;
	private Intent expBackInt;
	private Intent newitemInt;
	private Intent editExp;
	private ArrayList<ExpensesObject> expList;
	private ArrayAdapter<ExpensesObject> adapterView;
	private ListView expListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expenses);
		newitem = (Button)findViewById(R.id.newitem);
		expBack = (Button)findViewById(R.id.expBack);
		expListView = (ListView)findViewById(R.id.expListView);
		newitemInt = new Intent(this,NewExpense.class);
		editExp = new Intent(this,EditExpense.class);
		expBackInt = new Intent(this,MainActivity.class);
		expBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(expBackInt);
			}
		});
		expListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putSerializable("expList", expList);
				Log.v("LISTCHECK", expList.size() + "THIS IS THE SIZE OF THE ARRAY");
				editExp.putExtras(bundle);
				editExp.putExtra("expId", position);
				startActivity(editExp);
			}
		});
		newitem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(newitemInt);
			}
		});
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		expList = loadFromFile();
		adapterView = new ArrayAdapter<ExpensesObject>(this,
				R.layout.claim_view, expList);
		expListView.setAdapter(adapterView);
		
	}

	// From LonelyTwitterActivity by Abram Hindle
	private ArrayList<ExpensesObject> loadFromFile() {
		Gson gson = new Gson();
		ArrayList<ExpensesObject> expList = new ArrayList<ExpensesObject>();
		try {
			FileInputStream fis = openFileInput(File.getExpfile());
			//https://sites.google.com/site/gson/gson-user-guide 2015-01-21
			Type dataType = new TypeToken<ArrayList<ExpensesObject>>() {}.getType();
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
		if (expList == null){
			expList = new ArrayList<ExpensesObject>();
			
		}
		return expList;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expenses, menu);
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
