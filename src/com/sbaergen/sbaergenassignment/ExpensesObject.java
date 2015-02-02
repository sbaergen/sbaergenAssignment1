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

import java.io.Serializable;
import java.text.DecimalFormat;

public class ExpensesObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String name;
	protected int day;
	protected int month;
	protected int year;
	protected Double cost;
	protected String currency;
	protected int id = -1;
	protected String dayString;
	protected String monthString;
	protected String costString;
	protected String description;
	public String category;
	
	
	public String getDayString() {
		return dayString;
	}

	// Ensures Day is printed with 2 digits
	public void setDayString(int day) {
		if (day < 10)
			this.dayString = "0"+day;
		else
			this.dayString = "" + day;
	}


	public String getMonthString() {
		return monthString;
	}

	// Ensures Month is printed with 2 digits
	public void setMonthString(int month) {
		if (month < 10)
			this.monthString = "0"+month;
		else
			this.monthString = "" + month;
	}


	public String getCostString() {
		return costString;
	}


	public void setCostString(Double cost) {
		DecimalFormat twoDigit = new DecimalFormat("#0.00");
		this.costString = twoDigit.format(cost);
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String output;
		if (currency.equals("GBP"))
			output = name + "\n" + dayString + "-" + monthString + "-" + year + "\n" + "£" + costString + " " + currency;
		else if (currency.equals("EUR"))
			output = name + "\n" + dayString + "-" + monthString + "-" + year + "\n" + "€" + costString + " " + currency;
		else
			output = name + "\n" + dayString + "-" + monthString + "-" + year + "\n" + "$" + costString + " " + currency;

		return output;
	}
	
}
