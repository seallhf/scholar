package com.nlp;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

class floatMapValue{
	public float n = 0;
	public floatMapValue(float n){
		this.n = n;
	}
}

class intMapValue{
	public int n = 0;
	public intMapValue(int n){
		this.n = n;
	}
}

/**
 * Map的结构:
 * 	id1	<id2,sim> <id4, sim>,...
 *  id2 <id3, sim> <id5, sim>,...
 *  sim取值如下：
 * 		-1	输入错误
 * 		0	t1与t2完全不同
 * 		1	t1与t2基本相同
 * 		2	t1是t2的部分内容
 * 		3	t2是t1的部分内容
 * */
class SimilarResult{
	private Map ret = new TreeMap();
	
	public Map get(){
		return(ret);
	}
	
	public void push(String id1, String id2, int r){
		Map v = (TreeMap)ret.get(id1);
		if (v == null){
			v = new TreeMap();
			v.put(id2, new intMapValue(r));
			ret.put(id1, v);
		}else{
			v.put(id2, new intMapValue(r));
		}
	}
	
	public boolean find(String id1, String id2){
		Map v = (TreeMap)ret.get(id1);
		if (v == null){
			return(false);
		}else{
			if (v.containsKey(id2) == true)
				return(true);
			else
				return(false);
		}
	}
	
	private void printLocal(String id1, final Map ids){
		Map.Entry entry = null;
		String id2;
		intMapValue v;
		Iterator it = ids.entrySet().iterator();
		
		while (it.hasNext()){
			entry = (Map.Entry)it.next();
			id2 = (String)entry.getKey();
			v = (intMapValue)entry.getValue();
			System.out.println(id1 + "\t" + id2 + "\t" + v.n);
		}
	}
}

class WordPosPair{
	public String word = null;
	public String pos = null;
	public WordPosPair(String w, String p){
		word = w;
		pos = p;
	}
}

class CompareResult{
	public float n1 = 0;
	public float n2 = 0;
	public float common = 0;
	public float union = 0;
	public void print(){
		System.out.println("n1="+n1+" n2="+n2 + " comm="+common+" union="+union);
	}
}

public class CommonStruct {

}
