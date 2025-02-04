use openApi;

drop table if exists user;
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    user_name     varchar(256)                           null comment '用户昵称（账号）',
    user_avatar   varchar(1024)                          null comment '用户头像',
    gender        tinyint                                null comment '性别',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    user_password varchar(512)                           not null comment '密码',
    access_key    varchar(128)                           not null comment 'accessKey',
    secret_key    varchar(128)                           not null comment 'secretKey',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    unique key idx_user_name (user_name),
    key idx_create_time (create_time),
    key idx_update_time (update_time)
) comment '用户';

alter table user
    add column `accessKey` varchar(256) not null comment 'access_key' after `user_password`;
alter table user
    add column `secretKey` varchar(256) not null comment 'secret_key' after `accessKey`;

drop table if exists interface_info;
-- 接口信息
create table if not exists interface_info
(
    `id`              bigint                             not null auto_increment comment '主键' primary key,
    `name`            varchar(256)                       not null comment '名称',
    `url`             varchar(512)                       not null comment '接口地址',
    `method`          varchar(256)                       not null comment '请求类型',
    `requestParams`   text                               null comment '请求参数',
    `responseParams`  text                               null comment '响应参数',
    `requestExample` text                                null comment '请求示例',
    `request_header`  text                               null comment '请求头',
    `response_header` text                               null comment '响应头',
    `returnFormat`   varchar(256) default 'JSON'            null comment '返回格式(JSON等)',
    `description`    varchar(256)                           null comment '描述信息',
    `status`          tinyint    default 0                 not null comment '接口状态（0-默认下线，1-上线）',
    `totalInvokes`   bigint       default 0                 not null comment '接口总调用次数',
    `avatarUrl`      varchar(1024)                          null comment '接口头像',
    `updated_by`      varchar(256)                       not null comment '更新人',
    `created_by`      varchar(256)                       not null comment '创建人',
    `create_time`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    key idx_create_time (create_time),
    key idx_update_time (update_time)
) comment '接口信息' character set = utf8;

-- 用户调用接口关系表
create table if not exists `user_interface_info`
(
    `id`               bigint                             not null auto_increment comment '主键' primary key,
    `user_name`        varchar(256)                       not null comment '调用人',
    `interfaceInfo_id` bigint                             not null comment '接口 id',
    `total_num`        int      default 0                 not null comment '总调用次数',
    `status`           int      default 0                 not null comment '调用状态（0- 正常 1- 封号）',
    `create_time`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_create_time (create_time),
    index idx_update_time (update_time)
) comment '用户调用接口关系';
