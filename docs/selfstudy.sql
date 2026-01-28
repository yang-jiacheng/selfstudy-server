SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_info
-- ----------------------------
DROP TABLE IF EXISTS `admin_info`;
CREATE TABLE `admin_info`  (
  `id` bigint NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `profile_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int NULL DEFAULT 1 COMMENT '状态 1正常 2禁用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_admin_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_admin_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '后台管理员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for admin_role_relate
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_relate`;
CREATE TABLE `admin_role_relate`  (
  `id` bigint NOT NULL,
  `admin_id` bigint NULL DEFAULT NULL COMMENT '管理员id',
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for business_config
-- ----------------------------
DROP TABLE IF EXISTS `business_config`;
CREATE TABLE `business_config`  (
  `id` bigint NOT NULL,
  `bkey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bvalue` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `show_status` int NULL DEFAULT 1 COMMENT '0 后台不可编辑 1可编辑',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_bkey`(`bkey` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for catalog
-- ----------------------------
DROP TABLE IF EXISTS `catalog`;
CREATE TABLE `catalog`  (
  `id` bigint NOT NULL,
  `classify_id` bigint NULL DEFAULT NULL COMMENT '图书馆id',
  `parent_id` bigint NULL DEFAULT -1 COMMENT '父id',
  `level` int NULL DEFAULT NULL COMMENT '层级',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点名称',
  `person_count` int NULL DEFAULT NULL COMMENT '容纳人数',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '自习室' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for classify
-- ----------------------------
DROP TABLE IF EXISTS `classify`;
CREATE TABLE `classify`  (
  `id` bigint NOT NULL,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `icon_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `cover_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面地址',
  `person_count` int NULL DEFAULT NULL COMMENT '容纳人数（冗余字段）',
  `use_type` int NULL DEFAULT NULL COMMENT '是否开放使用 是否开放 (0否 1是)',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书馆' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈内容',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈图片',
  `reply` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '回复内容',
  `admin_id` bigint NULL DEFAULT NULL COMMENT '回复人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `reply_time` datetime NULL DEFAULT NULL COMMENT '回复时间',
  `status` int NULL DEFAULT 1 COMMENT '用户是否可见 1可见 2不可见',
  `reply_status` int NULL DEFAULT 0 COMMENT '回复状态 0未回复 1已回复',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '意见反馈' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for object_storage
-- ----------------------------
DROP TABLE IF EXISTS `object_storage`;
CREATE TABLE `object_storage`  (
  `id` bigint NOT NULL,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原始文件名',
  `file_size` decimal(10, 2) NULL DEFAULT NULL COMMENT '文件大小 MB',
  `download_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下载地址',
  `creator_id` bigint NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '对象存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint NOT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模块标题',
  `business_type` int NULL DEFAULT NULL COMMENT '业务类型（0其它 1新增 2修改 3删除 4导入 5导出）',
  `user_type` int NULL DEFAULT NULL COMMENT '0后台用户 1APP用户',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求地址',
  `request_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方式',
  `request_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `request_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求结果',
  `client_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客端ip',
  `status` int NULL DEFAULT NULL COMMENT '操作状态（0成功 1失败）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  `duration_str` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求耗时  ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 173 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint NOT NULL COMMENT '权限id',
  `parent_id` bigint NULL DEFAULT -1 COMMENT '父id',
  `node_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '权限类型：1目录 2菜单 3按钮',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `permission_str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限信息',
  `ui_meta` json NULL COMMENT '前端路由元数据',
  `sort` int NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '每条记录的创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '每条记录的更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_permission_permissionstr`(`permission_str` ASC) USING BTREE,
  INDEX `idx_permission_parentid`(`parent_id` ASC) USING BTREE,
  INDEX `idx_permission_type`(`type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for phone_code
-- ----------------------------
DROP TABLE IF EXISTS `phone_code`;
CREATE TABLE `phone_code`  (
  `id` bigint NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `use_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '使用状态，字典：`use_status`',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `end_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phonecode_phone_code`(`phone` ASC, `code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '验证码' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL COMMENT '角色id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '每条记录的创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '每条记录的更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role_permission_relate
-- ----------------------------
DROP TABLE IF EXISTS `role_permission_relate`;
CREATE TABLE `role_permission_relate`  (
  `id` bigint NOT NULL,
  `role_id` bigint NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for study_record
-- ----------------------------
DROP TABLE IF EXISTS `study_record`;
CREATE TABLE `study_record`  (
  `id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `classify_id` bigint NULL DEFAULT NULL COMMENT '图书馆id',
  `catalog_id` bigint NULL DEFAULT NULL COMMENT '节点id',
  `tag` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学习标签',
  `seat` int NULL DEFAULT NULL COMMENT '座位号',
  `timing_mode` int NULL DEFAULT NULL COMMENT '计时方式：1正计时 2倒计时',
  `setting_duration` int NULL DEFAULT NULL COMMENT '设置的自习时长，单位分钟（仅在倒计时有）',
  `actual_duration` int NULL DEFAULT NULL COMMENT '实际学习时长，单位分钟',
  `status` int NULL DEFAULT NULL COMMENT '状态（1自习中 2离开 3已完成）',
  `note_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学习笔记文字',
  `note_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学习笔记图片',
  `note_status` int NULL DEFAULT 0 COMMENT '是否有笔记 0否 1是',
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学习记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for study_statistics
-- ----------------------------
DROP TABLE IF EXISTS `study_statistics`;
CREATE TABLE `study_statistics`  (
  `id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `study_day` date NULL DEFAULT NULL COMMENT '学习日期',
  `duration` int NULL DEFAULT NULL COMMENT '学习时长',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_studystatistics_userid_studyday`(`user_id` ASC, `study_day` ASC) USING BTREE COMMENT '每个用户每天一条记录'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学习统计' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `profile_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `cover_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人资料背景图',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别（男|女）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `register_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册类型 ，字典：`register_type`',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '常驻地址',
  `balance` decimal(11, 2) NULL DEFAULT NULL COMMENT '余额',
  `total_duration` int NULL DEFAULT 0 COMMENT '总学习时长',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_agreement
-- ----------------------------
DROP TABLE IF EXISTS `user_agreement`;
CREATE TABLE `user_agreement`  (
  `id` bigint NOT NULL,
  `type` int NULL DEFAULT NULL COMMENT '1 隐私政策 2用户协议',
  `title` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ids_is_ag_type`(`type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户协议与隐私政策' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for version
-- ----------------------------
DROP TABLE IF EXISTS `version`;
CREATE TABLE `version`  (
  `id` bigint NOT NULL,
  `version_code` int NULL DEFAULT NULL COMMENT '版本号',
  `version_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本名称',
  `download_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下载地址',
  `status` int NULL DEFAULT 0 COMMENT '是否强制更新 1是 0 否',
  `check_update` int NULL DEFAULT 0 COMMENT '是否检测更新 1是 0 否',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '版本控制' ROW_FORMAT = DYNAMIC;
