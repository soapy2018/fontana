CREATE database if NOT EXISTS `common-online` default character set utf8mb4 collate utf8mb4_general_ci;
use `common-online`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bn_online_page
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_page`;
CREATE TABLE `bn_online_page` (
  `page_id` bigint(20) NOT NULL COMMENT '主键Id',
  `page_code` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '页面编码',
  `page_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '页面名称',
  `page_type` int(11) NOT NULL COMMENT '页面类型',
  `status` int(11) NOT NULL COMMENT '页面编辑状态',
  `published` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否发布',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`page_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_page
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_page` VALUES (1440945149889744896, 'pageFlowLeave', '请假申请', 10, 2, b'1', '2021-09-23 15:45:08', '2021-09-23 15:44:30');
INSERT INTO `bn_online_page` VALUES (1440946020174270464, 'pageSubmit', '报销申请', 10, 2, b'1', '2021-09-23 15:57:43', '2021-09-23 15:47:57');
INSERT INTO `bn_online_page` VALUES (1440952710487609344, 'pageFlowContract', '合同审批', 10, 2, b'1', '2021-09-23 16:23:02', '2021-09-23 16:14:32');
INSERT INTO `bn_online_page` VALUES (1440958861153406976, 'formFirstParty', '甲方企业', 1, 2, b'1', '2021-09-23 16:40:03', '2021-09-23 16:38:59');
INSERT INTO `bn_online_page` VALUES (1440961119001776128, 'pageSecondParty', '乙方企业管理', 1, 2, b'1', '2021-09-23 16:48:58', '2021-09-23 16:47:57');
INSERT INTO `bn_online_page` VALUES (1440962061336055808, 'pageProduct', '产品管理', 1, 2, b'1', '2021-09-23 16:53:12', '2021-09-23 16:51:42');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_column
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_column`;
CREATE TABLE `bn_online_column` (
  `column_id` bigint(20) NOT NULL COMMENT '主键Id',
  `column_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '字段名',
  `table_id` bigint(20) NOT NULL COMMENT '数据表Id',
  `column_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据表中的字段类型',
  `full_column_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据表中的完整字段类型(包括了精度和刻度)',
  `primary_key` bit(1) NOT NULL COMMENT '是否为主键',
  `auto_increment` bit(1) NOT NULL COMMENT '是否是自增主键(0: 不是 1: 是)',
  `nullable` bit(1) NOT NULL COMMENT '是否可以为空 (0: 不可以为空 1: 可以为空)',
  `column_default` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '缺省值',
  `column_show_order` int(11) NOT NULL COMMENT '字段在数据表中的显示位置',
  `column_comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '数据表中的字段注释',
  `object_field_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '对象映射字段名称',
  `object_field_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '对象映射字段类型',
  `filter_type` int(11) NOT NULL DEFAULT '1' COMMENT '字段过滤类型',
  `parent_key` bit(1) NOT NULL COMMENT '是否是主键的父Id',
  `dept_filter` bit(1) NOT NULL COMMENT '是否部门过滤字段',
  `user_filter` bit(1) NOT NULL COMMENT '是否用户过滤字段',
  `field_kind` int(11) DEFAULT NULL COMMENT '字段类别',
  `max_file_count` int(11) DEFAULT NULL COMMENT '包含的文件文件数量，0表示无限制',
  `dict_id` bigint(20) DEFAULT NULL COMMENT '字典Id',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`column_id`),
  KEY `idx_table_id` (`table_id`) USING BTREE,
  KEY `idx_dict_id` (`dict_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_column
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_column` VALUES (1440945228088348672, 'id', 1440945228079960064, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'id', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:44:49', '2021-09-23 15:44:49');
INSERT INTO `bn_online_column` VALUES (1440945228092542976, 'user_id', 1440945228079960064, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 2, '请假用户', 'userId', 'Long', 0, b'0', b'0', b'0', 21, NULL, 1440944417170001920, '2021-09-24 09:29:41', '2021-09-23 15:44:49');
INSERT INTO `bn_online_column` VALUES (1440945228100931584, 'leave_reason', 1440945228079960064, 'varchar', 'varchar(512)', b'0', b'0', b'0', NULL, 3, '请假原因', 'leaveReason', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:44:49', '2021-09-23 15:44:49');
INSERT INTO `bn_online_column` VALUES (1440945228105125888, 'leave_type', 1440945228079960064, 'int', 'int(11)', b'0', b'0', b'0', NULL, 4, '请假类型', 'leaveType', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, 1440943031288074240, '2021-09-23 15:45:01', '2021-09-23 15:44:49');
INSERT INTO `bn_online_column` VALUES (1440945228113514496, 'leave_begin_time', 1440945228079960064, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 5, '请假开始时间', 'leaveBeginTime', 'Date', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:44:49', '2021-09-23 15:44:49');
INSERT INTO `bn_online_column` VALUES (1440945228117708800, 'leave_end_time', 1440945228079960064, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 6, '请假结束时间', 'leaveEndTime', 'Date', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:44:49', '2021-09-23 15:44:49');
INSERT INTO `bn_online_column` VALUES (1440945228126097408, 'apply_time', 1440945228079960064, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 7, '申请时间', 'applyTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 15:45:06', '2021-09-23 15:44:49');
INSERT INTO `bn_online_column` VALUES (1440946127468761088, 'id', 1440946127460372480, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'id', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:48:23', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127481344000, 'submit_name', 1440946127460372480, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 2, '报销名称', 'submitName', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:48:23', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127489732608, 'submit_kind', 1440946127460372480, 'int', 'int(11)', b'0', b'0', b'0', NULL, 3, '报销类别', 'submitKind', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, 1440943168626364416, '2021-09-23 15:49:11', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127493926912, 'total_amount', 1440946127460372480, 'int', 'int(11)', b'0', b'0', b'0', NULL, 4, '报销金额', 'totalAmount', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:49:52', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127498121216, 'description', 1440946127460372480, 'varchar', 'varchar(512)', b'0', b'0', b'0', NULL, 5, '报销描述', 'description', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:48:23', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127506509824, 'memo', 1440946127460372480, 'varchar', 'varchar(512)', b'0', b'0', b'1', NULL, 6, '备注', 'memo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:48:23', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127510704128, 'update_user_id', 1440946127460372480, 'bigint', 'bigint(20)', b'0', b'0', b'1', NULL, 7, '修改人', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 15:50:04', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127514898432, 'update_time', 1440946127460372480, 'datetime', 'datetime', b'0', b'0', b'1', NULL, 8, '修改时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 15:50:08', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127523287040, 'create_user_id', 1440946127460372480, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '创建人', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 15:50:12', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440946127527481344, 'create_time', 1440946127460372480, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 15:50:16', '2021-09-23 15:48:23');
INSERT INTO `bn_online_column` VALUES (1440947089222668288, 'id', 1440947089218473984, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'id', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:52:12', '2021-09-23 15:52:12');
INSERT INTO `bn_online_column` VALUES (1440947089231056896, 'submit_id', 1440947089218473984, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 2, '报销单据Id', 'submitId', 'Long', 1, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:57:41', '2021-09-23 15:52:12');
INSERT INTO `bn_online_column` VALUES (1440947089235251200, 'expense_type', 1440947089218473984, 'int', 'int(11)', b'0', b'0', b'0', NULL, 3, '费用类型', 'expenseType', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, 1440943309924077568, '2021-09-23 15:53:15', '2021-09-23 15:52:12');
INSERT INTO `bn_online_column` VALUES (1440947089243639808, 'expense_time', 1440947089218473984, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 4, '发生日期', 'expenseTime', 'Date', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:52:12', '2021-09-23 15:52:12');
INSERT INTO `bn_online_column` VALUES (1440947089252028416, 'amount', 1440947089218473984, 'int', 'int(11)', b'0', b'0', b'0', NULL, 5, '金额', 'amount', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:52:12', '2021-09-23 15:52:12');
INSERT INTO `bn_online_column` VALUES (1440947089260417024, 'image_url', 1440947089218473984, 'varchar', 'varchar(512)', b'0', b'0', b'1', NULL, 6, '报销凭证', 'imageUrl', 'String', 0, b'0', b'0', b'0', 2, 1, NULL, '2021-09-23 15:53:53', '2021-09-23 15:52:12');
INSERT INTO `bn_online_column` VALUES (1440947089264611328, 'description', 1440947089218473984, 'varchar', 'varchar(255)', b'0', b'0', b'1', NULL, 7, '费用描述', 'description', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 15:52:12', '2021-09-23 15:52:12');
INSERT INTO `bn_online_column` VALUES (1440952815303266304, 'contract_id', 1440952815294877696, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'contractId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:14:57', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815307460608, 'first_party_id', 1440952815294877696, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 2, '甲方企业', 'firstPartyId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, 1440943452526219264, '2021-09-23 16:16:50', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815311654912, 'second_party_id', 1440952815294877696, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 3, '乙方企业', 'secondPartyId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, 1440943955939168256, '2021-09-23 16:16:59', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815315849216, 'contract_type', 1440952815294877696, 'int', 'int(11)', b'0', b'0', b'0', NULL, 4, '合同类型', 'contractType', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, 1440944300799037440, '2021-09-23 16:17:03', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815320043520, 'due_date', 1440952815294877696, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 5, '到期日期', 'dueDate', 'Date', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:14:57', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815324237824, 'sales_id', 1440952815294877696, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 6, '业务员', 'salesId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, 1440944417170001920, '2021-09-23 16:17:10', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815332626432, 'commission_rate', 1440952815294877696, 'int', 'int(11)', b'0', b'0', b'0', NULL, 7, '提成比例（%）', 'commissionRate', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:17:38', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815336820736, 'attachment', 1440952815294877696, 'varchar', 'varchar(512)', b'0', b'0', b'1', NULL, 8, '合同附件', 'attachment', 'String', 0, b'0', b'0', b'0', 1, 1, NULL, '2021-09-23 16:17:46', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815341015040, 'security_attachment', 1440952815294877696, 'varchar', 'varchar(512)', b'0', b'0', b'1', NULL, 9, '保密协议', 'securityAttachment', 'String', 0, b'0', b'0', b'0', 1, 1, NULL, '2021-09-23 16:17:53', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815345209344, 'intellectual_property_attachment', 1440952815294877696, 'varchar', 'varchar(512)', b'0', b'0', b'1', NULL, 10, '知识产权协议', 'intellectualPropertyAttachment', 'String', 0, b'0', b'0', b'0', 1, 1, NULL, '2021-09-23 16:18:00', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815349403648, 'other_attachment', 1440952815294877696, 'varchar', 'varchar(512)', b'0', b'0', b'1', NULL, 11, '其他附件', 'otherAttachment', 'String', 0, b'0', b'0', b'0', 1, 1, NULL, '2021-09-23 16:18:05', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815353597952, 'create_user_id', 1440952815294877696, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 12, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:18:09', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815357792256, 'create_time', 1440952815294877696, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 13, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:18:15', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815361986560, 'update_user_id', 1440952815294877696, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 14, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:18:19', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815366180864, 'update_time', 1440952815294877696, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 15, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:18:24', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952815370375168, 'deleted_flag', 1440952815294877696, 'int', 'int(11)', b'0', b'0', b'0', '0', 16, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:18:28', '2021-09-23 16:14:57');
INSERT INTO `bn_online_column` VALUES (1440952921037475840, 'first_party_id', 1440952921024892928, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'firstPartyId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921041670144, 'company_name', 1440952921024892928, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 2, '公司名称', 'companyName', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921050058752, 'legal_person', 1440952921024892928, 'varchar', 'varchar(64)', b'0', b'0', b'0', NULL, 3, '公司法人', 'legalPerson', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:18:38', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921054253056, 'legal_person_id', 1440952921024892928, 'char', 'char(18)', b'0', b'0', b'0', NULL, 4, '法人身份证号', 'legalPersonId', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921058447360, 'registry_address', 1440952921024892928, 'varchar', 'varchar(512)', b'0', b'0', b'0', NULL, 5, '注册地址', 'registryAddress', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921066835968, 'contact_info', 1440952921024892928, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 6, '联系方式', 'contactInfo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921079418880, 'business_scope', 1440952921024892928, 'varchar', 'varchar(4000)', b'0', b'0', b'0', NULL, 7, '经营范围', 'businessScope', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921087807488, 'memo', 1440952921024892928, 'varchar', 'varchar(1024)', b'0', b'0', b'1', NULL, 8, '备注', 'memo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921092001792, 'create_user_id', 1440952921024892928, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:18:48', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921100390400, 'create_time', 1440952921024892928, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:18:51', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921104584704, 'update_user_id', 1440952921024892928, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 11, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:18:54', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921108779008, 'update_time', 1440952921024892928, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 12, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:19:00', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952921112973312, 'deleted_flag', 1440952921024892928, 'int', 'int(11)', b'0', b'0', b'0', '0', 13, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:19:04', '2021-09-23 16:15:23');
INSERT INTO `bn_online_column` VALUES (1440952988393803776, 'second_party_id', 1440952988389609472, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'secondPartyId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988406386688, 'company_name', 1440952988389609472, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 2, '公司名称', 'companyName', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988456718336, 'legal_person', 1440952988389609472, 'varchar', 'varchar(64)', b'0', b'0', b'0', NULL, 3, '公司法人', 'legalPerson', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:19:12', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988469301248, 'legal_person_id', 1440952988389609472, 'char', 'char(18)', b'0', b'0', b'0', NULL, 4, '法人身份证号', 'legalPersonId', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988473495552, 'registry_address', 1440952988389609472, 'varchar', 'varchar(512)', b'0', b'0', b'0', NULL, 5, '注册地址', 'registryAddress', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988486078464, 'contact_info', 1440952988389609472, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 6, '联系方式', 'contactInfo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988494467072, 'business_scope', 1440952988389609472, 'varchar', 'varchar(4000)', b'0', b'0', b'0', NULL, 7, '经营范围', 'businessScope', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988498661376, 'memo', 1440952988389609472, 'varchar', 'varchar(1024)', b'0', b'0', b'1', NULL, 8, '备注', 'memo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988502855680, 'create_user_id', 1440952988389609472, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:19:17', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988507049984, 'create_time', 1440952988389609472, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:19:21', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988515438592, 'update_user_id', 1440952988389609472, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 11, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:19:24', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988519632896, 'update_time', 1440952988389609472, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 12, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:19:28', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440952988528021504, 'deleted_flag', 1440952988389609472, 'int', 'int(11)', b'0', b'0', b'0', '0', 13, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:19:33', '2021-09-23 16:15:39');
INSERT INTO `bn_online_column` VALUES (1440953088910299136, 'contract_detail_id', 1440953088901910528, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'contractDetailId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:03', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088918687744, 'contract_id', 1440953088901910528, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 2, '合同Id', 'contractId', 'Long', 1, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:42:59', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088922882048, 'product_id', 1440953088901910528, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 3, '合同产品', 'productId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, 1440944049128214528, '2021-09-23 16:20:03', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088927076352, 'total_count', 1440953088901910528, 'int', 'int(11)', b'0', b'0', b'0', NULL, 4, '产品数量', 'totalCount', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:20:10', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088935464960, 'total_amount', 1440953088901910528, 'int', 'int(11)', b'0', b'0', b'0', NULL, 5, '产品总价', 'totalAmount', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:20:56', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088939659264, 'meno', 1440953088901910528, 'varchar', 'varchar(1024)', b'0', b'0', b'1', NULL, 6, '备注', 'meno', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:03', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088948047872, 'create_user_id', 1440953088901910528, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 7, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:21:00', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088952242176, 'create_time', 1440953088901910528, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 8, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:21:04', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088960630784, 'update_user_id', 1440953088901910528, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:21:07', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088964825088, 'update_time', 1440953088901910528, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:21:12', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953088973213696, 'deleted_flag', 1440953088901910528, 'int', 'int(11)', b'0', b'0', b'0', '0', 11, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:21:15', '2021-09-23 16:16:03');
INSERT INTO `bn_online_column` VALUES (1440953170518872064, 'pay_detail_id', 1440953170514677760, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'payDetailId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:22', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170531454976, 'contract_id', 1440953170514677760, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 2, '合同Id', 'contractId', 'Long', 1, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:43:04', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170535649280, 'pay_date', 1440953170514677760, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 3, '付款日期', 'payDate', 'Date', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:22', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170539843584, 'pay_type', 1440953170514677760, 'int', 'int(11)', b'0', b'0', b'0', NULL, 4, '付款类型', 'payType', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, 1440944184381935616, '2021-09-23 16:21:27', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170548232192, 'percentage', 1440953170514677760, 'int', 'int(11)', b'0', b'0', b'0', NULL, 5, '百分比', 'percentage', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:22', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170552426496, 'memo', 1440953170514677760, 'varchar', 'varchar(1024)', b'0', b'0', b'1', NULL, 6, '备注', 'memo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:22', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170560815104, 'create_user_id', 1440953170514677760, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 7, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:21:43', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170569203712, 'create_time', 1440953170514677760, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 8, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:21:46', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170573398016, 'update_user_id', 1440953170514677760, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:21:49', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170581786624, 'update_time', 1440953170514677760, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:21:52', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953170585980928, 'deleted_flag', 1440953170514677760, 'int', 'int(11)', b'0', b'0', b'0', '0', 11, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:21:55', '2021-09-23 16:16:22');
INSERT INTO `bn_online_column` VALUES (1440953245668216832, 'delivery_id', 1440953245664022528, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'deliveryId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:40', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245676605440, 'contract_id', 1440953245664022528, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 2, '合同Id', 'contractId', 'Long', 1, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:43:09', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245684994048, 'delivery_date', 1440953245664022528, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 3, '交付日期', 'deliveryDate', 'Date', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:40', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245697576960, 'product_id', 1440953245664022528, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 4, '交付产品', 'productId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, 1440944049128214528, '2021-09-23 16:22:18', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245705965568, 'total_count', 1440953245664022528, 'int', 'int(11)', b'0', b'0', b'0', NULL, 5, '交付数量', 'totalCount', 'Integer', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:22:21', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245718548480, 'memo', 1440953245664022528, 'varchar', 'varchar(1024)', b'0', b'0', b'1', NULL, 6, '备注', 'memo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:16:40', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245722742784, 'create_user_id', 1440953245664022528, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 7, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:22:45', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245726937088, 'create_time', 1440953245664022528, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 8, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:22:48', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245731131392, 'update_user_id', 1440953245664022528, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:22:52', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245735325696, 'update_time', 1440953245664022528, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:22:56', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440953245743714304, 'deleted_flag', 1440953245664022528, 'int', 'int(11)', b'0', b'0', b'0', '0', 11, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:22:59', '2021-09-23 16:16:40');
INSERT INTO `bn_online_column` VALUES (1440958971132252160, 'first_party_id', 1440958971128057856, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'firstPartyId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:39:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971140640768, 'company_name', 1440958971128057856, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 2, '公司名称', 'companyName', 'String', 3, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:41:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971144835072, 'legal_person', 1440958971128057856, 'varchar', 'varchar(64)', b'0', b'0', b'0', NULL, 3, '公司法人', 'legalPerson', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:39:32', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971149029376, 'legal_person_id', 1440958971128057856, 'char', 'char(18)', b'0', b'0', b'0', NULL, 4, '法人身份证号', 'legalPersonId', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:39:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971153223680, 'registry_address', 1440958971128057856, 'varchar', 'varchar(512)', b'0', b'0', b'0', NULL, 5, '注册地址', 'registryAddress', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:39:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971157417984, 'contact_info', 1440958971128057856, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 6, '联系方式', 'contactInfo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:39:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971161612288, 'business_scope', 1440958971128057856, 'varchar', 'varchar(4000)', b'0', b'0', b'0', NULL, 7, '经营范围', 'businessScope', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:39:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971165806592, 'memo', 1440958971128057856, 'varchar', 'varchar(1024)', b'0', b'0', b'1', NULL, 8, '备注', 'memo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:39:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971170000896, 'create_user_id', 1440958971128057856, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:39:40', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971174195200, 'create_time', 1440958971128057856, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:39:43', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971178389504, 'update_user_id', 1440958971128057856, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 11, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:39:46', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971182583808, 'update_time', 1440958971128057856, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 12, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:39:53', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440958971186778112, 'deleted_flag', 1440958971128057856, 'int', 'int(11)', b'0', b'0', b'0', '0', 13, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:39:58', '2021-09-23 16:39:25');
INSERT INTO `bn_online_column` VALUES (1440961208285925376, 'second_party_id', 1440961208273342464, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'secondPartyId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:48:18', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208294313984, 'company_name', 1440961208273342464, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 2, '公司名称', 'companyName', 'String', 3, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:48:23', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208298508288, 'legal_person', 1440961208273342464, 'varchar', 'varchar(64)', b'0', b'0', b'0', NULL, 3, '公司法人', 'legalPerson', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:48:28', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208302702592, 'legal_person_id', 1440961208273342464, 'char', 'char(18)', b'0', b'0', b'0', NULL, 4, '法人身份证号', 'legalPersonId', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:48:18', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208306896896, 'registry_address', 1440961208273342464, 'varchar', 'varchar(512)', b'0', b'0', b'0', NULL, 5, '注册地址', 'registryAddress', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:48:18', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208315285504, 'contact_info', 1440961208273342464, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 6, '联系方式', 'contactInfo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:48:18', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208319479808, 'business_scope', 1440961208273342464, 'varchar', 'varchar(4000)', b'0', b'0', b'0', NULL, 7, '经营范围', 'businessScope', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:48:18', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208323674112, 'memo', 1440961208273342464, 'varchar', 'varchar(1024)', b'0', b'0', b'1', NULL, 8, '备注', 'memo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:48:18', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208327868416, 'create_user_id', 1440961208273342464, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:48:36', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208332062720, 'create_time', 1440961208273342464, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:48:40', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208336257024, 'update_user_id', 1440961208273342464, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 11, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:48:44', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208340451328, 'update_time', 1440961208273342464, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 12, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:48:49', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440961208344645632, 'deleted_flag', 1440961208273342464, 'int', 'int(11)', b'0', b'0', b'0', '0', 13, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:48:55', '2021-09-23 16:48:18');
INSERT INTO `bn_online_column` VALUES (1440962162720772096, 'product_id', 1440962162712383488, 'bigint', 'bigint(20)', b'1', b'0', b'0', NULL, 1, '主键Id', 'productId', 'Long', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:52:06', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162729160704, 'product_name', 1440962162712383488, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 2, '产品名称', 'productName', 'String', 3, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:52:10', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162733355008, 'product_spec', 1440962162712383488, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 3, '规格', 'productSpec', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:52:06', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162737549312, 'type', 1440962162712383488, 'varchar', 'varchar(255)', b'0', b'0', b'0', NULL, 4, '型号', 'type', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:52:06', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162741743616, 'cost_price', 1440962162712383488, 'int', 'int(11)', b'0', b'0', b'0', NULL, 5, '产品价格', 'costPrice', 'Integer', 2, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:53:05', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162745937920, 'memo', 1440962162712383488, 'varchar', 'varchar(1024)', b'0', b'0', b'1', NULL, 6, '备注', 'memo', 'String', 0, b'0', b'0', b'0', NULL, NULL, NULL, '2021-09-23 16:52:06', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162750132224, 'create_user_id', 1440962162712383488, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 7, '创建者', 'createUserId', 'Long', 0, b'0', b'0', b'0', 21, NULL, NULL, '2021-09-23 16:52:20', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162754326528, 'create_time', 1440962162712383488, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 8, '创建时间', 'createTime', 'Date', 0, b'0', b'0', b'0', 20, NULL, NULL, '2021-09-23 16:52:23', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162762715136, 'update_user_id', 1440962162712383488, 'bigint', 'bigint(20)', b'0', b'0', b'0', NULL, 9, '更新者', 'updateUserId', 'Long', 0, b'0', b'0', b'0', 23, NULL, NULL, '2021-09-23 16:52:27', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162766909440, 'update_time', 1440962162712383488, 'datetime', 'datetime', b'0', b'0', b'0', NULL, 10, '最后更新时间', 'updateTime', 'Date', 0, b'0', b'0', b'0', 22, NULL, NULL, '2021-09-23 16:52:31', '2021-09-23 16:52:06');
INSERT INTO `bn_online_column` VALUES (1440962162771103744, 'deleted_flag', 1440962162712383488, 'int', 'int(11)', b'0', b'0', b'0', '0', 11, '删除标记(1: 正常 -1: 已删除)', 'deletedFlag', 'Integer', 0, b'0', b'0', b'0', 31, NULL, NULL, '2021-09-23 16:52:34', '2021-09-23 16:52:06');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_column_rule
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_column_rule`;
CREATE TABLE `bn_online_column_rule` (
  `column_id` bigint(20) NOT NULL COMMENT '字段Id',
  `rule_id` bigint(20) NOT NULL COMMENT '规则Id',
  `prop_data_json` text COLLATE utf8mb4_bin COMMENT '规则属性数据',
  PRIMARY KEY (`column_id`,`rule_id`) USING BTREE,
  KEY `idx_rule_id` (`rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_column_rule
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_column_rule` VALUES (1440946127493926912, 2, '{\"message\":\"报销金额必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440946127493926912, 4, '{\"message\":\"报销金额必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440946261518716928, 2, '{\"message\":\"报销金额必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440946261518716928, 4, '{\"message\":\"报销金额必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440947089252028416, 2, '{\"message\":\"报销金额必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440947089252028416, 4, '{\"message\":\"报销金额必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440949025019793408, 2, '{\"message\":\"提成比例必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440949025019793408, 4, '{\"message\":\"提成比例必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440949181786099712, 6, '{\"message\":\"请输入正确的手机号码\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440949481989214208, 1, '{\"message\":\"产品数量必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440949481989214208, 4, '{\"message\":\"产品数量必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440949481993408512, 2, '{\"message\":\"产品总价必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440949481993408512, 4, '{\"message\":\"产品总价必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440949804128538624, 2, '{\"message\":\"交付商品数量必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440949804128538624, 4, '{\"message\":\"交付商品数量必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440952815332626432, 2, '{\"message\":\"提成比例必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440952815332626432, 4, '{\"message\":\"提成比例必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440953088927076352, 2, '{\"message\":\"产品数量必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440953088927076352, 4, '{\"message\":\"产品数量必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440953088935464960, 2, '{\"message\":\"产品总价必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440953088935464960, 4, '{\"message\":\"产品总价必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440953245705965568, 2, '{\"message\":\"交付数量必须大于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440953245705965568, 4, '{\"message\":\"交付数量必须大于0\",\"min\":0}');
INSERT INTO `bn_online_column_rule` VALUES (1440962162741743616, 2, '{\"message\":\"产品价格不能小于0\"}');
INSERT INTO `bn_online_column_rule` VALUES (1440962162741743616, 4, '{\"message\":\"产品价格不能小于0\",\"min\":0}');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_datasource
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_datasource`;
CREATE TABLE `bn_online_datasource` (
  `datasource_id` bigint(20) NOT NULL COMMENT '主键Id',
  `datasource_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '数据源名称',
  `variable_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '数据源变量名',
  `dblink_id` bigint(20) NOT NULL COMMENT '数据库链接Id',
  `master_table_id` bigint(20) NOT NULL COMMENT '主表Id',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`datasource_id`),
  UNIQUE KEY `idx_variable_name` (`variable_name`) USING BTREE,
  KEY `idx_master_table_id` (`master_table_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_datasource
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_datasource` VALUES (1440945228130291712, '请假申请', 'dsFlowLeave', 1, 1440945228079960064, '2021-09-23 15:44:49', '2021-09-23 15:44:49');
INSERT INTO `bn_online_datasource` VALUES (1440946127531675648, '报销申请', 'dsFlowSubmit', 1, 1440946127460372480, '2021-09-23 15:48:23', '2021-09-23 15:48:23');
INSERT INTO `bn_online_datasource` VALUES (1440952815374569472, '合同审批', 'dsFlowContract', 1, 1440952815294877696, '2021-09-23 16:14:57', '2021-09-23 16:14:57');
INSERT INTO `bn_online_datasource` VALUES (1440958971190972416, '甲方管理', 'dsFirstParty', 1, 1440958971128057856, '2021-09-23 16:39:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_datasource` VALUES (1440961208344645633, '乙方管理', 'dsSecondParty', 1, 1440961208273342464, '2021-09-23 16:48:18', '2021-09-23 16:48:18');
INSERT INTO `bn_online_datasource` VALUES (1440962162771103745, '产品管理', 'dsProduct', 1, 1440962162712383488, '2021-09-23 16:52:06', '2021-09-23 16:52:06');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_datasource_relation
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_datasource_relation`;
CREATE TABLE `bn_online_datasource_relation` (
  `relation_id` bigint(20) NOT NULL COMMENT '主键Id',
  `relation_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '关联名称',
  `variable_name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '变量名',
  `datasource_id` bigint(20) NOT NULL COMMENT '主数据源Id',
  `relation_type` int(11) NOT NULL COMMENT '关联类型',
  `master_column_id` bigint(20) NOT NULL COMMENT '主表关联字段Id',
  `slave_table_id` bigint(20) NOT NULL COMMENT '从表Id',
  `slave_column_id` bigint(20) NOT NULL COMMENT '从表关联字段Id',
  `cascade_delete` bit(1) NOT NULL COMMENT '删除主表的时候是否级联删除一对一和一对多的从表数据，多对多只是删除关联，不受到这个标记的影响。',
  `left_join` bit(1) NOT NULL COMMENT '是否左连接',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`relation_id`) USING BTREE,
  KEY `idx_datasource_id` (`datasource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_datasource_relation
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_datasource_relation` VALUES (1440947089268805632, '报销详情', 'id_bn_test_flow_submit_detail_submit_idRelation', 1440946127531675648, 1, 1440946127468761088, 1440947089218473984, 1440947089231056896, b'1', b'1', '2021-09-23 15:52:12', '2021-09-23 15:52:12');
INSERT INTO `bn_online_datasource_relation` VALUES (1440952921117167616, '甲方企业', 'first_party_id_bn_test_flow_first_party_first_party_idRelation', 1440952815374569472, 0, 1440952815307460608, 1440952921024892928, 1440952921037475840, b'0', b'1', '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_datasource_relation` VALUES (1440952988536410112, '乙方企业', 'second_party_id_bn_test_flow_second_party_second_party_idRelation', 1440952815374569472, 0, 1440952815311654912, 1440952988389609472, 1440952988393803776, b'0', b'1', '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_datasource_relation` VALUES (1440953088977408000, '合同详情', 'contract_id_bn_test_flow_contract_detail_contract_idRelation', 1440952815374569472, 1, 1440952815303266304, 1440953088901910528, 1440953088918687744, b'1', b'1', '2021-09-23 16:16:03', '2021-09-23 16:16:03');
INSERT INTO `bn_online_datasource_relation` VALUES (1440953170590175232, '付款详情', 'contract_id_bn_test_flow_pay_detail_contract_idRelation', 1440952815374569472, 1, 1440952815303266304, 1440953170514677760, 1440953170531454976, b'1', b'1', '2021-09-23 16:16:22', '2021-09-23 16:16:22');
INSERT INTO `bn_online_datasource_relation` VALUES (1440953245747908608, '交付详情', 'contract_id_bn_test_flow_delivery_detail_contract_idRelation', 1440952815374569472, 1, 1440952815303266304, 1440953245664022528, 1440953245676605440, b'1', b'1', '2021-09-23 16:16:40', '2021-09-23 16:16:40');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_datasource_table
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_datasource_table`;
CREATE TABLE `bn_online_datasource_table` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据源Id',
  `relation_id` bigint(20) DEFAULT NULL COMMENT '数据源关联Id',
  `table_id` bigint(20) NOT NULL COMMENT '数据表Id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_relation_id` (`relation_id`) USING BTREE,
  KEY `idx_datasource_id` (`datasource_id`) USING BTREE,
  KEY `idx_table_id` (`table_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_datasource_table
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_datasource_table` VALUES (1440945228134486016, 1440945228130291712, NULL, 1440945228079960064);
INSERT INTO `bn_online_datasource_table` VALUES (1440946127535869952, 1440946127531675648, NULL, 1440946127460372480);
INSERT INTO `bn_online_datasource_table` VALUES (1440947089272999936, 1440946127531675648, 1440947089268805632, 1440947089218473984);
INSERT INTO `bn_online_datasource_table` VALUES (1440952815378763776, 1440952815374569472, NULL, 1440952815294877696);
INSERT INTO `bn_online_datasource_table` VALUES (1440952921121361920, 1440952815374569472, 1440952921117167616, 1440952921024892928);
INSERT INTO `bn_online_datasource_table` VALUES (1440952988540604416, 1440952815374569472, 1440952988536410112, 1440952988389609472);
INSERT INTO `bn_online_datasource_table` VALUES (1440953088981602304, 1440952815374569472, 1440953088977408000, 1440953088901910528);
INSERT INTO `bn_online_datasource_table` VALUES (1440953170594369536, 1440952815374569472, 1440953170590175232, 1440953170514677760);
INSERT INTO `bn_online_datasource_table` VALUES (1440953245752102912, 1440952815374569472, 1440953245747908608, 1440953245664022528);
INSERT INTO `bn_online_datasource_table` VALUES (1440958971195166720, 1440958971190972416, NULL, 1440958971128057856);
INSERT INTO `bn_online_datasource_table` VALUES (1440961208348839936, 1440961208344645633, NULL, 1440961208273342464);
INSERT INTO `bn_online_datasource_table` VALUES (1440962162775298048, 1440962162771103745, NULL, 1440962162712383488);
COMMIT;

-- ----------------------------
-- Table structure for bn_online_dblink
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_dblink`;
CREATE TABLE `bn_online_dblink` (
  `dblink_id` bigint(20) NOT NULL COMMENT '主键Id',
  `dblink_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '链接中文名称',
  `variable_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '链接英文名称',
  `dblink_desc` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '链接描述',
  `dblink_config_constant` int(255) NOT NULL COMMENT '数据源配置常量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`dblink_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_dblink
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_dblink` VALUES (1, 'first', 'first', '第一个链接', 0, '2021-09-23 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_dict
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_dict`;
CREATE TABLE `bn_online_dict` (
  `dict_id` bigint(20) NOT NULL COMMENT '主键Id',
  `dict_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '字典名称',
  `dict_type` int(11) NOT NULL COMMENT '字典类型',
  `dblink_id` bigint(20) DEFAULT NULL COMMENT '数据库链接Id',
  `table_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '字典表名称',
  `key_column_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '字典表键字段名称',
  `parent_key_column_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '字典表父键字段名称',
  `value_column_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '字典值字段名称',
  `deleted_column_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '逻辑删除字段',
  `user_filter_column_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户过滤滤字段名称',
  `dept_filter_column_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'dept_filter_column_name',
  `tenant_filter_column_name` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '租户过滤字段名称',
  `tree_flag` bit(1) NOT NULL COMMENT '是否树形标记',
  `dict_list_url` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '获取字典列表数据的url',
  `dict_ids_url` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '根据主键id批量获取字典数据的url',
  `dict_data_json` text COLLATE utf8mb4_bin COMMENT '字典的JSON数据',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_dict
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_dict` VALUES (1440943031288074240, '请假类型', 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0', NULL, NULL, '{\"dictData\":[{\"type\":\"Integer\",\"id\":1,\"name\":\"年假\"},{\"type\":\"Integer\",\"id\":2,\"name\":\"事假\"},{\"type\":\"Integer\",\"id\":3,\"name\":\"婚假\"}],\"paramList\":[]}', '2021-09-23 15:36:05', '2021-09-23 15:36:05');
INSERT INTO `bn_online_dict` VALUES (1440943168626364416, '报销类别', 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0', NULL, NULL, '{\"dictData\":[{\"type\":\"Integer\",\"id\":1,\"name\":\"差旅报销\"},{\"type\":\"Integer\",\"id\":2,\"name\":\"日常报销\"}],\"paramList\":[]}', '2021-09-23 15:36:37', '2021-09-23 15:36:37');
INSERT INTO `bn_online_dict` VALUES (1440943309924077568, '费用类别', 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0', NULL, NULL, '{\"dictData\":[{\"type\":\"Integer\",\"id\":1,\"name\":\"食宿费用\"},{\"type\":\"Integer\",\"id\":2,\"name\":\"交通费用\"}],\"paramList\":[]}', '2021-09-23 15:37:11', '2021-09-23 15:37:11');
INSERT INTO `bn_online_dict` VALUES (1440943452526219264, '甲方企业', 1, 1, 'bn_test_flow_first_party', 'first_party_id', NULL, 'company_name', 'deleted_flag', NULL, NULL, NULL, b'0', NULL, NULL, '{\"paramList\":[]}', '2021-09-23 15:37:45', '2021-09-23 15:37:45');
INSERT INTO `bn_online_dict` VALUES (1440943955939168256, '乙方企业', 1, 1, 'bn_test_flow_second_party', 'second_party_id', NULL, 'company_name', 'deleted_flag', NULL, NULL, NULL, b'0', NULL, NULL, '{\"paramList\":[]}', '2021-09-23 15:39:45', '2021-09-23 15:39:45');
INSERT INTO `bn_online_dict` VALUES (1440944049128214528, '商品字典', 1, 1, 'bn_test_flow_product', 'product_id', NULL, 'product_name', 'deleted_flag', NULL, NULL, NULL, b'0', NULL, NULL, '{\"paramList\":[]}', '2021-09-23 15:40:07', '2021-09-23 15:40:07');
INSERT INTO `bn_online_dict` VALUES (1440944184381935616, '付款类型', 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0', NULL, NULL, '{\"dictData\":[{\"type\":\"Integer\",\"id\":1,\"name\":\"预付款\"},{\"type\":\"Integer\",\"id\":2,\"name\":\"分期款\"},{\"type\":\"Integer\",\"id\":3,\"name\":\"项目尾款\"}],\"paramList\":[]}', '2021-09-23 15:40:40', '2021-09-23 15:40:40');
INSERT INTO `bn_online_dict` VALUES (1440944300799037440, '合同类型', 15, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, b'0', NULL, NULL, '{\"dictData\":[{\"type\":\"Integer\",\"id\":1,\"name\":\"生产合同\"},{\"type\":\"Integer\",\"id\":2,\"name\":\"代工合同\"}],\"paramList\":[]}', '2021-09-23 15:41:07', '2021-09-23 15:41:07');
INSERT INTO `bn_online_dict` VALUES (1440944417170001920, '用户字典', 1, 1, 'bn_sys_user', 'user_id', NULL, 'show_name', 'deleted_flag', NULL, NULL, NULL, b'0', NULL, NULL, '{\"paramList\":[]}', '2021-09-23 15:41:35', '2021-09-23 15:41:35');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_form
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_form`;
CREATE TABLE `bn_online_form` (
  `form_id` bigint(20) NOT NULL COMMENT '主键Id',
  `page_id` bigint(20) NOT NULL COMMENT '页面id',
  `form_code` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '表单编码',
  `form_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '表单名称',
  `form_kind` int(11) NOT NULL COMMENT '表单类别',
  `form_type` int(11) NOT NULL COMMENT '表单类型',
  `master_table_id` bigint(20) NOT NULL COMMENT '表单主表id',
  `widget_json` mediumtext COLLATE utf8mb4_bin COMMENT '表单组件JSON',
  `params_json` text COLLATE utf8mb4_bin COMMENT '表单参数JSON',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`form_id`) USING BTREE,
  UNIQUE KEY `uk_page_id_form_code` (`page_id`,`form_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_form
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_form` VALUES (1440945411354267648, 1440945149889744896, 'formFlowLeave', '请假申请', 5, 10, 1440945228079960064, '{\"formConfig\":{\"formKind\":5,\"formType\":10,\"gutter\":20,\"labelWidth\":120,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":10,\"span\":24,\"placeholder\":\"\",\"id\":1632383156919,\"datasourceId\":\"1440945228130291712\",\"tableId\":\"1440945228079960064\",\"columnId\":\"1440945228105125888\",\"columnName\":\"leave_type\",\"showName\":\"请假类型\",\"variableName\":\"leaveType\",\"dictParamList\":[],\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":20,\"span\":24,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":false,\"disabled\":false,\"id\":1632383163405,\"datasourceId\":\"1440945228130291712\",\"tableId\":\"1440945228079960064\",\"columnId\":\"1440945228113514496\",\"columnName\":\"leave_begin_time\",\"showName\":\"开始时间\",\"variableName\":\"leaveBeginTime\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":20,\"span\":24,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":false,\"disabled\":false,\"id\":1632383171595,\"datasourceId\":\"1440945228130291712\",\"tableId\":\"1440945228079960064\",\"columnId\":\"1440945228117708800\",\"columnName\":\"leave_end_time\",\"showName\":\"结束时间\",\"variableName\":\"leaveEndTime\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632383177429,\"datasourceId\":\"1440945228130291712\",\"tableId\":\"1440945228079960064\",\"columnId\":\"1440945228100931584\",\"columnName\":\"leave_reason\",\"showName\":\"请假原因\",\"variableName\":\"leaveReason\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440945228088348672\",\"columnName\":\"id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 15:44:49\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"id\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440945228079960064\",\"updateTime\":\"2021-09-23 15:44:49\",\"userFilter\":false}]', '2021-09-23 15:46:50', '2021-09-23 15:45:32');
INSERT INTO `bn_online_form` VALUES (1440945468593934336, 1440945149889744896, 'formOrderLeave', '请假工单', 5, 11, 1440945228079960064, '{\"formConfig\":{\"formKind\":5,\"formType\":11,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}],\"tableWidget\":{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440945228092542976\",\"tableId\":\"1440945228079960064\",\"showName\":\"请假用户\",\"showOrder\":1,\"sortable\":false},{\"columnId\":\"1440945228105125888\",\"tableId\":\"1440945228079960064\",\"showName\":\"请假类型\",\"showOrder\":2,\"sortable\":false},{\"columnId\":\"1440945228113514496\",\"tableId\":\"1440945228079960064\",\"showName\":\"开始时间\",\"showOrder\":3,\"sortable\":false},{\"columnId\":\"1440945228117708800\",\"tableId\":\"1440945228079960064\",\"showName\":\"结束时间\",\"showOrder\":4,\"sortable\":false}],\"operationList\":[],\"queryParamList\":[],\"tableId\":\"1440945228079960064\",\"variableName\":\"formOrderLeave\",\"showName\":\"请假工单\",\"hasError\":false,\"datasourceId\":\"1440945228130291712\"}},\"widgetList\":[]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440945228088348672\",\"columnName\":\"id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 15:44:49\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"id\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440945228079960064\",\"updateTime\":\"2021-09-23 15:44:49\",\"userFilter\":false}]', '2021-09-23 17:56:51', '2021-09-23 15:45:46');
INSERT INTO `bn_online_form` VALUES (1440947675041107968, 1440946020174270464, 'formFlowSubmit', '报销申请', 5, 10, 1440946127460372480, '{\"formConfig\":{\"formKind\":5,\"formType\":10,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632383719604,\"datasourceId\":\"1440946127531675648\",\"tableId\":\"1440946127460372480\",\"columnId\":\"1440946127481344000\",\"columnName\":\"submit_name\",\"showName\":\"报销名称\",\"variableName\":\"submitName\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632383720428,\"datasourceId\":\"1440946127531675648\",\"tableId\":\"1440946127460372480\",\"columnId\":\"1440946127489732608\",\"columnName\":\"submit_kind\",\"showName\":\"报销类别\",\"variableName\":\"submitKind\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":3,\"span\":12,\"defaultValue\":0,\"min\":0,\"step\":1,\"precision\":2,\"controlVisible\":1,\"controlPosition\":0,\"readOnly\":false,\"disabled\":false,\"id\":1632383724574,\"datasourceId\":\"1440946127531675648\",\"tableId\":\"1440946127460372480\",\"columnId\":\"1440946127493926912\",\"columnName\":\"total_amount\",\"showName\":\"报销金额\",\"variableName\":\"totalAmount\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632383744379,\"datasourceId\":\"1440946127531675648\",\"tableId\":\"1440946127460372480\",\"columnId\":\"1440946127498121216\",\"columnName\":\"description\",\"showName\":\"报销描述\",\"variableName\":\"description\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632383752809,\"datasourceId\":\"1440946127531675648\",\"tableId\":\"1440946127460372480\",\"columnId\":\"1440946127506509824\",\"columnName\":\"memo\",\"showName\":\"备注\",\"variableName\":\"memo\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440947089235251200\",\"tableId\":\"1440947089218473984\",\"showName\":\"费用类型\",\"showOrder\":1,\"sortable\":false,\"relationId\":\"1440947089268805632\",\"dataFieldName\":null},{\"columnId\":\"1440947089252028416\",\"tableId\":\"1440947089218473984\",\"showName\":\"金额\",\"showOrder\":2,\"sortable\":false,\"relationId\":\"1440947089268805632\",\"dataFieldName\":null},{\"columnId\":\"1440947089260417024\",\"tableId\":\"1440947089218473984\",\"showName\":\"报销凭证\",\"showOrder\":3,\"sortable\":false,\"relationId\":\"1440947089268805632\",\"dataFieldName\":null},{\"columnId\":\"1440947089264611328\",\"tableId\":\"1440947089218473984\",\"showName\":\"费用描述\",\"showOrder\":4,\"sortable\":false,\"relationId\":\"1440947089268805632\",\"dataFieldName\":null}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":true,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false,\"formId\":\"1440947791881834496\"},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\",\"formId\":\"1440947791881834496\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[{\"tableId\":\"1440947089218473984\",\"columnId\":\"1440947089231056896\",\"paramValueType\":0,\"paramValue\":\"id\"}],\"id\":1632383762135,\"relationId\":\"1440947089268805632\",\"tableId\":\"1440947089218473984\",\"columnName\":\"id_bn_test_flow_submit_detail_submit_idRelation\",\"showName\":\"报销详情\",\"variableName\":\"id_bn_test_flow_submit_detail_submit_idRelation\",\"dictParamList\":null,\"hasError\":false}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440946127468761088\",\"columnName\":\"id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 15:48:23\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"id\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440946127460372480\",\"updateTime\":\"2021-09-23 15:48:23\",\"userFilter\":false}]', '2021-09-23 15:58:07', '2021-09-23 15:54:32');
INSERT INTO `bn_online_form` VALUES (1440947791881834496, 1440946020174270464, 'formEditFlowSubmitDetail', '编辑报销详情', 1, 5, 1440947089218473984, '{\"formConfig\":{\"formKind\":1,\"formType\":5,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":10,\"span\":24,\"placeholder\":\"\",\"id\":1632383900080,\"datasourceId\":\"1440946127531675648\",\"relationId\":\"1440947089268805632\",\"tableId\":\"1440947089218473984\",\"columnId\":\"1440947089235251200\",\"columnName\":\"expense_type\",\"showName\":\"费用类型\",\"variableName\":\"expenseType\",\"dictParamList\":[],\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":20,\"span\":24,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":false,\"disabled\":false,\"id\":1632383900902,\"datasourceId\":\"1440946127531675648\",\"relationId\":\"1440947089268805632\",\"tableId\":\"1440947089218473984\",\"columnId\":\"1440947089243639808\",\"columnName\":\"expense_time\",\"showName\":\"发生日期\",\"variableName\":\"expenseTime\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":3,\"span\":24,\"defaultValue\":0,\"min\":0,\"step\":1,\"precision\":2,\"controlVisible\":1,\"controlPosition\":0,\"readOnly\":false,\"disabled\":false,\"id\":1632383906027,\"datasourceId\":\"1440946127531675648\",\"relationId\":\"1440947089268805632\",\"tableId\":\"1440947089218473984\",\"columnId\":\"1440947089252028416\",\"columnName\":\"amount\",\"showName\":\"金额\",\"variableName\":\"amount\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":4,\"readOnly\":false,\"disabled\":false,\"id\":1632383907850,\"datasourceId\":\"1440946127531675648\",\"relationId\":\"1440947089268805632\",\"tableId\":\"1440947089218473984\",\"columnId\":\"1440947089264611328\",\"columnName\":\"description\",\"showName\":\"费用描述\",\"variableName\":\"description\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":31,\"span\":24,\"isImage\":true,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632383908452,\"datasourceId\":\"1440946127531675648\",\"relationId\":\"1440947089268805632\",\"tableId\":\"1440947089218473984\",\"columnId\":\"1440947089260417024\",\"columnName\":\"image_url\",\"showName\":\"报销凭证\",\"variableName\":\"imageUrl\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440947089222668288\",\"columnName\":\"id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 15:52:12\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"id\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440947089218473984\",\"updateTime\":\"2021-09-23 15:52:12\",\"userFilter\":false}]', '2021-09-23 15:59:08', '2021-09-23 15:55:00');
INSERT INTO `bn_online_form` VALUES (1440954920348946432, 1440952710487609344, 'formFlowContract', '合同审批', 5, 10, 1440952815294877696, '{\"formConfig\":{\"formKind\":5,\"formType\":10,\"gutter\":20,\"labelWidth\":120,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"contract_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385543720,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815307460608\",\"columnName\":\"first_party_id\",\"showName\":\"甲方企业\",\"variableName\":\"firstPartyId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385544316,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815311654912\",\"columnName\":\"second_party_id\",\"showName\":\"乙方企业\",\"variableName\":\"secondPartyId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385545012,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815315849216\",\"columnName\":\"contract_type\",\"showName\":\"合同类型\",\"variableName\":\"contractType\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385545729,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815324237824\",\"columnName\":\"sales_id\",\"showName\":\"业务员\",\"variableName\":\"salesId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":20,\"span\":12,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":false,\"disabled\":false,\"id\":1632385550074,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815320043520\",\"columnName\":\"due_date\",\"showName\":\"到期日期\",\"variableName\":\"dueDate\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":3,\"span\":12,\"defaultValue\":0,\"min\":0,\"step\":1,\"precision\":2,\"controlVisible\":1,\"controlPosition\":0,\"readOnly\":false,\"disabled\":false,\"id\":1632385552142,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815332626432\",\"columnName\":\"commission_rate\",\"showName\":\"提成比例\",\"variableName\":\"commissionRate\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":31,\"span\":12,\"isImage\":false,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632385586521,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815336820736\",\"columnName\":\"attachment\",\"showName\":\"合同附件\",\"variableName\":\"attachment\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":31,\"span\":12,\"isImage\":false,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632385587136,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815341015040\",\"columnName\":\"security_attachment\",\"showName\":\"保密协议\",\"variableName\":\"securityAttachment\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":31,\"span\":12,\"isImage\":false,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632385587746,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815345209344\",\"columnName\":\"intellectual_property_attachment\",\"showName\":\"知识产权协议\",\"variableName\":\"intellectualPropertyAttachment\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":31,\"span\":12,\"isImage\":false,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632385589128,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815349403648\",\"columnName\":\"other_attachment\",\"showName\":\"其他附件\",\"variableName\":\"otherAttachment\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":1,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440953088922882048\",\"tableId\":\"1440953088901910528\",\"showName\":\"合同产品\",\"showOrder\":1,\"sortable\":false,\"relationId\":\"1440953088977408000\",\"dataFieldName\":null},{\"columnId\":\"1440953088927076352\",\"tableId\":\"1440953088901910528\",\"showName\":\"产品数量\",\"showOrder\":2,\"sortable\":false,\"relationId\":\"1440953088977408000\",\"dataFieldName\":null},{\"columnId\":\"1440953088935464960\",\"tableId\":\"1440953088901910528\",\"showName\":\"产品总价\",\"showOrder\":3,\"sortable\":false,\"relationId\":\"1440953088977408000\",\"dataFieldName\":null},{\"columnId\":\"1440953088939659264\",\"tableId\":\"1440953088901910528\",\"showName\":\"备注\",\"showOrder\":4,\"sortable\":false,\"relationId\":\"1440953088977408000\",\"dataFieldName\":null}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":true,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false,\"formId\":\"1440955295755931648\"},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\",\"formId\":\"1440955295755931648\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[{\"tableId\":\"1440953088901910528\",\"columnId\":\"1440953088918687744\",\"paramValueType\":1,\"paramValue\":\"1440952815303266304\"}],\"id\":1632385613866,\"relationId\":\"1440953088977408000\",\"tableId\":\"1440953088901910528\",\"columnName\":\"contract_id_bn_test_flow_contract_detail_contract_idRelation\",\"showName\":\"合同详情\",\"variableName\":\"contract_id_bn_test_flow_contract_detail_contract_idRelation\",\"dictParamList\":null,\"hasError\":false},{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":1,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440953245684994048\",\"tableId\":\"1440953245664022528\",\"showName\":\"交付日期\",\"showOrder\":1,\"sortable\":false,\"relationId\":\"1440953245747908608\",\"dataFieldName\":null},{\"columnId\":\"1440953245697576960\",\"tableId\":\"1440953245664022528\",\"showName\":\"交付产品\",\"showOrder\":2,\"sortable\":false,\"relationId\":\"1440953245747908608\",\"dataFieldName\":null},{\"columnId\":\"1440953245705965568\",\"tableId\":\"1440953245664022528\",\"showName\":\"交付数量\",\"showOrder\":3,\"sortable\":false,\"relationId\":\"1440953245747908608\",\"dataFieldName\":null},{\"columnId\":\"1440953245718548480\",\"tableId\":\"1440953245664022528\",\"showName\":\"备注\",\"showOrder\":4,\"sortable\":false,\"relationId\":\"1440953245747908608\",\"dataFieldName\":null}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":true,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false,\"formId\":\"1440955424638504960\"},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\",\"formId\":\"1440955424638504960\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[{\"tableId\":\"1440953245664022528\",\"columnId\":\"1440953245676605440\",\"paramValueType\":1,\"paramValue\":\"1440952815303266304\"}],\"id\":1632385617093,\"relationId\":\"1440953245747908608\",\"tableId\":\"1440953245664022528\",\"columnName\":\"contract_id_bn_test_flow_delivery_detail_contract_idRelation\",\"showName\":\"交付详情\",\"variableName\":\"contract_id_bn_test_flow_delivery_detail_contract_idRelation\",\"dictParamList\":null,\"hasError\":false},{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440953170535649280\",\"tableId\":\"1440953170514677760\",\"showName\":\"付款日期\",\"showOrder\":1,\"sortable\":false,\"relationId\":\"1440953170590175232\",\"dataFieldName\":null},{\"columnId\":\"1440953170539843584\",\"tableId\":\"1440953170514677760\",\"showName\":\"付款类型\",\"showOrder\":2,\"sortable\":false,\"relationId\":\"1440953170590175232\",\"dataFieldName\":null},{\"columnId\":\"1440953170548232192\",\"tableId\":\"1440953170514677760\",\"showName\":\"付款比例（%）\",\"showOrder\":3,\"sortable\":false,\"relationId\":\"1440953170590175232\",\"dataFieldName\":null},{\"columnId\":\"1440953170552426496\",\"tableId\":\"1440953170514677760\",\"showName\":\"备注\",\"showOrder\":4,\"sortable\":false,\"relationId\":\"1440953170590175232\",\"dataFieldName\":null}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":true,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false,\"formId\":\"1440955361006718976\"},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\",\"formId\":\"1440955361006718976\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[{\"tableId\":\"1440953170514677760\",\"columnId\":\"1440953170531454976\",\"paramValueType\":1,\"paramValue\":\"1440952815303266304\"}],\"id\":1632385617965,\"relationId\":\"1440953170590175232\",\"tableId\":\"1440953170514677760\",\"columnName\":\"contract_id_bn_test_flow_pay_detail_contract_idRelation\",\"showName\":\"付款详情\",\"variableName\":\"contract_id_bn_test_flow_pay_detail_contract_idRelation\",\"dictParamList\":null,\"hasError\":false}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440952815303266304\",\"columnName\":\"contract_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:14:57\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"contractId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440952815294877696\",\"updateTime\":\"2021-09-23 16:14:57\",\"userFilter\":false}]', '2021-09-24 09:36:35', '2021-09-23 16:23:19');
INSERT INTO `bn_online_form` VALUES (1440955001093492736, 1440952710487609344, 'formFlowContractLaw', '法务信息', 5, 10, 1440952815294877696, '{\"formConfig\":{\"formKind\":5,\"formType\":10,\"gutter\":20,\"labelWidth\":120,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"contract_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":2,\"widgetType\":40,\"span\":24,\"position\":\"center\",\"id\":1632385804854,\"columnName\":\"divider\",\"showName\":\"甲方信息\",\"variableName\":\"divider\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632385840611,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440952921117167616\",\"tableId\":\"1440952921024892928\",\"columnId\":\"1440952921041670144\",\"columnName\":\"company_name\",\"showName\":\"公司名称\",\"variableName\":\"companyName\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632385850871,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440952921117167616\",\"tableId\":\"1440952921024892928\",\"columnId\":\"1440952921066835968\",\"columnName\":\"contact_info\",\"showName\":\"联系方式\",\"variableName\":\"contactInfo\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632385854474,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440952921117167616\",\"tableId\":\"1440952921024892928\",\"columnId\":\"1440952921058447360\",\"columnName\":\"registry_address\",\"showName\":\"注册地址\",\"variableName\":\"registryAddress\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632385855796,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440952921117167616\",\"tableId\":\"1440952921024892928\",\"columnId\":\"1440952921079418880\",\"columnName\":\"business_scope\",\"showName\":\"经营范围\",\"variableName\":\"businessScope\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":2,\"widgetType\":40,\"span\":24,\"position\":\"center\",\"id\":1632385870258,\"columnName\":\"divider\",\"showName\":\"乙方信息\",\"variableName\":\"divider1\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632385881057,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440952988536410112\",\"tableId\":\"1440952988389609472\",\"columnId\":\"1440952988406386688\",\"columnName\":\"company_name\",\"showName\":\"公司名称\",\"variableName\":\"companyName1\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632385882845,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440952988536410112\",\"tableId\":\"1440952988389609472\",\"columnId\":\"1440952988486078464\",\"columnName\":\"contact_info\",\"showName\":\"联系方式\",\"variableName\":\"contactInfo1\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632385886068,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440952988536410112\",\"tableId\":\"1440952988389609472\",\"columnId\":\"1440952988473495552\",\"columnName\":\"registry_address\",\"showName\":\"注册地址\",\"variableName\":\"registryAddress1\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632385886979,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440952988536410112\",\"tableId\":\"1440952988389609472\",\"columnId\":\"1440952988494467072\",\"columnName\":\"business_scope\",\"showName\":\"经营范围\",\"variableName\":\"businessScope1\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":2,\"widgetType\":40,\"span\":24,\"position\":\"center\",\"id\":1632385900924,\"columnName\":\"divider\",\"showName\":\"合同信息\",\"variableName\":\"divider2\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385921859,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815315849216\",\"columnName\":\"contract_type\",\"showName\":\"合同类型\",\"variableName\":\"contractType\",\"readOnly\":true,\"dictParamList\":[],\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":20,\"span\":12,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":true,\"disabled\":false,\"id\":1632385943091,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815320043520\",\"columnName\":\"due_date\",\"showName\":\"到期日期\",\"variableName\":\"dueDate\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":31,\"span\":12,\"isImage\":false,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632385951296,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815336820736\",\"columnName\":\"attachment\",\"showName\":\"合同附件\",\"variableName\":\"attachment\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":31,\"span\":12,\"isImage\":false,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632385954049,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815341015040\",\"columnName\":\"security_attachment\",\"showName\":\"保密协议\",\"variableName\":\"securityAttachment\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":31,\"span\":12,\"isImage\":false,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632385954722,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815345209344\",\"columnName\":\"intellectual_property_attachment\",\"showName\":\"知识产权协议\",\"variableName\":\"intellectualPropertyAttachment\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":31,\"span\":12,\"isImage\":false,\"fileFieldName\":\"uploadFile\",\"actionUrl\":\"/admin/flow/flowOnlineOperation/upload\",\"downloadUrl\":\"/admin/flow/flowOnlineOperation/download\",\"id\":1632385955926,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815349403648\",\"columnName\":\"other_attachment\",\"showName\":\"其他附件\",\"variableName\":\"otherAttachment\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440952815303266304\",\"columnName\":\"contract_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:14:57\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"contractId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440952815294877696\",\"updateTime\":\"2021-09-23 16:14:57\",\"userFilter\":false}]', '2021-09-23 16:32:45', '2021-09-23 16:23:39');
INSERT INTO `bn_online_form` VALUES (1440955127790833664, 1440952710487609344, 'formFlowContractPay', '付款详情', 5, 10, 1440952815294877696, '{\"formConfig\":{\"formKind\":5,\"formType\":10,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"contract_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385985050,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815307460608\",\"columnName\":\"first_party_id\",\"showName\":\"甲方企业\",\"variableName\":\"firstPartyId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385985722,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815311654912\",\"columnName\":\"second_party_id\",\"showName\":\"乙方企业\",\"variableName\":\"secondPartyId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385986562,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815315849216\",\"columnName\":\"contract_type\",\"showName\":\"合同类型\",\"variableName\":\"contractType\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632385987354,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815324237824\",\"columnName\":\"sales_id\",\"showName\":\"业务员\",\"variableName\":\"salesId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":20,\"span\":12,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":false,\"disabled\":false,\"id\":1632385998420,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815320043520\",\"columnName\":\"due_date\",\"showName\":\"到期日期\",\"variableName\":\"dueDate\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440953170535649280\",\"tableId\":\"1440953170514677760\",\"showName\":\"付款日期\",\"showOrder\":1,\"sortable\":false,\"relationId\":\"1440953170590175232\",\"dataFieldName\":null},{\"columnId\":\"1440953170539843584\",\"tableId\":\"1440953170514677760\",\"showName\":\"付款类型\",\"showOrder\":2,\"sortable\":false,\"relationId\":\"1440953170590175232\",\"dataFieldName\":null},{\"columnId\":\"1440953170548232192\",\"tableId\":\"1440953170514677760\",\"showName\":\"付款比例（%）\",\"showOrder\":3,\"sortable\":false,\"relationId\":\"1440953170590175232\",\"dataFieldName\":null},{\"columnId\":\"1440953170552426496\",\"tableId\":\"1440953170514677760\",\"showName\":\"备注\",\"showOrder\":4,\"sortable\":false,\"relationId\":\"1440953170590175232\",\"dataFieldName\":null}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":false,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":false,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":false,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[{\"tableId\":\"1440953170514677760\",\"columnId\":\"1440953170531454976\",\"paramValueType\":1,\"paramValue\":\"1440952815303266304\"}],\"id\":1632386005350,\"relationId\":\"1440953170590175232\",\"tableId\":\"1440953170514677760\",\"columnName\":\"contract_id_bn_test_flow_pay_detail_contract_idRelation\",\"showName\":\"付款详情\",\"variableName\":\"contract_id_bn_test_flow_pay_detail_contract_idRelation\",\"dictParamList\":null,\"hasError\":false}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440952815303266304\",\"columnName\":\"contract_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:14:57\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"contractId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440952815294877696\",\"updateTime\":\"2021-09-23 16:14:57\",\"userFilter\":false}]', '2021-09-23 16:45:43', '2021-09-23 16:24:09');
INSERT INTO `bn_online_form` VALUES (1440955194991972352, 1440952710487609344, 'formFlowContractDetail', '合同详情', 5, 10, 1440952815294877696, '{\"formConfig\":{\"formKind\":5,\"formType\":10,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"contract_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632386076617,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815307460608\",\"columnName\":\"first_party_id\",\"showName\":\"甲方企业\",\"variableName\":\"firstPartyId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632386077249,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815311654912\",\"columnName\":\"second_party_id\",\"showName\":\"乙方企业\",\"variableName\":\"secondPartyId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632386077906,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815315849216\",\"columnName\":\"contract_type\",\"showName\":\"合同类型\",\"variableName\":\"contractType\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":12,\"placeholder\":\"\",\"id\":1632386078973,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815324237824\",\"columnName\":\"sales_id\",\"showName\":\"业务员\",\"variableName\":\"salesId\",\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":20,\"span\":12,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":false,\"disabled\":false,\"id\":1632386079478,\"datasourceId\":\"1440952815374569472\",\"tableId\":\"1440952815294877696\",\"columnId\":\"1440952815320043520\",\"columnName\":\"due_date\",\"showName\":\"到期日期\",\"variableName\":\"dueDate\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440953088922882048\",\"tableId\":\"1440953088901910528\",\"showName\":\"合同产品\",\"showOrder\":1,\"sortable\":false,\"relationId\":\"1440953088977408000\",\"dataFieldName\":null},{\"columnId\":\"1440953088927076352\",\"tableId\":\"1440953088901910528\",\"showName\":\"产品数量\",\"showOrder\":2,\"sortable\":false,\"relationId\":\"1440953088977408000\",\"dataFieldName\":null},{\"columnId\":\"1440953088935464960\",\"tableId\":\"1440953088901910528\",\"showName\":\"产品总价\",\"showOrder\":3,\"sortable\":false,\"relationId\":\"1440953088977408000\",\"dataFieldName\":null},{\"columnId\":\"1440953088939659264\",\"tableId\":\"1440953088901910528\",\"showName\":\"备注\",\"showOrder\":4,\"sortable\":false,\"relationId\":\"1440953088977408000\",\"dataFieldName\":null}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":false,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":false,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":false,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[{\"tableId\":\"1440953088901910528\",\"columnId\":\"1440953088918687744\",\"paramValueType\":1,\"paramValue\":\"1440952815303266304\"}],\"id\":1632386082967,\"relationId\":\"1440953088977408000\",\"tableId\":\"1440953088901910528\",\"columnName\":\"contract_id_bn_test_flow_contract_detail_contract_idRelation\",\"showName\":\"合同详情\",\"variableName\":\"contract_id_bn_test_flow_contract_detail_contract_idRelation\",\"dictParamList\":null,\"hasError\":false},{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440953245684994048\",\"tableId\":\"1440953245664022528\",\"showName\":\"交付日期\",\"showOrder\":1,\"sortable\":false,\"relationId\":\"1440953245747908608\",\"dataFieldName\":null},{\"columnId\":\"1440953245697576960\",\"tableId\":\"1440953245664022528\",\"showName\":\"交付产品\",\"showOrder\":2,\"sortable\":false,\"relationId\":\"1440953245747908608\",\"dataFieldName\":null},{\"columnId\":\"1440953245705965568\",\"tableId\":\"1440953245664022528\",\"showName\":\"交付数量\",\"showOrder\":3,\"sortable\":false,\"relationId\":\"1440953245747908608\",\"dataFieldName\":null},{\"columnId\":\"1440953245718548480\",\"tableId\":\"1440953245664022528\",\"showName\":\"备注\",\"showOrder\":4,\"sortable\":false,\"relationId\":\"1440953245747908608\",\"dataFieldName\":null}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":false,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":false,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":false,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[{\"tableId\":\"1440953245664022528\",\"columnId\":\"1440953245676605440\",\"paramValueType\":1,\"paramValue\":\"1440952815303266304\"}],\"id\":1632386085719,\"relationId\":\"1440953245747908608\",\"tableId\":\"1440953245664022528\",\"columnName\":\"contract_id_bn_test_flow_delivery_detail_contract_idRelation\",\"showName\":\"交付详情\",\"variableName\":\"contract_id_bn_test_flow_delivery_detail_contract_idRelation\",\"dictParamList\":null,\"hasError\":false}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440952815303266304\",\"columnName\":\"contract_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:14:57\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"contractId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440952815294877696\",\"updateTime\":\"2021-09-23 16:14:57\",\"userFilter\":false}]', '2021-09-23 16:46:06', '2021-09-23 16:24:25');
INSERT INTO `bn_online_form` VALUES (1440955295755931648, 1440952710487609344, 'formEditProduct', '编辑商品信息', 1, 5, 1440953088901910528, '{\"formConfig\":{\"formKind\":1,\"formType\":5,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"contract_detail_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":10,\"span\":24,\"placeholder\":\"\",\"id\":1632386167477,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953088977408000\",\"tableId\":\"1440953088901910528\",\"columnId\":\"1440953088922882048\",\"columnName\":\"product_id\",\"showName\":\"合同产品\",\"variableName\":\"productId\",\"dictParamList\":[],\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":3,\"span\":24,\"defaultValue\":0,\"min\":0,\"step\":1,\"precision\":0,\"controlVisible\":1,\"controlPosition\":0,\"readOnly\":false,\"disabled\":false,\"id\":1632386168097,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953088977408000\",\"tableId\":\"1440953088901910528\",\"columnId\":\"1440953088927076352\",\"columnName\":\"total_count\",\"showName\":\"产品数量\",\"variableName\":\"totalCount\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":3,\"span\":24,\"defaultValue\":0,\"min\":0,\"step\":1,\"precision\":2,\"controlVisible\":1,\"controlPosition\":0,\"readOnly\":false,\"disabled\":false,\"id\":1632386168937,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953088977408000\",\"tableId\":\"1440953088901910528\",\"columnId\":\"1440953088935464960\",\"columnName\":\"total_amount\",\"showName\":\"产品总价\",\"variableName\":\"totalAmount\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":4,\"readOnly\":false,\"disabled\":false,\"id\":1632386170038,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953088977408000\",\"tableId\":\"1440953088901910528\",\"columnId\":\"1440953088939659264\",\"columnName\":\"meno\",\"showName\":\"备注\",\"variableName\":\"meno\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440953088910299136\",\"columnName\":\"contract_detail_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:16:03\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"contractDetailId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440953088901910528\",\"updateTime\":\"2021-09-23 16:16:03\",\"userFilter\":false}]', '2021-09-23 16:40:38', '2021-09-23 16:24:49');
INSERT INTO `bn_online_form` VALUES (1440955361006718976, 1440952710487609344, 'formEditPay', '编辑付款信息', 1, 5, 1440953170514677760, '{\"formConfig\":{\"formKind\":1,\"formType\":5,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"pay_detail_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":10,\"span\":24,\"placeholder\":\"\",\"id\":1632386206508,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953170590175232\",\"tableId\":\"1440953170514677760\",\"columnId\":\"1440953170539843584\",\"columnName\":\"pay_type\",\"showName\":\"付款类型\",\"variableName\":\"payType\",\"dictParamList\":[],\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":20,\"span\":24,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":false,\"disabled\":false,\"id\":1632386207312,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953170590175232\",\"tableId\":\"1440953170514677760\",\"columnId\":\"1440953170535649280\",\"columnName\":\"pay_date\",\"showName\":\"付款日期\",\"variableName\":\"payDate\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":3,\"span\":24,\"defaultValue\":0,\"min\":0,\"max\":100,\"step\":1,\"precision\":2,\"controlVisible\":1,\"controlPosition\":0,\"readOnly\":false,\"disabled\":false,\"id\":1632386208455,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953170590175232\",\"tableId\":\"1440953170514677760\",\"columnId\":\"1440953170548232192\",\"columnName\":\"percentage\",\"showName\":\"百分比\",\"variableName\":\"percentage\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386209209,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953170590175232\",\"tableId\":\"1440953170514677760\",\"columnId\":\"1440953170552426496\",\"columnName\":\"memo\",\"showName\":\"备注\",\"variableName\":\"memo\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440953170518872064\",\"columnName\":\"pay_detail_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:16:22\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"payDetailId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440953170514677760\",\"updateTime\":\"2021-09-23 16:16:22\",\"userFilter\":false}]', '2021-09-23 16:40:43', '2021-09-23 16:25:04');
INSERT INTO `bn_online_form` VALUES (1440955424638504960, 1440952710487609344, 'formEditDelivery', '编辑交付信息', 1, 5, 1440953245664022528, '{\"formConfig\":{\"formKind\":1,\"formType\":5,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"delivery_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":20,\"span\":24,\"placeholder\":\"\",\"type\":\"date\",\"format\":\"yyyy-MM-dd\",\"valueFormat\":\"yyyy-MM-dd\",\"readOnly\":false,\"disabled\":false,\"id\":1632386237515,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953245747908608\",\"tableId\":\"1440953245664022528\",\"columnId\":\"1440953245684994048\",\"columnName\":\"delivery_date\",\"showName\":\"交付日期\",\"variableName\":\"deliveryDate\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":10,\"span\":24,\"placeholder\":\"\",\"id\":1632386238087,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953245747908608\",\"tableId\":\"1440953245664022528\",\"columnId\":\"1440953245697576960\",\"columnName\":\"product_id\",\"showName\":\"交付产品\",\"variableName\":\"productId\",\"dictParamList\":[],\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":3,\"span\":24,\"defaultValue\":0,\"min\":0,\"step\":1,\"precision\":0,\"controlVisible\":1,\"controlPosition\":0,\"readOnly\":false,\"disabled\":false,\"id\":1632386238799,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953245747908608\",\"tableId\":\"1440953245664022528\",\"columnId\":\"1440953245705965568\",\"columnName\":\"total_count\",\"showName\":\"交付数量\",\"variableName\":\"totalCount\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":4,\"readOnly\":false,\"disabled\":false,\"id\":1632386239345,\"datasourceId\":\"1440952815374569472\",\"relationId\":\"1440953245747908608\",\"tableId\":\"1440953245664022528\",\"columnId\":\"1440953245718548480\",\"columnName\":\"memo\",\"showName\":\"备注\",\"variableName\":\"memo\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440953245668216832\",\"columnName\":\"delivery_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:16:40\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"deliveryId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440953245664022528\",\"updateTime\":\"2021-09-23 16:16:40\",\"userFilter\":false}]', '2021-09-23 16:40:49', '2021-09-23 16:25:20');
INSERT INTO `bn_online_form` VALUES (1440955483438452736, 1440952710487609344, 'formOrderContract', '合同工单', 5, 11, 1440952815294877696, '{\"formConfig\":{\"formKind\":5,\"formType\":11,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"contract_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}],\"tableWidget\":{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440952815307460608\",\"tableId\":\"1440952815294877696\",\"showName\":\"甲方企业\",\"showOrder\":1,\"sortable\":false},{\"columnId\":\"1440952815311654912\",\"tableId\":\"1440952815294877696\",\"showName\":\"乙方企业\",\"showOrder\":2,\"sortable\":false},{\"columnId\":\"1440952815315849216\",\"tableId\":\"1440952815294877696\",\"showName\":\"合同类型\",\"showOrder\":3,\"sortable\":false},{\"columnId\":\"1440952815320043520\",\"tableId\":\"1440952815294877696\",\"showName\":\"到期日期\",\"showOrder\":4,\"sortable\":false},{\"columnId\":\"1440952815324237824\",\"tableId\":\"1440952815294877696\",\"showName\":\"业务员\",\"showOrder\":5,\"sortable\":false}],\"operationList\":[],\"queryParamList\":[],\"tableId\":\"1440952815294877696\",\"variableName\":\"formOrderContract\",\"showName\":\"合同工单\",\"hasError\":false,\"datasourceId\":\"1440952815374569472\"}},\"widgetList\":[]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440952815303266304\",\"columnName\":\"contract_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:14:57\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"contractId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440952815294877696\",\"updateTime\":\"2021-09-23 16:14:57\",\"userFilter\":false}]', '2021-09-23 16:38:33', '2021-09-23 16:25:34');
INSERT INTO `bn_online_form` VALUES (1440959226632474624, 1440958861153406976, 'formFirstParty', '甲方企业管理', 5, 1, 1440958971128057856, '{\"formConfig\":{\"formKind\":5,\"formType\":1,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[],\"tableWidget\":{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440958971140640768\",\"tableId\":\"1440958971128057856\",\"showName\":\"公司名称\",\"showOrder\":1,\"sortable\":false},{\"columnId\":\"1440958971144835072\",\"tableId\":\"1440958971128057856\",\"showName\":\"公司法人\",\"showOrder\":2,\"sortable\":false},{\"columnId\":\"1440958971157417984\",\"tableId\":\"1440958971128057856\",\"showName\":\"联系方式\",\"showOrder\":3,\"sortable\":false},{\"columnId\":\"1440958971153223680\",\"tableId\":\"1440958971128057856\",\"showName\":\"注册地址\",\"showOrder\":4,\"sortable\":false}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":true,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false,\"formId\":\"1440959420396736512\"},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\",\"formId\":\"1440959420396736512\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[],\"tableId\":\"1440958971128057856\",\"variableName\":\"formFirstParty\",\"showName\":\"甲方企业管理\",\"datasourceId\":\"1440958971190972416\",\"hasError\":false}},\"widgetList\":[{\"widgetKind\":0,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386494962,\"datasourceId\":\"1440958971190972416\",\"tableId\":\"1440958971128057856\",\"columnId\":\"1440958971140640768\",\"columnName\":\"company_name\",\"showName\":\"公司名称\",\"variableName\":\"companyName\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]}]}', '[{\"autoIncrement\":false,\"columnComment\":\"公司名称\",\"columnId\":\"1440958971140640768\",\"columnName\":\"company_name\",\"columnShowOrder\":2,\"columnType\":\"varchar\",\"createTime\":\"2021-09-23 16:39:25\",\"deptFilter\":false,\"filterType\":3,\"fullColumnType\":\"varchar(255)\",\"nullable\":false,\"objectFieldName\":\"companyName\",\"objectFieldType\":\"String\",\"parentKey\":false,\"primaryKey\":false,\"tableId\":\"1440958971128057856\",\"updateTime\":\"2021-09-23 16:41:25\",\"userFilter\":false}]', '2021-09-23 16:42:32', '2021-09-23 16:40:26');
INSERT INTO `bn_online_form` VALUES (1440959420396736512, 1440958861153406976, 'formViewFirstParty', '编辑甲方企业', 1, 5, 1440958971128057856, '{\"formConfig\":{\"formKind\":1,\"formType\":5,\"gutter\":20,\"labelWidth\":120,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"first_party_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386807099,\"datasourceId\":\"1440958971190972416\",\"tableId\":\"1440958971128057856\",\"columnId\":\"1440958971140640768\",\"columnName\":\"company_name\",\"showName\":\"公司名称\",\"variableName\":\"companyName\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386808030,\"datasourceId\":\"1440958971190972416\",\"tableId\":\"1440958971128057856\",\"columnId\":\"1440958971144835072\",\"columnName\":\"legal_person\",\"showName\":\"公司法人\",\"variableName\":\"legalPerson\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386809518,\"datasourceId\":\"1440958971190972416\",\"tableId\":\"1440958971128057856\",\"columnId\":\"1440958971149029376\",\"columnName\":\"legal_person_id\",\"showName\":\"法人身份证号\",\"variableName\":\"legalPersonId\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386811364,\"datasourceId\":\"1440958971190972416\",\"tableId\":\"1440958971128057856\",\"columnId\":\"1440958971157417984\",\"columnName\":\"contact_info\",\"showName\":\"联系方式\",\"variableName\":\"contactInfo\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386814864,\"datasourceId\":\"1440958971190972416\",\"tableId\":\"1440958971128057856\",\"columnId\":\"1440958971153223680\",\"columnName\":\"registry_address\",\"showName\":\"注册地址\",\"variableName\":\"registryAddress\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386817801,\"datasourceId\":\"1440958971190972416\",\"tableId\":\"1440958971128057856\",\"columnId\":\"1440958971161612288\",\"columnName\":\"business_scope\",\"showName\":\"经营范围\",\"variableName\":\"businessScope\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386819192,\"datasourceId\":\"1440958971190972416\",\"tableId\":\"1440958971128057856\",\"columnId\":\"1440958971165806592\",\"columnName\":\"memo\",\"showName\":\"备注\",\"variableName\":\"memo\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440958971132252160\",\"columnName\":\"first_party_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:39:25\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"firstPartyId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440958971128057856\",\"updateTime\":\"2021-09-23 16:39:25\",\"userFilter\":false}]', '2021-09-23 16:47:25', '2021-09-23 16:41:12');
INSERT INTO `bn_online_form` VALUES (1440961456601305088, 1440961119001776128, 'formSecondParty', '乙方管理', 5, 1, 1440961208273342464, '{\"formConfig\":{\"formKind\":5,\"formType\":1,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[],\"tableWidget\":{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440961208294313984\",\"tableId\":\"1440961208273342464\",\"showName\":\"公司名称\",\"showOrder\":1,\"sortable\":false,\"dataFieldName\":\"company_name\"},{\"columnId\":\"1440961208298508288\",\"tableId\":\"1440961208273342464\",\"showName\":\"公司法人\",\"showOrder\":2,\"sortable\":false,\"dataFieldName\":\"legal_person\"},{\"columnId\":\"1440961208315285504\",\"tableId\":\"1440961208273342464\",\"showName\":\"联系方式\",\"showOrder\":3,\"sortable\":false,\"dataFieldName\":\"contact_info\"},{\"columnId\":\"1440961208306896896\",\"tableId\":\"1440961208273342464\",\"showName\":\"注册地址\",\"showOrder\":4,\"sortable\":false,\"dataFieldName\":\"registry_address\"}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":true,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false,\"formId\":\"1440961779596267520\"},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\",\"formId\":\"1440961779596267520\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[],\"tableId\":\"1440961208273342464\",\"variableName\":\"formSecondParty\",\"showName\":\"乙方管理\",\"datasourceId\":\"1440961208344645633\",\"hasError\":false}},\"widgetList\":[{\"widgetKind\":0,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632386975051,\"datasourceId\":\"1440961208344645633\",\"tableId\":\"1440961208273342464\",\"columnId\":\"1440961208294313984\",\"columnName\":\"company_name\",\"showName\":\"公司名称\",\"variableName\":\"companyName\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[],\"hasError\":false}]}', '[{\"autoIncrement\":false,\"columnComment\":\"公司名称\",\"columnId\":\"1440961208294313984\",\"columnName\":\"company_name\",\"columnShowOrder\":2,\"columnType\":\"varchar\",\"createTime\":\"2021-09-23 16:48:18\",\"deptFilter\":false,\"filterType\":3,\"fullColumnType\":\"varchar(255)\",\"nullable\":false,\"objectFieldName\":\"companyName\",\"objectFieldType\":\"String\",\"parentKey\":false,\"primaryKey\":false,\"tableId\":\"1440961208273342464\",\"updateTime\":\"2021-09-23 16:48:23\",\"userFilter\":false}]', '2021-09-23 16:50:51', '2021-09-23 16:49:18');
INSERT INTO `bn_online_form` VALUES (1440961779596267520, 1440961119001776128, 'formViewSecondParty', '编辑乙方信息', 1, 5, 1440961208273342464, '{\"formConfig\":{\"formKind\":1,\"formType\":5,\"gutter\":20,\"labelWidth\":120,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[{\"columnName\":\"second_party_id\",\"primaryKey\":true,\"slaveClumn\":false,\"builtin\":true}]},\"widgetList\":[{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632387055657,\"datasourceId\":\"1440961208344645633\",\"tableId\":\"1440961208273342464\",\"columnId\":\"1440961208294313984\",\"columnName\":\"company_name\",\"showName\":\"公司名称\",\"variableName\":\"companyName\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632387056269,\"datasourceId\":\"1440961208344645633\",\"tableId\":\"1440961208273342464\",\"columnId\":\"1440961208298508288\",\"columnName\":\"legal_person\",\"showName\":\"公司法人\",\"variableName\":\"legalPerson\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632387056789,\"datasourceId\":\"1440961208344645633\",\"tableId\":\"1440961208273342464\",\"columnId\":\"1440961208302702592\",\"columnName\":\"legal_person_id\",\"showName\":\"法人身份证号\",\"variableName\":\"legalPersonId\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632387059360,\"datasourceId\":\"1440961208344645633\",\"tableId\":\"1440961208273342464\",\"columnId\":\"1440961208315285504\",\"columnName\":\"contact_info\",\"showName\":\"联系方式\",\"variableName\":\"contactInfo\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632387060899,\"datasourceId\":\"1440961208344645633\",\"tableId\":\"1440961208273342464\",\"columnId\":\"1440961208306896896\",\"columnName\":\"registry_address\",\"showName\":\"注册地址\",\"variableName\":\"registryAddress\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":4,\"readOnly\":false,\"disabled\":false,\"id\":1632387061874,\"datasourceId\":\"1440961208344645633\",\"tableId\":\"1440961208273342464\",\"columnId\":\"1440961208319479808\",\"columnName\":\"business_scope\",\"showName\":\"经营范围\",\"variableName\":\"businessScope\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":1,\"widgetType\":1,\"span\":24,\"type\":\"textarea\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":4,\"readOnly\":false,\"disabled\":false,\"id\":1632387062646,\"datasourceId\":\"1440961208344645633\",\"tableId\":\"1440961208273342464\",\"columnId\":\"1440961208323674112\",\"columnName\":\"memo\",\"showName\":\"备注\",\"variableName\":\"memo\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]}]}', '[{\"autoIncrement\":false,\"columnComment\":\"主键Id\",\"columnId\":\"1440961208285925376\",\"columnName\":\"second_party_id\",\"columnShowOrder\":1,\"columnType\":\"bigint\",\"createTime\":\"2021-09-23 16:48:18\",\"deptFilter\":false,\"filterType\":0,\"fullColumnType\":\"bigint(20)\",\"nullable\":false,\"objectFieldName\":\"secondPartyId\",\"objectFieldType\":\"Long\",\"parentKey\":false,\"primaryKey\":true,\"tableId\":\"1440961208273342464\",\"updateTime\":\"2021-09-23 16:48:18\",\"userFilter\":false}]', '2021-09-23 16:51:20', '2021-09-23 16:50:35');
INSERT INTO `bn_online_form` VALUES (1440962496864194560, 1440962061336055808, 'formProduct', '产品管理', 5, 1, 1440962162712383488, '{\"formConfig\":{\"formKind\":5,\"formType\":1,\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"paramList\":[],\"tableWidget\":{\"widgetKind\":2,\"widgetType\":100,\"span\":24,\"supportBottom\":0,\"tableInfo\":{\"paged\":true,\"optionColumnWidth\":150},\"titleColor\":\"#409EFF\",\"tableColumnList\":[{\"columnId\":\"1440962162729160704\",\"tableId\":\"1440962162712383488\",\"showName\":\"产品名称\",\"showOrder\":1,\"sortable\":false},{\"columnId\":\"1440962162733355008\",\"tableId\":\"1440962162712383488\",\"showName\":\"规格\",\"showOrder\":2,\"sortable\":false},{\"columnId\":\"1440962162741743616\",\"tableId\":\"1440962162712383488\",\"showName\":\"产品价格\",\"showOrder\":3,\"sortable\":false},{\"columnId\":\"1440962162745937920\",\"tableId\":\"1440962162712383488\",\"showName\":\"备注\",\"showOrder\":4,\"sortable\":false}],\"operationList\":[{\"id\":1,\"type\":0,\"name\":\"新建\",\"enabled\":true,\"builtin\":true,\"rowOperation\":false,\"btnType\":\"primary\",\"plain\":false,\"formId\":\"1440962547132928000\"},{\"id\":2,\"type\":1,\"name\":\"编辑\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn success\",\"formId\":\"1440962547132928000\"},{\"id\":3,\"type\":2,\"name\":\"删除\",\"enabled\":true,\"builtin\":true,\"rowOperation\":true,\"btnClass\":\"table-btn delete\"}],\"queryParamList\":[],\"tableId\":\"1440962162712383488\",\"variableName\":\"formProduct\",\"showName\":\"产品管理\",\"datasourceId\":\"1440962162771103745\",\"hasError\":false}},\"widgetList\":[{\"widgetKind\":0,\"widgetType\":1,\"span\":12,\"type\":\"text\",\"placeholder\":\"\",\"defaultValue\":\"\",\"minRows\":2,\"maxRows\":2,\"readOnly\":false,\"disabled\":false,\"id\":1632387220352,\"datasourceId\":\"1440962162771103745\",\"tableId\":\"1440962162712383488\",\"columnId\":\"1440962162729160704\",\"columnName\":\"product_name\",\"showName\":\"产品名称\",\"variableName\":\"productName\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]},{\"widgetKind\":0,\"widgetType\":4,\"readOnly\":false,\"disabled\":false,\"id\":1632387221253,\"datasourceId\":\"1440962162771103745\",\"tableId\":\"1440962162712383488\",\"columnId\":\"1440962162741743616\",\"columnName\":\"cost_price\",\"showName\":\"产品价格\",\"variableName\":\"costPrice\",\"dictParamList\":null,\"queryParamList\":[],\"tableColumnList\":[]}]}', '[{\"autoIncrement\":false,\"columnComment\":\"产品名称\",\"columnId\":\"1440962162729160704\",\"columnName\":\"product_name\",\"columnShowOrder\":2,\"columnType\":\"varchar\",\"createTime\":\"2021-09-23 16:52:06\",\"deptFilter\":false,\"filterType\":3,\"fullColumnType\":\"varchar(255)\",\"nullable\":false,\"objectFieldName\":\"productName\",\"objectFieldType\":\"String\",\"parentKey\":false,\"primaryKey\":false,\"tableId\":\"1440962162712383488\",\"updateTime\":\"2021-09-23 16:52:10\",\"userFilter\":false},{\"autoIncrement\":false,\"columnComment\":\"产品价格\",\"columnId\":\"1440962162741743616\",\"columnName\":\"cost_price\",\"columnShowOrder\":5,\"columnType\":\"int\",\"createTime\":\"2021-09-23 16:52:06\",\"deptFilter\":false,\"filterType\":2,\"fullColumnType\":\"int(11)\",\"nullable\":false,\"objectFieldName\":\"costPrice\",\"objectFieldType\":\"Integer\",\"parentKey\":false,\"primaryKey\":false,\"tableId\":\"1440962162712383488\",\"updateTime\":\"2021-09-23 16:53:05\",\"userFilter\":false}]', '2021-09-23 16:54:15', '2021-09-23 16:53:26');
INSERT INTO `bn_online_form` VALUES (1440962547132928000, 1440962061336055808, 'formEditProduct', '编辑产品', 1, 5, 1440962162712383488, '{\"formConfig\":{\"gutter\":20,\"labelWidth\":100,\"labelPosition\":\"right\",\"width\":800,\"widgetList\":[],\"paramList\":[]},\"widgetList\":[]}', '[]', '2021-09-23 16:53:38', '2021-09-23 16:53:38');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_form_datasource
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_form_datasource`;
CREATE TABLE `bn_online_form_datasource` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `form_id` bigint(20) NOT NULL COMMENT '表单Id',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据源Id',
  PRIMARY KEY (`id`),
  KEY `idx_form_id` (`form_id`) USING BTREE,
  KEY `idx_datasource_id` (`datasource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_form_datasource
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_form_datasource` VALUES (1440945738333818880, 1440945411354267648, 1440945228130291712);
INSERT INTO `bn_online_form_datasource` VALUES (1440948576577392640, 1440947675041107968, 1440946127531675648);
INSERT INTO `bn_online_form_datasource` VALUES (1440948832954224640, 1440947791881834496, 1440946127531675648);
INSERT INTO `bn_online_form_datasource` VALUES (1440957294211764224, 1440955001093492736, 1440952815374569472);
INSERT INTO `bn_online_form_datasource` VALUES (1440958752122474496, 1440955483438452736, 1440952815374569472);
INSERT INTO `bn_online_form_datasource` VALUES (1440959275592585216, 1440955295755931648, 1440952815374569472);
INSERT INTO `bn_online_form_datasource` VALUES (1440959299529478144, 1440955361006718976, 1440952815374569472);
INSERT INTO `bn_online_form_datasource` VALUES (1440959323269238784, 1440955424638504960, 1440952815374569472);
INSERT INTO `bn_online_form_datasource` VALUES (1440959757073518592, 1440959226632474624, 1440958971190972416);
INSERT INTO `bn_online_form_datasource` VALUES (1440960555518005248, 1440955127790833664, 1440952815374569472);
INSERT INTO `bn_online_form_datasource` VALUES (1440960654734266368, 1440955194991972352, 1440952815374569472);
INSERT INTO `bn_online_form_datasource` VALUES (1440960985459331072, 1440959420396736512, 1440958971190972416);
INSERT INTO `bn_online_form_datasource` VALUES (1440961848324132864, 1440961456601305088, 1440961208344645633);
INSERT INTO `bn_online_form_datasource` VALUES (1440961970739089408, 1440961779596267520, 1440961208344645633);
INSERT INTO `bn_online_form_datasource` VALUES (1440962547137122304, 1440962547132928000, 1440962162771103745);
INSERT INTO `bn_online_form_datasource` VALUES (1440962702619971584, 1440962496864194560, 1440962162771103745);
INSERT INTO `bn_online_form_datasource` VALUES (1440978457667309568, 1440945468593934336, 1440945228130291712);
INSERT INTO `bn_online_form_datasource` VALUES (1441214948859449344, 1440954920348946432, 1440952815374569472);
COMMIT;


-- ----------------------------
-- Table structure for bn_online_page_datasource
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_page_datasource`;
CREATE TABLE `bn_online_page_datasource` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `page_id` bigint(20) NOT NULL COMMENT '页面主键Id',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据源主键Id',
  PRIMARY KEY (`id`),
  KEY `idx_page_id` (`page_id`) USING BTREE,
  KEY `idx_datasource_id` (`datasource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_page_datasource
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_page_datasource` VALUES (1440945228138680320, 1440945149889744896, 1440945228130291712);
INSERT INTO `bn_online_page_datasource` VALUES (1440946127540064256, 1440946020174270464, 1440946127531675648);
INSERT INTO `bn_online_page_datasource` VALUES (1440952815382958080, 1440952710487609344, 1440952815374569472);
INSERT INTO `bn_online_page_datasource` VALUES (1440958971195166721, 1440958861153406976, 1440958971190972416);
INSERT INTO `bn_online_page_datasource` VALUES (1440961208353034240, 1440961119001776128, 1440961208344645633);
INSERT INTO `bn_online_page_datasource` VALUES (1440962162779492352, 1440962061336055808, 1440962162771103745);
COMMIT;

-- ----------------------------
-- Table structure for bn_online_rule
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_rule`;
CREATE TABLE `bn_online_rule` (
  `rule_id` bigint(20) NOT NULL COMMENT '主键Id',
  `rule_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '规则名称',
  `rule_type` int(11) NOT NULL COMMENT '规则类型',
  `builtin` bit(1) NOT NULL COMMENT '内置规则标记',
  `pattern` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '自定义规则的正则表达式',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `deleted_flag` int(11) NOT NULL COMMENT '逻辑删除标记',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_rule
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_rule` VALUES (1, '只允许整数', 1, b'1', NULL, '2021-09-23 00:00:00', '2021-09-23 00:00:00', 1);
INSERT INTO `bn_online_rule` VALUES (2, '只允许数字', 2, b'1', NULL, '2021-09-23 00:00:00', '2021-09-23 00:00:00', 1);
INSERT INTO `bn_online_rule` VALUES (3, '只允许英文字符', 3, b'1', NULL, '2021-09-23 00:00:00', '2021-09-23 00:00:00', 1);
INSERT INTO `bn_online_rule` VALUES (4, '范围验证', 4, b'1', NULL, '2021-09-23 00:00:00', '2021-09-23 00:00:00', 1);
INSERT INTO `bn_online_rule` VALUES (5, '邮箱格式验证', 5, b'1', NULL, '2021-09-23 00:00:00', '2021-09-23 00:00:00', 1);
INSERT INTO `bn_online_rule` VALUES (6, '手机格式验证', 6, b'1', NULL, '2021-09-23 00:00:00', '2021-09-23 00:00:00', 1);
COMMIT;

-- ----------------------------
-- Table structure for bn_online_table
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_table`;
CREATE TABLE `bn_online_table` (
  `table_id` bigint(20) NOT NULL COMMENT '主键Id',
  `table_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '表名称',
  `model_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '实体名称',
  `dblink_id` bigint(20) NOT NULL COMMENT '数据库链接Id',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`table_id`),
  KEY `idx_dblink_id` (`dblink_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_table
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_table` VALUES (1440945228079960064, 'bn_test_flow_leave', 'ZzTestFlowLeave', 1, '2021-09-23 15:44:48', '2021-09-23 15:44:48');
INSERT INTO `bn_online_table` VALUES (1440946127460372480, 'bn_test_flow_submit', 'ZzTestFlowSubmit', 1, '2021-09-23 15:48:23', '2021-09-23 15:48:23');
INSERT INTO `bn_online_table` VALUES (1440947089218473984, 'bn_test_flow_submit_detail', 'ZzTestFlowSubmitDetail', 1, '2021-09-23 15:52:12', '2021-09-23 15:52:12');
INSERT INTO `bn_online_table` VALUES (1440952815294877696, 'bn_test_flow_contract', 'ZzTestFlowContract', 1, '2021-09-23 16:14:57', '2021-09-23 16:14:57');
INSERT INTO `bn_online_table` VALUES (1440952921024892928, 'bn_test_flow_first_party', 'ZzTestFlowFirstParty', 1, '2021-09-23 16:15:23', '2021-09-23 16:15:23');
INSERT INTO `bn_online_table` VALUES (1440952988389609472, 'bn_test_flow_second_party', 'ZzTestFlowSecondParty', 1, '2021-09-23 16:15:39', '2021-09-23 16:15:39');
INSERT INTO `bn_online_table` VALUES (1440953088901910528, 'bn_test_flow_contract_detail', 'ZzTestFlowContractDetail', 1, '2021-09-23 16:16:03', '2021-09-23 16:16:03');
INSERT INTO `bn_online_table` VALUES (1440953170514677760, 'bn_test_flow_pay_detail', 'ZzTestFlowPayDetail', 1, '2021-09-23 16:16:22', '2021-09-23 16:16:22');
INSERT INTO `bn_online_table` VALUES (1440953245664022528, 'bn_test_flow_delivery_detail', 'ZzTestFlowDeliveryDetail', 1, '2021-09-23 16:16:40', '2021-09-23 16:16:40');
INSERT INTO `bn_online_table` VALUES (1440958971128057856, 'bn_test_flow_first_party', 'ZzTestFlowFirstParty', 1, '2021-09-23 16:39:25', '2021-09-23 16:39:25');
INSERT INTO `bn_online_table` VALUES (1440961208273342464, 'bn_test_flow_second_party', 'ZzTestFlowSecondParty', 1, '2021-09-23 16:48:18', '2021-09-23 16:48:18');
INSERT INTO `bn_online_table` VALUES (1440962162712383488, 'bn_test_flow_product', 'ZzTestFlowProduct', 1, '2021-09-23 16:52:06', '2021-09-23 16:52:06');
COMMIT;

-- ----------------------------
-- Table structure for bn_online_virtual_column
-- ----------------------------
DROP TABLE IF EXISTS `bn_online_virtual_column`;
CREATE TABLE `bn_online_virtual_column` (
  `virtual_column_id` bigint(20) NOT NULL COMMENT '主键Id',
  `table_id` bigint(20) NOT NULL COMMENT '所在表Id',
  `object_field_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '字段名称',
  `object_field_type` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '属性类型',
  `column_prompt` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '字段提示名',
  `virtual_type` int(11) NOT NULL COMMENT '虚拟字段类型(0: 聚合)',
  `datasource_id` bigint(20) NOT NULL COMMENT '关联数据源Id',
  `relation_id` bigint(20) DEFAULT NULL COMMENT '关联Id',
  `aggregation_table_id` bigint(20) DEFAULT NULL COMMENT '聚合字段所在关联表Id',
  `aggregation_column_id` bigint(20) DEFAULT NULL COMMENT '关联表聚合字段Id',
  `aggregation_type` int(11) DEFAULT NULL COMMENT '聚合类型(0: sum 1: count 2: avg 3: min 4: max)',
  `where_clause_json` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '存储过滤条件的json',
  PRIMARY KEY (`virtual_column_id`) USING BTREE,
  KEY `idx_database_id` (`datasource_id`) USING BTREE,
  KEY `idx_relation_id` (`relation_id`) USING BTREE,
  KEY `idx_table_id` (`table_id`) USING BTREE,
  KEY `idx_aggregation_column_id` (`aggregation_column_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_online_virtual_column
-- ----------------------------
BEGIN;
INSERT INTO `bn_online_virtual_column` VALUES (1440947297541165056, 1440946127460372480, 'totalAmount', 'Long', '报销总金额', 0, 1440946127531675648, 1440947089268805632, 1440947089218473984, 1440947089252028416, 0, '[]');
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_contract
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_contract`;
CREATE TABLE `bn_test_flow_contract` (
  `contract_id` bigint(20) NOT NULL COMMENT '主键Id',
  `first_party_id` bigint(20) NOT NULL COMMENT '甲方信息Id',
  `second_party_id` bigint(20) NOT NULL COMMENT '乙方信息Id',
  `contract_type` int(11) NOT NULL COMMENT '合同类型',
  `due_date` datetime NOT NULL COMMENT '到期日期',
  `sales_id` bigint(20) NOT NULL COMMENT '业务员Id',
  `commission_rate` int(11) NOT NULL COMMENT '提成比例',
  `attachment` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '合同附件URL',
  `security_attachment` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '保密协议附件URL',
  `intellectual_property_attachment` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '知识产权协议附件',
  `other_attachment` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '其他附件',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`contract_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_contract
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_contract` VALUES (1424647986075406336, 1424645184158699520, 1424645446290116608, 1, '2021-09-18 00:00:00', 1424607792735457280, 5, '[{\"name\":\"logo.png\",\"filename\":\"c29d9f9302c04114b559f130fe606c4f.png\"}]', '[{\"name\":\"新建位图图像.png\",\"filename\":\"d861d009ae2c46ddbb7ccaf2d679a639.png\"}]', '[{\"name\":\"logo.png\",\"filename\":\"46c75b9445f44540922a84f471f61340.png\"}]', '[{\"name\":\"新建位图图像.png\",\"filename\":\"08c56233600d4da9822c07f22bf4fa0e.png\"}]', 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_contract` VALUES (1441215377508929536, 1424645184158699520, 1424645446290116608, 1, '2021-09-30 00:00:00', 1440966324770574336, 5, '[{\"name\":\"新建位图图像.png\",\"filename\":\"d9aa27ea7eec4375b123e11ce92ec9b5.png\"}]', '[{\"name\":\"新建位图图像.png\",\"filename\":\"0c20b1c7256148018451852ddae9f8aa.png\"}]', NULL, NULL, 1440966324770574336, '2021-09-24 09:38:17', 1440966324770574336, '2021-09-24 09:38:17', 1);
INSERT INTO `bn_test_flow_contract` VALUES (1441217206602960896, 1424645184158699520, 1424645446290116608, 1, '2021-10-09 00:00:00', 1440966324770574336, 7, '[{\"name\":\"新建位图图像.png\",\"filename\":\"dcd2db9b75664251a3baf595f6752d90.png\"}]', '[{\"name\":\"新建位图图像.png\",\"filename\":\"e85a5b89d3bd4a3f94d58b0630afc7af.png\"}]', NULL, NULL, 1440966324770574336, '2021-09-24 09:45:33', 1440966324770574336, '2021-09-24 09:45:33', 1);
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_contract_detail
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_contract_detail`;
CREATE TABLE `bn_test_flow_contract_detail` (
  `contract_detail_id` bigint(20) NOT NULL COMMENT '主键Id',
  `contract_id` bigint(20) NOT NULL COMMENT '合同Id',
  `product_id` bigint(20) NOT NULL COMMENT '产品Id',
  `total_count` int(11) NOT NULL COMMENT '数量',
  `total_amount` int(11) NOT NULL COMMENT '总价',
  `meno` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`contract_detail_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_contract_detail
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_contract_detail` VALUES (1424647986087989249, 1424647986075406336, 1424645586203709440, 1, 12001, '加急', 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_contract_detail` VALUES (1424647986092183552, 1424647986075406336, 1424645694550970368, 5, 143680, NULL, 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_contract_detail` VALUES (1441215377550872576, 1441215377508929536, 1424645586203709440, 100, 1230000, '加急', 1440966324770574336, '2021-09-24 09:38:17', 1440966324770574336, '2021-09-24 09:38:17', 1);
INSERT INTO `bn_test_flow_contract_detail` VALUES (1441217206628126720, 1441217206602960896, 1424645586203709440, 50, 234000, '加急', 1440966324770574336, '2021-09-24 09:45:33', 1440966324770574336, '2021-09-24 09:45:33', 1);
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_delivery_detail
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_delivery_detail`;
CREATE TABLE `bn_test_flow_delivery_detail` (
  `delivery_id` bigint(20) NOT NULL COMMENT '主键Id',
  `contract_id` bigint(20) NOT NULL COMMENT '合同Id',
  `delivery_date` datetime NOT NULL COMMENT '交付日期',
  `product_id` bigint(20) NOT NULL COMMENT '产品Id',
  `total_count` int(11) NOT NULL COMMENT '数量',
  `memo` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`delivery_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_delivery_detail
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_delivery_detail` VALUES (1424647986079600640, 1424647986075406336, '2021-08-14 00:00:00', 1424645586203709440, 1, NULL, 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_delivery_detail` VALUES (1424647986083794944, 1424647986075406336, '2021-08-28 00:00:00', 1424645694550970368, 2, NULL, 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_delivery_detail` VALUES (1424647986087989248, 1424647986075406336, '2021-09-11 00:00:00', 1424645694550970368, 3, NULL, 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_delivery_detail` VALUES (1441215377538289664, 1441215377508929536, '2021-09-27 00:00:00', 1424645586203709440, 40, NULL, 1440966324770574336, '2021-09-24 09:38:17', 1440966324770574336, '2021-09-24 09:38:17', 1);
INSERT INTO `bn_test_flow_delivery_detail` VALUES (1441215377546678272, 1441215377508929536, '2021-09-30 00:00:00', 1424645586203709440, 60, '加急', 1440966324770574336, '2021-09-24 09:38:17', 1440966324770574336, '2021-09-24 09:38:17', 1);
INSERT INTO `bn_test_flow_delivery_detail` VALUES (1441217206619738113, 1441217206602960896, '2021-09-27 00:00:00', 1424645586203709440, 20, NULL, 1440966324770574336, '2021-09-24 09:45:33', 1440966324770574336, '2021-09-24 09:45:33', 1);
INSERT INTO `bn_test_flow_delivery_detail` VALUES (1441217206623932416, 1441217206602960896, '2021-09-29 00:00:00', 1424645586203709440, 30, NULL, 1440966324770574336, '2021-09-24 09:45:33', 1440966324770574336, '2021-09-24 09:45:33', 1);
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_first_party
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_first_party`;
CREATE TABLE `bn_test_flow_first_party` (
  `first_party_id` bigint(20) NOT NULL COMMENT '主键Id',
  `company_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '公司名称',
  `legal_person` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '法人',
  `legal_person_id` char(18) COLLATE utf8mb4_bin NOT NULL COMMENT '法人身份证号',
  `registry_address` varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '注册地址',
  `contact_info` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '联系方式',
  `business_scope` varchar(4000) COLLATE utf8mb4_bin NOT NULL COMMENT '经营范围',
  `memo` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`first_party_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_first_party
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_first_party` VALUES (1424645184158699520, '天津哈哈乐商贸公司', '王哈哈', '120104197604150375', '天津市津南区', '022-23451234', '商贸商贸', '天津商贸', 1424545631073996884, '2021-08-08 16:14:15', 1424545631073996884, '2021-08-08 16:14:15', 1);
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_leave
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_leave`;
CREATE TABLE `bn_test_flow_leave` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `user_id` bigint(20) NOT NULL COMMENT '请假用户Id',
  `leave_reason` varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '请假原因',
  `leave_type` int(11) NOT NULL COMMENT '请假类型',
  `leave_begin_time` datetime NOT NULL COMMENT '请假开始时间',
  `leave_end_time` datetime NOT NULL COMMENT '请假结束时间',
  `apply_time` datetime NOT NULL COMMENT '申请时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_leave
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_leave` VALUES (1424636082313498624, 1424545631073996884, '请假2天', 1, '2021-08-01 00:00:00', '2021-08-02 00:00:00', '2021-08-08 15:38:05');
INSERT INTO `bn_test_flow_leave` VALUES (1424649303212691456, 1424545631073996884, '请假一天', 1, '2021-08-01 00:00:00', '2021-08-01 00:00:00', '2021-08-08 16:30:37');
INSERT INTO `bn_test_flow_leave` VALUES (1424652775060410368, 1424545631073996884, '请假2天', 1, '2021-08-01 00:00:00', '2021-08-02 00:00:00', '2021-08-08 16:44:25');
INSERT INTO `bn_test_flow_leave` VALUES (1424653286929076224, 1424545631073996884, '请假一天', 1, '2021-08-01 00:00:00', '2021-08-01 00:00:00', '2021-08-08 16:46:27');
INSERT INTO `bn_test_flow_leave` VALUES (1424653896705380352, 1424545631073996884, '请假3天！！！', 2, '2021-08-01 00:00:00', '2021-08-03 00:00:00', '2021-08-08 16:48:52');
INSERT INTO `bn_test_flow_leave` VALUES (1424654125999591424, 1424545631073996884, '结婚了结婚了', 3, '2021-08-01 00:00:00', '2021-08-07 00:00:00', '2021-08-08 16:49:47');
INSERT INTO `bn_test_flow_leave` VALUES (1424657782186971136, 1424654900813369344, '请假请假', 1, '2021-08-01 00:00:00', '2021-08-07 00:00:00', '2021-08-08 17:04:19');
INSERT INTO `bn_test_flow_leave` VALUES (1424658915961868288, 1424545631073996884, '测试', 1, '2021-08-01 00:00:00', '2021-08-07 00:00:00', '2021-08-08 17:08:49');
INSERT INTO `bn_test_flow_leave` VALUES (1424659588543680512, 1424608402885054464, '测试请假', 1, '2021-08-01 00:00:00', '2021-08-02 00:00:00', '2021-08-08 17:11:29');
INSERT INTO `bn_test_flow_leave` VALUES (1424659995714129920, 1424608402885054464, '测试了测试了', 1, '2021-08-01 00:00:00', '2021-08-07 00:00:00', '2021-08-08 17:13:07');
INSERT INTO `bn_test_flow_leave` VALUES (1424660095102357504, 1424608402885054464, '测试了测试了！！！', 1, '2021-08-01 00:00:00', '2021-08-07 00:00:00', '2021-08-08 17:13:30');
INSERT INTO `bn_test_flow_leave` VALUES (1441213240326492160, 1440966324770574336, '请假一天', 1, '2021-09-17 00:00:00', '2021-09-18 00:00:00', '2021-09-24 09:29:48');
INSERT INTO `bn_test_flow_leave` VALUES (1441218427220922368, 1440911410581213417, '请假2天', 1, '2021-09-02 00:00:00', '2021-09-04 00:00:00', '2021-09-24 09:50:24');
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_pay_detail
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_pay_detail`;
CREATE TABLE `bn_test_flow_pay_detail` (
  `pay_detail_id` bigint(20) NOT NULL COMMENT '主键Id',
  `contract_id` bigint(20) NOT NULL COMMENT '合同Id',
  `pay_date` datetime NOT NULL COMMENT '付款日期',
  `pay_type` int(11) NOT NULL COMMENT '付款类型',
  `percentage` int(11) NOT NULL COMMENT '百分比',
  `memo` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`pay_detail_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_pay_detail
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_pay_detail` VALUES (1424647986096377856, 1424647986075406336, '2021-08-18 00:00:00', 1, 20, '预付款', 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1424647986100572160, 1424647986075406336, '2021-08-28 00:00:00', 2, 50, '分期款', 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1424647986104766464, 1424647986075406336, '2021-09-04 00:00:00', 2, 29, '分期款', 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1424647986108960768, 1424647986075406336, '2021-09-17 00:00:00', 3, 10, '尾款', 1424545631073996884, '2021-08-08 16:25:23', 1424545631073996884, '2021-08-08 16:25:23', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1441215377521512448, 1441215377508929536, '2021-09-25 00:00:00', 1, 20, NULL, 1440966324770574336, '2021-09-24 09:38:17', 1440966324770574336, '2021-09-24 09:38:17', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1441215377529901056, 1441215377508929536, '2021-09-28 00:00:00', 2, 60, NULL, 1440966324770574336, '2021-09-24 09:38:17', 1440966324770574336, '2021-09-24 09:38:17', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1441215377534095360, 1441215377508929536, '2021-10-05 00:00:00', 3, 20, NULL, 1440966324770574336, '2021-09-24 09:38:17', 1440966324770574336, '2021-09-24 09:38:17', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1441217206607155200, 1441217206602960896, '2021-09-26 00:00:00', 1, 20, NULL, 1440966324770574336, '2021-09-24 09:45:33', 1440966324770574336, '2021-09-24 09:45:33', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1441217206611349504, 1441217206602960896, '2021-10-01 00:00:00', 2, 40, NULL, 1440966324770574336, '2021-09-24 09:45:33', 1440966324770574336, '2021-09-24 09:45:33', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1441217206615543808, 1441217206602960896, '2021-10-07 00:00:00', 2, 30, NULL, 1440966324770574336, '2021-09-24 09:45:33', 1440966324770574336, '2021-09-24 09:45:33', 1);
INSERT INTO `bn_test_flow_pay_detail` VALUES (1441217206619738112, 1441217206602960896, '2021-10-15 00:00:00', 3, 10, NULL, 1440966324770574336, '2021-09-24 09:45:33', 1440966324770574336, '2021-09-24 09:45:33', 1);
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_product
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_product`;
CREATE TABLE `bn_test_flow_product` (
  `product_id` bigint(20) NOT NULL COMMENT '主键Id',
  `product_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '产品名称',
  `product_spec` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '规格',
  `type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '型号',
  `cost_price` int(11) NOT NULL COMMENT '成本价',
  `memo` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_product
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_product` VALUES (1424645586203709440, '单动薄板拉伸（冲压）液压机', '630T', '630T', 12001, '机身采用四柱整体框架或组合框架结构，均为全钢板焊接；四角八面直角导轨导向、精度高，刚性好，并采用液压预紧。', 1424545631073996884, '2021-08-08 16:15:51', 1424545631073996884, '2021-08-08 16:15:51', 1);
INSERT INTO `bn_test_flow_product` VALUES (1424645694550970368, '单动薄板拉伸液压机', '315T', 'YLL27', 143680, '机身采用四柱整体框架或组合框架结构，均为全钢板焊接；四角八面直角导轨导向、精度高，刚性好，并采用液压预紧。液压系统采用二通插装集成阀；整个系统工作稳定、可靠、使用寿命长、泄露少、故障点少。', 1424545631073996884, '2021-08-08 16:16:17', 1424545631073996884, '2021-08-08 16:16:17', 1);
INSERT INTO `bn_test_flow_product` VALUES (1424645807918813184, '单柱校正液压机', '210T', 'YLL30', 89000, '计算机优化直立式C型结构设计，刚性好。机器各部件均安装于机身内，外形整齐美观。液压系统采用手动换向阀，调速方便。具有手动和脚踏两种操作方式。机器行程、压力可在规定范围内调节。', 1424545631073996884, '2021-08-08 16:16:44', 1424545631073996884, '2021-08-08 16:16:44', 1);
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_second_party
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_second_party`;
CREATE TABLE `bn_test_flow_second_party` (
  `second_party_id` bigint(20) NOT NULL COMMENT '主键Id',
  `company_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '公司名称',
  `legal_person` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '法人',
  `legal_person_id` char(18) COLLATE utf8mb4_bin NOT NULL COMMENT '法人身份证号',
  `registry_address` varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '注册地址',
  `contact_info` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '联系方式',
  `business_scope` varchar(4000) COLLATE utf8mb4_bin NOT NULL COMMENT '经营范围',
  `memo` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  `deleted_flag` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记(1: 正常 -1: 已删除)',
  PRIMARY KEY (`second_party_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_second_party
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_second_party` VALUES (1424645446290116608, '天津乐呵呵公司', '乐呵呵', '120106197003150043', '天津市河东区', '022-12345678', '乐呵呵乐呵呵', '乐呵呵乐呵呵', 1424545631073996884, '2021-08-08 16:15:18', 1424545631073996884, '2021-08-08 16:15:18', 1);
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_submit
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_submit`;
CREATE TABLE `bn_test_flow_submit` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `submit_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '报销名称',
  `submit_kind` int(11) NOT NULL COMMENT '报销类别',
  `total_amount` int(11) NOT NULL COMMENT '报销金额',
  `description` varchar(512) COLLATE utf8mb4_bin NOT NULL COMMENT '报销描述',
  `memo` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_submit_kind` (`submit_kind`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_submit
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_submit` VALUES (1424636815804993536, '7月份出差报销', 1, 1200, '7月份出差报销', '出差报销', 1424545631073996884, '2021-08-08 15:41:47', 1424545631073996884, '2021-08-08 15:41:00');
INSERT INTO `bn_test_flow_submit` VALUES (1441213876367527936, '出差报销', 1, 1200, '出差', '出差', 1440966324770574336, '2021-09-24 09:32:19', 1440966324770574336, '2021-09-24 09:32:19');
INSERT INTO `bn_test_flow_submit` VALUES (1441312736167333888, '出差', 1, 800, '出差', NULL, 1440966324770574336, '2021-09-24 16:05:09', 1440966324770574336, '2021-09-24 16:05:09');
INSERT INTO `bn_test_flow_submit` VALUES (1441340309442138112, '团建', 2, 500, '团建', NULL, 1440966324770574336, '2021-09-24 17:54:43', 1440966324770574336, '2021-09-24 17:54:43');
COMMIT;

-- ----------------------------
-- Table structure for bn_test_flow_submit_detail
-- ----------------------------
DROP TABLE IF EXISTS `bn_test_flow_submit_detail`;
CREATE TABLE `bn_test_flow_submit_detail` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `submit_id` bigint(20) NOT NULL COMMENT '报销单据Id',
  `expense_type` int(11) NOT NULL COMMENT '费用类型',
  `expense_time` datetime NOT NULL COMMENT '发生日期',
  `amount` int(11) NOT NULL COMMENT '金额',
  `image_url` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '上传图片URL',
  `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '费用描述',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_submit_id` (`submit_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of bn_test_flow_submit_detail
-- ----------------------------
BEGIN;
INSERT INTO `bn_test_flow_submit_detail` VALUES (1424636815809187840, 1424636815804993536, 1, '2021-07-06 00:00:00', 500, '[{\"name\":\"图片1.png\",\"filename\":\"5981b24ffaf640a590b57c462d9ff889.png\"}]', '住宿一天');
INSERT INTO `bn_test_flow_submit_detail` VALUES (1424636815813382144, 1424636815804993536, 2, '2021-07-22 00:00:00', 700, '[{\"name\":\"logo.png\",\"filename\":\"4b63d00a0e3b420a9e86e751ba210f6a.png\"}]', '交通费用');
INSERT INTO `bn_test_flow_submit_detail` VALUES (1441213876384305152, 1441213876367527936, 1, '2021-09-23 00:00:00', 600, '[{\"name\":\"logo.png\",\"filename\":\"38b7e4a097654ff38e176846ba005fcf.png\"}]', NULL);
INSERT INTO `bn_test_flow_submit_detail` VALUES (1441312736230248448, 1441312736167333888, 1, '2021-09-24 00:00:00', 600, '[{\"name\":\"logo.png\",\"filename\":\"1252138daaf744a3b1fdf06a07c5a173.png\"}]', '吃饭');
INSERT INTO `bn_test_flow_submit_detail` VALUES (1441340309446332416, 1441340309442138112, 1, '2021-09-30 00:00:00', 500, '[{\"name\":\"logo.png\",\"filename\":\"f33b658b052f487c9a34af274f48dcd3.png\"}]', '团建费用');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

