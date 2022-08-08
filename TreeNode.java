package STICPinterpreter;

import java.util.LinkedList;
import java.util.List;

public class TreeNode {
	Token t;
	List<TreeNode> children = new LinkedList<>();
	
	TreeNode(Token t){
		this.t = t;
	}
}
