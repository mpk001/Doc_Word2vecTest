package com.ckcest.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.log4j.Logger;
//import org.apache.lucene.queryparser.surround.query.SimpleTerm.MatchingTermVisitor;

public class CatalogItemParser {
	/*
	 * private static final Logger logger =
	 * Logger.getLogger(CatalogItemParser.class); public static void init(){
	 * NLPIRTool.init(); } public static void exit(){ NLPIRTool.exit(); }
	 */
	public static void main(String[] args) {
		String str = "§6．1 概述";
		System.out.println(trim(str));
	}
	
	private static String specialstr = "^.[维|阶|元|次|价|极|叉]";
	
	public static String complex2simple(String title) {
		if (title == null || title.equals(""))
			return "";
		String result = Complex2SimpleTool.conver(title, 0);
		return result;
	}

	public static String trim(String title) {
		String str = title;
		Pattern pattern = Pattern.compile("^(\\d+){0,5}[　|\\s]+");
		Matcher matcher = pattern.matcher(str);
		str = matcher.replaceAll("");
		
		pattern = Pattern.compile(specialstr);
		matcher = pattern.matcher(str);
		if (!matcher.find()) {
			pattern = Pattern.compile("^[一二三四五六七八九十百千]+[　|\\s]+");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			
			// str = str.replaceAll("[　|\\s]+|\t|\r|\n", "");//删除所有空格

			String[] specialChar = { "〖", "〗", "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ", "Ⅴ", "Ⅵ", "Ⅶ",
					"Ⅷ", "Ⅸ", "Ⅹ", "Ⅺ", "Ⅻ", "【", "】", "\\[", "\\]", "］", "［", "“",
					"”", "§", "⊙", "「", "」", "◆", "★", "▲", "●  ", "●", "■", "◎" };

			for (int i = 0; i < specialChar.length; i++) {
				str = str.replaceAll(specialChar[i], "");
			}/*
			 * for (int i = 0; i < startChar.length; i++) { if
			 * (str.startsWith(startChar[i])) str = str.replace(startChar[i], ""); }
			 */

			pattern = Pattern
					.compile("^(?i)Chapter [1-9]|第([1-9][0-9]*|[一二三四五六七八九十百千]+)[节|章|篇|部分|单元|章节|上篇|下篇|课|回|题|讲|卷|集|幕|编|画|期]");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			// .{1,2}[　| |\\.|，|,|：|:]*
			// logger.debug("first pattern: " + str);

			pattern = Pattern
					.compile("^(\\d+[　|\\s]*[\\.．·\\-—、。°]){0,5}[　|\\s]*\\d*");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			// logger.debug("second pattern: " + str);

			pattern = Pattern
					.compile("^[\\(|（|〔]*.{1,3}[\\)|）|〕]*[　|\\s|\\.|．|·|，|,|：|:|、]+");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			// logger.debug("third pattern: " + str);

			pattern = Pattern.compile("^[\\(|（|〔|\\[].{1,2}[\\)|）|〕|\\]]");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			// logger.debug("fourth pattern: " + str);

			pattern = Pattern
					.compile("^[\\(|（|〔|\\[]*[　|\\s]*[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789АБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЭЮЯ][　|\\s]*[\\)|）|〕|\\]]");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");

			pattern = Pattern
					.compile("^([ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz][-—](\\d){1,3})");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
		}

		str = str.trim();
		pattern = Pattern.compile(specialstr);
		matcher = pattern.matcher(str);
		if (!matcher.find()) {
			pattern = Pattern.compile("^[一二三四五六七八九十百千]+[　|\\s]*[\\.|．|·|、]+");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			// logger.debug("sixth pattern: " + str);

			pattern = Pattern
					.compile("^[\\(|（]*[　|\\s]*[一二三四五六七八九十百千]+[　|\\s]*[\\)|）]*[　|\\s|\\.|．|·|，|,|：|:|、]+");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			// logger.debug("seven pattern: " + str);

			pattern = Pattern
					.compile("^[\\(|（]*[　|\\s]*[一二三四五六七八九十百千]+[　|\\s]*[\\)|）]*");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");

			pattern = Pattern
					.compile("^[表|问题|例|图][　|\\s]*([1-9][0-9]*|[一二三四五六七八九十]+)[　|\\s|\\.|．|·|，|,|：|:|、]*");
			matcher = pattern.matcher(str);
			if (matcher.find())
				str = "";

			// “xxx 组成，xxx类型，xxx分类， xxx选介,
			// 各种xxx,常用的xxx，常用xxx，xxx举例,xxx的类型，xxx种类，xxx的品种，xxx主要品种介绍，xxx简表”
			// pattern =
			// Pattern.compile("[组成|类型|分类|选介|举例|的类型|种类|的品种|主要品种介绍|简表|品种]+$");
			// pattern = Pattern.compile("^[(各种)|常用的|常用]+");
			String[] p2 = { "绪论", "参考文献", "导论", "概述", "概论", "引言", "序言", "习题",
					"思考题", "复习题", "本章小结", "小结", "小节", "练习", "本章总结", "结论", "总结",
					"结束语", "答案", "附录", "文献", "总论", "其他", "其它", "总则", "附表", "附件",
					"概说", "概况", "实验", "定理", "定义", "实例", "试卷", "实习", "参考书", "综述",
					"试验", "作业", "方案", "年", "正文"};
			for (String p : p2) {
				if (str.startsWith(p)) {
					str = "";
					break;
				}
			}

			String[] p1 = { "绪", "引", "言", "论", "序", "卷", "上篇", "下篇", "上", "下", "析" };
			for (String p : p1) {
				if (str.startsWith(p)) {
					str = str.replace(p, "");
					break;
				}
			}
			/*
			 * String[] p1 =
			 * {"绪论","绪","参考文献","导论","概述","引言","引","言","论","序言","序","习题"
			 * ,"思考题","复习题","本章小结","小结","小节","练习","本章总结","总结","结束语"};
			 */
			pattern = Pattern
					.compile("^[\\(|（|〔]*[　|\\s]*(([一二三四五六七八九十百千]+)|(\\d+))[　|\\s]*[\\)|）|〕]*[　|\\s|\\.|．|·|，|,|：|:|、]*");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");

			// logger.debug("seven pattern:" + str);
			/*
			 * for(String p:p2){ if(str.endsWith(p)){ str = str.replace(p, "");
			 * break; } }
			 */
			// logger.debug("eight pattern:" + str);

			pattern = Pattern
					.compile("^第[0-9一二三四五六七八九十百千]+[节|章|篇|部分|单元|章节|上篇|下篇|课|回|题|讲|卷|集|幕|编|画|期]+");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			// logger.debug("nine pattern: " + str);

			if (str.length() == 1) {
				pattern = Pattern
						.compile("^[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ぁぃテぇぉかきくけニさしすせ？たちってとなたぬねのはひふへほまみむめもゃゅょりるゎАБВГДЕЖЗИКЛМНОПРСТУФХЦЧШЩЭЮЯァィェォキクケコサシスセソタチツトナノハフヘネマメュョラリレロワ ]");
				matcher = pattern.matcher(str);
				str = matcher.replaceAll("");
			}

			pattern = Pattern
					.compile("^([\\.|．|·|，|,|：|:|、|\\|/|*|@|#|$|%|&|!|！||~|?|？|。|\\(|\\)|（|）|\\[|\\]])");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");

			pattern = Pattern
					.compile("([\\.|．|·|，|,|：|:|、|\\|/|*|@|#|$|%|&|!|！||~|?|？|。])$");
			matcher = pattern.matcher(str);
			str = matcher.replaceAll("");
			
			pattern = Pattern
					.compile("^([卷|期|言|幕|论|划|画])");
			matcher = pattern.matcher(str.trim());
			if (matcher.find())
				str = "";
			
			if (str.contains("试题") || str.contains("试卷") || str.contains("答题") || str.contains("答案"))
				str = "";

			int l = 0;
			int r = 0;
			if (str.contains("（") || str.contains("(")) {
				System.out.println("debug:" + str);
				if (str.contains("（")) l = str.indexOf("（");
				else l = str.indexOf("(");
				
				if (str.contains("）") || str.contains(")")) {
					if (str.contains("）")) r = str.indexOf("）");
					else r = str.indexOf(")");
					
					if (r < str.length()) {
						str = str.substring(0, l) + str.substring(r + 1, str.length());
					}else str = "";
					
				}
			}
		}
		
		return str.trim();
	}

