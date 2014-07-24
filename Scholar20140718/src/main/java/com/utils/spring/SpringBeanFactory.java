package com.utils.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pojo.Paper;

public class SpringBeanFactory {

	@SuppressWarnings("unused")
	private static final ApplicationContext APPLICATION_CONTEXT =
		new ClassPathXmlApplicationContext(new String[] {
			"classpath:applicationContext.xml"
		});

	public static Object getBean(String beanId) {

		return SpringContextUtil.getBean(beanId);
	}

	public static void main(String[] args) {
		System.out.println((Paper)SpringBeanFactory.getBean("paper"));
	}

}
