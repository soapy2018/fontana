package com.fontana.seata.account.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fontana.seata.account.model.Account;
import com.fontana.seata.account.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author cqf
 * @date 2019/9/14
 */
@Slf4j
@Service
public class AccountService {
    @Resource
    private AccountMapper accountMapper;

    /**
     * 减账号金额
     */
    //@Transactional(rollbackFor = Exception.class)
    public void reduce(String userId, int money) {
        if ("U002".equals(userId)) {
            throw new RuntimeException("this is a mock Exception");
        }

        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.setEntity(new Account().setUserId(userId));
        Account account = accountMapper.selectOne(wrapper);
        account.setMoney(account.getMoney() - money);
        accountMapper.updateById(account);
    }
}
