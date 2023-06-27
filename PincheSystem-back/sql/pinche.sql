create table if not exists log
(
    id           bigint auto_increment comment '日志id'
        primary key,
    url          varchar(64)                        null comment '请求地址',
    type         varchar(16)                        null comment '请求类型',
    method       varchar(32)                        null comment '请求方法名',
    args         varchar(64)                        null comment '请求参数列表',
    total_time   float                              null comment '执行时间',
    operate_user varchar(32)                        null comment '当前操作的用户',
    time         datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '日志记录时间'
)
    comment '日志表';

create table if not exists user
(
    id          bigint auto_increment comment '用户id'
        primary key,
    username    varchar(32)                          null comment '用户名',
    password    varchar(60)                          not null comment '用户密码',
    mobile      varchar(11)                          null comment '用户电话号码',
    sex         varchar(1) default '无'              null comment '用户性别',
    role        tinyint    default 0                 not null comment '角色 0-普通用户 1-管理员',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint    default 0                 not null comment '是否删除'
)
    comment '用户表';

create table if not exists info
(
    info_id     bigint auto_increment
        primary key,
    owner_id    bigint                             not null,
    owner_name  varchar(32)                        not null,
    days        date                               not null,
    ending      varchar(32)                        null,
    car_num     varchar(10)                        null,
    price       float                              null,
    remain      tinyint                            null,
    remark      varchar(64)                        null,
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint info_ibfk_1
        foreign key (owner_id) references user (id)
)
    comment '拼车信息表';

create index info_owner_id
    on info (owner_id);

create table if not exists orders
(
    order_id      bigint auto_increment
        primary key,
    passenger_num tinyint                            not null,
    ending        varchar(32)                        not null,
    info_id       bigint                             not null,
    start_time    date                               not null,
    create_time   datetime default CURRENT_TIMESTAMP null,
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    price         float                              not null,
    order_state   tinyint  default 0                 not null comment '状态（0，1，2）',
    remark        varchar(32)                        null,
    constraint orders_fk_1
        foreign key (info_id) references info (info_id)
)
    comment '订单表';

create index order_info_id
    on orders (info_id);

create table if not exists owner_order
(
    id       bigint auto_increment comment '主键'
        primary key,
    order_id bigint not null comment '订单id',
    owner_id bigint not null comment '车主id',
    constraint owner_order_orders_null_fk
        foreign key (order_id) references orders (order_id),
    constraint owner_order_user_null_fk
        foreign key (owner_id) references user (id)
)
    comment '车主订单关联表';

create index owner_order_1
    on owner_order (order_id);

create index owner_order_2
    on owner_order (owner_id);

create table if not exists owner_score
(
    id       bigint auto_increment comment '主键'
        primary key,
    owner_id bigint        not null comment '车主id',
    score    int default 0 not null comment '积分',
    constraint OwnerScore_user_id_fk
        foreign key (owner_id) references user (id)
)
    comment '车主积分表';

create index owner_score
    on owner_score (owner_id);

create index user_mobile
    on user (mobile);

create index user_username
    on user (username);

create table if not exists user_order
(
    id       bigint auto_increment comment '主键'
        primary key,
    order_id bigint not null comment '订单id',
    user_id  bigint not null comment '用户id',
    constraint user_order_ibfk_1
        foreign key (order_id) references orders (order_id),
    constraint user_order_ibfk_2
        foreign key (user_id) references user (id)
)
    comment '用户和订单的中间表';

create index user_order_1
    on user_order (order_id);

create index user_order_2
    on user_order (user_id);

