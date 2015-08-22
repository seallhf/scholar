package circle;

import java.util.HashMap;
import java.util.Map;

import com.utils.spring.SpringBeanFactory;
import com.worker.feature.SimInterface;
import com.worker.feature.featureImpl.BoolSimCaculateImpl;
import com.worker.feature.featureImpl.CosSimCaculateImpl;

import junit.framework.TestCase;

public class CosineSimTest extends TestCase {

	public void testCosCaculate() {
		SimInterface sim = (CosSimCaculateImpl) SpringBeanFactory
				.getBean("cosSimCaculateImpl");
		Map<Object, Double> map = new HashMap<Object, Double>();
		map.put("1", 1d);
		map.put("2", 1d);
		map.put("3", 1d);
		Map<Object, Double> map1 = new HashMap<Object, Double>();
		map1.put("1", 1d);
		map1.put("2", 1d);
		map1.put("4", 1d);
		double a = sim.simCaculate(map, map1);
		System.out.println(a);
	}
	
	public void testBoolCaculate(){
		SimInterface sim = (BoolSimCaculateImpl) SpringBeanFactory
				.getBean("boolSimCaculateImpl");
		System.out.println(sim.simCaculate("中国 四川 乐山", "中国 四川 成都"));
	}
}
