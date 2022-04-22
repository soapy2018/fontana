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
package com.bluetron.app.xxx.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bluetron.app.xxx.entity.Dept;
import com.bluetron.app.xxx.vo.DeptVO;
import com.bluetron.app.xxx.service.IDeptService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 部门表 控制器
 *
 * @author Blade
 * @since 2022-04-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@Api(value = "部门表", tags = "部门表接口")
public class DeptController extends BladeController {

	private IDeptService deptService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入dept")
	public R<Dept> detail(Dept dept) {
		Dept detail = deptService.getOne(Condition.getQueryWrapper(dept));
		return R.data(detail);
	}

	/**
	 * 分页 部门表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入dept")
	public R<IPage<Dept>> list(Dept dept, Query query) {
		IPage<Dept> pages = deptService.page(Condition.getPage(query), Condition.getQueryWrapper(dept));
		return R.data(pages);
	}

	/**
	 * 自定义分页 部门表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入dept")
	public R<IPage<DeptVO>> page(DeptVO dept, Query query) {
		IPage<DeptVO> pages = deptService.selectDeptPage(Condition.getPage(query), dept);
		return R.data(pages);
	}

	/**
	 * 新增 部门表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入dept")
	public R save(@Valid @RequestBody Dept dept) {
		return R.status(deptService.save(dept));
	}

	/**
	 * 修改 部门表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入dept")
	public R update(@Valid @RequestBody Dept dept) {
		return R.status(deptService.updateById(dept));
	}

	/**
	 * 新增或修改 部门表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入dept")
	public R submit(@Valid @RequestBody Dept dept) {
		return R.status(deptService.saveOrUpdate(dept));
	}

	
	/**
	 * 删除 部门表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(deptService.deleteLogic(Func.toLongList(ids)));
	}

	
}
