package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.Presenter.EvtFreqWordsPresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.WordRes;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordHandleTask extends ListActivity {


    @BindView(R.id.tn_add_word)
    Button tn_add_word;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_word_handle_task);
        ButterKnife.bind(this);
        tn_add_word.setVisibility(View.VISIBLE);
        init();
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);
    }

    private void init() {
        wordAdapter = new WordAdapter(this);
        setAdapter(wordAdapter);

        tn_add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(WordHandleTask.this);
                builder.setTitle("常用语")
                        .setPlaceholder("在此输入新增的常用语")
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {

                                CharSequence text = builder.getEditText().getText();
                                if (text != null && text.length() > 0) {
                                    WordRes wordRes = new WordRes();
                                    wordRes.setContent(text.toString());
                                    PublicSqliteHelper.getInstance(WordHandleTask.this).getWordDao().create(wordRes);
                                    dialog.dismiss();
                                    Toast.makeText(WordHandleTask.this, "增加常用语成功", Toast.LENGTH_SHORT).show();
                                    wordAdapter.add(wordRes);
                                } else {
                                    Toast.makeText(WordHandleTask.this, "请填入要增加的内容", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
        });

        fetchData(true, false);
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        fetchData(true, false);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        fetchData(false, true);
    }

    private void fetchData(boolean refresh, boolean loadMore) {

        if (refresh) {
            wordAdapter.clear();
            EvtFreqWordsPresenter presenter = new EvtFreqWordsPresenter();
            presenter.getWords(WordHandleTask.this, new EvtFreqWordsPresenter.EvtFreqWordsCallBack() {
                @Override
                public void onSuccess(List<WordRes> data) {
                    mRefreshLayout.finishRefresh();
                    if (data == null) {
                        return;
                    }
                    wordAdapter.addAll(data);
                }

                @Override
                public void onError() {
                    mRefreshLayout.finishRefresh();
                }
            });
        }

    }

    WordAdapter wordAdapter;

    class WordAdapter extends ObjectAdapter<WordRes> {

        private Context context;
        private String searchKey;

        public WordAdapter(Context context) {
            super(R.layout.word_list);
            this.context = context;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, WordRes object, int position) {
            Log.d("position", position + "");

            TextView tvIndex = holder.getView(R.id.tvIndex, TextView.class);
            tvIndex.setText(String.valueOf(position + 1));

            TextView tv_change = holder.getView(R.id.tv_change, TextView.class);
            TextView tv_delete = holder.getView(R.id.tv_delete, TextView.class);

            TextView tv_word = holder.getView(R.id.tv_word, TextView.class);
            tv_word.setText(object.getContent());


            AppCompatCheckBox ck_seletor = holder.getView(R.id.ck_seletor, AppCompatCheckBox.class);

            tv_change.setTag(object);
            tv_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WordRes wordRes = (WordRes) view.getTag();
                    final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(WordHandleTask.this);
                    builder.setTitle("常用语编辑")
                            .setDefaultText(wordRes.getContent())
                            .setInputType(InputType.TYPE_CLASS_TEXT)
                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    CharSequence text = builder.getEditText().getText();
                                    if (text != null && text.length() > 0) {
                                        wordRes.setContent(text.toString());
                                        PublicSqliteHelper.getInstance(WordHandleTask.this).getWordDao().update(wordRes);
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                        ToastUtils.show(WordHandleTask.this, "修改常用语成功");
                                    } else {
                                        ToastUtils.show(WordHandleTask.this, "请填入要修改的内容");
                                    }
                                }
                            })
                            .show();

                }
            });

            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PublicSqliteHelper.getInstance(WordHandleTask.this).getWordDao().delete(object);
                    wordAdapter.remove(object);
                }
            });

            ck_seletor.setChecked(object.isCheck());
            ck_seletor.setTag(object);
            ck_seletor.setTextColor(object.isCheck() ? getResources().getColor(R.color.umeng_blue) : getResources().getColor(R.color.umeng_black));

            ck_seletor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < wordAdapter.size(); i++) {
                        WordRes wordRes = wordAdapter.get(i);
                        if (wordRes.isCheck()) {
                            wordRes.setCheck(false);
                            PublicSqliteHelper.getInstance(WordHandleTask.this).getWordDao().update(wordRes);
                        }
                    }

//                     Log.d("position",position+"notifyDataSetChanged完成");
                    WordRes wordRes = (WordRes) view.getTag();
                    wordRes.setCheck(!wordRes.isCheck());
                    PublicSqliteHelper.getInstance(WordHandleTask.this).getWordDao().update(wordRes);
                    notifyDataSetChanged();
                }
            });
//            ck_seletor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                 @Override
//                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                     WordRes wordRes = (WordRes) compoundButton.getTag();
//                     Log.d("position",position+wordRes.getContent()+"notifyDataSetChanged完成");
//                     wordRes.setCheck(b);
//                 }
//             });
        }
    }


    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {

//        WordRes res = wordAdapter.get(position);
//        Intent intent = new Intent();
//        intent.putExtra("WordRes", res);
//        setResult(Activity.RESULT_OK, intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        finish();
    }


    @Override
    public void finish() {

        for (int i = 0; i < wordAdapter.size(); i++) {
            WordRes wordRes = wordAdapter.get(i);
            if (wordRes.isCheck()) {
//                PublicSqliteHelper.getInstance(WordHandleTask.this).getWordDao().update(wordRes);
                Intent intent = new Intent();
                intent.putExtra("WordRes", wordRes);
                setResult(Activity.RESULT_OK, intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }
        }
        super.finish();
    }
}
