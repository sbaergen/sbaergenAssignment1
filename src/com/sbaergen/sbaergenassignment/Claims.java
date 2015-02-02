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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Claims extends Activity {

	private Button newclaim;
	private Button claimBack;
	private Intent newclaimInt;
	private Intent sumClaim;
	private Intent claimBackInt;
	private ArrayList<ClaimsObject> claimList;
	private ArrayAdapter<ClaimsObject> adapterViewClaim;
	private ListView claimListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claims);
		claimListView = (ListView)findViewById(R.id.claimsListView);
		newclaim = (Button)findViewById(R.id.newclaim);
		claimBack = (Button)findViewById(R.id.claimBack);
		newclaimInt = new Intent(this, NewClaim.class);
		sumClaim = new Intent(this, ClaimSummary.class);
		claimBackInt = new Intent(this, MainActivity.class);
		claimBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(claimBackInt);
			}
		});
		claimListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putSerializable("sumList", claimList);
				sumClaim.putExtras(bundle);
				sumClaim.putExtra("claimId", position);
				startActivity(sumClaim);
			}
		});
		newclaim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(newclaimInt);
			}
		});
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//Update ListView
		claimList = loadFromFile();
		adapterViewClaim = new ArrayAdapter<ClaimsObject>(this,
				R.layout.claim_view, claimList);
		claimListView.setAdapter(adapterViewClaim);
		
	}
	
	// From LonelyTwitterActivity by Abram Hindle
	private ArrayList<ClaimsObject> loadFromFile() {
		Gson gson = new Gson();
		ArrayList<ClaimsObject> claimList = new ArrayList<ClaimsObject>();
		try {
			FileInputStream fis = openFileInput(File.getClaimfile());
			//https://sites.google.com/site/gson/gson-user-guide 2015-01-21
			Type dataType = new TypeToken<ArrayList<ClaimsObject>>() {}.getType();
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
		if (claimList == null){
			claimList = new ArrayList<ClaimsObject>();
			
		}
		return claimList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claims, menu);
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
