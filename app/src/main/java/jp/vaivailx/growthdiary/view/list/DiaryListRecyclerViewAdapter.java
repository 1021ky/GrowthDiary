package jp.vaivailx.growthdiary.view.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import jp.vaivailx.growthdiary.R;
import jp.vaivailx.growthdiary.controller.DiaryManager;
import jp.vaivailx.growthdiary.view.list.DiaryListContent.DiaryItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DiaryItem} and makes a call to the
 * specified {@link DiaryListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DiaryListRecyclerViewAdapter extends RecyclerView.Adapter<DiaryListRecyclerViewAdapter.ViewHolder> {

  private final List<DiaryItem> mValues;
  private final DiaryListFragment.OnListFragmentInteractionListener mListener;
  private DiaryManager diaryManager;
  private final static int VIEWTYPE_NEEDS_HEADER = 0;

  public DiaryListRecyclerViewAdapter(List<DiaryItem> items, DiaryListFragment.OnListFragmentInteractionListener listener) {
    mValues = items;
    mListener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // TODO ヘッダを入れるのはここ？
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fragment_diary_line, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    if (mValues.size() == 0) {
      return;
    }
    holder.mItem = mValues.get(position);
    holder.mDateView.setText(mValues.get(position).titleDate);
    holder.mRateView.setText(mValues.get(position).rate);
    holder.mContentView.setText(mValues.get(position).content);

    holder.mView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mListener != null) {
          // Notify the active callbacks interface (the activity, if the
          // fragment is attached to one) that an item has been selected.
          mListener.onListFragmentInteraction(holder.mItem.id);
        }
      }
    });

  }



  @Override
  public int getItemCount() {
    return mValues.size();
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return VIEWTYPE_NEEDS_HEADER;
    }
    return super.getItemViewType(position);
  }

  /**
   * view holder。日記リストを保持するのはこれだけであるためインナークラスで定義。
   */
  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mDateView;
    public final TextView mRateView;
    public final TextView mContentView;
    public DiaryItem mItem;

    public ViewHolder(View view) {
      super(view);
      mView = view;
      mDateView = (TextView) view.findViewById(R.id.title);
      mRateView = (TextView) view.findViewById(R.id.rate);
      mContentView = (TextView) view.findViewById(R.id.content);
    }

    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }
}
