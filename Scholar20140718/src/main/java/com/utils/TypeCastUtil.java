package com.utils;

public class TypeCastUtil {

	public static Float castInteger2Float(int a) {
		return Float.parseFloat(String.valueOf(a));
	}
	
	public static Integer castLong2Integer(long a) {
		return Integer.parseInt(String.valueOf(a));
	}
	
	public static Long castInteger2Long(int a) {
		return Long.parseLong(String.valueOf(a));
	}
}
