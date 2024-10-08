package com.example.reward_monitoring.mission.missionCS.repository;




import com.example.reward_monitoring.mission.missionCS.entity.MissionCS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.ZonedDateTime;
import java.util.List;

public interface MissionCSRepository extends JpaRepository<MissionCS,Integer> {
    public MissionCS findByIdx(int idx);

    @Query("SELECT m FROM MissionCS m WHERE m.firstRegDate BETWEEN :startDate AND :endDate")
    public List<MissionCS> findByBothDate(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

    @Query("SELECT m FROM MissionCS m WHERE m.firstRegDate > :startAt")
    public List<MissionCS> findByStartAt(ZonedDateTime startAt);

    @Query("SELECT m FROM MissionCS m WHERE m.firstRegDate < :endAt")
    public List<MissionCS> findByEndAt(ZonedDateTime endAt);

    public List<MissionCS> findByCsType(String type);

    @Query("SELECT m FROM MissionCS m  WHERE m.msnTitle LIKE %:keyword% ")
    public List<MissionCS> findByMissionTitle(String keyword);
}
