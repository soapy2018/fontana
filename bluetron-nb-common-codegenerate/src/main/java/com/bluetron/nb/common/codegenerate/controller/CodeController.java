/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluetron.nb.common.codegenerate.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.codegenerate.entity.Code;
import com.bluetron.nb.common.codegenerate.service.ICodeService;
import com.bluetron.nb.common.codegenerate.support.BladeCodeGenerator;
import com.bluetron.nb.common.db.support.Condition;
import com.bluetron.nb.common.db.support.Query;
import com.bluetron.nb.common.util.tools.SupUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * 控制器
 *
 * @author Chill
 */
@ApiIgnore
@RestController
@AllArgsConstructor
@RequestMapping("/code")
@Api(value = "代码生成", tags = "代码生成")
public class CodeController {

	private ICodeService codeService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入code")
	public Result<Code> detail(Code code) {
		Code detail = codeService.getOne(Condition.getQueryWrapper(code));
		return Result.succeed(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "codeName", value = "模块名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "tableName", value = "表名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "modelName", value = "实体名", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入code")
	public Result<IPage<Code>> list(@ApiIgnore @RequestParam Map<String, Object> code, Query query) {
		IPage<Code> pages = codeService.page(Condition.getPage(query), Condition.getQueryWrapper(code, Code.class));
		return Result.succeed(pages);
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增或修改", notes = "传入code")
	public Result submit(@Valid @RequestBody Code code) {
		return Result.succeed(codeService.submit(code));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "删除", notes = "传入ids")
	public Result remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return Result.status(codeService.removeByIds(SupUtil.toLongList(ids)));
	}

	/**
	 * 复制
	 */
	@PostMapping("/copy")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "复制", notes = "传入id")
	public Result copy(@ApiParam(value = "主键", required = true) @RequestParam Long id) {
		Code code = codeService.getById(id);
		code.setId(null);
		code.setCodeName(code.getCodeName() + "-copy");
		return Result.status(codeService.save(code));
	}

	/**
	 * 代码生成
	 */
//	@PostMapping("/gen-code")
//	@ApiOperationSupport(order = 6)
//	@ApiOperation(value = "代码生成", notes = "传入ids")
//	public Result genCode(@ApiParam(value = "主键集合", required = true) @RequestParam String ids, @RequestParam(defaultValue = "react") String system) {
//		Collection<Code> codes = codeService.listByIds(SupUtil.toLongList(ids));
//		codes.forEach(code -> {
//			BladeCodeGenerator generator = new BladeCodeGenerator();
//			// 设置数据源
//			Datasource datasource = datasourceService.getById(code.getDatasourceId());
//			generator.setDriverName(datasource.getDriverClass());
//			generator.setUrl(datasource.getUrl());
//			generator.setUsername(datasource.getUsername());
//			generator.setPassword(datasource.getPassword());
//			// 设置基础配置
//			generator.setSystemName(system);
//			generator.setServiceName(code.getServiceName());
//			generator.setPackageName(code.getPackageName());
//			generator.setPackageDir(code.getApiPath());
//			generator.setPackageWebDir(code.getWebPath());
//			generator.setTablePrefix(SupUtil.toStrArray(code.getTablePrefix()));
//			generator.setIncludeTables(SupUtil.toStrArray(code.getTableName()));
//			// 设置是否继承基础业务字段
//			generator.setHasSuperEntity(code.getBaseMode() == 2);
//			// 设置是否开启包装器模式
//			generator.setHasWrapper(code.getWrapMode() == 2);
//			generator.run();
//		});
//		return R.success("代码生成成功");
//	}

}
