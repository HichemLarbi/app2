package com.example.hlarbi.firebaseappbeta1.AccountActivity.ViewClasses.linechart_animation_pollu;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {

	public static String format(long millis){
		SimpleDateFormat format = new SimpleDateFormat("dd", Locale.getDefault());
		return format.format(millis);
	}
}
