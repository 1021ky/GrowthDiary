package jp.vaivailx.growthdiary.model.dataaccess;

/**
 * Created by vaivailx on 2016/08/06.
 */
public class DiaryDAOFactory {
  public static IDiaryDAO createInstance(SaveLocation saveLocation) {
    switch (saveLocation) {
      case LOCAL:
        return new LocalDiaryDAO();
      case DROP_BOX:
        return new DropBoxDiaryDAO();
      default:
        return null;
    }
  }
}
