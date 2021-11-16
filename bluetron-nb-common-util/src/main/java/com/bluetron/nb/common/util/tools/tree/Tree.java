package com.bluetron.nb.common.util.tools.tree;

import lombok.Data;

import java.util.List;

/**
 * @className: Tree
 * @Description: 树的常见操作
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/10/20 9:29
 */
@Data
public class Tree<T extends Tree> {
    /**
     * id
     */

    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父id ，根节点为-1
     */
    private Integer parentId;
    /**
     * 子节点信息
     */
    private List<T> childList;

    public Tree(Integer id, String name, Integer parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Tree(Integer id, String name, Integer parentId, List<T> childList) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.childList = childList;
    }

}


