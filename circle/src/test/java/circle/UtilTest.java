package circle;

import java.util.Properties;
import junit.framework.TestCase; 

import com.utils.StringUtil;
import com.utils.spring.SpringBeanFactory;

public class UtilTest extends TestCase {
	
	public void testGetProperties(){ 
		StringUtil stringUtil = (StringUtil) SpringBeanFactory
				.getBean("stringUtil");
		Properties p = stringUtil.getProperties();
		System.out.println(p); 
    } 
}
