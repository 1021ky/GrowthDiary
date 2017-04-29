package jp.vaivailx.growthdiary.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.vaivailx.growthdiary.model.dataaccess.DiaryDAOFactory;
import jp.vaivailx.growthdiary.model.DiaryData;
import jp.vaivailx.growthdiary.model.dataaccess.IDiaryDAO;
import jp.vaivailx.growthdiary.model.dataaccess.SaveLocation;
import jp.vaivailx.growthdiary.view.DiaryDTO;

/**
 * Created by vaivailx on 2016/07/27.
 */
public class DiaryManager {
  private static SaveLocation saveLocation;

  private IDiaryDAO diaryDAO;

  public DiaryManager() {}

  public void onCreate() {
    // とりあえず今はローカル固定
    saveLocation = SaveLocation.LOCAL;
    diaryDAO = DiaryDAOFactory.createInstance(saveLocation);
    diaryDAO.initialize();
  }

  public void onDestroy() {
    diaryDAO.close();
  }

  public void setSaveLocation(SaveLocation location) {
    // TODO: 保存先を可変にするならば、設定管理オブジェクトがこいつに通知する
    saveLocation = location;
  }

  /**
   * 日記データを更新する。更新対象がない場合は追加する。
   * @param diaryDTO 追加・更新データ
   * @return 追加・更新結果
   */
  public DiaryDTO updateDiary(DiaryDTO diaryDTO) {
    DiaryData updateData = convertDTOToData(diaryDTO);
    DiaryData data = diaryDAO.getDiary(updateData);
    if (data == null) {
      diaryDAO.addDiary(updateData);
    } else {
      diaryDAO.updateDiary(updateData);
    }
    DiaryData updatedData = diaryDAO.getDiary(updateData);
    return convertDataToDTO(updatedData);
  }

  public List<DiaryDTO> getDiaryList(Date startDate, Date endDate) {
    List<DiaryData> dataList = diaryDAO.getDiaryList(startDate, endDate);
    List<DiaryDTO> dtoList = new ArrayList<DiaryDTO>();
    for (DiaryData data : dataList) {
      dtoList.add(convertDataToDTO(data));
    }
    return dtoList;
  }

  /**
   * 現在から指定された件数分だけの日記を返す
   * @param diaryNum 取得する日記の数
   * @return 取得された日記
   */
  public List<DiaryDTO> getLaterDiaryList(int diaryNum) {
    List<DiaryData> dataList = diaryDAO.getLaterDiaryList(diaryNum);
    List<DiaryDTO> dtoList = new ArrayList<DiaryDTO>();
    for (DiaryData data : dataList) {
      dtoList.add(convertDataToDTO(data));
    }
    return dtoList;
  }

  public List<DiaryDTO> getLaterDiaryList(int diaryNum, int offset) {
    List<DiaryData> dataList = diaryDAO.getLaterDiaryList(diaryNum, offset);
    List<DiaryDTO> dtoList = new ArrayList<DiaryDTO>();
    for (DiaryData data : dataList) {
      dtoList.add(convertDataToDTO(data));
    }
    return dtoList;
  }

  /**
   * 指定されたIDの日記を返す
   * @param diaryID 日記のID
   * @return 日記
   */
  public DiaryDTO getDiary(long diaryID) {
    DiaryData data = diaryDAO.getDiary(diaryID);
    return data != null ? convertDataToDTO(data) : null;
  }

  public DiaryDTO getDiary(Date titleDate) {
    DiaryData data = diaryDAO.getDiary(titleDate);
    return data != null ? convertDataToDTO(data) : null;
  }

  public void deleteDiary(Date titleDate) {
    diaryDAO.deleteDiary(titleDate);
  }

  private DiaryData convertDTOToData(DiaryDTO dto) {
    DiaryData data = new DiaryData();
    data.id = dto.id;
    data.titleDate = dto.titleDate;
    data.fact = dto.fact;
    data.knowledge = dto.knowledge;
    data.realization = dto.realization;
    data.theme = dto.theme;
    data.evaluation = dto.evaluation;
    data.updateDate = dto.updateDate;
    return data;
  }

  private DiaryDTO convertDataToDTO(DiaryData data) {
    DiaryDTO dto = new DiaryDTO();
    dto.id = data.id;
    dto.titleDate = data.titleDate;
    dto.fact = data.fact;
    dto.knowledge = data.knowledge;
    dto.realization = data.realization;
    dto.theme = data.theme;
    dto.evaluation = data.evaluation;
    dto.updateDate = data.updateDate;
    return dto;
  }

}
