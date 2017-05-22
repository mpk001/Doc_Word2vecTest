package com.ansj.vec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.ansj.vec.domain.Neuron;
import com.ansj.vec.domain.WordEntry;
import com.ansj.vec.domain.WordNeuron;
import com.ansj.vec.util.Haffman;

public class LearnMoreVec extends LearnDocVec{

	public LearnMoreVec(Map<String, Neuron> wordMap) throws IOException {
		super(wordMap);
	}
	
	/**
	 * trainModel训练模型
	 * 在训练即将结束的时候把信息保存到配置文件property中
	 * 
	 * @param file: 训练文件
	 * @param property: 配置文件
	 */
	private void trainModel(File file, File property) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String temp = null;
			long nextRandom = 5;
			int wordCount = 0;
			int lastWordCount = 0;
			int wordCountActual = 0;

			int sent_no = 0;

			int lineNo = 0;
			
			while ((temp = br.readLine()) != null) {
				System.out.println("doc2VEC: " + (lineNo++) + "/" + totalline + "-->");
				if (wordCount - lastWordCount > 10000) {
					System.out.println("alpha:"
							+ alpha
							+ "\tProgress: "
							+ (int) (wordCountActual
									/ (double) (trainWordsCount + 1) * 100)
							+ "%");
					wordCountActual += wordCount - lastWordCount;
					lastWordCount = wordCount;
					alpha = startingAlpha
							* (1 - wordCountActual
									/ (double) (trainWordsCount + 1));
					if (alpha < startingAlpha * 0.0001) {
						alpha = startingAlpha * 0.0001;
					}
				}
				String[] strs = temp.split(" ");
				wordCount += strs.length;
				List<WordNeuron> sentence = new ArrayList<WordNeuron>();
				for (int i = 0; i < strs.length; i++) {
					Neuron entry = wordMap.get(strs[i]);
					if (entry == null) {
						continue;
					}
					if (sample > 0) {
						double ran = (Math.sqrt(entry.freq
								/ (sample * trainWordsCount)) + 1)
								* (sample * trainWordsCount) / entry.freq;
						nextRandom = nextRandom * 25214903917L + 11;
						if (ran < (nextRandom & 0xFFFF) / (double) 65536) {
							continue;
						}
					}
					sentence.add((WordNeuron) entry);
				}

				for (int index = 0; index < sentence.size(); index++) {
					nextRandom = nextRandom * 25214903917L + 11;
					if (isCbow) {
						cbowGram(index, sent_no, sentence, (int) nextRandom
								% window);
					} else {
						skipGram(index, sent_no, sentence, (int) nextRandom
								% window);
					}
				}

				sent_no++;

			}

			br.close();
			
