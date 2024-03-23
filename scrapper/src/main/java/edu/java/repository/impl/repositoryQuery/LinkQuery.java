package edu.java.repository.impl.repositoryQuery;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkQuery {
    public static final String SAVE = """
        insert into link (url) values (?)
        """;

    public final static String DELETE = """
        delete from link where url = (?)
        """;

    public static final String FIND = """
        select * from link where url = ?
        """;

    public static final String FIND_DATE = """
        select id, url, last_checked, last_updated from link where last_checked < ?
        """;

    public static final String UPDATE = """
        update link set last_checked = ?, last_updated = ?
        where url = ?;
        """;
}
