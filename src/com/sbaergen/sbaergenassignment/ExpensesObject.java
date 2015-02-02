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

	public void setDayString(int day) {
		if (day < 10)
			this.dayString = "0"+day;
		else
			this.dayString = "" + day;
	}


	public String getMonthString() {
		return monthString;
	}


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
