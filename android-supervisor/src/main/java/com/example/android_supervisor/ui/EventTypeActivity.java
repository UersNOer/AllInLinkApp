package com.example.android_supervisor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.EventType;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.adapter.PrimaryTypeAdapter;
import com.example.android_supervisor.ui.adapter.SecondaryTypeAdapter;
import com.example.android_supervisor.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wujie
 */
public class EventTypeActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, AdapterView.OnItemClickListener,
        ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnChildClickListener {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.list_left)
    ListView mListLeft;

    @BindView(R.id.list_right)
    ExpandableListView mListRight;

    private PrimaryTypeAdapter mPrimaryAdapter;
    private SecondaryTypeAdapter mSecondaryAdapter;

    private String intentTypeId;
    private String intentPrimaryTypeId;
    private String intentSecondaryTypeId;
    private String intentStandardId;

    private EventType mEvtType;
    private EventType mPrimaryEvtType;
    private EventType mSecondaryEvtType;
    private EventType mEvtStandard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMenu("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onComplete();
            }
        });

        setContentView(R.layout.activity_event_type);
        ButterKnife.bind(this);

        initIntentData();
        initListView();
        initTabLayout();
    }

    private void initIntentData() {
        String typeId = getIntent().getStringExtra("typeId");
        if (typeId != null) {
            PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
            EventType secondaryEvtType = sqliteHelper.getEventTypeDao().queryForId(typeId);
            if (secondaryEvtType != null) {
                intentSecondaryTypeId = secondaryEvtType.getId();
                EventType primaryEvtType = sqliteHelper.getEventTypeDao().queryForId(secondaryEvtType.getPid());
                if (primaryEvtType != null) {
                    intentPrimaryTypeId = primaryEvtType.getId();
                    EventType eventType = sqliteHelper.getEventTypeDao().queryForId(primaryEvtType.getPid());
                    if (eventType != null) {
                        intentTypeId = eventType.getId();
                    }
                }
            }
        }
        intentStandardId = getIntent().getStringExtra("standardId");
    }

    private void initListView() {
        mPrimaryAdapter = new PrimaryTypeAdapter(this);
        mListLeft.setAdapter(mPrimaryAdapter);
        mListLeft.setOnItemClickListener(this);

        mSecondaryAdapter = new SecondaryTypeAdapter(this);
        mListRight.setAdapter(mSecondaryAdapter);
        mListRight.setOnGroupCollapseListener(this);
        mListRight.setOnGroupExpandListener(this);
        mListRight.setOnChildClickListener(this);
    }

    private void initTabLayout() {
        mTabLayout.addOnTabSelectedListener(this);

        int originTabIndex = 0;
        boolean isCheckEvent = true;//标识默认选中事件

        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
        List<EventType> rEvtTypes = sqliteHelper.getEventTypeDao()
                .queryForEq("pid", "0");
        if (rEvtTypes != null && rEvtTypes.size() > 0) {
            List<EventType> pEvtTypes = sqliteHelper.getEventTypeDao()
                    .queryForEq("pid", rEvtTypes.get(0).getId());
            for (int index = 0; index < pEvtTypes.size(); index++) {
                EventType evtType = pEvtTypes.get(index);
                if (evtType.getId() != null && evtType.getId().equals(intentTypeId)) {
                    originTabIndex = index;
                    isCheckEvent  = false;
                    break;
                }
            }
            intentTypeId = null;

            for (int index = 0; index < pEvtTypes.size(); index++) {
                EventType evtType = pEvtTypes.get(index);
                TabLayout.Tab tab = mTabLayout.newTab().setText(evtType.getName()).setTag(evtType);
                if (originTabIndex == index) {
                    mTabLayout.addTab(tab, true);
                } else {
                    mTabLayout.addTab(tab, false);
                }
            }

            if (!isCheckEvent)return;
            for (int i= 0;i<mTabLayout.getTabCount();i++){
                EventType evtType = (EventType) mTabLayout.getTabAt(i).getTag();
                if (evtType.getName().equals("事件")){
                    TabLayout.Tab tab =mTabLayout.getTabAt(i);
                    tab.select();
                    break;
                }
            }

        }
    }

    private void onComplete() {
        if (mEvtType == null) {
            ToastUtils.show(this, "请选择类型");
            return;
        }
        if (mPrimaryEvtType == null) {
            ToastUtils.show(this, "请选择\"" + mEvtType.getName() + "\"大类");
            return;
        }
        if (mSecondaryEvtType == null) {
            ToastUtils.show(this, "请选择\"" + mPrimaryEvtType.getName() + "\"小类");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("typeId", mEvtType.getId());
        intent.putExtra("typeName", mEvtType.getName());

        if (mPrimaryEvtType!=null){
            intent.putExtra("bigId", mPrimaryEvtType.getId());
            intent.putExtra("bigName", mPrimaryEvtType.getName());
        }

        if (mSecondaryEvtType!=null){
            intent.putExtra("smallId", mSecondaryEvtType.getId());
            intent.putExtra("smallName", mSecondaryEvtType.getName());
        }

        if (mEvtStandard != null) {
            intent.putExtra("standardId", mEvtStandard.getId());
            intent.putExtra("standardName", mEvtStandard.getName());
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mEvtType = (EventType) tab.getTag();
        mPrimaryEvtType = null;
        mSecondaryEvtType = null;
        mEvtStandard = null;

        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
        List<EventType> evtTypes = sqliteHelper.getEventTypeDao()
                .queryForEq("pid", mEvtType.getId());

        mPrimaryAdapter.clearData();
        mPrimaryAdapter.setData(evtTypes);
        mSecondaryAdapter.clearData();
        mListLeft.clearChoices();

        if (intentPrimaryTypeId != null) {
            for (int i = 0; i < evtTypes.size(); i++) {
                String dataId = evtTypes.get(i).getId();
                if (dataId != null && dataId.equals(intentPrimaryTypeId)) {
                    View v = mPrimaryAdapter.getView(i, null, null);
                    mListLeft.performItemClick(v, i, i);
                    break;
                }
            }
            intentPrimaryTypeId = null;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPrimaryEvtType = mPrimaryAdapter.getItem(position);
        mSecondaryEvtType = null;
        mEvtStandard = null;

        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
        List<EventType> evtTypes = sqliteHelper.getEventTypeDao()
                .queryForEq("pid", mPrimaryEvtType.getId());

        mSecondaryAdapter.clearData();
        mSecondaryAdapter.setGroupData(evtTypes);
        mListRight.clearChoices();

        if (intentSecondaryTypeId != null) {
            for (int i = 0; i < evtTypes.size(); i++) {
                String dataId = evtTypes.get(i).getId();
                if (dataId != null && dataId.equals(intentSecondaryTypeId)) {
                    mListRight.expandGroup(i, true);
                    break;
                }
            }
            intentSecondaryTypeId = null;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mEvtStandard = mSecondaryAdapter.getChild(groupPosition, childPosition);
        selectItem(parent, groupPosition, childPosition, true);
        return true;
    }

    private void selectItem(ExpandableListView parent, int groupPosition, int childPosition, boolean selected) {
        long packedPosition = ExpandableListView.getPackedPositionForChild(groupPosition, childPosition);
        int index = parent.getFlatListPosition(packedPosition);
        parent.setItemChecked(index, selected);
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        mSecondaryEvtType = null;
        mEvtStandard = null;
        mListRight.clearChoices();
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        int groupCount = mSecondaryAdapter.getGroupCount();
        for (int position = 0; position < groupCount; position++) {
            if (position != groupPosition && mListRight.isGroupExpanded(position)) {
                mListRight.collapseGroup(position);
            }
        }

        mSecondaryEvtType = mSecondaryAdapter.getGroup(groupPosition);

        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
        List<EventType> evtTypes = sqliteHelper.getEventTypeDao()
                .queryForEq("pid", mSecondaryEvtType.getId());

        mSecondaryAdapter.setChildData(mSecondaryEvtType.getId(), evtTypes);

        int childPosition = 0;
        if (intentStandardId != null) {
            for (int i = 0; i < evtTypes.size(); i++) {
                String dataId = evtTypes.get(i).getId();
                if (dataId != null && dataId.equals(intentStandardId)) {
                    childPosition = i;
                    break;
                }
            }
            intentStandardId = null;
        }
        if (mSecondaryAdapter.getChildrenCount(groupPosition) > childPosition) {
            mEvtStandard = mSecondaryAdapter.getChild(groupPosition, childPosition);
            selectItem(mListRight, groupPosition, childPosition, true);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
