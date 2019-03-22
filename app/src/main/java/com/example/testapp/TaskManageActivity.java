package com.example.testapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hehedownload.OkDown;
import com.example.hehedownload.callback.DownloadCallback;
import com.example.hehedownload.data.DownloadData;
import com.example.hehedownload.db.Db;
import com.example.hehedownload.download.DownloadManager;
import com.example.hehedownload.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benboerboluo on 2019/3/22.
 */
public class TaskManageActivity extends AppCompatActivity {

    private String url1 = "http://orp6z38cm.bkt.clouddn.com/107117018";//你还要我怎样
    private String url2 = "http://orp6z38cm.bkt.clouddn.com/112633517";//安和桥
    private String url3 = "http://orp6z38cm.bkt.clouddn.com/116279020";//真的爱你
    private String url4 = "http://orp6z38cm.bkt.clouddn.com/120828463";//我在红尘中遇见你
    private String url5 = "http://orp6z38cm.bkt.clouddn.com/121523297";//平凡之路

    private String path = Environment.getExternalStorageDirectory() + "/Benboerboluo/";

    private RecyclerView downloadList;
    private ProgressBar progressBar;    private TextView mProgress;
    private Context mContext;

    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manage);

        mContext = this;
//        OkDown.newInstance(this);
//        DownloadManger.getInstance(mContext).setTaskPoolSize(2, 10);
        downloadManager = OkDown.newInstance(this)
                .path(path)
                .url(url2)
                .build()
                .start(new DownloadCallback() {
                    @Override
                    public void onStart(long currentSize, long totalSize, float progress) {

                    }

                    @Override
                    public void onProgress(long currentSize, long totalSize, float progress) {
                        progressBar.setProgress((int) progress);
                        mProgress.setText(Utils.formatSize(currentSize) + " / " + Utils.formatSize(totalSize) + "--------" + progress + "%");
                    }

                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFinish(File file) {

                    }

                    @Override
                    public void onWait() {

                    }

                    @Override
                    public void onError(String error) {

                    }
                });

        downloadList = (RecyclerView) findViewById(R.id.download_list);
        final List<DownloadData> datas = new ArrayList<>();
        if (Db.getInstance(mContext).getData(url1) != null) {
            datas.add(Db.getInstance(mContext).getData(url1));
        } else {
            datas.add(new DownloadData(url1, path, "你还要我怎样.mp4"));
        }

        if (Db.getInstance(mContext).getData(url2) != null) {
            datas.add(Db.getInstance(mContext).getData(url2));
        } else {
            datas.add(new DownloadData(url2, path, "安和桥.mp4"));
        }

        if (Db.getInstance(mContext).getData(url3) != null) {
            datas.add(Db.getInstance(mContext).getData(url3));
        } else {
            datas.add(new DownloadData(url3, path, "真的爱你.mp4"));
        }

        if (Db.getInstance(mContext).getData(url4) != null) {
            datas.add(Db.getInstance(mContext).getData(url4));
        } else {
            datas.add(new DownloadData(url4, path, "我在红尘中遇见你.mp4"));
        }

        if (Db.getInstance(mContext).getData(url5) != null) {
            datas.add(Db.getInstance(mContext).getData(url5));
        } else {
            datas.add(new DownloadData(url5, path, "平凡之路.mp4"));
        }

//        downloadListAdapter = new DownloadListAdapter(this, datas, false);
////
////        //开始
////        downloadListAdapter.setOnItemChildClickListener(R.id.start, new OnItemChildClickListener<DownloadData>() {
////            @Override
////            public void onItemChildClick(final ViewHolder viewHolder, final DownloadData data, int position) {
////                DownloadManager.getInstance(mContext).start(data.getUrl());
////            }
////        });
////
////        //暂停
////        downloadListAdapter.setOnItemChildClickListener(R.id.pause, new OnItemChildClickListener<DownloadData>() {
////            @Override
////            public void onItemChildClick(ViewHolder viewHolder, DownloadData data, int position) {
////                DownloadManager.getInstance(mContext).pause(data.getUrl());
////            }
////        });
////
////        //继续
////        downloadListAdapter.setOnItemChildClickListener(R.id.resume, new OnItemChildClickListener<DownloadData>() {
////            @Override
////            public void onItemChildClick(ViewHolder viewHolder, DownloadData data, int position) {
////                DownloadManager.getInstance(mContext).resume(data.getUrl());
////            }
////        });
////
////        //取消
////        downloadListAdapter.setOnItemChildClickListener(R.id.cancel, new OnItemChildClickListener<DownloadData>() {
////            @Override
////            public void onItemChildClick(ViewHolder viewHolder, DownloadData data, int position) {
////                DownloadManager.getInstance(mContext).cancel(data.getUrl());
////            }
////        });
////
////        //重新开始
////        downloadListAdapter.setOnItemChildClickListener(R.id.restart, new OnItemChildClickListener<DownloadData>() {
////            @Override
////            public void onItemChildClick(ViewHolder viewHolder, DownloadData data, int position) {
////                DownloadManager.getInstance(mContext).restart(data.getUrl());
////            }
////        });
////
////        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
////        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
////        downloadList.setLayoutManager(layoutManager);
////        downloadList.setAdapter(downloadListAdapter);
    }

    @Override
    protected void onDestroy() {
        DownloadManager.getInstance(mContext).destroy(url1, url2, url3, url4, url5);
        super.onDestroy();
    }
}
