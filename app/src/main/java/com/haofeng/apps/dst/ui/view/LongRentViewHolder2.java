package com.haofeng.apps.dst.ui.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.LongOrderParams;
import com.haofeng.apps.dst.ui.LongRentActivity;

/**
 * Created by WIN10 on 2017/6/18.
 */

public class LongRentViewHolder2 {
    public final static int resId = R.layout.view_long_rent_2;
    private LongRentActivity mContext;
    private View mParent;

    private EditText mEtCompanyName;
    private EditText mEtName;
    private EditText mEtPhone;
    private EditText mEtEmail;



    public LongRentViewHolder2(LongRentActivity context, View parent) {
        this.mContext = context;
        this.mParent = parent;
        this.mEtName = (EditText) parent.findViewById(R.id.et_name);
        this.mEtCompanyName =(EditText) parent.findViewById(R.id.et_company_name);
        this.mEtPhone = (EditText) parent.findViewById(R.id.et_phone);
        this.mEtEmail = (EditText) parent.findViewById(R.id.et_email);
    }

    public void show() {
        mParent.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mParent.setVisibility(View.GONE);
    }

    public boolean checkParams() {
        String name = mEtName.getText().toString();
        String phone = mEtPhone.getText().toString();
        if(TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "请输入联系人姓名", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "请输入联系人手机", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public LongOrderParams putOrderParams(LongOrderParams params) {
        params.m_company_name = mEtCompanyName.getText().toString();
        params.m_contact_name = mEtName.getText().toString();
        params.m_contact_mobile = mEtPhone.getText().toString();
        params.m_contact_email = mEtEmail.getText().toString();
        return params;
    }


}
