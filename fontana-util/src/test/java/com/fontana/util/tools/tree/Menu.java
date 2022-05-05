package com.fontana.util.tools.tree;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @className: menu
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/10/20 9:59
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Menu extends Tree<Menu> {

    String description;

    public Menu(Integer id, String name, Integer parentId, String description) {
        super(id, name, parentId);
        this.description = description;
    }

}


