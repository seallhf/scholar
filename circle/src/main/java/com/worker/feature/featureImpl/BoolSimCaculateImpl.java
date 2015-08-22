package com.worker.feature.featureImpl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.utils.StringUtil;
import com.utils.spring.SpringBeanFactory;
import com.worker.feature.SimInterface;

@Service
public class BoolSimCaculateImpl implements SimInterface {


	@Override
	public double simCaculate(Map<Object, Double> u1, Map<Object, Double> u2) {
		// TODO Auto-generated method stub
		return -1d;
	}

	@Override
	public double simCaculate(String u1, String u2) {
		// TODO Auto-generated method stub
		StringUtil config = (StringUtil) SpringBeanFactory.getBean("stringUtil");
		String[] list1 = u1.split(" ");
		String[] list2 = u2.split(" ");
		double a = Double.parseDouble(config.getProperties().getProperty(
				"location"));
		double score = 0.0d;
		for (int i = 0; i < list1.length; i++) {
			if (list1[i].equals(list2[i]))
				score += a;
			a += Double.parseDouble(config.getProperties().getProperty(
					"locationChange"));
		}
		return score;
	}
	
}
