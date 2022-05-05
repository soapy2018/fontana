package com.fontana.util.tools.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author wuwenli & cqf
 */
@SuppressWarnings("all")
public class TreeUtil {


    /**
     * 递归查询子节点
     *
     * @param root 根节点
     * @param all  所有节点
     * @return 根节点信息
     */
    private static <T extends Tree> List<T> getChildrens(T root, List<T> all) {
        List<T> children = all.stream().filter(m -> {
            return Objects.equals(m.getParentId(), root.getId());
        }).map(
                (m) -> {
                    m.setChildList(getChildrens(m, all));
                    return m;
                }
        ).collect(Collectors.toList());
        return children;
    }

    /**
     * 根据所有树节点列表，生成含有所有树形结构的列表
     *
     * @param <T>
     * @param nodes  所有节点List
     * @param rootId 父节点Id
     * @return
     */
    public static <T extends Tree> List<T> generateTree(List<T> tree, Serializable rootId) {

        List<T> collect = tree.stream().filter(m -> m.getParentId() == rootId).map(
                (m) -> {
                    m.setChildList(getChildrens(m, tree));
                    return m;
                }
        ).collect(Collectors.toList());
        return collect;
    }

    private static boolean hasChildren(Tree tree) {
        List<Tree> childres = tree.getChildList();
        return childres != null && childres.size() > 0;
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
    public static <T extends Tree> void traversing(List<T> root, Consumer<T> behavior) {
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T node = stack.pop();
            behavior.accept(node);
            if (hasChildren(node)) {
                List<T> childrens = node.getChildList();
                childrens.forEach(stack::push);
            }
        }
    }

    /**
     * 平铺树结构
     *
     * @param root 节点树结构
     * @param <T>  树节点对象
     * @return 平铺结构
     */
    public static <T extends Tree> List<T> tileTree(List<T> root) {
        List<T> list = new ArrayList<>();
        traversing(root, list::add);
        return list;
    }


}
