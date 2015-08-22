package com.worker.feature.featureImpl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.utils.MathUtil;
import com.worker.feature.SimInterface;

/**
 * 余弦相似度计算
 * 
 * @author seal
 *
 */
@Service
public class CosSimCaculateImpl implements SimInterface {

	/**
	 * 计算2个用户特征的余弦相似度
	 * 
	 */
	@Override
	public double simCaculate(Map<Object, Double> u1, Map<Object, Double> u2) {
		// TODO Auto-generated method stub
		double module1 = MathUtil.caculateMoudle(u1);
		double module2 = MathUtil.caculateMoudle(u2);
		double cartesianproduct = MathUtil.cartesianProduct(u1, u2);
		if (module1 != 0 && module2 != 0)
			return cartesianproduct / (module1 * module2);
		return -1d;
	}

	@Override
	public double simCaculate(String u1, String u2) {
		// TODO Auto-generated method stub
		return -1d;
	}

}
