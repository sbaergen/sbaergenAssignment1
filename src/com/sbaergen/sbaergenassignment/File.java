package com.sbaergen.sbaergenassignment;

import android.app.Application;

public class File extends Application {
	public static final String EXPFILE = "efile.sav";
	public static final String CLAIMFILE = "cfile.sav";
	
	public static String getExpfile() {
		return EXPFILE;
	}
	public static String getClaimfile() {
		return CLAIMFILE;
	}

	

}
