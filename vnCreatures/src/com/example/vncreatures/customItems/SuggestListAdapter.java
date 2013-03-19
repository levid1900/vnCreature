package com.example.vncreatures.customItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.vncreatures.R;
import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.model.NewsItem;
import com.example.vncreatures.model.NewsModel;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.facebook.widget.ProfilePictureView;

public class SuggestListAdapter extends BaseAdapter {
    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private ThreadModel mThreadModel;
    private Callback mCallback = null;
    private Thread mThread;

    public interface Callback {
        public void onClick(Thread thread);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public SuggestListAdapter(Context context, ThreadModel model) {
        super();
        this.mContext = context;
        this.mThreadModel = model;
    }

    @Override
    public int getCount() {
        return mThreadModel.count();
    }

    @Override
    public Object getItem(int position) {
        return mThreadModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public ThreadModel getModel() {
        return mThreadModel;
    }

    public void setModel(ThreadModel model) {
        this.mThreadModel = model;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.suggest_item, null);
            holder.mThread = (TextView) convertView.findViewById(R.id.thread_name_textView);
            holder.mUsername = (TextView) convertView.findViewById(R.id.userName_textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AQuery aQuery = new AQuery(convertView);
        mThread = mThreadModel.get(position);
        aQuery.id(holder.mThread).text(mThread.getThread_title());
        aQuery.id(holder.mUsername).text(mContext.getString(R.string.by, mThread.getName()));
        
        return convertView;
    }

    static class ViewHolder {
        TextView mThread;
        TextView mUsername;
    }
}