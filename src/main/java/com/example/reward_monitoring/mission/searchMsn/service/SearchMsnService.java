package com.example.reward_monitoring.mission.searchMsn.service;



import com.example.reward_monitoring.general.advertiser.entity.Advertiser;
import com.example.reward_monitoring.general.advertiser.repository.AdvertiserRepository;
import com.example.reward_monitoring.general.userServer.entity.Server;
import com.example.reward_monitoring.general.userServer.repository.ServerRepository;
import com.example.reward_monitoring.mission.searchMsn.dto.*;
import com.example.reward_monitoring.mission.searchMsn.entity.SearchMsn;
import com.example.reward_monitoring.mission.searchMsn.repository.SearchMsnRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SearchMsnService {

    @Autowired
    private SearchMsnRepository searchMsnRepository;
    @Autowired
    private AdvertiserRepository advertiserRepository;
    @Autowired
    private ServerRepository serverRepository;

    public SearchMsn edit(int idx, SearchMsnEditDto dto) {
        SearchMsn searchMsn =searchMsnRepository.findByIdx(idx);
        if(searchMsn==null)
            return null;
        if(dto.getMissionDefaultQty() != null)
            searchMsn.setMissionDefaultQty(dto.getMissionDefaultQty());
        if(dto.getMissionDailyCap() !=null)
            searchMsn.setMissionDailyCap(dto.getMissionDailyCap());
        if(dto.getMissionTitle()!=null)
            searchMsn.setMissionTitle(dto.getMissionTitle());
        if(dto.getSearchKeyword()!=null)
            searchMsn.setSearchKeyword(dto.getSearchKeyword());
        if (dto.getStartAtMsn() != null)
            searchMsn.setStartAtMsn(dto.getStartAtMsn());
        if (dto.getEndAtMsn() != null)
            searchMsn.setEndAtMsn(dto.getEndAtMsn());
        if (dto.getStartAtCap() != null)
            searchMsn.setStartAtCap(dto.getStartAtCap());
        if (dto.getEndAtCap() != null)
            searchMsn.setEndAtCap(dto.getEndAtCap());
        if (dto.getMissionActive() != null) {
            boolean bool = dto.getMissionActive();
            searchMsn.setMissionActive(bool);
        }
        if (dto.getMissionExposure() != null) {
            boolean bool = dto.getMissionExposure();
            searchMsn.setMissionExposure(bool);
        }
        if (dto.getDupParticipation() != null) {
            boolean bool = dto.getDupParticipation();
            searchMsn.setDupParticipation(bool);
        }
        if (dto.getReEngagementDay() != null) {
            searchMsn.setReEngagementDay(dto.getReEngagementDay());
        }
        return searchMsn;
    }

    public SearchMsn add(SearchMsnReadDto dto) {
        Server serverEntity = serverRepository.findByServerUrl_(dto.getUrl());
        Advertiser advertiserEntity = advertiserRepository.findByAdvertiser_(dto.getAdvertiser());
        return dto.toEntity(advertiserEntity,serverEntity);
    }

    public SearchMsn getSearchMsn(int idx) {
        return searchMsnRepository.findByIdx(idx);
    }

    public List<SearchMsn> getSearchMsns() {
        return searchMsnRepository.findAll();
    }

    public SearchMsn delete(int idx) {
        SearchMsn target = searchMsnRepository.findByIdx(idx);
        if(target==null)
            return null;
        searchMsnRepository.delete(target);
        return target;
    }

    public List<SearchMsn> searchSearchMsn(SearchMsnSearchDto dto) {

        List<SearchMsn> target_date=null;
        List<SearchMsn> target_dailyCap = null;

        List<SearchMsn> target_dup_Participation = null;
        List<SearchMsn> target_mission_active = null;
        List<SearchMsn> target_mission_exposure = null;
        List<SearchMsn> target_data_Type = null;

        List<SearchMsn> target_advertiser=null;
        List<SearchMsn> target_advertiser_details=null; // 선택 1
        List<SearchMsn> target_mission_title = null; // 선택 2


        List<SearchMsn> result=null;


        if(dto.getStartAtMsn() != null || dto.getEndAtMsn() != null){
            if(dto.getStartAtMsn() != null){
                if(dto.getEndAtMsn() == null){
                    ZoneId zoneId = ZoneId.of("Asia/Seoul");
                    ZonedDateTime start_time = dto.getStartAtMsn().atStartOfDay(zoneId);
                    target_date = searchMsnRepository.findByStartDate(start_time);
                }else{
                    ZoneId zoneId = ZoneId.of("Asia/Seoul");
                    ZonedDateTime start_time = dto.getStartAtMsn().atStartOfDay(zoneId);
                    ZonedDateTime end_time = dto.getEndAtMsn().atStartOfDay(zoneId);

                    target_date = searchMsnRepository.findByBothDate(start_time,end_time);
                }

            }
            else {
                ZoneId zoneId = ZoneId.of("Asia/Seoul");
                ZonedDateTime end_time = dto.getEndAtMsn().atStartOfDay(zoneId);

                target_date = searchMsnRepository.findByEndDate(end_time);
            }

        }

        if(dto.getStartAtCap() != null || dto.getEndAtCap() != null){
            if(dto.getStartAtCap() != null){
                if(dto.getEndAtCap() == null){
                    target_dailyCap = searchMsnRepository.findByStartAtCap(dto.getStartAtCap());
                }else{
                    target_dailyCap = searchMsnRepository.findByBothCap(dto.getStartAtCap(),dto.getEndAtCap());
                }

            }
            else {
                target_dailyCap = searchMsnRepository.findByEndAtCap(dto.getEndAtCap());
            }

        }

        if(dto.getMissionActive() != null){
            target_mission_active = searchMsnRepository.findByMissionActive(dto.getMissionActive());
        }

        if(dto.getDupParticipation() != null){
            target_dup_Participation = searchMsnRepository.findByDupParticipation(dto.getDupParticipation());
        }
        if(dto.getMissionExposure() != null){
            target_mission_exposure = searchMsnRepository.findByMissionExposure(dto.getMissionExposure());
        }

        if(dto.getDataType() != null){
            target_data_Type= searchMsnRepository.findByDataType(dto.getDataType());
        }

        if(dto.getAdvertiser()!=null){
            target_advertiser = searchMsnRepository.findByAdvertiser(dto.getAdvertiser());
        }

        if(dto.getAdvertiserDetails()!=null){
            target_advertiser_details = searchMsnRepository.findByAdvertiserDetails(dto.getAdvertiserDetails());
        }

        if(dto.getMissionTitle()!= null) {
            target_mission_title = searchMsnRepository.findByMissionTitle(dto.getMissionTitle());
        }


        if(target_date!=null) {
            result = new ArrayList<>(target_date);
            if(target_dailyCap != null)
                result.retainAll(target_dailyCap);

            if(target_dup_Participation !=null)
                result.retainAll(target_dup_Participation);
            if(target_mission_active != null)
                result.retainAll(target_mission_active);
            if(target_mission_exposure != null)
                result.retainAll(target_mission_exposure);
            if(target_data_Type != null)
                result.retainAll(target_data_Type);

            if(target_advertiser != null)
                result.retainAll(target_advertiser);
            if(target_advertiser_details != null)
                result.retainAll(target_advertiser_details);
            if(target_mission_title != null)
                result.retainAll(target_mission_title);
        }


        else if(target_dailyCap !=null) {
            result = new ArrayList<>(target_dailyCap);

            if(target_dup_Participation !=null)
                result.retainAll(target_dup_Participation);
            if(target_mission_active != null)
                result.retainAll(target_mission_active);
            if(target_mission_exposure != null)
                result.retainAll(target_mission_exposure);
            if(target_data_Type != null)
                result.retainAll(target_data_Type);
            if(target_advertiser != null)
                result.retainAll(target_advertiser);
            if(target_advertiser_details != null)
                result.retainAll(target_advertiser_details);
            if(target_mission_title != null)
                result.retainAll(target_mission_title);

        }

        else if(target_dup_Participation !=null) {
            result = new ArrayList<>(target_dup_Participation);

            if(target_mission_active != null)
                result.retainAll(target_mission_active);
            if(target_mission_exposure != null)
                result.retainAll(target_mission_exposure);
            if(target_data_Type != null)
                result.retainAll(target_data_Type);
            if(target_advertiser != null)
                result.retainAll(target_advertiser);
            if(target_advertiser_details != null)
                result.retainAll(target_advertiser_details);
            if(target_mission_title != null)
                result.retainAll(target_mission_title);
        }
        else if(target_mission_active !=null) {
            result = new ArrayList<>(target_mission_active);

            if(target_mission_exposure != null)
                result.retainAll(target_mission_exposure);
            if(target_data_Type != null)
                result.retainAll(target_data_Type);
            if(target_advertiser != null)
                result.retainAll(target_advertiser);
            if(target_advertiser_details != null)
                result.retainAll(target_advertiser_details);
            if(target_mission_title != null)
                result.retainAll(target_mission_title);
        }
        else if(target_mission_exposure !=null) {
            result = new ArrayList<>(target_mission_exposure);

            if(target_data_Type != null)
                result.retainAll(target_data_Type);
            if(target_advertiser != null)
                result.retainAll(target_advertiser);
            if(target_advertiser_details != null)
                result.retainAll(target_advertiser_details);
            if(target_mission_title != null)
                result.retainAll(target_mission_title);
        }
        else if(target_data_Type !=null) {
            result = new ArrayList<>(target_data_Type);

            if(target_advertiser != null)
                result.retainAll(target_advertiser);
            if(target_advertiser_details != null)
                result.retainAll(target_advertiser_details);
            if(target_mission_title != null)
                result.retainAll(target_mission_title);
        }
        else if(target_advertiser !=null) {
            result = new ArrayList<>(target_advertiser);

            if(target_advertiser_details != null)
                result.retainAll(target_advertiser_details);
            if(target_mission_title != null)
                result.retainAll(target_mission_title);
        }
        else if (target_advertiser_details != null)
            result = new ArrayList<>(target_advertiser_details);
        else if (target_mission_title != null)
            result = new ArrayList<>(target_mission_title);

        return result;

    }
    public Sheet excelDownload(List<SearchMsn> list, Workbook wb){

        int size = list.size();
        Sheet sheet = wb.createSheet("정답 미션 목록");
        Row row;
        Cell cell;
        CellStyle cellStyle = wb.createCellStyle();
        applyCellStyle(cellStyle);
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("quizIdx");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1);
        cell.setCellValue("기본 수량");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2);
        cell.setCellValue("데일리캡");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellValue("광고주");
        sheet.setColumnWidth(3, 16 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        cell.setCellValue("광고주 상세");
        sheet.setColumnWidth(4, 16 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        cell.setCellValue("미션 제목");
        cell.setCellStyle(cellStyle);
        sheet.setColumnWidth(5, 16 * 256);
        cell = row.createCell(6);
        cell.setCellValue("검색 키워드");
        sheet.setColumnWidth(6, 20 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        cell.setCellValue("미션 시작일시");
        sheet.setColumnWidth(7, 20 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(8);
        cell.setCellValue("미션 종료일시");
        sheet.setColumnWidth(8, 20 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9);
        cell.setCellValue("데일리캡 시작일");
        sheet.setColumnWidth(9, 20 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10);
        cell.setCellValue("데일리캡 종료일");
        sheet.setColumnWidth(10, 20 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(11);
        cell.setCellValue("미션 사용여부");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(12);
        cell.setCellValue("미션 노출여부");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(13);
        cell.setCellValue("중복참여");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(14);
        cell.setCellValue("재참여 가능일");
        cell.setCellStyle(cellStyle);
        sheet.setColumnWidth(14, 20 * 256);

        for (SearchMsn searchMsn : list) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(searchMsn.getIdx());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(1);
            cell.setCellValue(searchMsn.getMissionDefaultQty());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue(searchMsn.getMissionDailyCap());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellValue(searchMsn.getAdvertiser().getAdvertiser());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellValue(searchMsn.getAdvertiserDetails());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellValue(searchMsn.getMissionTitle());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            cell.setCellValue(searchMsn.getSearchKeyword());
            cell.setCellStyle(cellStyle);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(searchMsn.getStartAtMsn().format(formatter));
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(searchMsn.getEndAtMsn().format(formatter));
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle);
            DateTimeFormatter formatter_ = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            cell.setCellValue(searchMsn.getStartAtCap().format(formatter_));
            cell.setCellStyle(cellStyle);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(searchMsn.getEndAtCap().format(formatter_));
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle);
            if(searchMsn.isMissionActive())
                cell.setCellValue("활성");
            else
                cell.setCellValue("비활성");
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle);
            if(searchMsn.isMissionExposure())
                cell.setCellValue("노출");
            else
                cell.setCellValue("비노출");

            cell = row.createCell(13);
            cell.setCellStyle(cellStyle);
            if(searchMsn.isDupParticipation())
                cell.setCellValue("중복 허용");
            else
                cell.setCellValue("중복 불가");
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(searchMsn.getReEngagementDay());
        }
        return sheet;
    }

    private void applyCellStyle(CellStyle cellStyle) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
