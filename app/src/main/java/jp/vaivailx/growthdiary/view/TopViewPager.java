package jp.vaivailx.growthdiary.view;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by vaivailx on 2016/07/01.
 */
public class TopViewPager extends ViewPager {

  public TopViewPager(Context context) {
    super(context);
  }

  public TopViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setCurrentDiaryItemID(long diaryItemID) {
    ((SectionPageAdapter)getAdapter()).setCurrentFocusedDiaryItemID(diaryItemID);
  }

  public void setFragmentManager(FragmentManager fragmentManager) {
    setAdapter(new SectionPageAdapter(fragmentManager, getContext()));
  }

  @Override
  public void setCurrentItem(int item) {
    super.setCurrentItem(item);
    // TODO リロードする
  }
}
