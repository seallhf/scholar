package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class StringUtil {

	public Properties getProperties() {
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("weightConfig.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;
	}

}
