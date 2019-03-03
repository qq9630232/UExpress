package com.wty.app.uexpress.base;

import android.app.Application;

import com.mob.MobSDK;
import com.orhanobut.logger.Logger;

import cn.bmob.v3.Bmob;

/**
 * @author wty
 * App入口
 **/
public class UExpressApplication extends Application {
	private String applicationId = "069aabfc928d699fd66856a2bd1dfc94";
	@Override
	public void onCreate() {
		super.onCreate();
//		if (LeakCanary.isInAnalyzerProcess(this)) {
//			// This process is dedicated to LeakCanary for heap analysis.
//			// You should not init your app in this process.
//			return;
//		}
//		LeakCanary.install(this);
		MobSDK.init(this);
		Bmob.initialize(this, applicationId);

		UExpressUtil.initDataBase(this,"UExpress_db");
		Logger.init("UExpress")
                .methodCount(10)
				.hideThreadInfo();
	}
}
