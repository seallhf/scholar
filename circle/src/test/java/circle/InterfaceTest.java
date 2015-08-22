package circle;

import junit.framework.TestCase;

import com.utils.spring.SpringBeanFactory;
import com.web.action.UserSimilarityAction;

public class InterfaceTest extends TestCase{

	public void testHttpInterface() {

		UserSimilarityAction user = (UserSimilarityAction) SpringBeanFactory
				.getBean("userSimilarityAction");
		System.out.println(user.getUserScore("David").toJSONString());
	}
}
