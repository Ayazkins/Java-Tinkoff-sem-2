create table if not exists chat (
    id bigint primary key
);
create table if not exists link (
    id bigserial primary key,
    url varchar(255) not null,
    last_checked timestamp with time zone default current_timestamp,
    last_updated timestamp with time zone default current_timestamp,
    unique(url)
);

create table if not exists chat_link (
    id bigserial primary key,
    chat_id bigint references chat(id) on delete cascade,
    link_id bigint references link(id) on delete cascade,
    unique (chat_id, link_id)
);
