package com.example.reward_monitoring.statistics.answerMsnStat.detail.repository;


import com.example.reward_monitoring.statistics.answerMsnStat.detail.entity.AnswerMsnDetailsStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AnswerMsnDetailStatRepository extends JpaRepository<AnswerMsnDetailsStat,Integer> {

    @Query("SELECT a FROM AnswerMsnDetailsStat a WHERE a.registrationDate > :startAt")
    public List<AnswerMsnDetailsStat> findByStartAt(LocalDate startAt);

    @Query("SELECT a FROM AnswerMsnDetailsStat a WHERE a.registrationDate < :endAt")
    public List<AnswerMsnDetailsStat> findByEndAt(LocalDate endAt);

    @Query("SELECT a FROM AnswerMsnDetailsStat a WHERE a.registrationDate BETWEEN :startAt AND :endAt")
    public List<AnswerMsnDetailsStat> findByBothAt(@Param("startAt") LocalDate startAt, @Param("endAt") LocalDate endAt);

    public List<AnswerMsnDetailsStat> findByAdvertiser_Advertiser(String advertiser);

    public List<AnswerMsnDetailsStat> findByServer_ServerUrl(String url);

    public List<AnswerMsnDetailsStat> findByMediaCompany_companyName(String companyName);

    public List<AnswerMsnDetailsStat> findByIsAbuse(boolean check);

    public List<AnswerMsnDetailsStat> findByResponse(boolean check);

    public List<AnswerMsnDetailsStat> findByAnswerMsn_Idx(int idx);
}
