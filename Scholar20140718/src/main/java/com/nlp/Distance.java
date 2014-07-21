package com.nlp;

/*
 * Levenshtein算法，用于计算两个字符串之间的Levenshtein距离。而Levenshtein距离又称为编辑距离，
 */
public class Distance {

    public static void main(String[] args) {
        Distance distance = new Distance();
        int i = distance.LD("agambola", "gambol");
        System.out.println(i);
    }

    // ****************************
    // Get minimum of three values
    // ****************************

    private int minimum(int a, int b, int c) {
        int mi;

        mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        return mi;

    }

    // *****************************
    // Compute Levenshtein distance
    // *****************************

    public int getDiff(String s, String t) {
        s = s.toLowerCase();
        t = t.toLowerCase();
        return LD(s, t);
    }

    public double similarity(String s, String t) {
        int diff = getDiff(s, t);
        return 1 - diff / (double) Math.max(s.length(), t.length());
    }

    private int LD(String s, String t) {
        //构建一个二维数据
        int d[][]; // matrix
        //s的长度
        int n; // length of s
        //t的长度
        int m; // length of t
        //s的偏移量
        int i; // iterates through s
        //t的偏移量
        int j; // iterates through t
        //s偏移量所在的char
        char s_i; // ith character of s
        //t偏移量所在的char
        char t_j; // jth character of t
        //临时变量对比差值
        int cost; // cost

        // Step 1

        n = s.length();
        m = t.length();
        //当n为0时.则变化为m所有的值
        if (n == 0) {
            return m;
        }
        //同上
        if (m == 0) {
            return n;
        }

        d = new int[n + 1][m + 1];

        // Step 2 将数组首行首列添加内容.为当前行号列号

        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3

        for (i = 1; i <= n; i++) {
            s_i = s.charAt(i - 1);
            // Step 4
            //判断i位置的值和 t的每个字的差值
            for (j = 1; j <= m; j++) {
                t_j = t.charAt(j - 1);
                // Step 5
                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                // Step 6
                //在数组的
                d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
            }
        }
        
        
        
        // Step 7
        //取得最右面最下面的值就是文本的相似度了
        return d[n][m];

    }

}
