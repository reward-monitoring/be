package com.example.reward_monitoring.statistics.searchMsn.daily.service;


import com.example.reward_monitoring.statistics.answerMsnStat.daily.entity.AnswerMsnDailyStat;
import com.example.reward_monitoring.statistics.searchMsn.daily.dto.SearchMsnDailyStatSearchDto;
import com.example.reward_monitoring.statistics.searchMsn.daily.entity.SearchMsnDailyStat;
import com.example.reward_monitoring.statistics.searchMsn.daily.repository.SearchMsnDailyStatRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class SearchMsnDailyService {

    @Autowired
    private SearchMsnDailyStatRepository searchMsnDailyStatRepository;





    public Sheet excelDownload(List<SearchMsnDailyStat> list, Workbook wb){

        int size = list.size();
        Sheet sheet = wb.createSheet("정답 미션 목록");
        Row row = null;
        Cell cell = null;
        CellStyle cellStyle = wb.createCellStyle();
        applyCellStyle(cellStyle);
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("참여일");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1);
        cell.setCellValue("매체사 IDX");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2);
        cell.setCellValue("광고주 IDX");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellValue("광고주 디테일");
        sheet.setColumnWidth(3, 16 * 256); //8자
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        cell.setCellValue("광고주 상세");
        sheet.setColumnWidth(4, 16 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        cell.setCellValue("미션 IDX");
        cell.setCellStyle(cellStyle);
        sheet.setColumnWidth(5, 16 * 256);
        cell = row.createCell(6);
        cell.setCellValue("미션 제목");
        sheet.setColumnWidth(6, 20 * 256); //8자
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        cell.setCellValue("랜딩 카운트");
        sheet.setColumnWidth(7, 20 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(8);
        cell.setCellValue("참여 카운트");
        sheet.setColumnWidth(8, 20 * 256);
        cell.setCellStyle(cellStyle);


        for (SearchMsnDailyStat searchMsnDailyStat: list) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(searchMsnDailyStat.getPartDate());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(1);
            cell.setCellValue(searchMsnDailyStat.getMediaCompany().getIdx());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue(searchMsnDailyStat.getAdvertiser().getIdx());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellValue(searchMsnDailyStat.getSearchMsn().getAdvertiserDetails());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellValue(searchMsnDailyStat.getSearchMsn().getIdx());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellValue(searchMsnDailyStat.getSearchMsn().getMissionTitle());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            cell.setCellValue(searchMsnDailyStat.getLandingCnt());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(7);
            cell.setCellValue(searchMsnDailyStat.getPartCnt());
            cell.setCellStyle(cellStyle);
        }
        return sheet;
    }

    private void applyCellStyle(CellStyle cellStyle) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
    }

    public List<SearchMsnDailyStat> getSearchMsnsDailys() {
        return searchMsnDailyStatRepository.findAll();
    }

    public List<SearchMsnDailyStat> searchSearchMsnDaily(SearchMsnDailyStatSearchDto dto) {

        List<SearchMsnDailyStat> target_date = null;
        List<SearchMsnDailyStat>  target_serverUrl = null;
        List<SearchMsnDailyStat>  target_advertiser = null;
        List<SearchMsnDailyStat>  target_companyName = null;

        List<SearchMsnDailyStat>  result;
        boolean changed = false;

        result = new ArrayList<>(searchMsnDailyStatRepository.findAll());

        if(dto.getUrl() != null)
            target_serverUrl = searchMsnDailyStatRepository.findByServer_ServerUrl(dto.getUrl());

        if(dto.getAdvertiser()!=null)
            target_advertiser = searchMsnDailyStatRepository.findByAdvertiser_Advertiser(dto.getAdvertiser());

        if(dto.getMediacompany()!=null)
            target_companyName = searchMsnDailyStatRepository.findByMediaCompany_CompanyName(dto.getMediacompany());

        if(dto.getStartAt() != null || dto.getEndAt() != null){
            if(dto.getStartAt() != null){
                if(dto.getEndAt() == null)
                    target_date = searchMsnDailyStatRepository.findByStartAt(dto.getStartAt());
                else
                    target_date = searchMsnDailyStatRepository.findByBothAt(dto.getStartAt(),dto.getEndAt());

            }
            else
                target_date = searchMsnDailyStatRepository.findByEndAt(dto.getEndAt());
        }

        if(target_date != null){
            Set<Integer> idxSet = target_date.stream().map(SearchMsnDailyStat::getIdx).collect(Collectors.toSet());
            result = result.stream().filter(searchMsnDailyStat-> idxSet.contains(searchMsnDailyStat.getIdx())).distinct().collect(Collectors.toList());
            changed = true;
        }
        if(target_serverUrl != null){
            Set<Integer> idxSet = target_serverUrl.stream().map(SearchMsnDailyStat::getIdx).collect(Collectors.toSet());
            result = result.stream().filter(searchMsnDailyStat-> idxSet.contains(searchMsnDailyStat.getIdx())).distinct().collect(Collectors.toList());
            changed = true;
        }
        if(target_advertiser != null){
            Set<Integer> idxSet = target_advertiser.stream().map(SearchMsnDailyStat::getIdx).collect(Collectors.toSet());
            result = result.stream().filter(searchMsnDailyStat-> idxSet.contains(searchMsnDailyStat.getIdx())).distinct().collect(Collectors.toList());
            changed = true;
        }

        if(target_companyName != null){
            Set<Integer> idxSet = target_companyName.stream().map(SearchMsnDailyStat::getIdx).collect(Collectors.toSet());
            result = result.stream().filter(searchMsnDailyStat-> idxSet.contains(searchMsnDailyStat.getIdx())).distinct().collect(Collectors.toList());
            changed = true;
        }
        if(!changed)
            result = new ArrayList<>();

        return result;
    }
}
