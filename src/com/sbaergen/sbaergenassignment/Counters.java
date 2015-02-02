package com.sbaergen.sbaergenassignment;

public class Counters {
	static int claimCount = 0;
	static int expCount = 0;

	public int getCount() {
		return claimCount;
	}

	public void setCount(int count) {
		Counters.claimCount = count;
	}

	public static int getExpCount() {
		return expCount;
	}

	public static void setExpCount(int expCount) {
		Counters.expCount = expCount;
	}
	
}
