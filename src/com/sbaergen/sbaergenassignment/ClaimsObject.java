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

public class ClaimsObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	protected String name;
	protected int fromYear;
	protected int fromMonth;
	protected int fromDay;
	protected int toYear;
	protected int toMonth;
	protected int toDay;
	protected int id = -1;
	protected String fromMonthString;
	protected String fromDayString;
	protected String toMonthString;
	protected String toDayString;
	protected String status = "In Progress";
	protected String claimDes;
	protected double CAD;
	protected double USD;
	protected double EUR;
	protected double GBP;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFromMonthString() {
		return fromMonthString;
	}

	public void setFromMonthString(int fromMonth) {
		if (fromMonth < 10)
			this.fromMonthString = "0"+fromMonth;
		else
			this.fromMonthString = "" + fromMonth;
	}

	public String getFromDayString() {
		return fromDayString;
	}

	public void setFromDayString(int fromDay) {
		if (fromDay < 10)
			this.fromDayString = "0"+fromDay;
		else
			this.fromDayString = "" + fromDay;
	}

	public String getToMonthString() {
		return toMonthString;
	}

	public void setToMonthString(int toMonth) {
		if (toMonth < 10)
			this.toMonthString = "0"+toMonth;
		else
			this.toMonthString = "" + toMonth;
	}

	public String getToDayString() {
		return toDayString;
	}

	public void setToDayString(int toDay) {
		if (toDay < 10)
			this.toDayString = "0"+toDay;
		else
			this.toDayString = "" + toDay;
	}

	public int getFromYear() {
		return fromYear;
	}

	public void setFromYear(int fromYear) {
		this.fromYear = fromYear;
	}

	public int getFromMonth() {
		return fromMonth;
	}

	public void setFromMonth(int fromMonth) {
		this.fromMonth = fromMonth;
	}

	public int getFromDay() {
		return fromDay;
	}

	public void setFromDay(int fromDay) {
		this.fromDay = fromDay;
	}

	public int getToYear() {
		return toYear;
	}

	public void setToYear(int toYear) {
		this.toYear = toYear;
	}

	public int getToMonth() {
		return toMonth;
	}

	public void setToMonth(int toMonth) {
		this.toMonth = toMonth;
	}

	public int getToDay() {
		return toDay;
	}

	public void setToDay(int toDay) {
		this.toDay = toDay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name + "\n" + fromDayString + "-" + fromMonthString + "-"
				+ fromYear + " to " + toDayString + "-" + toMonthString + "-"
				+ toYear + "\n" + status;
	}
}
