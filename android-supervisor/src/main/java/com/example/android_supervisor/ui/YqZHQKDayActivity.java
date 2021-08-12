package com.example.android_supervisor.ui;


import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.Presenter.YqTypePresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.ui.model.CaseClassTreeVO;
import com.example.android_supervisor.ui.model.YqcsDayPara;
import com.example.android_supervisor.ui.model.YqcsDayRes;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class YqZHQKDayActivity extends ListActivity {

    View btnSave;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnableRefresh(false);
        setEnableLoadMore(false);
//
//        btnSave = addMenu("上报", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.show(YqZHQKDayActivity.this,"dddddd");
//                Intent intent =  new Intent(YqZHQKDayActivity.this,YqZHQKDayReportActivity.class);
//                startActivity(intent);
//            }
//        });
        itemAdapter  = new ItemAdapter();
        setAdapter(itemAdapter);
        getDayJk();
    }


    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {

        CaseClassTreeVO object = itemAdapter.get(position);

        LinearLayout linearLayout = view.findViewById(R.id.ll_child_item);

        if (object.isExpandable()){
            linearLayout.removeAllViews();
            linearLayout.setVisibility(View.GONE);
            object.setExpandable(false);
            return;
        }
        YqTypePresenter presenter = new YqTypePresenter();
        YqcsDayPara para = new YqcsDayPara();
        List<String>  list = new ArrayList<>();
        list.add( object.getWhType());
        para.setType(list);

        YqcsDayPara.PageBean page = new YqcsDayPara.PageBean();
        page.setPage(false);
        para.setPage(page);

        presenter.getCsDayJk(this,para ,new YqTypePresenter.CsBack() {
            @Override
            public void onSuccess(YqcsDayRes types) {
                ArrayList<CaseClassTreeVO>  temp = new ArrayList<>();
                if (types!=null && types.getWhDetailsList()!=null){
                    object.setExpandable(true);
                    linearLayout.setVisibility(View.VISIBLE);
                    for (YqcsDayRes.WhDetailsListBean whDetailsListBean:types.getWhDetailsList()){
                        View childView   = LayoutInflater.from(YqZHQKDayActivity.this).inflate(R.layout.item_cs_list_c,linearLayout,false);
                        TextView tv_css_type = childView.findViewById(R.id.tv_css_type);
                        tv_css_type.setText(whDetailsListBean.getObjname());
                        linearLayout.addView(childView);
                        childView.setTag(whDetailsListBean);
                        childView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ToastUtils.show(YqZHQKDayActivity.this,((YqcsDayRes.WhDetailsListBean)view.getTag()).getObjname());
                                Intent intent =  new Intent(YqZHQKDayActivity.this,YqZHQKDayDetailsActivity.class);
                                intent.putExtra("item",whDetailsListBean.getId());
                                intent.putExtra("areaCode",whDetailsListBean.getAreaCode());

                                //增加类型 和名称
                                intent.putExtra("csId",object.getId());

                                startActivity(intent);
                            }
                        });
                    }
                }

            }
        });
    }

    private void getDayJk() {
        YqTypePresenter presenter = new YqTypePresenter();
        presenter.getType(this, new YqTypePresenter.CallBack() {
            @Override
            public void onSuccess(List<CaseClassTreeVO> types) {
                ArrayList<CaseClassTreeVO>  temp = new ArrayList<>();
                for (CaseClassTreeVO caseClassTreeVO:types){
                    if (caseClassTreeVO.getQuestionClassId().equals("0101")){
                        temp.add(caseClassTreeVO);
                    }
                }
                itemAdapter.addAll(temp);
            }
        });

    }



    public class ItemAdapter extends ObjectAdapter<CaseClassTreeVO> {


        public ItemAdapter() {
            super(R.layout.item_cs_list_p);
        }


        @Override
        public void onBindViewHolder(BaseViewHolder holder, CaseClassTreeVO object, int position) {
            TextView tv_cs_type = holder.getView(R.id.tv_cs_type, TextView.class);
            tv_cs_type.setText(object.getCountWhSys() == 0? String.format("%s ",object.getNodeName()):
                    String.format("%s ( %d )",object.getNodeName(),object.getCountWhSys()));

            LinearLayout ll_child_item = holder.getView(R.id.ll_child_item, LinearLayout.class);
            ll_child_item.setVisibility(View.GONE);
        }

    }

}
