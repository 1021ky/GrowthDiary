package jp.vaivailx.growthdiary.view.editor;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.vaivailx.growthdiary.GrowthDiaryLogUtil;
import jp.vaivailx.growthdiary.R;
import jp.vaivailx.growthdiary.model.DiaryData;
import jp.vaivailx.growthdiary.controller.DiaryManager;
import jp.vaivailx.growthdiary.view.DiaryDTO;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditorFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorFragment extends Fragment implements DatePickerDialogFragment.OnDateDataSetListener {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnEditorFragmentInteractionListener mListener;

  private static final String DATE_PATTERN = "yyyy/MM/dd";

  @Bind(R.id.title_edit_box)
  EditText titleEditText;

  @Bind(R.id.fact_body)
  EditText factEditText;

  @Bind(R.id.realization_body)
  EditText realizationEditText;

  @Bind(R.id.knowledge_body)
  EditText knowledgeEditText;

  @Bind(R.id.theme_body)
  EditText themeEditText;

  @Bind(R.id.rate_bar)
  RatingBar ratingBar;

  private DiaryDTO editorDiaryDTO;

  private DiaryManager diaryManager;

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment EditorFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static EditorFragment newInstance(String param1, String param2) {
    EditorFragment fragment = new EditorFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  public EditorFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
    diaryManager = new DiaryManager();
    editorDiaryDTO = new DiaryDTO();
    diaryManager.onCreate();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_editor, container, false);
    ButterKnife.bind(this, view);

    // デフォルトはアプリ実行日
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH) + 1;
    int day = c.get(Calendar.DAY_OF_MONTH);
    StringBuilder sb = new StringBuilder();
    try {
      Date titleDate = new SimpleDateFormat(DATE_PATTERN, Locale.JAPAN).parse(sb.append(year).append("/").append(month).append("/").append(day).toString());
      loadPastDiary(titleDate);
    } catch (ParseException e) {
      ;
    }
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnEditorFragmentInteractionListener) {
      mListener = (OnEditorFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
              + " must implement OnEditorFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onDataSet(int year, int monthOfYear, int dayOfMonth) {
    StringBuilder sb = new StringBuilder();
    titleEditText.setText(sb.append(year).append("/").append(monthOfYear).append("/").append(dayOfMonth), TextView.BufferType.NORMAL);
    // 過去に書いた日記の場合は読み込む
    try {
      Date titleDate = new SimpleDateFormat(DATE_PATTERN, Locale.JAPAN).parse(titleEditText.getText().toString());
      loadPastDiary(titleDate);
    } catch (ParseException e) {
      // 再入力してもらうために削除
      titleEditText.setText("");
    }
  }

  @OnClick(R.id.diary_clear_button)
  protected void onClickDiaryClearButton(View v){

    new AlertDialog.Builder(getActivity())
            .setTitle(getString(R.string.EDITOR_CLEAR_CONFIRM_TITLE))
            .setMessage(getString(R.string.EDITOR_CLEAR_CONFIRM_TEXT))
            .setPositiveButton(getString(R.string.OK_BUTTON_TEXT), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                clearDiary();
              }
            })
            .setNegativeButton(getString(R.string.CANCEL_BUTTON_TEXT), null)
            .show();
  }

  private void clearDiary() {
    factEditText.setText("");
    realizationEditText.setText("");
    knowledgeEditText.setText("");
    themeEditText.setText("");
    ratingBar.setNumStars(0);
  }


  private void loadPastDiary(Date titleDate) {
    DiaryDTO dto = diaryManager.getDiary(titleDate);
    if (dto != null) {
      editorDiaryDTO = dto;
    } else {
      editorDiaryDTO.titleDate = titleDate;
    }
    convertDTOToView();
  }

  @Override
  public void onStart() {
    super.onStart();
    setEventListener();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    diaryManager.onDestroy();
  }

  @Override
  public void onPause() {
    super.onPause();
    saveDiary();
    mListener.onEditorFragmentInteraction(null);
  }

  private void setEventListener() {
    setTitleEditTextEventListener();
    setFactEditTextEventListener();
    setKnowledgeEditTextEventListener();
    setThemeEditTextEventListener();
    setRealizationEditTextEventListener();
    setRatingBarEventLister();
  }

  private void setTitleEditTextEventListener() {
    final DatePickerDialogFragment dataDatePickerDialogFragment = DatePickerDialogFragment.newInstance(this);
    this.titleEditText.setOnClickListener (new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        dataDatePickerDialogFragment.show(getFragmentManager(), "datePicker");
      }
    });
    this.titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          saveDiary();
          mListener.onEditorFragmentInteraction(null);
        }
      }
    });
  }

  private void setFactEditTextEventListener() {
    this.factEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        saveDiary();
        mListener.onEditorFragmentInteraction(null);
      }
    });
  }

  private void setRealizationEditTextEventListener() {
    this.realizationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        saveDiary();
        mListener.onEditorFragmentInteraction(null);
      }
    });
  }

  private void setKnowledgeEditTextEventListener() {
    this.knowledgeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        saveDiary();
        mListener.onEditorFragmentInteraction(null);
      }
    });
  }

  private void setThemeEditTextEventListener() {
    this.themeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        saveDiary();
        mListener.onEditorFragmentInteraction(null);
      }
    });
  }

  private void setRatingBarEventLister() {
    this.ratingBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        saveDiary();
        mListener.onEditorFragmentInteraction(null);
      }
    });
  }

  /**
   * 日記データを保存する
   */
  protected void saveDiary() {
    try {
      String titleDateString = EditorFragment.this.titleEditText.getText().toString();
      if (!titleDateString.equals("")) {
        editorDiaryDTO.titleDate = new SimpleDateFormat(DATE_PATTERN, Locale.JAPAN).parse(titleDateString);
        editorDiaryDTO.fact = factEditText.getText().toString();
        editorDiaryDTO.knowledge = knowledgeEditText.getText().toString();
        editorDiaryDTO.realization = realizationEditText.getText().toString();
        editorDiaryDTO.theme = themeEditText.getText().toString();
        editorDiaryDTO.evaluation = Math.round(ratingBar.getRating());

        editorDiaryDTO = diaryManager.updateDiary(editorDiaryDTO);
      }
    } catch (ParseException e) {
      Log.w(GrowthDiaryLogUtil.ERROR_TAG, "onClick: ", e);
      e.printStackTrace();
    }

  }

  public void update(long itemID) {
    if (itemID != 0) {
      editorDiaryDTO = this.diaryManager.getDiary(itemID);
      convertDTOToView();
    }
  }

  private void convertDTOToView() {
    titleEditText.setText(new SimpleDateFormat(DATE_PATTERN).format(editorDiaryDTO.titleDate));
    factEditText.setText(editorDiaryDTO.fact);
    realizationEditText.setText(editorDiaryDTO.realization);
    knowledgeEditText.setText(editorDiaryDTO.knowledge);
    themeEditText.setText(editorDiaryDTO.theme);
    ratingBar.setRating(editorDiaryDTO.evaluation);
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnEditorFragmentInteractionListener {
    void onEditorFragmentInteraction(Uri uri);
  }
}
