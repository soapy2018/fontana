/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fontana.db.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fontana.util.bean.BeanUtil;
import com.fontana.util.lang.StringUtil;
import com.fontana.util.tools.Kv;
import com.fontana.util.tools.SupUtil;

import java.util.Map;

/**
 * 分页工具
 *
 * @author cqf
 */
public class Condition {

	/**
	 * 转化成mybatis plus中的Page
	 *
	 * @param query 查询条件
	 * @return IPage
	 */
	public static <T> IPage<T> getPage(Query query) {
		Page<T> page = new Page<>(SupUtil.toInt(query.getCurrent(), 1), SupUtil.toInt(query.getSize(), 10));
		String[] ascArr = SupUtil.toStrArray(query.getAscs());
		for (String asc : ascArr) {
			page.addOrder(OrderItem.asc(StringUtil.cleanIdentifier(asc)));
		}
		String[] descArr = SupUtil.toStrArray(query.getDescs());
		for (String desc : descArr) {
			page.addOrder(OrderItem.desc(StringUtil.cleanIdentifier(desc)));
		}
		return page;
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param entity 实体
	 * @param <T>    类型
	 * @return QueryWrapper
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
		return new QueryWrapper<>(entity);
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param query 查询条件
	 * @param clazz 实体类
	 * @param <T>   类型
	 * @return QueryWrapper
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
		Kv exclude = Kv.init().set("current", "current").set("size", "size").set("ascs", "ascs").set("descs", "descs");
		return getQueryWrapper(query, exclude, clazz);
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param query   查询条件
	 * @param exclude 排除的查询条件
	 * @param clazz   实体类
	 * @param <T>     类型
	 * @return QueryWrapper
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Map<String, Object> exclude, Class<T> clazz) {
		exclude.forEach((k, v) -> query.remove(k));
		QueryWrapper<T> qw = new QueryWrapper<>();
		qw.setEntity(BeanUtil.newInstance(clazz));
		SqlKeyword.buildCondition(query, qw);
		return qw;
	}

}
