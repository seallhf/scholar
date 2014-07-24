package com.nlp;

import org.springframework.stereotype.Service;

import com.utils.PaperUtil;

@Service
public class CCFComparation {

	/**
	 * 通过简写和期刊名称来判断是否是期刊
	 * 
	 * @param name
	 * @return
	 */
	public static String getPaperType(String name) {
		if (name != null && !name.equals(""))
			if (PaperUtil.paperShortType.containsKey(name))
				return PaperUtil.paperShortType.get(name);
			else if (PaperUtil.paperType.containsKey(name))
				return PaperUtil.paperType.get(name);
			else {
				for (String jounalName : PaperUtil.paperType.keySet()) {
					if (isTheSameJounal(name, jounalName)) {
						return PaperUtil.paperType.get(jounalName);
					}
				}
			}
		return "";
	}

	/**
	 * 通过简写和期刊名称来判断是否匹配
	 * 
	 * @param name
	 * @param jounalName
	 * @return
	 */
	private static boolean isTheSameJounal(String name, String jounalName) {
		if (name.toLowerCase().contains((jounalName.toLowerCase())))
			return true;
		if (jounalName.toLowerCase().contains((name.toLowerCase())))
			return true;
		return false;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println(CCFComparation.getPaperType(""));
		System.out.println(System.currentTimeMillis() - start + "ms");
		start = System.currentTimeMillis();
		System.out
				.println(CCFComparation
						.getPaperType("IEEE Transactions on knowledge and Data Engineering"));
		System.out.println(System.currentTimeMillis() - start + "ms");
	}
}
