package jp.vaivailx.growthdiary.view.list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.vaivailx.growthdiary.R;
import jp.vaivailx.growthdiary.controller.DiaryManager;
import jp.vaivailx.growthdiary.view.DiaryDTO;
import jp.vaivailx.growthdiary.view.list.DiaryListContent.DiaryItem;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DiaryListFragment extends Fragment {

  // TODO: Customize parameter argument names
  private static final String ARG_COLUMN_COUNT = "column-count";
  // 一回に取得する日記数
  private static final int GET_DIARY_NUM = 15;
  // TODO: Customize parameters
  private int mColumnCount = 1;
  private OnListFragmentInteractionListener mListener;
  private DiaryManager diaryManager;

  private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
  private SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
  private SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
  private SimpleDateFormat dayOfTheWeekFormat = new SimpleDateFormat("EEE");
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private DiaryListRecyclerViewAdapter mListAdapter;
  private List<DiaryItem> mDiaryItemList;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public DiaryListFragment() {
  }

  // TODO: Customize parameter initialization
  @SuppressWarnings("unused")
  public static DiaryListFragment newInstance(int columnCount) {
    DiaryListFragment fragment = new DiaryListFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_COLUMN_COUNT, columnCount);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    diaryManager = new DiaryManager();
    diaryManager.onCreate();
    mDiaryItemList = new ArrayList<DiaryItem>();
    List<DiaryDTO> diaryDataList = diaryManager.getLaterDiaryList(GET_DIARY_NUM);
    updateDiaryItems(diaryDataList);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    View rootView = inflater.inflate(R.layout.fragment_diary_list, container, false);
    RecyclerView listView = (RecyclerView)(rootView.findViewById(R.id.tab_diary_list_content));
    listView.addOnScrollListener(new ScrollListener(layoutManager){
      @Override
      public void onLoadMore(int current_page) {
        int offset = GET_DIARY_NUM * (current_page - 1);
        List<DiaryDTO> diaryList = diaryManager.getLaterDiaryList(GET_DIARY_NUM, offset);
        addDiaryItems(diaryList);
      }
    });

    listView.setLayoutManager(layoutManager);
//      if (mColumnCount <= 1) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//      } else {
//        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//      }
    mListAdapter = new DiaryListRecyclerViewAdapter(mDiaryItemList, mListener);
    mListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        super.onChanged();
        List<DiaryDTO> diaryDataList = diaryManager.getLaterDiaryList(GET_DIARY_NUM);
        updateDiaryItems(diaryDataList);
      }
    });
    listView.setAdapter(mListAdapter);

    return listView;
  }

  @NonNull
  private void addDiaryItems(List<DiaryDTO> diaryDataList) {
    StringBuffer dateStringBuffer = new StringBuffer();
    for (DiaryDTO diaryDTO : diaryDataList) {
      if (Locale.getDefault().getDisplayLanguage().equals(Locale.JAPANESE.getDisplayLanguage())) {
        dateStringBuffer.append(yearFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append("/");
        dateStringBuffer.append(monthFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append("/");
        dateStringBuffer.append(dayFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append(" ");
//        dateStringBuffer.append(LINE_SEPARATOR);
        dateStringBuffer.append(dayOfTheWeekFormat.format(diaryDTO.titleDate.getTime()));
      } else {
        dateStringBuffer.append(dayOfTheWeekFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append(".");
//        dateStringBuffer.append(LINE_SEPARATOR);
        dateStringBuffer.append(monthFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append("/");
        dateStringBuffer.append(dayFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append("/");
        dateStringBuffer.append(yearFormat.format(diaryDTO.titleDate.getTime()));
      }
      mDiaryItemList.add(
              new DiaryItem(diaryDTO.id
                      , dateStringBuffer.toString()
                      , String.valueOf(diaryDTO.evaluation)
                      , diaryDTO.fact)
      );
      dateStringBuffer.delete(0, dateStringBuffer.length());
    }
  }

  @NonNull
  private void updateDiaryItems(List<DiaryDTO> diaryDataList) {
    mDiaryItemList.clear();

    StringBuffer dateStringBuffer = new StringBuffer();
    for (DiaryDTO diaryDTO : diaryDataList) {
      if (Locale.getDefault().getDisplayLanguage().equals(Locale.JAPANESE.getDisplayLanguage())) {
        dateStringBuffer.append(yearFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append("/");
        dateStringBuffer.append(monthFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append("/");
        dateStringBuffer.append(dayFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append(" ");
//        dateStringBuffer.append(LINE_SEPARATOR);
        dateStringBuffer.append(dayOfTheWeekFormat.format(diaryDTO.titleDate.getTime()));
      } else {
        dateStringBuffer.append(dayOfTheWeekFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append(".");
//        dateStringBuffer.append(LINE_SEPARATOR);
        dateStringBuffer.append(monthFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append("/");
        dateStringBuffer.append(dayFormat.format(diaryDTO.titleDate.getTime()));
        dateStringBuffer.append("/");
        dateStringBuffer.append(yearFormat.format(diaryDTO.titleDate.getTime()));
      }
      mDiaryItemList.add(
              new DiaryItem(diaryDTO.id
                      , dateStringBuffer.toString()
                      , String.valueOf(diaryDTO.evaluation)
                      , diaryDTO.fact)
      );
      dateStringBuffer.delete(0, dateStringBuffer.length());
    }
  }

  public void update() {
    mListAdapter.notifyDataSetChanged();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnListFragmentInteractionListener) {
      mListener = (OnListFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
              + " must implement OnListFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    diaryManager.onDestroy();
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnListFragmentInteractionListener {
    void onListFragmentInteraction(long diaryID);
  }
}
