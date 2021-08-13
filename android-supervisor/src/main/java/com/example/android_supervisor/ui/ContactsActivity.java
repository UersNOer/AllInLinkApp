package com.example.android_supervisor.ui;

import android.accounts.NetworkErrorException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.Contact;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.ui.adapter.ContactAdapter;
import com.example.android_supervisor.ui.view.LetterSideBar;
import com.example.android_supervisor.ui.view.ProgressDialog;
import com.example.android_supervisor.ui.view.SideBar;
import com.example.android_supervisor.utils.PinYinUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class ContactsActivity extends ListActivity implements LetterSideBar.OnSideBarChangedListener, SideBar.OnTouchingLetterChangedListener {
    private ContactAdapter adapter;

    @BindView(R.id.letter_side_bar)
    SideBar letterSideBar;


    @BindView(R.id.filter_edit)
    EditText mClearEditText;

    @BindView(R.id.dialog)
    TextView dialog;


    private HashMap<String, Integer> letterIndexs;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        letterSideBar.setOnTouchingLetterChangedListener(this);
        letterSideBar.setTextView(dialog);
        setEnableRefresh(false);
        setEnableLoadMore(false);
        adapter = new ContactAdapter();
        setAdapter(adapter);
        fetchData();

        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())){
                    return;
                }
                String pinyinInitial = PinYinUtils.getPinyinInitial(s.toString());
                Integer index = letterIndexs.get(pinyinInitial);
                if (index != null) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        layoutManager.scrollToPositionWithOffset(index, 0);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 设置默认键盘不弹出


    }

    private void fetchData() {
        BasicService basicService = ServiceGenerator.create(BasicService.class);
        basicService.getContactList()
                .compose(this.<Response<List<Contact>>>bindToLifecycle())
                .map(new Function<Response<List<Contact>>, Response<List<Contact>>>() {
                    @Override
                    public Response<List<Contact>> apply(Response<List<Contact>> response) throws Exception {
                        if (!response.isSuccess()) {
                            throw new NetworkErrorException();
                        }
                        List<Contact> data = response.getData();
                        if(data!=null){
                            for (Contact res : data) {
                                if (!TextUtils.isEmpty(res.getNickName())) {
                                    String pinyinInitial = PinYinUtils.getPinyinInitial(res.getNickName());
                                    res.setInitialName(pinyinInitial);
                                }
                            }
                            Collections.sort(data, new Comparator<Contact>() {
                                @Override
                                public int compare(Contact res1, Contact res2) {
                                    String letter1 = res1.getInitialName();
                                    String letter2 = res2.getInitialName();
                                    if (letter1.equals("#")) {
                                        return 1;
                                    } else if (letter2.equals("#")) {
                                        return -1;
                                    } else {
                                        return letter1.compareTo(letter2);
                                    }
                                }
                            });
                            letterIndexs = new HashMap<>();
                            for (int i = 0, size = data.size(); i < size; i++) {
                                String initialName = data.get(i).getInitialName();
                                if (!letterIndexs.containsKey(initialName)) {
                                    letterIndexs.put(initialName, i);
                                }
                            }
                        }
                        return response;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        pDialog = ProgressDialog.show(ContactsActivity.this, "正在加载...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (pDialog != null) {
                            pDialog.dismiss();
                            pDialog = null;
                        }
                        setNoData(adapter.size() == 0);
                    }
                })
                .subscribe(new Consumer<Response<List<Contact>>>() {
                    @Override
                    public void accept(Response<List<Contact>> response) throws Exception {
                        if (response.isSuccess()) {
                            List<Contact> data = response.getData();
                            if (data!=null){
                                adapter.clear();
                                adapter.addAll(data);
                            }

                        } else {
                            ToastUtils.show(ContactsActivity.this, "未获取联系人、请检查是否授权通讯组");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.show(ContactsActivity.this, "获取联系人失败");
                    }
                });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Contact contact = adapter.get(position);
        final String mobile = contact.getMobile();
        if (TextUtils.isEmpty(mobile) || !mobile.matches(
                "^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            ToastUtils.show(this, "空的手机号码");
            return;
        }
        if (TextUtils.isEmpty(mobile) || !mobile.matches(
                "^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$")) {
            ToastUtils.show(this, "无效的手机号码");
            return;
        }
        new AlertDialog.Builder(this)
                .setItems(new String[]{"拨打电话", "发送短信"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri;
                        switch (which) {
                            case 0: {
                                uri = Uri.parse("tel:" + mobile);
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(uri);
                                startActivity(intent);
                                break;
                            }
                            case 1: {
                                uri = Uri.parse("smsto:" + mobile);
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(uri);
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }).show();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    public void onChanged(int position, String letter) {
        if (letterIndexs == null) {
            return;
        }

        Integer index = letterIndexs.get(letter);
        if (index != null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPositionWithOffset(index, 0);
            }
        }
    }


    @Override
    public void onTouchingLetterChanged(String s) {
           // 该字母首次出现的位置
        if (letterIndexs == null) {
            return;
        }

        Integer index = letterIndexs.get(s);
        if (index != null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPositionWithOffset(index, 0);
            }
        }
    }


}
