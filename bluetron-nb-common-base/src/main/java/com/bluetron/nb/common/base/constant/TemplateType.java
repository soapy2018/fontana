package com.bluetron.nb.common.base.constant;

/**
 * 模版类型
 *
 * @author xingyue.wang
 */
public enum TemplateType {
    /**
     * 所有字段模板
     */
    AllField,
    /**
     * 基础字段模版
     * 2021-05-24 后续不再需要基础字段扩展模板
     */
    @Deprecated
    BasicField,

    /**
     * 用来存 模板信息
     * {
     *     "name": "设备",            //名称
     *     "listPageName": "设备台账",  //列表页名称
     *     "detailPageName": "设备详情",  //详情页名称
     *     "includeFooter": true        //详情页是否显示 创建人、创建时间、更新人、更新时间
     * }
     */
    TemplateInfo
}
