package jp.vaivailx.growthdiary.view.graph;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.vaivailx.growthdiary.R;
import jp.vaivailx.growthdiary.controller.DiaryManager;
import jp.vaivailx.growthdiary.view.DiaryDTO;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnGraphFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GrowthGraphChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GrowthGraphChartFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnGraphFragmentInteractionListener mListener;

  @Bind(R.id.chart1)
  public LineChart mChart;

  private DiaryManager diaryManager;

  private AdView  mAdView;
  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment GrowthGraphChartFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static GrowthGraphChartFragment newInstance(String param1, String param2) {
    GrowthGraphChartFragment fragment = new GrowthGraphChartFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }
  public GrowthGraphChartFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
    super.onCreate(savedInstanceState);
    diaryManager = new DiaryManager();
    diaryManager.onCreate();
  }

  private void setData(int count, float range) {

    List<DiaryDTO> diaryDataList = diaryManager.getLaterDiaryList(count);
    ArrayList<Entry> values = new ArrayList<Entry>();

    for (int i = 0; i < diaryDataList.size(); i++ ) {
      values.add(new Entry(i+1, diaryDataList.get(diaryDataList.size() - 1 - i).evaluation));
    }

    LineDataSet set1;

    // create a dataset and give it a type
    set1 = new LineDataSet(values, getResources().getString(R.string.GRAPH_TITLE));

    // set the line to be drawn like this "- - - - - -"
    set1.enableDashedLine(10f, 5f, 0f);
    set1.enableDashedHighlightLine(10f, 5f, 0f);
    set1.setColor(Color.BLACK);
    set1.setCircleColor(Color.BLACK);
    set1.setLineWidth(2f);
    set1.setCircleRadius(3f);
    set1.setDrawCircleHole(false);
    set1.setValueTextSize(9f);
    set1.setDrawFilled(true);
    set1.setFormLineWidth(1f);
    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
    set1.setFormSize(15.f);

    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    dataSets.add(set1); // add the datasets

    // create a data object with the datasets
    LineData data = new LineData(dataSets);

    // set data
    mChart.setData(data);
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_growth_graph_chart, container, false);
    ButterKnife.bind(this, view);

    mAdView = (AdView) view.findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    // no description text
    mChart.getDescription().setEnabled(false);

    // enable touch gestures
    mChart.setTouchEnabled(true);

    // enable scaling and dragging
    mChart.setDragEnabled(true);
    mChart.setScaleEnabled(true);

    XAxis xAxis = mChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.enableGridDashedLine(10f, 10f, 0f);

    YAxis leftAxis = mChart.getAxisLeft();
    leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

    leftAxis.setAxisMaximum(6f);
    leftAxis.setAxisMinimum(0f);

    leftAxis.setDrawZeroLine(false);

    // limit lines are drawn behind data (and not on top)
    leftAxis.setDrawLimitLinesBehindData(true);

    mChart.getAxisRight().setEnabled(false);

    // add data
    setData(30, 5);

    mChart.animateX(2500);

    // get the legend (only possible after setting data)
    Legend l = mChart.getLegend();

    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnGraphFragmentInteractionListener) {
      mListener = (OnGraphFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
              + " must implement OnGraphFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  public void update() {
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
  public interface OnGraphFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
