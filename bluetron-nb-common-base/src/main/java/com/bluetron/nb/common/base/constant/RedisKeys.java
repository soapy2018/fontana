package com.bluetron.nb.common.base.constant;

/**
 * 分布式锁定的名称
 *
 * @author xingyue.wang
 */
public class RedisKeys {

    // 编码锁的命名规则，locker:seq:租户Id:编码名称
    private final static String SeqLockerName = "locker:seq:%s:%s";

    public static final String FormatSeqLockerKey(String tenantId, String seqName) {
        return String.format(SeqLockerName, tenantId, seqName);
    }

    private final static String InsertAndImportLocker = "InsertAndImportLocker:%s:%s";
    public static final String FormatInsertAndImportLocker(String tenantId, String businessType) {
        return String.format(InsertAndImportLocker, tenantId, businessType);
    }

    // 初始化锁,locker:initial:租户Id
    private final static String InitialLockerName = "locker:initial:%s";

    public static final String FormatInitialLockerKey(String tenantId) {
        return String.format(InitialLockerName, tenantId);
    }

    /**
     * 数据导入的进度
     */
    private final static String DATA_IMPORT_PROGRESS_CACHE_KEY = "import:%s:%s:%s:progress";

    public static final String formatDataImportProgressCacheKey(String tenantId, String businessType, String uploadId) {
        return String.format(DATA_IMPORT_PROGRESS_CACHE_KEY, tenantId, businessType, uploadId);
    }

    /**
     * 数据导入 校验结果列表
     */
    private final static String DATA_VERIFY_DATA_LIST_CACHE_KEY = "import:%s:%s:%s:verifyList";

    public static final String formatDataVerifyListCacheKey(String tenantId, String businessType, String uploadId) {
        return String.format(DATA_VERIFY_DATA_LIST_CACHE_KEY, tenantId, businessType, uploadId);
    }

    /**
     * 数据导入 校验失败部分 列表
     */
    private final static String DATA_VERIFY_FAIL_DATA_LIST_CACHE_KEY = "import:%s:%s:%s:verifyFailList";

    public static final String formatDataVerifyFailListCacheKey(String tenantId, String businessType, String uploadId) {
        return String.format(DATA_VERIFY_FAIL_DATA_LIST_CACHE_KEY, tenantId, businessType, uploadId);
    }

    /**
     * 数据导入 插入数据库结果列表
     */
    private final static String DATA_IMPORT_DATA_LIST_CACHE_KEY = "import:%s:%s:%s:importList";

    public static String formatDataImportListCacheKey(String tenantId, String businessType, String uploadId) {
        return String.format(DATA_IMPORT_DATA_LIST_CACHE_KEY, tenantId, businessType, uploadId);
    }

    /**
     * 数据导入 插入数据库 失败部分 列表
     */
    private final static String DATA_IMPORT_FAIL_DATA_LIST_CACHE_KEY = "import:%s:%s:%s:importFailList";

    public static String formatDataImportFailListCacheKey(String tenantId, String businessType, String uploadId) {
        return String.format(DATA_IMPORT_FAIL_DATA_LIST_CACHE_KEY, tenantId, businessType, uploadId);
    }

    /**
     * DingTalk Map<cellPhone, userId>
     */
    private static final String CELL_PHONE_TO_DING_TALK_USER_ID_MAP_CACHE_KEY = "cellPhoneToDingTalkUserIdMap:%s";

    public static String formatCellPhoneToUserIdMapCacheKey(String tenantId) {
        return String.format(CELL_PHONE_TO_DING_TALK_USER_ID_MAP_CACHE_KEY, tenantId);
    }

    /**
     * DingTalk accessToken
     */
    private static final String DING_TALK_ACCESS_TOKEN_CACHE_KEY = "dingTalkAccessToken:%s";

    public static String formatDingTalkAccessTokenCacheKey(String tenantId) {
        return String.format(DING_TALK_ACCESS_TOKEN_CACHE_KEY, tenantId);
    }

    /**
     * DingTalk accessToken
     */
    private static final String WE_CHAT_ACCESS_TOKEN_CACHE_KEY = "weChatAccessToken:%s";

    public static String formatWeChatAccessTokenCacheKey(String tenantId) {
        return String.format(WE_CHAT_ACCESS_TOKEN_CACHE_KEY, tenantId);
    }


    private static final String ENTITY_BY_ID_CACHE_KEY = "entity:%s:id:%d";
    public static String formatEntityByIdCacheKey(Class entityType, long id) {
        return String.format(ENTITY_BY_ID_CACHE_KEY, entityType.getSimpleName(), id);
    }
    private final static String EXTEND_ATTRIBUTES_CACHE_KEY = "attributes:%s:%s:extendAttributes";
    public static final String formatExtendAttributesCacheKey(String tenantId, String businessType) {
        return String.format(EXTEND_ATTRIBUTES_CACHE_KEY, tenantId, businessType);
    }

    private final static String EXTEND_CONTROLS_CACHE_KEY = "attributes:%s:%s:extendControls";
    public static final String formatExtendControlsCacheKey(String tenantId, String businessType) {
        return String.format(EXTEND_CONTROLS_CACHE_KEY, tenantId, businessType);
    }

    private final static String BASIC_ATTRIBUTE_CACHE_KEY = "attributes:%s:%s:basicAttributes";
    public static final String formatBasicAttributeCacheKey(String tenantId, String businessType) {
        return String.format(BASIC_ATTRIBUTE_CACHE_KEY, tenantId, businessType);
    }

    private final static String TEMPLATE_INFO_CACHE_KEY = "attributes:%s:%s:templateInfo";
    public static final String formatTemplateInfoCacheKey(String tenantId, String businessType) {
        return String.format(TEMPLATE_INFO_CACHE_KEY, tenantId, businessType);
    }

    private final static String EQUIPMENT_CHECK_PLAN_LOG_CACHE_KEY = "equipmentCheckPlanLog:%s:%d";
    public static final String formatEquipmentCheckPlanLogCacheKey(String tenantId, long equipmentCheckPlanLogId) {
        return String.format(EQUIPMENT_CHECK_PLAN_LOG_CACHE_KEY, tenantId, equipmentCheckPlanLogId);
    }

}
