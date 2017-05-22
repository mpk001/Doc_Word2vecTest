package com.ckcest.base;

import java.util.ArrayList;

public class Jaccard {
	public static double JaccardDistance(ArrayList<Integer> A, ArrayList<Integer> B) {
		int intersection = 0;
		int union = 0;
		for (int a : A) {
			if (B.contains(a)) intersection++;
			else union++;
		}
		union += B.size();
		
		return (((double)intersection) / union);
	}
}
