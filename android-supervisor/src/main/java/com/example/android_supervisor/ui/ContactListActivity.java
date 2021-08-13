package com.example.android_supervisor.ui;//package com.example.android_supervisor.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import com.google.android.material.tabs.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//
//import com.hyphenate.EMMessageListener;
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMConversation;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.easeui.EaseConstant;
//import com.hyphenate.easeui.EaseUI;
//import com.hyphenate.easeui.domain.EaseUser;
//import com.hyphenate.easeui.ui.EaseContactListFragment;
//import com.hyphenate.easeui.ui.EaseConversationListFragment;
//import com.example.android_supervisor.R;
//import com.example.android_supervisor.chat.ChatActivity;
//import com.example.android_supervisor.entities.Contact;
//import com.example.android_supervisor.sqlite.PublicSqliteHelper;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * @author wujie
// */
//public class ContactListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
//    @BindView(R.id.bottom_tab_layout)
//    TabLayout mTabLayout;
//
//    private EaseConversationListFragment conversationListFragment;
//    private EaseContactListFragment contactListFragment;
//
//    private List<Fragment> mFragments;
//    private int currentPosition = -1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_list);
//        ButterKnife.bind(this);
//
//        initFragments(savedInstanceState);
//        mTabLayout.addOnTabSelectedListener(this);
//
//        int savedPosition = -1;
//        if (savedInstanceState != null) {
//            savedPosition = savedInstanceState.getInt("position", -1);
//        }
//        if (savedPosition != -1) {
//            mTabLayout.setScrollPosition(savedPosition, 0, true);
//            selectFragment(savedPosition);
//        } else {
//            mTabLayout.setScrollPosition(0, 0, true);
//            selectFragment(0);
//        }
//
//        EaseUI.EaseUserProfileProvider userProfileProvider = EaseUI.getInstance().getUserProfileProvider();
//
//        Map<String, EaseUser> easeUserMap = new HashMap<>();
//        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(this);
//        List<Contact> contacts = sqliteHelper.getContactDao().queryForAll();
//        for (Contact contact : contacts) {
//            String userId = contact.getChatUserId();
//            EaseUser easeUser = userProfileProvider.getUser(userId);
//            if (easeUser != null) {
//                easeUserMap.put(userId, easeUser);
//            }
//        }
//        contactListFragment.setContactsMap(easeUserMap);
//    }
//
//    private void initFragments(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            conversationListFragment = (EaseConversationListFragment) fragmentManager.findFragmentByTag("0");
//            contactListFragment = (EaseContactListFragment) fragmentManager.findFragmentByTag("1");
//        }
//
//        mFragments = new ArrayList<>(3);
//        if (conversationListFragment == null) {
//            conversationListFragment = new EaseConversationListFragment();
//            conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
//                @Override
//                public void onListItemClicked(EMConversation conversation) {
////                    Intent intent = new Intent(ContactListActivity.this, EaseChatActivity.class);
//                    Intent intent = new Intent(ContactListActivity.this, ChatActivity.class);
//                    intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
//                    startActivity(intent);
//                }
//            });
//        }
//        mFragments.add(conversationListFragment);
//        if (contactListFragment == null) {
//            contactListFragment = new EaseContactListFragment();
//            contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
//                @Override
//                public void onListItemClicked(EaseUser user) {
//                    Intent intent = new Intent(ContactListActivity.this, ChatActivity.class);
//                    intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
//                    startActivity(intent);
//                }
//            });
//        }
//        mFragments.add(contactListFragment);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        EMClient.getInstance().chatManager().addMessageListener(messageListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
//    }
//
//    EMMessageListener messageListener = new EMMessageListener() {
//
//        @Override
//        public void onMessageReceived(List<EMMessage> messages) {
//            conversationListFragment.refresh();
//        }
//
//        @Override
//        public void onCmdMessageReceived(List<EMMessage> messages) {
//            conversationListFragment.refresh();
//        }
//
//        @Override
//        public void onMessageRead(List<EMMessage> messages) {
//        }
//
//        @Override
//        public void onMessageDelivered(List<EMMessage> message) {
//        }
//
//        @Override
//        public void onMessageRecalled(List<EMMessage> messages) {
//            conversationListFragment.refresh();
//        }
//
//        @Override
//        public void onMessageChanged(EMMessage message, Object change) {}
//    };
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("position", currentPosition);
//    }
//
//    final int[] iconNormalRes = {R.drawable.ic_community, R.drawable.ic_friend};
//    final int[] iconSelectRes = {R.drawable.ic_community_fill, R.drawable.ic_friend_fill};
//
//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//        int position = tab.getPosition();
//        if (position != -1) {
//            tab.setIcon(iconSelectRes[position]);
//        }
//        tab.getCustomView().setSelected(true);
//        selectFragment(position);
//    }
//
//    @Override
//    public void onTabUnselected(TabLayout.Tab tab) {
//        int position = tab.getPosition();
//        if (position != -1) {
//            tab.setIcon(iconNormalRes[position]);
//        }
//        tab.getCustomView().setSelected(false);
//        unselectFragment(position);
//    }
//
//    @Override
//    public void onTabReselected(TabLayout.Tab tab) {
//
//    }
//
//    private void selectFragment(int position) {
//        if (position != -1 && currentPosition != position) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            Fragment fragment = mFragments.get(position);
//            if (fragment != null) {
//                if (!fragment.isAdded()) {
//                    ft.add(R.id.container, fragment, String.valueOf(position));
//                }
//                ft.show(fragment).commit();
//                currentPosition = position;
//            }
//        }
//    }
//
//    private void unselectFragment(int position) {
//        if (position != -1 && currentPosition == position) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            Fragment fragment = mFragments.get(position);
//            if (fragment != null && fragment.isAdded() && !fragment.isHidden()) {
//                ft.hide(fragment).commit();
//            }
//            currentPosition = -1;
//        }
//    }
//}
