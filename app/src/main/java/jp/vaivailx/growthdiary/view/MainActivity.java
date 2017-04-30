package jp.vaivailx.growthdiary.view;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import jp.vaivailx.growthdiary.R;
import jp.vaivailx.growthdiary.view.graph.GrowthGraphChartFragment;
import jp.vaivailx.growthdiary.view.editor.EditorFragment;
import jp.vaivailx.growthdiary.view.list.DiaryListFragment;

public class MainActivity
        extends AppCompatActivity
        implements ViewPager.OnPageChangeListener
        , GrowthGraphChartFragment.OnGraphFragmentInteractionListener
        , EditorFragment.OnEditorFragmentInteractionListener
        , DiaryListFragment.OnListFragmentInteractionListener{

//  /**
//   * The {@link android.support.v4.view.PagerAdapter} that will provide
//   * fragments for each of the sections. We use a
//   * {@link FragmentPagerAdapter} derivative, which will keep every
//   * loaded fragment in memory. If this becomes too memory intensive, it
//   * may be best to switch to a
//   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
//   */
//  private SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  private TopViewPager topViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setViews();
  }

  private void setViews() {
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.

    // Set up the ViewPager with the sections adapter.
    topViewPager = (TopViewPager) findViewById(R.id.topViewPager);
    FragmentPagerAdapter pagerAdapter = new SectionPageAdapter(getSupportFragmentManager(), this.getApplicationContext());
    topViewPager.setAdapter(pagerAdapter);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(topViewPager);
  }

  @Override
  protected void onDestroy() {
    // Fragmentが終了するまで待つ
    while(getFragmentManager().isDestroyed()){
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    super.onDestroy();
  }

  /**
   * メニュー作成
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  /**
   * メニューアイテム選択イベント
   * @param item
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        // コードを追加
        Intent intent = new Intent(MainActivity.this, OSSLicenseActivity.class);
        startActivity(intent);
        break;
    }
    return super.onOptionsItemSelected(item);
  }


  @Override
  public void onFragmentInteraction(Uri uri) {
    Log.i("TAG", "onFragmentInteraction: ");
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {

  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }

  @Override
  public void onListFragmentInteraction(long diaryID) {
    if (topViewPager instanceof TopViewPager) {
      topViewPager.setCurrentDiaryItemID(diaryID);
      topViewPager.setCurrentItem(0, true);
    }
  }

  @Override
  public void onEditorFragmentInteraction(Uri uri) {
    String s = "";
    // TODO 全体にリロードをかける
  }
}
