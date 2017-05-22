package com.ckcest.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;


public class MapSortByValue {
	public static void main(String[] args) {

		Map<Integer, Double> map = new TreeMap<Integer, Double>();

		map.put(1, 4.0);
		map.put(2, 2.0);
		map.put(3, 1.0);
		map.put(4, 3.0);

//		Map<String, String> resultMap = sortMapByKey(map);	//按Key进行排序
		Map<Integer, Double> resultMap = revertsortMapByValue(map); //按Value进行排序

		for (Map.Entry<Integer, Double> entry : resultMap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}
	
	/**
	 * 使用 Map按value进行排序
	 * @param map
	 * @return
	 */
	public static Map<Integer, Double> sortMapByValue(Map<Integer, Double> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		List<Map.Entry<Integer, Double>> entryList = new ArrayList<Map.Entry<Integer, Double>>(
				oriMap.entrySet());
		Collections.sort(entryList, new MapValueComparator());

		Iterator<Map.Entry<Integer, Double>> iter = entryList.iterator();
		Map.Entry<Integer, Double> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
	
	public static Map<Integer, Double> revertsortMapByValue(Map<Integer, Double> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		List<Map.Entry<Integer, Double>> entryList = new ArrayList<Map.Entry<Integer, Double>>(
				oriMap.entrySet());
		Collections.sort(entryList, new MapValueComparator1());

		Iterator<Map.Entry<Integer, Double>> iter = entryList.iterator();
		Map.Entry<Integer, Double> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
	
	public static Map<String, Integer> revertsortMapByValue1(Map<String, Integer> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
				oriMap.entrySet());
		Collections.sort(entryList, new MapValueComparator2());

		Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
		Map.Entry<String, Integer> tmpEntry = null;
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
}

class MapValueComparator implements Comparator<Map.Entry<Integer, Double>> {

	@Override
	public int compare(Entry<Integer, Double> me1, Entry<Integer, Double> me2) {

		return me1.getValue().compareTo(me2.getValue());
	}
}

class MapValueComparator1 implements Comparator<Map.Entry<Integer, Double>> {

	@Override
	public int compare(Entry<Integer, Double> me1, Entry<Integer, Double> me2) {

		return - (me1.getValue().compareTo(me2.getValue()));
	}
}

class MapValueComparator2 implements Comparator<Map.Entry<String, Integer>> {

	@Override
	public int compare(Entry<String, Integer> me1, Entry<String, Integer> me2) {

		return - (me1.getValue().compareTo(me2.getValue()));
	}
}
