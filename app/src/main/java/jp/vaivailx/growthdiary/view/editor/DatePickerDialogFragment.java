package jp.vaivailx.growthdiary.view.editor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by vaivailx on 2016/07/13.
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

//  static DatePickerDialogFragment newInstance() {
//
//    return new DatePickerDialogFragment();
//  }
  static DatePickerDialogFragment newInstance(Fragment target) {
    DatePickerDialogFragment datePickerDialogFragment =  new DatePickerDialogFragment();
    datePickerDialogFragment.setTargetFragment(target, 100);
    return datePickerDialogFragment;
  }

  public DatePickerDialogFragment() {
    // must be empty
  }

  @Override
  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    Fragment targetFragment = getTargetFragment();
    if (targetFragment != null && targetFragment instanceof OnDateDataSetListener) {
      ((OnDateDataSetListener) getTargetFragment()).onDataSet(year, monthOfYear + 1, dayOfMonth);
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    //getFragmentManager().putFragment(new Bundle(), "DPDF", this);

    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  interface OnDateDataSetListener {
    void onDataSet(int year, int monthOfYear, int dayOfMonth);
  }
}
