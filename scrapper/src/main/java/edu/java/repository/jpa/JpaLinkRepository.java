package edu.java.repository.jpa;

import edu.java.entity.hibernate.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    @Modifying
    @Query("UPDATE Link SET lastUpdated = :lastUpdated, lastChecked = :checked WHERE url = :url")
    void update(String url, OffsetDateTime lastUpdated, OffsetDateTime checked);

    @Query("SELECT l FROM Link l WHERE l.lastChecked < :threshold")
    List<Link> findAllLinksCheckedLongAgo(OffsetDateTime threshold);

    Optional<Link> findByUrl(String url);
}
