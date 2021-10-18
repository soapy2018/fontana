package com.bluetron.nb.common.util.tools.tree;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

/**
 *
 * @author wuwenli
 *
 */
@SuppressWarnings("all")
public class TreeUtil {

	/**
	 * 根据所有树节点列表，生成含有所有树形结构的列表
	 * @param <T>
	 * @param nodes 所有节点List
	 * @param rootId 父节点Id
	 * @return
	 */
	public static <T extends TreeNode<?>> List<T> generateTrees(List<T> nodes , Serializable rootId) {
		
		// 所有首层节点
		List<T> roots = Lists.newArrayList();
		
		for (Iterator<T> ite = nodes.iterator(); ite.hasNext();) {
			
			T node = ite.next();
			
			if (node.getParentId().equals(rootId)) {
				roots.add(node);
				// 从所有节点列表中删除该节点，以免后续重复遍历该节点
				ite.remove();
			}
		}
		
		roots.forEach(r -> {
			setChildren(r, nodes);
		});
		return roots;
	}

	/**
	 * 从所有节点列表中查找并设置parent的所有子节点
	 *
	 * @param parent 父节点
	 * @param nodes  所有树节点列表
	 */
	private static <T extends TreeNode> void setChildren(T parent, List<T> nodes) {
		Object parentId = parent.getId();
		
		for (Iterator<T> ite = nodes.iterator(); ite.hasNext();) {
			T node = ite.next();
			if (parentId.equals(node.getParentId())) {
				parent.addChildren(node);
				// 从所有节点列表中删除该节点，以免后续重复遍历该节点
				ite.remove();
			}
		}

		parent.getChildren().forEach( m -> {
			// 递归设置子节点
			setChildren( (T) m, nodes);
		});
	}
	
	///////////////////////////////// 树的两个常用操作
	
	/**
     * 通过栈遍历树结构
     *
     * @param root              节点树结构
     * @param loadChildrenNodes 加载树的子节点列表函数 接收一个节点 返回节点的子结构
     * @param behavior          遍历到的节点行为
     * @param <T>               树节点对象
     */
    public static <T extends TreeNode> void traversing(List<T> root, Consumer<T> behavior) {
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T node = stack.pop();
            behavior.accept(node);
            if (hasChildren(node) ) {
            	List<T> childrens = node.getChildren();
                childrens.forEach(stack::push);
            }
        }
    }
    
    /**
     * 平铺树结构
     *
     * @param root              节点树结构
     * @param <T>               树节点对象
     * @return 平铺结构
     */
    public static <T extends TreeNode> List<T> tileTree(List<T> root) {
        List<T> list = new ArrayList<>();
        traversing(root, list::add);
        return list;
    }
    
	private static boolean hasChildren(TreeNode treeNode) {
		List<TreeNode> childres = treeNode.getChildren();
		return childres != null && childres.size() > 0 ;
	}

}