	/**
	 * 剔除的、和、及、与
	 * 
	 * @param str
	 * @return
	 */
	public static String remove_de_he_ji_yu(String str) {
		String[] s = str.split(" ");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length; i++) {
			if (s[i].equals("的") || s[i].equals("和") || s[i].equals("及")
					|| s[i].equals("与") || s[i].equals("中的") || s[i].equals("及其") || s[i].equals("之"))
				continue;
			else
				result.append(s[i]).append(" ");
		}
		return result.toString();
	}

	/**
	 * 剔除常见标点符号
	 * 
	 * @param str
	 * @return
	 */
	public static String remove_Punctuation(String str) {
		String[] s = str.split(" ");
		StringBuilder result = new StringBuilder();
		Pattern pattern = Pattern.compile("^[、,，:：。.”“\"\"‘’''?？()（）——-]");
		Matcher matcher;
		for (int i = 0; i < s.length; i++) {
			matcher = pattern.matcher(s[i]);
			if (matcher.find())
				continue;
			else
				result.append(s[i]).append(" ");
		}
		return result.toString();
	}
	
	
	public static boolean isQuestion(String title) {
		if (title.contains("如何") || title.contains("怎样")
				|| title.contains("什么 ") || title.endsWith("?")
				|| title.endsWith("？") || title.contains("哪些"))
			return true;
		return false;
	}

}
