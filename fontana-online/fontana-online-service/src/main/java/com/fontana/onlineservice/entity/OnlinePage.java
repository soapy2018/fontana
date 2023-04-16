package com.fontana.onlineservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fontana.base.annotation.RelationConstDict;
import com.fontana.db.mapper.BaseModelMapper;
import com.fontana.onlineapi.dict.PageStatus;
import com.fontana.onlineapi.dict.PageType;
import com.fontana.onlineapi.dto.OnlinePageDto;
import com.fontana.onlineapi.vo.OnlinePageVo;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.Map;

/**
 * 在线表单所在页面实体对象。这里我们可以把页面理解为表单的容器。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
@TableName(value = "bn_online_page")
public class OnlinePage {

    /**
     * 主键Id。
     */
    @TableId(value = "page_id")
    private Long pageId;

    /**
     * 页面编码。
     */
    @TableField(value = "page_code")
    private String pageCode;

    /**
     * 页面名称。
     */
    @TableField(value = "page_name")
    private String pageName;

    /**
     * 页面类型。
     */
    @TableField(value = "page_type")
    private Integer pageType;

    /**
     * 页面编辑状态。
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否发布。
     */
    @TableField(value = "published")
    private Boolean published;

    /**
     * 更新时间。
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 创建时间。
     */
    @TableField(value = "create_time")
    private Date createTime;

    @RelationConstDict(
            masterIdField = "pageType",
            constantDictClass = PageType.class)
    @TableField(exist = false)
    private Map<String, Object> pageTypeDictMap;

    @RelationConstDict(
            masterIdField = "status",
            constantDictClass = PageStatus.class)
    @TableField(exist = false)
    private Map<String, Object> statusDictMap;

    @Mapper
    public interface OnlinePageModelMapper extends BaseModelMapper<OnlinePageDto, OnlinePage, OnlinePageVo> {
        /**
         * 转换Dto对象到实体对象。
         *
         * @param onlinePageDto 域对象。
         * @return 实体对象。
         */
        @Override
        OnlinePage toModel(OnlinePageDto onlinePageDto);
        /**
         * 转换实体对象到VO对象。
         *
         * @param onlinePage 实体对象。
         * @return 域对象。
         */
        @Override
        OnlinePageVo fromModel(OnlinePage onlinePage);
    }
    public static final OnlinePageModelMapper INSTANCE = Mappers.getMapper(OnlinePageModelMapper.class);
}
