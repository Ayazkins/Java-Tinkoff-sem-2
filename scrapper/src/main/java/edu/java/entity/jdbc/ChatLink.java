package edu.java.entity.jdbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatLink {
    private Long id;
    private Long chatId;
    private Long linkId;
}
