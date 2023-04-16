package com.fontana.JeeOnline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fontana.base.constant.CommonConstants;
import com.fontana.JeeOnline.entity.OnlCgformIndex;
import com.fontana.JeeOnline.mapper.OnlCgformHeadMapper;
import com.fontana.JeeOnline.mapper.OnlCgformIndexMapper;
import com.fontana.JeeOnline.service.IOnlCgformIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * @className: OnlCgformIndexServiceImpl
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/12 15:49
 */
@Service("onlCgformIndexServiceImpl")
public class OnlCgformIndexServiceImpl extends ServiceImpl<OnlCgformIndexMapper, OnlCgformIndex> implements IOnlCgformIndexService {

    @Autowired
    private OnlCgformHeadMapper onlCgformHeadMapper;

    @Override
    public boolean isExistIndex(String countIndexSql) {
        if (countIndexSql == null) {
            return true;
        } else {
            Integer indexCount = ((OnlCgformIndexMapper)this.baseMapper).queryIndexCount(countIndexSql);
            return indexCount > 0;
        }
    }

    @Override
    public void createIndex(String code, String databaseType, String tableName) {
        LambdaQueryWrapper<OnlCgformIndex> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformIndex::getCgformHeadId, code);
        List onlCgformIndexList = this.list(lambdaQueryWrapper);
        if (onlCgformIndexList != null && onlCgformIndexList.size() > 0) {
            Iterator iterator = onlCgformIndexList.iterator();

            while(iterator.hasNext()) {
                OnlCgformIndex onlCgformIndex = (OnlCgformIndex)iterator.next();
                if (!CommonConstants.DEL_FLAG_1.equals(onlCgformIndex.getDelFlag()) && "N".equals(onlCgformIndex.getIsDbSynch())) {

                    String indexName = onlCgformIndex.getIndexName();
                    String indexField = onlCgformIndex.getIndexField();
                    String indexType = "normal".equals(onlCgformIndex.getIndexType()) ? " index " : onlCgformIndex.getIndexType() + " index ";
                    byte type = -1;
                    switch(databaseType.hashCode()) {
                        case -1955532418:
                            if (databaseType.equals("ORACLE")) {
                                type = 1;
                            }
                            break;
                        case -1620389036:
                            if (databaseType.equals("POSTGRESQL")) {
                                type = 3;
                            }
                            break;
                        case 73844866:
                            if (databaseType.equals("MYSQL")) {
                                type = 0;
                            }
                            break;
                        case 912124529:
                            if (databaseType.equals("SQLSERVER")) {
                                type = 2;
                            }
                    }

                    String sqlStr = "";
                    switch(type) {
                        case 0:
                            sqlStr = "create " + indexType + indexName + " on " + tableName + "(" + indexField + ")";
                            break;
                        case 1:
                            sqlStr = "create " + indexType + indexName + " on " + tableName + "(" + indexField + ")";
                            break;
                        case 2:
                            sqlStr = "create " + indexType + indexName + " on " + tableName + "(" + indexField + ")";
                            break;
                        case 3:
                            sqlStr = "create " + indexType + indexName + " on " + tableName + "(" + indexField + ")";
                            break;
                        default:
                            sqlStr = "create " + indexType + indexName + " on " + tableName + "(" + indexField + ")";
                    }

                    this.onlCgformHeadMapper.executeDDL(sqlStr);
                    onlCgformIndex.setIsDbSynch("Y");
                    this.updateById(onlCgformIndex);
                }
            }
        }

    }
}


