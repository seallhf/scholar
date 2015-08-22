package com.worker.feature;

import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 用户特征相似度计算接口
 * 
 * @author seal
 *
 */
@Repository
public interface SimInterface {

	abstract double simCaculate(Map<Object, Double> u1, Map<Object, Double> u2);

	abstract double simCaculate(String u1, String u2);
}
