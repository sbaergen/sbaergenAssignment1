package com.sbaergen.sbaergenassignment;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class TravelExArrays implements Parcelable {
	public ArrayList<ExpensesObject> expList;
	public ArrayList<ClaimsObject> claimList;
	
	public ArrayList<ExpensesObject> getExpList() {
		return expList;
	}
	public void setExpList(ArrayList<ExpensesObject> expList) {
		this.expList = expList;
	}
	public ArrayList<ClaimsObject> getClaimList() {
		return claimList;
	}
	public void setClaimList(ArrayList<ClaimsObject> claimList) {
		this.claimList = claimList;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
}
