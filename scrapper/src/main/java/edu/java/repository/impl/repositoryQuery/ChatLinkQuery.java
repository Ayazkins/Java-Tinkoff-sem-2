package edu.java.repository.impl.repositoryQuery;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatLinkQuery {
    public final static String ADD = """
        insert into chat_link (chat_id, link_id) values (?, ?)
        """;

    public final static String REMOVE = """
        delete from chat_link where chat_id = ? and link_id = ?
        """;

    public final static String FIND_ALL_BY_ID = """
        select link.id, link.url, link.last_checked, link.last_updated
         from link as link join chat_link as cl on link.id = cl.link_id where cl.chat_id = ?
        """;

    public final static String IS_LINK_TRACKED = """
        select count(*) from chat_link where link_id = ?;
        """;

    public final static String FIND_ALL_CHATS_BY_LINK = """
        select chat_id from chat_link where link_id = ?
        """;
}
