package com.utils;

import java.util.Map;

public class MathUtil {

	/**
	 * 模计算
	 * 
	 * @param feature
	 * @return
	 */
	public static double caculateMoudle(Map<Object, Double> feature) {
		double score = 0d;
		for (Object fea : feature.keySet()) {
			score += Math.pow(feature.get(fea), 2);
		}
		return Math.sqrt(score);
	}

	public static double cartesianProduct(Map<Object, Double> f1,
			Map<Object, Double> f2) {
		double score = 0d;
		for (Object fea : f1.keySet()) {
			if (f2.containsKey(fea)) {
				score += (f1.get(fea) * f2.get(fea));
			}
		}
		return score;
	}
}
