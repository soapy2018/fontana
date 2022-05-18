package com.fontana.demo.feign.controller;

import com.fontana.base.result.Result;
import com.fontana.upmsapi.client.SysUserClient;
import com.fontana.upmsapi.vo.SysUserVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @className: OpenFeignController
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/5/13 14:44
 */
@RestController
@RequestMapping("/openfeign")
public class OpenFeignController {

    @Resource
    SysUserClient sysUserClient;

//    localhost:8080/openfeign/sysUser/listByIds
//    Content-Type: application/x-www-form-urlencoded
//    body: userIds=1440911410581213417,1440965344985354240,1440965808049098752&withDict=false
    @PostMapping("/sysUser/listByIds")
    Result<List<SysUserVo>> listByIds(
            @RequestParam("userIds") Set<Long> userIds,
            @RequestParam("withDict") Boolean withDict){
        return sysUserClient.listByIds(userIds, withDict);
    }

}


