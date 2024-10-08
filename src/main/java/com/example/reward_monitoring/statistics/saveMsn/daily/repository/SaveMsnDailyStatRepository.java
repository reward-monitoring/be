package com.example.reward_monitoring.statistics.saveMsn.daily.repository;


import com.example.reward_monitoring.statistics.saveMsn.daily.entity.SaveMsnDailyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaveMsnDailyStatRepository extends JpaRepository<SaveMsnDailyStat,Integer> {

    @Query("SELECT s FROM SaveMsnDailyStat s WHERE s.partDate > :startAt")
    public List<SaveMsnDailyStat> findByStartAt(LocalDate startAt);

    @Query("SELECT s FROM SaveMsnDailyStat s WHERE s.partDate < :endAt")
    public List<SaveMsnDailyStat> findByEndAt(LocalDate endAt);

    @Query("SELECT s FROM SaveMsnDailyStat s WHERE s.partDate BETWEEN :startAt AND :endAt")
    public List<SaveMsnDailyStat> findByBothAt(@Param("startAt") LocalDate startAt, @Param("endAt") LocalDate endAt);


    public List<SaveMsnDailyStat> findByAdvertiser_Advertiser(String advertiser);

    public List<SaveMsnDailyStat> findByServer_ServerUrl(String url);

    public List<SaveMsnDailyStat> findByMediaCompany_CompanyName(String companyName);
}
