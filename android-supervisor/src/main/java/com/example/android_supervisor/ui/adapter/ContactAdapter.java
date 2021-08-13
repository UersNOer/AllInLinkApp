package com.example.android_supervisor.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.Contact;

/**
 * @author wujie
 */
public class ContactAdapter extends ObjectAdapter<Contact> {

    public ContactAdapter() {
        super(R.layout.item_contact);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final Contact object, int position) {
        TextView tvDelimit = holder.getView(R.id.tv_contact_delimit, TextView.class);
        tvDelimit.setText(object.getInitialName());

        TextView tvName = holder.getView(R.id.tv_contact_name, TextView.class);
        tvName.setText(object.getNickName());

        TextView tvMobile = holder.getView(R.id.tv_contact_mobile, TextView.class);
        tvMobile.setText(object.getMobile());

//        TextView tvGroup = holder.getView(R.id.tv_contact_group, TextView.class);
//        tvGroup.setText(object.getGroupName());

        ImageView ivCall = holder.getView(R.id.iv_contact_call, ImageView.class);
        ImageView ivSms = holder.getView(R.id.iv_contact_sms, ImageView.class);

        ivCall.setVisibility(View.GONE);
        ivSms.setVisibility(View.GONE);

//        final String mobile = object.getMobile();
//        if (mobile.matches("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$")) {
//            ivCall.setVisibility(View.VISIBLE);
//            ivCall.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    DialogUtils.askYesNo(v.getContext(), "呼叫", "确定呼叫" + object.getNickName() + "?",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Uri uri = Uri.parse("tel:" + mobile);
//                                    Intent intent = new Intent(Intent.ACTION_CALL);
//                                    intent.setData(uri);
//                                    v.getContext().startActivity(intent);
//                                }
//                    });
//                }
//            });
//            ivSms.setVisibility(View.VISIBLE);
//            ivSms.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Uri uri = Uri.parse("smsto:" + mobile);
//                    Intent intent = new Intent(Intent.ACTION_SENDTO);
//                    intent.setData(uri);
//                    v.getContext().startActivity(intent);
//                }
//            });
//        } else {
//            ivCall.setVisibility(View.GONE);
//            ivSms.setVisibility(View.GONE);
//        }

        View delimit = holder.getView(R.id.ll_contact_delimit);
        if (position == 0) {
            delimit.setVisibility(View.VISIBLE);
        } else {
            Contact lastObj = get(position - 1);
            if (TextUtils.isEmpty(lastObj.getInitialName())) {
                delimit.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(object.getInitialName())) {
                delimit.setVisibility(View.GONE);
            } else if (lastObj.getInitialName().equals(object.getInitialName())) {
                delimit.setVisibility(View.GONE);
            } else {
                delimit.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
//    public int getPositionForSection(int section)
//    {
//        for (int i = 0; i <this.getItemCount(); i++)
//        {
//            String sortStr = list.get(i).getSortLetters();
//            char firstChar = sortStr.toUpperCase().charAt(0);
//            if(firstChar == section)
//            {
//                return i;
//            }
//        }
//
//        return -1;
//    }
}
