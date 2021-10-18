package com.bluetron.nb.common.util.tools.tree;

import java.io.Serializable;
import java.util.List;

public interface TreeNode<PK extends Serializable> {

	/**
	 * 获取节点id
	 *
	 * @return 树节点id
	 */
	PK getId();

	/**
	 * 获取该节点的父节点id
	 *
	 * @return 父节点id
	 */
	PK getParentId();
	
	/**
	 * 放入儿子列表内
	 * @param node
	 */
	public void addChildren(TreeNode<PK> node);

	/**
	 * 获取所有子节点
	 *
	 * @return 子节点列表
	 */
	List<? extends TreeNode<PK>> getChildren();
	
}
