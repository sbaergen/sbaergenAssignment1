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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SubmittedClaim extends Activity {

	private ClaimsObject claim;
	private Button retClaim;
	private Button approve;
	private TextView subClaim;
	private Intent goBackSub;
	private ArrayList<ClaimsObject> claimEditList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submitted_claim);
		retClaim = (Button)findViewById(R.id.retClaim);
		approve = (Button)findViewById(R.id.approve);
		subClaim = (TextView)findViewById(R.id.subClaim);
		goBackSub = new Intent(this, Claims.class);
		retClaim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				claim.status = "Returned";
				claimEditList.set(claim.id, claim);
				saveClaimInFile();
				startActivity(goBackSub);
			}
		});
		
		approve.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				claim.status = "Approved";
				claimEditList.set(claim.id, claim);
				saveClaimInFile();
				startActivity(goBackSub);
			}
		});
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Intent intent = getIntent();
		claim = (ClaimsObject)intent.getExtras().getSerializable("claim");
		subClaim.setText(claim.name);
		claimEditList = loadFromFile();
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submitted_claim, menu);
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
