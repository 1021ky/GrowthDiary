package jp.vaivailx.growthdiary.view;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import jp.vaivailx.growthdiary.R;
import jp.vaivailx.growthdiary.view.editor.EditorFragment;
import jp.vaivailx.growthdiary.view.graph.GrowthGraphChartFragment;
import jp.vaivailx.growthdiary.view.list.DiaryListFragment;

/**
 * Created by vaivailx on 2016/07/01.
 */
public class SectionPageAdapter extends FragmentPagerAdapter {

  private Context appContext = null;
  private long currentFocusedDiaryItemID = 0;

  public SectionPageAdapter(FragmentManager fm, Context appContext) {
    super(fm);
    this.appContext = appContext;
  }

  public void setCurrentFocusedDiaryItemID(long itemID) {
    this.currentFocusedDiaryItemID = itemID;
  }

  @Override
  public Fragment getItem(int position) {
    Fragment selectedFragment = null;
    switch (position) {
      case 0:
        selectedFragment = EditorFragment.newInstance("", "");
        break;
      case 1:
        selectedFragment = DiaryListFragment.newInstance(10);
        break;
      case 2:
        selectedFragment = GrowthGraphChartFragment.newInstance("", "");
        break;
      default:
        selectedFragment = EditorFragment.newInstance("", "");
    }
    return  selectedFragment;
  }

  @Override
  public int getCount() {
    return 3;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return appContext.getString(R.string.MAIN_TAB_TITLE_EDITOR);
      case 1:
        return appContext.getString(R.string.MAIN_TAB_TITLE_LIST);
      case 2:
        return appContext.getString(R.string.MAIN_TAB_TITLE_GRAPH);
    }
    return "error";
  }

  @Override
  public void setPrimaryItem(ViewGroup container, int position, Object object) {
    super.setPrimaryItem(container, position, object);

    if (object instanceof EditorFragment) {
      ((EditorFragment)object).update(currentFocusedDiaryItemID);
    } else if (object instanceof DiaryListFragment) {
      ((DiaryListFragment)object).update();
    } else if (object instanceof GrowthGraphChartFragment) {
      ((GrowthGraphChartFragment)object).update();
    }
  }
}
