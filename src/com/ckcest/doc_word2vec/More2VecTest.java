package com.ckcest.doc_word2vec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import com.ansj.vec.Learn;
import com.ansj.vec.LearnMoreVec;
import com.ansj.vec.domain.Neuron;

public class More2VecTest {
	
	public static final String trainPath = "file/catalog_alllever_seg_v2";
	public static final String contextDir = "doc_word2vec_model_v2";
	public static final String configPath = contextDir + "/doc2vec.property";
	public static ArrayList<String> Doc = new ArrayList<>();

	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		train(); //训练模型，假如已经有上下文和配置文件了就不需要再训练
		work(); //计算新doc
	} 
	
	public static void train() throws IOException{
		File result = new File(trainPath);
		Learn learn = new Learn();
		learn.learnFile(result);
		Map<String, Neuron> word2vec_model = learn.getWord2VecModel();
		LearnMoreVec learn_doc = new LearnMoreVec(word2vec_model);
		learn_doc.learnFile(result, new File(configPath));
		learn_doc.saveContext(contextDir);
	}
	
	public static void work() throws IOException, ClassNotFoundException{
		LearnMoreVec learn_doc = new LearnMoreVec(null);
		learn_doc.loadProperty(new File(configPath));
		learn_doc.loadContext(contextDir);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(trainPath),"utf-8"));
		String line = null;
   	 	while((line = reader.readLine()) != null)
   	 	{
   	 		Doc.add(line);
   	 	}
   	 	reader.close();
		System.out.println("ready");
		while(true){
			System.out.println("w-xxx or d-xxx:");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String input = in.readLine();
			if(input.startsWith("d-")){
				input = input.substring(2);
				String[] doc = input.split(" ");
				int docId = Doc.size() + 1;
//				int docId = Integer.valueOf(input);
				docId = getdocId(input, Doc);
				System.out.println(docId);
				if (docId == Doc.size() + 1)//语料库中不存在该句子
				{
					docId = learn_doc.trainSentence(doc);//将新的sentence添加进语料库，训练新sentence的Doc2vec
					float[] vec = learn_doc.getDocVec().get(docId);
					
					for(float v : vec){
						System.out.print(v + ",");
					}
					String str = "";
					for (int i = 0; i < doc.length; i++) str += doc[i];
					Doc.add(str);
				}
				System.out.println();
				learn_doc.printRet(learn_doc.distance(docId, 10), Doc, 2);
			}else if(input.startsWith("w-")){
				input = input.substring(2);
				learn_doc.printRet(learn_doc.distance(input, 10), Doc, 1);	
			}
		}
	}
	
	public double getSim(String v1, String v2){
		String[] vs1 = v1.split(",");
		String[] vs2 = v2.split(",");
		double sim = 0;
		double a1 = 0;
		double a2 = 0;
		for(int i = 0; i < vs1.length; i++){
			float x1 = Float.valueOf(vs1[i]);
			float x2 = Float.valueOf(vs2[i]);
			a1 += (x1 * x1);
			a2 += (x2 * x2);
			sim += (x1 * x2);
		}
		sim = sim / (Math.sqrt(a1) * Math.sqrt(a2));
		return sim;
	}
	
	public static int getdocId(String doc, ArrayList<String> Doc) {
		int id = 0;
		for (int i = 0; i < Doc.size(); i++) {
			if (doc.equals(Doc.get(i))) {
				id = i;
				break;
			}
		}
		return id;
	}
}
