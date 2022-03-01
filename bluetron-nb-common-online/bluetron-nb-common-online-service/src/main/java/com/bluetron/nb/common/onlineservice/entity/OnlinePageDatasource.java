package com.bluetron.nb.common.onlineservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 在线表单页面和数据源多对多关联实体对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
@TableName(value = "bn_online_page_datasource")
public class OnlinePageDatasource {

    /**
     * 主键Id。
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 页面主键Id。
     */
    @TableField(value = "page_id")
    private Long pageId;

    /**
     * 数据源主键Id。
     */
    @TableField(value = "datasource_id")
    private Long datasourceId;
}
