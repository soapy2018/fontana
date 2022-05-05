package com.fontana.util.tools.tree;

import com.fontana.util.json.JacksonMapperUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TreeUtilTest {

    @Test
    public void testGenerateTree() {
        //模拟从数据库查询出来
        List<Tree> nodeList = Lists.newArrayList(//)Arrays.asList(
                new Tree(1, "根节点", -1),
                new Tree(2, "子节点1", 1),
                new Tree(3, "子节点1.1", 2),
                new Tree(4, "子节点1.2", 2),
                new Tree(5, "根节点1.3", 2),
                new Tree(6, "根节点2", 1),
                new Tree(7, "根节点2.1", 6),
                new Tree(8, "根节点2.2", 6),
                new Tree(9, "根节点2.2.1", 7),
                new Tree(10, "根节点2.2.2", 7),
                new Tree(11, "根节点3", 1),
                new Tree(12, "根节点3.1", 11)
        );

        List<Tree> tree = TreeUtil.generateTree(nodeList, -1);
        log.info("生成的树：" + JacksonMapperUtil.toJson(tree));
        assertThat(((Tree) tree.get(0).getChildList().get(0)).getId()).isEqualTo(2);

        log.info("平铺树：" + JacksonMapperUtil.toJson(TreeUtil.tileTree(tree)));

        Map<Integer, Tree> nodesMap = Maps.newHashMap();
        TreeUtil.traversing(tree, (x) -> nodesMap.put(x.getId(), x));
        log.info("操作数：" + JacksonMapperUtil.toJson(nodesMap));

        log.info(JacksonMapperUtil.toJson(TreeUtil.generateTree(nodeList, -1)));
    }


    @Test
    public void testGenerateTree2() {
        //模拟从数据库查询出来
        List<Menu> nodeList = Lists.newArrayList(//)Arrays.asList(
                new Menu(1, "根节点", -1, "菜单"),
                new Menu(2, "子节点1", 1, "菜单"),
                new Menu(3, "子节点1.1", 2, "菜单"),
                new Menu(4, "子节点1.2", 2, "菜单"),
                new Menu(5, "根节点1.3", 2, "菜单"),
                new Menu(6, "根节点2", 1, "菜单"),
                new Menu(7, "根节点2.1", 6, "菜单"),
                new Menu(8, "根节点2.2", 6, "菜单"),
                new Menu(9, "根节点2.2.1", 7, "菜单"),
                new Menu(10, "根节点2.2.2", 7, "菜单"),
                new Menu(11, "根节点3", 1, "菜单"),
                new Menu(12, "根节点3.1", 11, "菜单")
        );

        List<Menu> menu = TreeUtil.generateTree(nodeList, -1);
        log.info("生成的树：" + JacksonMapperUtil.toJson(menu));
        assertThat((menu.get(0).getChildList().get(0)).getId()).isEqualTo(2);

        log.info("平铺树：" + JacksonMapperUtil.toJson(TreeUtil.tileTree(menu)));

        Map<Integer, Menu> nodesMap = Maps.newHashMap();
        TreeUtil.traversing(menu, (x) -> nodesMap.put(x.getId(), x));
        log.info("操作数：" + JacksonMapperUtil.toJson(nodesMap));

    }

}
