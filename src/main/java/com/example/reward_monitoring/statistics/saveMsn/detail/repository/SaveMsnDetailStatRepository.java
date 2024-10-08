package com.example.reward_monitoring.statistics.saveMsn.detail.repository;


import com.example.reward_monitoring.statistics.saveMsn.detail.entity.SaveMsnDetailsStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaveMsnDetailStatRepository extends JpaRepository<SaveMsnDetailsStat,Integer> {

    @Query("SELECT s FROM SaveMsnDetailsStat s WHERE s.registrationDate > :startAt")
    public List<SaveMsnDetailsStat> findByStartAt(LocalDate startAt);

    @Query("SELECT s FROM SaveMsnDetailsStat s WHERE s.registrationDate < :endAt")
    public List<SaveMsnDetailsStat> findByEndAt(LocalDate endAt);

    @Query("SELECT s FROM SaveMsnDetailsStat s WHERE s.registrationDate BETWEEN :startAt AND :endAt")
    public List<SaveMsnDetailsStat> findByBothAt(@Param("startAt") LocalDate startAt, @Param("endAt") LocalDate endAt);

    public List<SaveMsnDetailsStat> findByAdvertiser_Advertiser(String advertiser);

    public List<SaveMsnDetailsStat> findByServer_ServerUrl(String url);

    public List<SaveMsnDetailsStat> findByMediaCompany_companyName(String companyName);

    public List<SaveMsnDetailsStat> findByIsAbuse(boolean check);

    public List<SaveMsnDetailsStat> findByResponse(boolean check);

    public List<SaveMsnDetailsStat> findBySaveMsn_Idx(int idx);
}
