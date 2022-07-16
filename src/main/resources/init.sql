create table if not exists file_meta(
    name varchar(50) not null,
    path varchar(100) not null,
    is_directory boolean not null,
    size bigint,
    last_modified timestamp not null,
    pinyin varchar(200),
    pinyin_first varchar(50)
)