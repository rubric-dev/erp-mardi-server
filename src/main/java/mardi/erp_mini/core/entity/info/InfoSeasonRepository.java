package mardi.erp_mini.core.entity.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InfoSeasonRepository extends JpaRepository<InfoSeason, Long> {

  @Query("SELECT i FROM InfoSeason i ORDER BY i.createdAt DESC LIMIT 1")
  InfoSeason findLatestInfoSeason();
}
