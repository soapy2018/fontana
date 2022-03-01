package com.bluetron.nb.common.upmsservice.service.impl;

import com.bluetron.nb.common.db.mapper.BaseDaoMapper;
import com.bluetron.nb.common.db.service.impl.ABaseService;
import com.bluetron.nb.common.upmsservice.entity.SysPermWhitelist;
import com.bluetron.nb.common.upmsservice.mapper.SysPermWhitelistMapper;
import com.bluetron.nb.common.upmsservice.service.SysPermWhitelistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 白名单数据服务类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Slf4j
@Service("sysPermWhitelistService")
public class SysPermWhitelistServiceImpl extends ABaseService<SysPermWhitelist, String> implements SysPermWhitelistService {

    @Autowired
    private SysPermWhitelistMapper sysPermWhitelistMapper;

    /**
     * 返回主对象的Mapper对象。
     *
     * @return 主对象的Mapper对象。
     */
    @Override
    protected BaseDaoMapper<SysPermWhitelist> mapper() {
        return sysPermWhitelistMapper;
    }

    /**
     * 获取白名单权限资源的列表。
     *
     * @return 白名单权限资源地址列表。
     */
    @Override
    public List<String> getWhitelistPermList() {
        List<SysPermWhitelist> dataList = this.getAllList();
        Function<SysPermWhitelist, String> getterFunc = SysPermWhitelist::getPermUrl;
        return dataList.stream()
                .filter(x -> getterFunc.apply(x) != null).map(getterFunc).collect(Collectors.toList());
    }
}
