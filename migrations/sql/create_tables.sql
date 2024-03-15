create table if not exists chat (
    id bigint primary key
);
create table if not exists link (
    id bigserial primary key,
    url varchar(255) not null,
    unique(url)
);

create table if not exists chat_link (
    chat_id bigint references chat(id) on delete cascade,
    link_id bigint references link(id) on delete cascade,
    primary key (chat_id, link_id)
);
