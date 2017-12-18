import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ruleClass {

	List<String> bList;
	List<String> hList;
	List<String> comp;
	int conf;
	int bSize;
	int hSize;
	int totalSize;
	
	
	public ruleClass(List<String> bList,List<String> hList){
		
		this.bList = bList;
		this.hList = hList;
		this.conf = genSubset.checkConf(bList,hList,true);
		this.bSize = bList.size();
		this.hSize = hList.size();
		this.totalSize = bSize + hSize;
		Set<String> set = new HashSet<>();
		set.addAll(bList);
		set.addAll(hList);
		this.comp = new ArrayList<>(set);
		
		
	}
	
	@Override
	public String toString() {
		
		return bList + " ==> " + hList;
	}
}
