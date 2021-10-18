package com.bluetron.nb.common.util.tools.tree;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestTreeNode implements TreeNode<String> {

	private String id;
	 
    private String parentId;
 
    private String name;
 
    private List<TestTreeNode> children = Lists.newArrayList();
    
    public TestTreeNode(String id , String name , String parentId) {
    	this.id = id;
    	this.name = name;
    	this.parentId = parentId;
    }


	@Override
	public List<? extends TreeNode<String>> getChildren() {
		return children;
	}


	@Override
	public void addChildren(TreeNode<String> node) {
		TestTreeNode thisnode = (TestTreeNode)node;
		children.add(thisnode);
	}
	

}
