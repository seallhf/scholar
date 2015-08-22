package circle;

import java.util.HashMap;
import java.util.Map;

import com.utils.MathUtil;
import com.utils.spring.SpringBeanFactory;
import com.worker.feature.SimInterface;
import com.worker.feature.featureImpl.CosSimCaculateImpl;

import junit.framework.TestCase;

public class MathUtilTest extends TestCase {

	public void testModuleCaculate() {
		Map<Object, Double> map = new HashMap<Object, Double>();
		map.put("1", 1d);
		map.put("2", 1d);
		map.put("3", 1d);
		System.out.println(MathUtil.caculateMoudle(map));
	}
	
	public void testCartesianProductCaculate() {
		Map<Object, Double> map = new HashMap<Object, Double>();
		map.put("1", 1d);
		map.put("2", 1d);
		map.put("3", 1d);
		Map<Object, Double> map1 = new HashMap<Object, Double>();
		map1.put("1", 1d);
		map1.put("2", 1d);
		map1.put("4", 1d);
		System.out.println(MathUtil.cartesianProduct(map,map1));
	}
}