			if(property != null){
				//保存配置信息
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(property)));
				bw.append("nextRandom:" + nextRandom + "\n");
				bw.append("wordCount:" + wordCount + "\n");
				bw.append("lastWordCount:" + lastWordCount + "\n");
				bw.append("wordCountActual:" + wordCountActual + "\n");
				bw.append("trainWordsCount:" + trainWordsCount + "\n");
				bw.append("sent_no:" + (sent_no-1) + "\n");
				bw.close();
			}
			
			System.out.println("Vocab size: " + wordMap.size());
			System.out.println("Words in train file: " + trainWordsCount);
			System.out.println("sucess train over!");
		}
	}
	
	/**
	 * 根据文件学习
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void learnFile(File file, File property) throws IOException {

		InitializeDocVec(file);
		new Haffman(layerSize).make(wordMap.values());

		for (Neuron neuron : wordMap.values()) {
			((WordNeuron) neuron).makeNeurons();
		}

		trainModel(file, property);
	}
	
	/**
	 * 读取配置信息
	 * @throws IOException 
	 */
	
	public void loadProperty(File property) throws IOException{
		Property.loadProperty(property);
	}
	
	/**
	 * train a sentence real time
	 * 返回训练好的doc的序号，根据这个序号可以来计算相似度
	 * 
	 * @param string[] 文档分词后
	 * @throws IOException
	 */
	public int trainSentence(String[] strs) throws IOException {
		long nextRandom = Property.nextRandom;
		int wordCount = Property.wordCount;
		int lastWordCount = Property.lastWordCount;
		int wordCountActual = Property.wordCountActual;
		int sent_no = Property.sent_no + 1;
		trainWordsCount = Property.trainWordsCount;
		
		float[] vector = new float[layerSize];
		Random random = new Random();
		for (int i = 0; i < vector.length; i++)
			vector[i] = (float) ((random.nextDouble() - 0.5) / layerSize);
		doc_vector.put(sent_no, vector);
		
		if (wordCount - lastWordCount > 10000) {
			System.out.println("alpha:"
					+ alpha
					+ "\tProgress: "
					+ (int) (wordCountActual
							/ (double) (trainWordsCount + 1) * 100)
					+ "%");
			wordCountActual += wordCount - lastWordCount;
			lastWordCount = wordCount;
			alpha = startingAlpha
					* (1 - wordCountActual
							/ (double) (trainWordsCount + 1));
			if (alpha < startingAlpha * 0.0001) {
				alpha = startingAlpha * 0.0001;
			}
		}
		wordCount += strs.length;
		List<WordNeuron> sentence = new ArrayList<WordNeuron>();
		for (int i = 0; i < strs.length; i++) {
			Neuron entry = wordMap.get(strs[i]);
			if (entry == null) {
				continue;
			}
			if (sample > 0) {
				double ran = (Math.sqrt(entry.freq
						/ (sample * trainWordsCount)) + 1)
						* (sample * trainWordsCount) / entry.freq;
				nextRandom = nextRandom * 25214903917L + 11;
				if (ran < (nextRandom & 0xFFFF) / (double) 65536) {
					continue;
				}
			}
			sentence.add((WordNeuron) entry);
		}

		for (int index = 0; index < sentence.size(); index++) {
			nextRandom = nextRandom * 25214903917L + 11;
			if (isCbow) {
				cbowGram(index, sent_no, sentence, (int) nextRandom
						% window);
			} else {
				skipGram(index, sent_no, sentence, (int) nextRandom
						% window);
			}
		}
		
		return sent_no;
	}

	/**
	 * loadContext 还原上下文
	 * @throws IOException, ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void loadContext(String context) throws IOException, ClassNotFoundException{
		File file = new File(context + "\\word_map.object");
		FileInputStream fs = new FileInputStream(file);
		ObjectInputStream os = new ObjectInputStream(fs);
		wordMap = (Map<String, Neuron>) os.readObject();
		os.close();
		
		file = new File(context + "\\doc_vector.object");
		fs = new FileInputStream(file);
		os = new ObjectInputStream(fs);
		doc_vector = (Map<Integer, float[]>) os.readObject();
		os.close();
		
		new Haffman(layerSize).make(wordMap.values());

		for (Neuron neuron : wordMap.values()) {
			((WordNeuron) neuron).makeNeurons();
		}
	}
	
	/**
	 * loadContext 保存上下文
	 * @throws IOException 
	 */
	public void saveContext(String context) throws IOException{
		System.out.println("saving the context");
		File file = new File(context + "\\word_map.object");
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream fs = new FileOutputStream(file);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(wordMap);
		os.close();
		
		file = new File(context + "\\doc_vector.object");
		if(!file.exists()){
			file.createNewFile();
		}
		fs = new FileOutputStream(file);
		os = new ObjectOutputStream(fs);
		os.writeObject(doc_vector);
		os.close();
		System.out.println("saving context over");
	}
	
	/**
	 * 计算top n相似度
	 * @param queryWord
	 * @return
	 */
	public Set<WordEntry> distance(Integer queryIndex, int topNSize) {

		float[] center = doc_vector.get(queryIndex);
		if (center == null) {
			return Collections.emptySet();
		}

		int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
		TreeSet<WordEntry> result = new TreeSet<WordEntry>();

		double norm = 0;
		for (int i = 0; i < center.length; i++) {
			norm += center[i] * center[i];
		}
		norm = Math.sqrt(norm);

		double min = Float.MIN_VALUE;

		for (Map.Entry<Integer, float[]> entry : doc_vector.entrySet()) {
			
//			if (entry.getKey() < boudery) {
//				System.out.println(entry.getKey());
				float[] vector = entry.getValue();
				float dist = 0;
				for (int i = 0; i < vector.length; i++) {
					dist += center[i] * vector[i];
				}
				double norm1 = 0;
				for (int i = 0; i < vector.length; i++) {

					norm1 += vector[i] * vector[i];
				}
				norm1 = Math.sqrt(norm1);
				dist = (float) (dist / (norm * norm1));
//				if (entry.getKey() == 775203) {
//					StringBuilder vec = new StringBuilder();
//					for (int j = 0; j <vector.length; j++) {
//						vec.append(vector[j] + " ");
//					}
//					System.out.println(vec.toString().trim());
//					System.out.println(dist);
//					
//					float[] vector1 = doc_vector.get(775203);
//					StringBuilder vec1 = new StringBuilder();
//					for (int j = 0; j <vector1.length; j++) {
//						vec1.append(vector1[j] + " ");
//					}
//					System.out.println(vec1.toString().trim());
//					float dist1 = 0;
//					for (int i = 0; i < vector.length; i++) {
//						dist1 += center[i] * vector[i];
//					}
//					double norm2 = 0;
//					for (int i = 0; i < vector1.length; i++) {
//
//						norm2 += vector1[i] * vector1[i];
//					}
//					norm2 = Math.sqrt(norm2);
//					dist1 = (float) (dist1 / (norm * norm2));
//					System.out.println(dist1);
//				}
					
					
				if (dist > min) {
					result.add(new WordEntry(String.valueOf(entry.getKey()), dist));
					if (resultSize < result.size()) {
						result.pollLast();
					}
					min = result.last().score;
				}
//			}
			
		}
//		result.pollFirst();
	
			
		
		return result;
	}
	
	public Set<WordEntry> distance(String word, int topNSize) {
		WordNeuron neuron = ((WordNeuron) wordMap.get(word));
		if(neuron == null) return null;
		double[] center = neuron.syn0;
		if (center == null) {
			return Collections.emptySet();
		}
		
		StringBuilder vec = new StringBuilder();
		for (int j = 0; j <center.length; j++) {
			vec.append(center[j] + " ");
		}
		System.out.println(vec.toString().trim());

		int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
		TreeSet<WordEntry> result = new TreeSet<WordEntry>();

		double norm = 0;
		for (int i = 0; i < center.length; i++) {
			norm += center[i] * center[i];
		}
		norm = Math.sqrt(norm);

		double min = Float.MIN_VALUE;

		for (Map.Entry<String, Neuron> entry : wordMap.entrySet()) {
			double[] vector = ((WordNeuron)entry.getValue()).syn0;
			float dist = 0;
			for (int i = 0; i < vector.length; i++) {
				dist += center[i] * vector[i];
			}
			double norm1 = 0;
			for (int i = 0; i < vector.length; i++) {

				norm1 += vector[i] * vector[i];
			}
			norm1 = Math.sqrt(norm1);
			dist = (float) (dist / (norm * norm1));
			if (dist > min) {
				result.add(new WordEntry(String.valueOf(entry.getKey()), dist));
				if (resultSize < result.size()) {
					result.pollLast();
				}
				min = result.last().score;
			}
		}
//		result.pollFirst();

		return result;
	}
	
	public float[] getd2vec(int docid) {
		float[] center = doc_vector.get(docid);
		return center;
	}
	
	public double[] getw2vec(String word) {
		WordNeuron neuron = ((WordNeuron) wordMap.get(word));
		if(neuron == null) return null;
		double[] center = neuron.syn0;
		if (center == null) {
			return null;
		}
		else return center;
	}
	
	/**
	 * 打印结果
	 * @param result
	 */
	public void printRet(Set<WordEntry> result, ArrayList<String> doc, int mode){
		if(result == null) return;
		long start = System.currentTimeMillis();
		Object[] list = result.toArray();
		for (int i = 0; i < list.length; i++)
		{
			if(mode == 2){//doc2vec
				System.out.println(list[i].toString() + ":" + doc.get(Integer.valueOf(list[i].toString().split("\t")[0])));
//				System.out.println(list[i].toString());
			}else if (mode == 1){//word2vec
				System.out.println(list[i].toString());
			}
		}
		
		System.out.println(System.currentTimeMillis() - start);
	}
	
	/**
	 * 内部类，保存配置信息
	 *
	 */
	public static class Property {
		
		public static long nextRandom;
		public static int wordCount;
		public static int lastWordCount;
		public static int wordCountActual;
		public static int trainWordsCount;
		public static int sent_no;
		
		public static void loadProperty(File property) throws IOException{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(property)));
			nextRandom = Long.valueOf(br.readLine().substring("nextRandom:".length()));
			wordCount = Integer.valueOf(br.readLine().substring("wordCount:".length()));
			lastWordCount = Integer.valueOf(br.readLine().substring("lastWordCount:".length()));
			wordCountActual = Integer.valueOf(br.readLine().substring("wordCountActual:".length()));
			trainWordsCount = Integer.valueOf(br.readLine().substring("trainWordsCount:".length()));
			sent_no = Integer.valueOf(br.readLine().substring("sent_no:".length()));
			br.close();
		}
	}
}
