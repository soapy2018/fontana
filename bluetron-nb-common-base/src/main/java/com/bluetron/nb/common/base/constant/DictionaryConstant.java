package com.bluetron.nb.common.base.constant;

/**
 * 字典相关的长度
 *
 * @author xingyue.wang
 */
public class DictionaryConstant {

    public static final String CompanyConfigName = "CompanyConfig";

    public static final String CompanyConfig_CompanyNameKey = "CompanyName";
    public static final String CompanyConfig_CompanyCodeKey = "CompanyCode";
    public static final String CompanyConfig_SerialNumberLengthKey = "SerialNumberLength";
    public static final int CompanyConfig_SerialNumber_DefaultLength = 8;

    public static final String CompanyConfig_SerialNumberRandomLengthKey = "SerialNumberRandomLength";
    public static final int CompanyConfig_SerialNumber_DefaultRandomLength = 3;

    public static final String CompanyConfig_WorkOrderMode = "WorkOrderMode";


    public static final String EquipmentStateConfigName = "EquipmentStateConfig";

    /**
     * 仓库列表  暂时保存在字典中
     */
    public static final String WarehouseConfigName = "WarehouseConfig";

    // 微信
    public static final String WeChatConfig = "WeChatConfig";

    public static final String WeChatConfig_CorpId = "CorpId";

    public static final String WeChatConfig_CorpSecret = "CorpSecret";

    public static final String WeChatConfig_AgentId = "AgentId";

    // 钉钉配置
    public static final String DingTalkConfig = "DingTalkConfig";

    public static final String DingTalkConfig_AppKey = "AppKey";

    public static final String DingTalkConfig_AppSecret = "AppSecret";

    public static final String DingTalkConfig_AgentId = "AgentId";

}
