package test.hellowdoc.com.hellowdoc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.BaseViewHolder> {

    private List<DataModel> mDataModelList;
    private OnBottomReachedListener onBottomReachedListener;

    public MyAdapter(List<DataModel> dataModelList) {
        mDataModelList = dataModelList;
    }

    public void updateList(@NonNull List<DataModel> dataModelList) {
        final int startPosition = getItemCount();
        final int count = dataModelList != null && !dataModelList.isEmpty() ? dataModelList.size() : 0;
        mDataModelList.addAll(dataModelList);
//        notifyDataSetChanged();
        notifyItemRangeInserted(startPosition, count);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        final DataModel dataModel = getItem(position);
        if (dataModel != null) {
            holder.title.setText(dataModel.getTitle());
            holder.description.setText(dataModel.getAuthor());
            holder.setUrl(dataModel.getUrl());
        }

        if (position == getItemCount() - 1 && onBottomReachedListener != null) {
            onBottomReachedListener.onBottomReached(position);
        }
    }

    public DataModel getItem(int position) {
        return mDataModelList.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataModelList != null && !mDataModelList.isEmpty() ? mDataModelList.size() : 0;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    protected class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final String TAG = BaseViewHolder.class.getSimpleName();

        protected TextView title;
        protected TextView description;

        private String url;

        public BaseViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(this);
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, url);
            final Context context = v.getContext();
            context.startActivity(WebViewActivity.getInstance(context, url));
        }
    }
}
