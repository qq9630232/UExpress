package com.wty.app.uexpress.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.wty.app.uexpress.BuildConfig;
import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.model.Money;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.activity.LoginActivity;
import com.wty.app.uexpress.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author wty
 * 首页
 */
public class SettingFragment extends BaseFragment {

    public static final String TAG = "SettingFragment";
    @BindView(R.id.quit_login)
    BootstrapButton mQuitLogin;
    @BindView(R.id.money_tv)
    TextView mMoneyTv;
    @BindView(R.id.count)
    TextView mCount;
    private String phone_num;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_setting;
    }
    private List<Money> moneyList = new ArrayList<>();
    private double mRmb = 0;
    @Override
    protected void onInitView() {

        activity.getFragmentManager().beginTransaction().replace(R.id.ll_fragment_container, new AboutFragment()).commit();
        mQuitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.putBoolean(getContext(),"login",false);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        phone_num = SPUtil.getString(getContext(), "phone_num");

        BmobQuery<Money> bmobQuery = new BmobQuery<Money>();

        bmobQuery.findObjects(new FindListener<Money>() {
            @Override
            public void done(List<Money> list, BmobException e) {
                if(!TextUtils.isEmpty(phone_num)){
                    for (Money money : list) {
                        if(phone_num.equals(money.getReceive_id())){
                            moneyList.add(money);

                            mRmb = mRmb+Double.parseDouble(money.getMoney());

                        }
                    }
                }
                mCount.setText("总接单："+moneyList.size()+"单");
                mMoneyTv.setText("总赏金："+mRmb+"元");
            }
        });
    }

    @Override
    public void doWorkOnResume() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.about_us))
                .getLeftButton()
                .hide();
        activity.getDefaultNavigation().getRightButton().hide();
    }

    public static class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private Preference mVersion;
        private Preference mShare;
        private Preference mEmail;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_about);

            mVersion = findPreference("version");
            mShare = findPreference("share");
            mEmail = findPreference("email");
            mVersion.setSummary("v " + BuildConfig.VERSION_NAME);
            setListener();
        }

        private void setListener() {
            mShare.setOnPreferenceClickListener(this);
            mEmail.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == mShare) {
                shareApp();
                return true;
            }else if(preference == mEmail){
                sendEmail();
            }
            return false;
        }

        private void sendEmail(){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL,
                    new String[] { "ajson0321@163.com" });
            i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
            i.putExtra(Intent.EXTRA_TEXT, "我们很希望能得到您的建议！！！");
            startActivity(Intent.createChooser(i,
                    "选择邮箱应用"));
        }

        private void shareApp(){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        }
    }

}
