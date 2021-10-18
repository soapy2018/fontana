package com.bluetron.nb.common.util.tools.tree;

import com.bluetron.nb.common.util.json.JacksonMapper;
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
	public void test() {

		TestTreeNode treeRoot = new TestTreeNode("0", "广东省", "-1");

		TestTreeNode treeNode1 = new TestTreeNode("1", "广州", "0");
		TestTreeNode treeNode2 = new TestTreeNode("2", "深圳", "0");

		TestTreeNode treeNode3 = new TestTreeNode("3", "天河区", "1");
		TestTreeNode treeNode4 = new TestTreeNode("4", "越秀区", "1");
		TestTreeNode treeNode5 = new TestTreeNode("5", "黄埔区", "1");
		TestTreeNode treeNode6 = new TestTreeNode("6", "石牌", "3");
		TestTreeNode treeNode7 = new TestTreeNode("7", "百脑汇", "6");

		TestTreeNode treeNode8 = new TestTreeNode("8", "南山区", "2");
		TestTreeNode treeNode9 = new TestTreeNode("9", "宝安区", "2");
		TestTreeNode treeNode10 = new TestTreeNode("10", "科技园", "8");

		List<TestTreeNode> list = Lists.newArrayList();

		list.add(treeNode10);
		list.add(treeNode6);
		list.add(treeNode7);
		list.add(treeNode8);
		list.add(treeNode9);
		list.add(treeNode3);
		list.add(treeNode4);
		list.add(treeNode5);
		list.add(treeNode1);
		list.add(treeNode2);

		List<TestTreeNode> treeList = TreeUtil.generateTrees(list, treeRoot.getId());

		log.info("{}", JacksonMapper.toJson(treeList));

		assertThat(treeList.size()).isEqualTo(2);
		assertThat(treeList.get(0).getChildren().get(0).getId()).isEqualTo("3");

		Map<String, TestTreeNode> nodesMap = Maps.newHashMap();
		TreeUtil.traversing(treeList, (x) -> nodesMap.put(x.getId(), x));

		log.info("{}", JacksonMapper.toJson(nodesMap));

	}

}
