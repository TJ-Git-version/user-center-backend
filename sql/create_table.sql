-- auto-generated definition
create database if not exists user_center;
use user_center;
create table user
(
    id           bigint auto_increment comment '主键' primary key,
    username     varchar(256)                       null comment '用户昵称',
    loginAccount varchar(256)                       null comment '账号账号',
    planetCode   varchar(512)                       null comment '星球编号',
    avatarUrl    varchar(512)                       null comment '头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '手机号',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态: 0-正常',
    userRole     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除'
) comment '用户';
