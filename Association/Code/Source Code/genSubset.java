import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class genSubset {
	
	static int count = 0;

	public List<List<String>> genSubset(List<List<String>> list) throws IOException{
		
	List<List<String>> result = new ArrayList<>();
	Set<List<String>> s = new HashSet<>();

	
	for(List<String> li : list){
		
//		List<List<String>> temp;
		List<List<String>> temp = new ArrayList<>(genSub(li));
//		if(li.size()>1){
//		temp = genSub(li);
//		}
//		else{
//		 temp = new ArrayList<>();	
//		}
//		temp.add(li);
		for(List<String> li2 : temp){
			if((li2.size()>0) && (li2.size()<li.size())){
//			if(li2.size()==1)continue;
//			Collections.sort(li2);
//			if(li!=null && li2!=null && (li2!=li))
			genRule(li2,li);
			}
		}
//		result.addAll(genSub(li));
//		Set<List<String>> s = new HashSet<>();
		s.addAll(temp);
//		result.addAll(s);
		
	}
	
	
	return (new ArrayList<>(s));
//	return result;
	
	
		
		
	}
	
	public List<List<String>> genSub(List<String> li){
		
		  List<List<String>> res = new ArrayList<>();
	        
	        for(String s : li){
	            List<List<String>> temp = new ArrayList<>();
	            
	            for(List<String> a : res){
//	                Collections.sort(a);
	            	temp.add(new ArrayList<>(a));
	            }
	            
	            for(List<String> a : temp){
//	                Collections.sort(a);
	                a.add(s);
	            }
	            
	            List<String> sin = new ArrayList<>();
	            sin.add(s);
	            temp.add(sin);
	            
//	            Collections.sort(temp);
	            res.addAll(temp);
	        }
	        
	        res.remove(li);
//	        res.add(new ArrayList<>());
	        
	        return res;
		
		
		
		
	}
	
	

	
	public void genRule(List<String> bodyList, List<String> freqItem) throws IOException{
		List<String> headList = new ArrayList<String>();
		for(String st : freqItem){
			if( !bodyList.contains(st) )
				headList.add(st);
		}
		
		
		boolean flag = checkConf(bodyList,headList);
		if(flag){
//		count++;
			
			Main.allRules.add(new ruleClass(bodyList,headList));
			FileWriter fileWriter1 = new FileWriter("rules.txt",true);
			BufferedWriter bw = new BufferedWriter(fileWriter1);
			
			bw.write(bodyList + " --> "+ headList);
			bw.newLine();
//			fileWriter1.append("Rule : "+bodyList + " --> "+ headList);
//			System.out.println("Rule : "+bodyList + " --> "+ headList);
			bw.close();
			fileWriter1.close();
		}
		
		
		
//		FileWriter fileWriter1 = new FileWriter("rules.txt",true);
//		BufferedWriter bw = new BufferedWriter(fileWriter1);
//		
//		bw.write("Rule : "+bodyList + " --> "+ headList);
//		bw.newLine();
////		fileWriter1.append("Rule : "+bodyList + " --> "+ headList);
////		System.out.println("Rule : "+bodyList + " --> "+ headList);
//		bw.close();
//		fileWriter1.close();
	}
	
	public boolean checkConf(List<String> body, List<String> head){
		
		int conf = Main.confidence;
//		System.out.println("Body is "+body);
		
		
		List<String> lcopy = new ArrayList<>(body);
		  Collections.sort(lcopy);
		  StringBuilder sb = new StringBuilder();
		  for(String c : lcopy){
			 sb.append(c + " ");
		  }
		  int sup1 = Main.cMap.get(sb.toString());
		
		
		Set<String> set = new HashSet<>();
		set.addAll(body);
		set.addAll(head);
		List<String> union = new ArrayList<>(set);
		List<String> lcopy2 = new ArrayList<>(union);
		  Collections.sort(lcopy2);
		  StringBuilder sb2 = new StringBuilder();
		  for(String c : lcopy2){
			 sb2.append(c + " ");
		  }
		
//		if(Main.cMap.containsKey(union)){
//		int sup2 = Main.cMap.get(union);
		  
		  int sup2 = Main.cMap.get(sb2.toString());
		  
		  
		
		if(sup1!=0){
			double confidence = (double) sup2 / (double) sup1;
			confidence *=100;
			if(confidence>=conf){
				return true;
			}
		}
//		}
		
		return false;
	}
	
	
public static int checkConf(List<String> body, List<String> head, boolean check){
		
		int conf = Main.confidence;
//		System.out.println("Body is "+body);
		
		
		List<String> lcopy = new ArrayList<>(body);
		  Collections.sort(lcopy);
		  StringBuilder sb = new StringBuilder();
		  for(String c : lcopy){
			 sb.append(c + " ");
		  }
		  int sup1 = Main.cMap.get(sb.toString());
		
		
		Set<String> set = new HashSet<>();
		set.addAll(body);
		set.addAll(head);
		List<String> union = new ArrayList<>(set);
		List<String> lcopy2 = new ArrayList<>(union);
		  Collections.sort(lcopy2);
		  StringBuilder sb2 = new StringBuilder();
		  for(String c : lcopy2){
			 sb2.append(c + " ");
		  }
		
//		if(Main.cMap.containsKey(union)){
//		int sup2 = Main.cMap.get(union);
		  
		  int sup2 = Main.cMap.get(sb2.toString());
		  
		  
		
		if(sup1!=0){
			double confidence = (double) sup2 / (double) sup1;
			confidence *=100;
			if(confidence>=conf){
//				return true;
				return (int) confidence;
			}
		}
//		}
		return -1;
		
//		return false;
	}
	
}
