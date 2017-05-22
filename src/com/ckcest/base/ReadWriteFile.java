package com.ckcest.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ReadWriteFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static String ReadFile(String file) {
		StringBuilder string = new StringBuilder();
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String s = "";
			while ((s = br.readLine()) != null) string.append(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string.toString();
	}
	
	public static ArrayList<String> ReadFiletoList(String file) {
		ArrayList<String> result = new ArrayList<>();
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String s = "";
			while ((s = br.readLine()) != null) result.add(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * 一次性写文件，或覆盖
	 */
	public static void WriteFile(String file, String content) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			bw.write(content);
			
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加进文件
	 * @param file
	 * @param content
	 */
	public static void WriteFileadd(String file, String content) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "utf-8"));
			bw.write(content);
			
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
