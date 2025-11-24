-- `user`
ALTER TABLE `user`
MODIFY COLUMN `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别(男，女)' AFTER `cover_path`;

ALTER TABLE `user`
CHANGE COLUMN `regist_type` `register_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注册类型 ，字典：`register_type`' AFTER `update_time`;

ALTER TABLE `user`
    MODIFY COLUMN `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别（男|女）' AFTER `cover_path`;

-- `phone_code`
ALTER TABLE `phone_code`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `phone_code`
    MODIFY COLUMN `use_status` char(1) NULL DEFAULT NULL COMMENT '使用状态，字典：`use_status`' AFTER `code`;
