package com.example.reward_monitoring.statistics.searchMsn.daily.repository;



import com.example.reward_monitoring.statistics.searchMsn.daily.entity.SearchMsnDailyStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SearchMsnDailyStatRepository extends JpaRepository<SearchMsnDailyStat,Integer> {

    @Query("SELECT s FROM SearchMsnDailyStat s WHERE s.partDate > :startAt")
    public List<SearchMsnDailyStat> findByStartAt(LocalDate startAt);

    @Query("SELECT s FROM SearchMsnDailyStat s WHERE s.partDate < :endAt")
    public List<SearchMsnDailyStat> findByEndAt(LocalDate endAt);

    @Query("SELECT s FROM SearchMsnDailyStat s WHERE s.partDate BETWEEN :startAt AND :endAt")
    public List<SearchMsnDailyStat> findByBothAt(@Param("startAt") LocalDate startAt, @Param("endAt") LocalDate endAt);


    public List<SearchMsnDailyStat> findByAdvertiser_Advertiser(String advertiser);

    public List<SearchMsnDailyStat> findByServer_ServerUrl(String url);

    public List<SearchMsnDailyStat> findByMediaCompany_CompanyName(String companyName);
}
