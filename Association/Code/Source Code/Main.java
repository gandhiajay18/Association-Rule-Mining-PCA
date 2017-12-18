import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
	static List<List<String>> rawData;
	static List<List<String>> pData;
	static Map<String,Integer> cMap;
	static int support;
	static int confidence;
	static List<ruleClass> allRules;
	static List<String> sampleQueries;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		
		genSubset gs = new genSubset();
		
		
		int rlen = 0; int clen = 0;
		
		rawData = new ArrayList<>();
		Scanner s = new Scanner(System.in);
		
		System.out.println("Please Enter Support Value");
		String input = s.nextLine();
		support = Integer.parseInt(input);
		System.out.println("Please Enter Confidence");
		String in2 = s.nextLine();
		confidence = Integer.parseInt(in2);
		System.out.println("Please Enter Filename");
		String f = s.nextLine();
		File file = new File(f);

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			
			String line;
			int c = 0;
			while((line = br.readLine())!=null){
//			while(line!=null){
//			for(int i = 0;i<100;i++){
//				String line = br.readLine();
//				if(line==null)
//					continue;
//					rlen++;
				c++;
//				System.out.println(c);
				List<String> list = new ArrayList<>();
				
				String[] col = line.split("\t");
				for(String st : col){
					list.add(st);
				}
				rawData.add(list);
//				clen = col.length;
				}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		System.out.println(rawData.toString());
		cMap = new HashMap<>();
		Map<Integer,List<List<String>>> lMap = new HashMap<>();
//		List<List<String>> pData = new ArrayList<>();
		 pData = new ArrayList<>();
		
		for(List<String> l : rawData){
			int ind = 0;
			List<String> sinList = new ArrayList<>();
			for(String st : l ){
			    ind++;
				if(st.startsWith("U")) 
					sinList.add("G"+ind+"_Up");
				else if(st.startsWith("D"))
					sinList.add("G"+ind+"_Down");
				else 
					sinList.add(st);
				
			}
			if(!sinList.isEmpty())
			pData.add(sinList);			
			
		}
		
		
		
		Map<String,Integer> map = new HashMap<>();
		Map<String,Integer> oneFreq = new HashMap<>();
		Set<String> oneSet = new HashSet<>();
		List<List<String>> listOne = new ArrayList<>();
		Map<String,Double> allFreq= new HashMap<>();
		
		for(int j = 0;j<pData.get(0).size();j++){
			map.clear();
			
		for(int i = 0;i<pData.size();i++){
			map.put(pData.get(i).get(j), map.getOrDefault(pData.get(i).get(j), 0)+1);			
//			System.out.println("String is: "+pData.get(i).get(j) + "  Value is : "+map.get(pData.get(i).get(j)));
		}
//		System.out.println(map.toString());
		Iterator it = map.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry<String, Integer> set = (Map.Entry<String, Integer>) it.next();
			String key = set.getKey();
			int count = set.getValue();
			
			double supp = (double) count / (double) pData.size();
			supp = supp*100;
			if(supp>=support){
				oneSet.add(key);
				List<String> l = new ArrayList<>();
				l.add(key);
				listOne.add(l);
				allFreq.put(key, supp);
				cMap.put(key+" ", (int)supp);
			}
			
		}
		
		}
		
		
		lMap.put(1,listOne);
//		System.out.println(lMap.toString());
		
		//Using listOne, create 2nd candidate list Ck by using Ck-1.
		int ind = 1;
		while(true){
			ind++;
			List<List<String>> cand = genCand(lMap,ind);
			
			
			cand = prune(cand,ind);
			
			if(cand.size()==0){
				break;
			}
			lMap.put(ind, cand);
			
		}
		
		allRules = new ArrayList<>();
		
		FileWriter fileWriter1 = new FileWriter("rules.txt");
		fileWriter1.close();
		
		
