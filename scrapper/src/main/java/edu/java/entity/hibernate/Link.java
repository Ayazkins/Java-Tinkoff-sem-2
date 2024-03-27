package edu.java.entity.hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "chatLinks")
@EqualsAndHashCode(of = "url")
@Entity
@Builder
@Table(name = "link")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @Column(name = "last_checked")
    private OffsetDateTime lastChecked;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;


    @OneToMany(mappedBy = "link")
    @Builder.Default
    private List<ChatLink> chatLinks = new ArrayList<>();
}
