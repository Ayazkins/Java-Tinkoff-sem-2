package edu.java.repository.impl.repositoryQuery;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatQuery {
    public static final String SAVE = """
        insert into chat (id) values (?)
        """;

    public static final String DELETE = """
        delete from chat where id = (?)
        """;

    public static final String FIND = """
        select id from chat where id = (?)
         """;
}