//		FileWriter fq = new FileWriter("cMap.txt");
//		fq.write("keySet is " +cMap.keySet());
//		fq.close();
//		System.out.println(lMap.size());
		Writer fileWriter = null;
		BufferedWriter bw = null;

		try {
			 Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		        System.out.println( sdf.format(cal.getTime()) );
			fileWriter = new FileWriter("FreqSets_supp"+support+"_conf"+confidence+".txt");
			bw = new BufferedWriter(fileWriter);
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
//		List<List<String>> ret1 = gs.genSubset(lMap);

		int totalc = 0;
		for(int i : lMap.keySet())
		{
			totalc += lMap.get(i).size();
			List<List<String>> li = lMap.get(i);
			try {
				bw.write("Number of Frequent Sets of length: " + i + " are "+li.size()+"\n");
				System.out.println("Number of Frequent Sets of length: " + i + " are "+li.size()+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			if(i>1){
			List<List<String>> ret1 = gs.genSubset(li);
//			}
			
//			for(List<String> lis : li){
////				try {
////					bw.write("Frequent Set :"+lis+"\n");
////					
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
//				
////				fileWriter.write(System.getProperty( "line.separator" ));
//
//			}
		}
		bw.write("Total Number of frequent itemsets for Support: "+support + " is "+totalc);
		System.out.println("Total Number of frequent itemsets for Support: "+support + " is "+totalc);
		
		try {
			bw.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sampleQueries = new ArrayList<>();
		sampleQueries.add("RULE HAS ANY OF (G59_Up)");
		sampleQueries.add("RULE HAS NONE OF (G59_Up)");
		sampleQueries.add("RULE HAS 1 OF (G59_Up,G10_Down)");
		sampleQueries.add("BODY HAS ANY OF (G59_Up)");
		sampleQueries.add("BODY HAS NONE OF (G59_Up)");
		sampleQueries.add("BODY HAS 1 OF (G59_Up, G10_Down)");
		sampleQueries.add("HEAD HAS ANY OF (G59_Up)");
		sampleQueries.add("HEAD HAS NONE OF (G59_Up)");
		sampleQueries.add("HEAD HAS 1 OF (G59_Up, G10_Down)");
		sampleQueries.add("SizeOf(RULE) >= 3");
		sampleQueries.add("SizeOf(BODY) >= 2");
		sampleQueries.add("SizeOf(HEAD) >= 1");
		sampleQueries.add("BODY HAS ANY OF (G10_Down) OR HEAD HAS 1 OF (G59_Up)");
		sampleQueries.add("BODY HAS ANY OF (G10_Down) AND HEAD HAS 1 OF (G59_Up)");
		sampleQueries.add("BODY HAS ANY OF (G10_Down) OR SizeOf(HEAD) >= 2");
		sampleQueries.add("BODY HAS ANY OF (G10_Down) AND SizeOf(HEAD) >= 2");
		sampleQueries.add("SizeOf(BODY) >= 1 OR SizeOf(HEAD) >= 2");
		sampleQueries.add("SizeOf(BODY) >= 1 AND SizeOf(HEAD) >= 2");
		
	

		
		System.out.println("Select further options: ");
		System.out.println("Enter D to display all rules");
		System.out.println("Enter S to print result of sample queries");
		System.out.println("Enter Q to enter custom query");
		System.out.println("Enter X to Exit");
		
		String option = "";
		
		while(!option.toLowerCase().equals("x")){
			option = s.nextLine();
//		switch(option.toLowerCase()){
//		case "d" :
			if(option.toLowerCase().equals("d")){
			int c = 0;
			for(ruleClass r : allRules){
				c++;
				System.out.println("Rule " + c + " : "+r.toString());
			}
			}
//		
			if(option.toLowerCase().equals("s")){
			printSample();
//			System.out.println("TEST");
			}
			if(option.toLowerCase().equals("q")){
			System.out.println("Enter your query in one of the following formats:");
			System.out.println("Template 1: RULE|BODY|HEAD HAS ANY|NUMBER|NONE OF (ITEM1, ITEM2, ..., ITEMn)");
			System.out.println("Template 2: SizeOf(BODY|HEAD|RULE) ≥ NUMBER");
			System.out.println("Template 3: Any combined templates using AND or OR. For example: " + 
			"\n" + "HEAD|BODY|RULE HAS ANY|NONE|NUMBER OF (ITEM1,ITEM2,...ITEMn) AND BODY HAS NONE OF (ITEM1,ITEM2,...ITEMn)");
//
			String tString = s.nextLine();
			
			Set<ruleClass> res = procTemp(tString);
			
//			if(res.size()!=0){
//				System.out.println("The total number of rules obtained for given query are "+res.size());
//				int c = 0;
//				for(ruleClass r : res){
//					c++;
//					System.out.println("Rule "+ c+" :"+r.toString());
//					}
//				
//			}
			
			
			 Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			FileWriter fileWriter = new FileWriter("SampleQueryOutput"+support+"_conf"+confidence+"_"+sdf.format(cal.getTime())+".txt");
		    FileWriter qWriter = new FileWriter("QueryOutput"+sdf.format(cal.getTime())+".txt");

		        BufferedWriter qbw = new BufferedWriter(qWriter);
				if(res.size()!=0){
					qbw.write("Query: "+tString+"\n");
					System.out.println("Rules for Query: "+tString);
					int c = 0;
					for(ruleClass r : res){
						c++;
						qbw.write("Rule "+ c+" :"+r.toString()+"\n");
						System.out.println("Rule "+ c+" :"+r.toString());
						}
					qbw.write("Total Count of Rules := "+c + "\n");
					System.out.println("Total Count of Rules := "+c);
					System.out.println("\n");
				}else{
					qbw.write("No matching rules found for Query : "+tString+"\n");
					System.out.println("No matching rules found for Query : "+tString);
					
				}
				qbw.write("\n");
				qbw.write("\n");

			
			qbw.close(); qWriter.close();
			
			
//			int template = Integer.parseInt(s.nextLine());
//			
//			if(template==1){
//				System.out.println("You have chosen Template 1: Please enter your 1st Parameter, choose from RULE, BODY, HEAD");
//				String p1 = s.nextLine();
//				System.out.println("Please Enter 2nd Parameter, choose from ANY, NONE and 1");
//				String p2 = s.nextLine();
//				System.out.println("Please Enter 3rd Parameter, attributes, like G59_Up, G10_Down");
//				String p3 = s.nextLine();
//				
////				temp1(p1,p2,p3);
//				
//
//			}
//			if(template==2){
//				System.out.println("You have chosen Template 2: Please enter your 1st Parameter, choose from RULE, BODY, HEAD");
//				String p1 = s.nextLine();
//				System.out.println("Please Enter 2nd Parameter, the least size of rule,body or head");
//				String p2 = s.nextLine();
////				System.out.println("Please Enter 3rd Parameter, attributes, like G59_Up, G10_Down");
////				String p3 = s.nextLine();
////				temp2(p1,p2);
//				
//				
//			}
			}

			
		}
//		}
//		else{
//			System.exit(0);
//		}
		
		
		
		
		
	}
	
	public static Set<ruleClass> procTemp(String query){
		
//		Set<ruleClass> result = new HashSet<>();
		
		if(query.contains("AND")){
			return temp3(query,"AND");
		}else if(query.contains("OR")){
			return temp3(query,"OR");
		}else if(query.contains("SizeOf")){
			return temp2(query);
		} else{
			return temp1(query);
			
		}
		
//		return result;
				
		
		
	}
	
	public static Set<ruleClass> temp3(String query, String cond){
		
		String[] q = query.split(cond);
		
		Set<ruleClass> result = new HashSet<>();
		Set<ruleClass> r1 = new HashSet<>();
		Set<ruleClass> r2 = new HashSet<>();
		
		if(q[0].contains("SizeOf")){
			r1 = temp2(q[0]);
		}else{
			r1 = temp1(q[0]);
		}
		
		if(q[1].contains("SizeOf")){
			r2 = temp2(q[1]);
		}else{
			r2 = temp1(q[1]);
		}
		
		if(cond.equals("AND"))
		{
			r1.retainAll(r2);
		}else{
			r1.addAll(r2);
		}
		
		return r1;
	}
	
	public static Set<ruleClass> temp2(String query){
		
		Set<ruleClass> result = new HashSet<>();
		
		String[] q = query.split(">=");
		String param = q[0].substring(q[0].indexOf("(")+1, q[0].indexOf(")"));
		int size = Integer.parseInt(q[1].trim());
		
		for(ruleClass r : allRules){
			if (param.equals("BODY")){
				if(r.bSize>=size)
					result.add(r);				
			}
			if (param.equals("HEAD")){
				if(r.hSize>=size)
					result.add(r);				
			}
			if (param.equals("RULE")){
				if(r.totalSize>=size)
					result.add(r);				
			}
			
		}
		
		
		
		return result;
		
		
		
	}
	
	public static Set<ruleClass> temp1(String query){
		
		Set<ruleClass> result = new HashSet<>();
		
		String[] q1 = query.split("OF");
		
//		q1[1].replaceAll("\\(|\\)", "");
		q1[1] = q1[1].trim();
		q1[1] = q1[1].replaceAll("\\(|\\)", "");
		String[] att = q1[1].split(",");
		Set<String> items = new HashSet<>();
		
		for(String s : att){
			items.add(s.trim());
		}
		
		String[] rul = q1[0].split("HAS");
		
		String par1 = rul[0].trim();
		String par2 = rul[1].trim();
		
		if(par2.equals("ANY")){
			
			for(ruleClass r : allRules){
				
				for(String s : items){
					
					if (par1.equals("BODY")){
						if(r.bList.contains(s))
							result.add(r);				
					}
					if (par1.equals("HEAD")){
						if(r.hList.contains(s))
							result.add(r);				
					}
					if (par1.equals("RULE")){
						if(r.comp.contains(s))
							result.add(r);				
					}
					
					
				}
			
			}			
		}
		
if(par2.equals("NONE")){
			
			for(ruleClass r : allRules){
				int count = 0;
				for(String s : items){
					
					if (par1.equals("BODY")){
						if(r.bList.contains(s)){
//							result.add(r);	
							count++;
						break;}
					}
					if (par1.equals("HEAD")){
						if(r.hList.contains(s)){
//							result.add(r);				
							count++;
						break;}
					}
					if (par1.equals("RULE")){
						if(r.comp.contains(s)){
//							result.add(r);
							count++;
						break;
						}
					}
					
					
				}
				if(count==0)
					result.add(r);
				
			}
			
			
		}

if(par2.equals("1")){
	
	for(ruleClass r : allRules){
		int count = 0;
		for(String s : items){
			
			if (par1.equals("BODY")){
				if(r.bList.contains(s))
//					result.add(r);	
					count++;
//				break;
			}
			if (par1.equals("HEAD")){
				if(r.hList.contains(s))
//					result.add(r);				
					count++;
//				break;
			}
			if (par1.equals("RULE")){
				if(r.comp.contains(s))
//					result.add(r);
					count++;
//				break;
			}
			
			
		}
		if(count==1)
			result.add(r);
		
	}
	
	
}
		
		
		
		return result;
		
		
	}
	
	public static void printSample() throws IOException{
		 Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		FileWriter fileWriter = new FileWriter("SampleQueryOutput"+support+"_conf"+confidence+"_"+sdf.format(cal.getTime())+".txt");
	    FileWriter fileWriter = new FileWriter("SampleQueryOutput.txt");

	        BufferedWriter bw = new BufferedWriter(fileWriter);
		for(String s : sampleQueries){
			Set<ruleClass> res = procTemp(s);
			if(res.size()!=0){
				bw.write("Query: "+s+"\n");
				System.out.println("Rules for Query: "+s);
				int c = 0;
				for(ruleClass r : res){
					c++;
					bw.write("Rule "+ c+" :"+r.toString()+"\n");
					System.out.println("Rule "+ c+" :"+r.toString());
					}
				bw.write("Total Count of Rules := "+c + "\n");
				System.out.println("Total Count of Rules := "+c);
				System.out.println("\n");
			}else{
				bw.write("No matching rules found for Query : "+s+"\n");
				System.out.println("No matching rules found for Query : "+s);
				
			}
			bw.write("\n");
			bw.write("\n");

		}
		bw.close(); fileWriter.close();
		
		
	}
	
	

	private static List<List<String>> prune(List<List<String>> cand, int ind) {
		// TODO Auto-generated method stub
		List<List<String>> result = new ArrayList<>();
		
		
		
		for(List<String> list : cand){
			int count = 0;
		  for(List<String> data : pData){
			  
			  boolean flag = true;
			for(String s : list){
				if(data.contains(s)){
					continue;
				}else{
					flag = false;
					break;
				}
				
				
			}
			if(flag) count++;
			
		  }
		  
		  double tSup = (double)count /(double) pData.size();
		  tSup = tSup*100;
		  if(tSup>=support){
//			  Collections.sort(list);
			  result.add(list);
			  List<String> lcopy = new ArrayList<>(list);
			  Collections.sort(lcopy);
			  StringBuilder sb = new StringBuilder();
			  for(String c : lcopy){
				 sb.append(c + " ");
			  }
			  cMap.put(sb.toString(),(int) tSup);
		  }
			
		}
		
		return result;

		
		
	}



	public static List<List<String>> genCand(Map<Integer,List<List<String>>> map, int index){
		
		List<List<String>> result = new ArrayList<>();
		
		int prev = index - 1;
		
		int comp = index - 2;
		List<List<String>> prevList = map.get(prev);
		
	    for(int i = 0;i<prevList.size();i++){
	    	
	    	for(int j = i + 1;j<prevList.size();j++){
	    		
	    		List<String> first = prevList.get(i);
	    		List<String> second = prevList.get(j);
	    		
	    		Collections.sort(first);
	    		Collections.sort(second);
	    		Set<String> set = new HashSet<>();
	    		boolean match = true;
	    		
	    		if(index>2){
	    		//FIX THIS maybe to compare Gi_UP and Gi_DOWN etc
	    		for(int k = 0;k<comp;k++){
	    			if(first.get(k).equals(second.get(k)))
	    				continue;
	    			else{
	    				match = false;
	    				break;
	    			}
	    		}
	    		}
	    		
	    		if(match)
	    		{
	    			set.addAll(first);
	    			set.addAll(second);
	    			result.add(new ArrayList<>(set));
	    		}
	    		
	    		
	    		
	    	}
	    	
	    }
		
		
		
		return result;}

}