//        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
    }

    @Transactional
    public boolean readExcel(MultipartFile file)throws IOException {



        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        SearchMsnReadDto dto = new SearchMsnReadDto();
        Advertiser advertiserEntity=null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {

            XSSFRow row = worksheet.getRow(i);

            if (row.getCell(1) != null && row.getCell(1).getCellType() == CellType.NUMERIC) {
                dto.setMissionDefaultQty((int) row.getCell(1).getNumericCellValue());
            }
            if(row.getCell(2)!=null)
                dto.setMissionDailyCap((int)row.getCell(2).getNumericCellValue());

            advertiserEntity = advertiserRepository.findByAdvertiser_(row.getCell(3).getStringCellValue());
            //셀에있는 데이터를 읽어와 그걸로 repository 에서 일치하는 advertiser 를 가져온다.
            if(row.getCell(4)!=null)
                dto.setAdvertiserDetails(row.getCell(4).getStringCellValue());
            if(row.getCell(5)!=null)
                dto.setMissionTitle(row.getCell(5).getStringCellValue());
            if(row.getCell(6)!=null)
                dto.setSearchKeyword(row.getCell(6).getStringCellValue());
            if(row.getCell(7)!=null)
                dto.setStartAtMsn(ZonedDateTime.of(LocalDateTime.parse(row.getCell(7).getStringCellValue(),formatter),ZoneId.systemDefault()));
            if(row.getCell(8)!=null)
                dto.setEndAtMsn(ZonedDateTime.of(LocalDateTime.parse(row.getCell(8).getStringCellValue(),formatter),ZoneId.systemDefault()));
            if(row.getCell(9)!=null)
                dto.setStartAtCap(LocalDate.parse(row.getCell(9).getStringCellValue(),formatter_date));
            if(row.getCell(10)!=null)
                dto.setEndAtCap(LocalDate.parse(row.getCell(10).getStringCellValue(),formatter_date));

            if(Objects.equals(row.getCell(11).getStringCellValue(), "활성"))
                dto.setMissionActive(true);
            else
                dto.setMissionActive(false);
            if(Objects.equals(row.getCell(12).getStringCellValue(), "노출"))
                dto.setMissionExposure(true);
            else
                dto.setMissionExposure(false);
            if(Objects.equals(row.getCell(13).getStringCellValue(),"중복 허용"))
                dto.setDupParticipation(true);
            else
                dto.setDupParticipation(false);
            dto.setReEngagementDay((int)row.getCell(14).getNumericCellValue());

            searchMsnRepository.save(dto.toEntity(advertiserEntity,null));

        }
        return true;
    }

    public boolean allMissionEnd() {

        List<SearchMsn> target = getSearchMsns();
        if(target == null)
            return false;

        for(SearchMsn searchMsn : target){
            searchMsn.setMissionExposure(false);
            searchMsn.setMissionActive(false);
            searchMsnRepository.save(searchMsn);
        }
        return true;
    }
    public List<SearchMsn> searchSearchMsnByConsumed(SearchMsnSearchDto dto) {//현재리스트 소진량(검색)에서 검색
        return null;
    }

    public boolean changeAbleDay(SearchMsnAbleDayDto dto, int idx) {
        SearchMsn searchMsn = searchMsnRepository.findByIdx(idx);
        if(searchMsn == null)
            return false;

        searchMsn.setDupParticipation(dto.isDupParticipation());
        if(dto.getReEngagementDay()!=null)
            searchMsn.setReEngagementDay(dto.getReEngagementDay());
        return true;

    }

    public boolean changeMissionActive(int idx, SearchMsnActiveDto dto) {
        SearchMsn target =searchMsnRepository.findByIdx(idx);
        if(target ==null)
            return false;
        target.setMissionActive(dto.isActive());
        return true;
    }
    public boolean changeMissionExpose(int idx, SearchMsnExposeDto dto) {

        SearchMsn target =searchMsnRepository.findByIdx(idx);
        if(target ==null)
            return false;
        target.setMissionExposure(dto.isExpose());
        return true;
    }
}
