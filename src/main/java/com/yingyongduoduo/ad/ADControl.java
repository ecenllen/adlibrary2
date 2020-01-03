package com.yingyongduoduo.ad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.interstitial.InterstitialADListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.yingyongduoduo.ad.dialog.GDTMuBanTuiPingDialog;
import com.yingyongduoduo.ad.dialog.GDTTuiPingDialog;
import com.yingyongduoduo.ad.dialog.SelfCPDialog;
import com.yingyongduoduo.ad.dialog.SelfTuiPingDialog;
import com.yingyongduoduo.ad.dialog.UpdateDialog;
import com.yingyongduoduo.ad.interfaceimpl.KPAdListener;
import com.yingyongduoduo.ad.interfaceimpl.SelfBannerAdListener;
import com.yingyongduoduo.ad.interfaceimpl.SelfBannerView;
import com.yingyongduoduo.ad.interfaceimpl.SelfKPAdListener;
import com.yingyongduoduo.ad.interfaceimpl.SelfKPView;
import com.yydd.net.net.CacheUtils;
import com.yydd.net.net.adbean.ADBean;
import com.yydd.net.net.constants.SysConfigEnum;

import java.io.Serializable;
import java.util.HashMap;


public class ADControl {

    private static int score = 0;

    public static long lastshowadTime = 0L;
    //展示5分好评广告，首次进来不展示，和插屏广告戳开，隔间10秒
    private static long divideTime = 8L * 1000L;
    private static long lastshowHaopingTime = System.currentTimeMillis();

    private static long showadTimeDuration = 120L * 1000L; //展示广告间隔时间，120秒


    public static Boolean isonshow = false;
    public static boolean ISGiveHaoping = false; //用户是否已经点击"给个好评"
    private static HashMap<String, String> giveHaoping = new HashMap<String, String>();


    public String GetChannel(Activity context) {
        if (Channel == null || "".equals(Channel)) {
            initChannel(context);
        }
        return Channel;
    }

    public String GetVersionCode(Activity context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(info.versionCode); //
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String oldADVersition = "";

    public void ChangeTVAddrVersion(Context context, String newVersion) {
        SharedPreferences mSettings = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Editor editor = mSettings.edit();
        editor.putString("addrversion", newVersion);
        editor.commit();
        ADControl.oldADVersition = newVersion;
    }


    public void initChannel(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("UMENG_CHANNEL");
            this.Channel = msg;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }

    }


    private static void ShowGDTKP(Context context, RelativeLayout adsParent, View skipView, final KPAdListener kpAdListener, String appid, String adplaceid) {

        SplashADListener listener = new SplashADListener() {

            @Override
            public void onADDismissed() {
                kpAdListener.onAdDismissed();
            }

            @Override
            public void onNoAD(AdError adError) {

                kpAdListener.onAdFailed(adError != null ? adError.getErrorMsg() : "");
            }

            @Override
            public void onADPresent() {
                kpAdListener.onAdPresent();
            }

            @Override
            public void onADClicked() {
                kpAdListener.onAdClick();
            }

            @Override
            public void onADTick(long l) {
                kpAdListener.onAdTick(l);
            }

            @Override
            public void onADExposure() {

            }
        };
        SplashAD splashAD = new SplashAD((Activity) context, skipView, appid, adplaceid, listener, 0);
        splashAD.fetchAndShowIn(adsParent);
    }

    private static void ShowSelfKP(final Context context, RelativeLayout adsParent, final KPAdListener kpAdListener) {

        SelfKPAdListener listener = new SelfKPAdListener() {
            @Override
            public void onAdDismissed(ADBean bean) {//广告展示完毕
                kpAdListener.onAdDismissed();
            }

            @Override
            public void onAdFailed(ADBean bean) {//广告获取失败
                kpAdListener.onAdFailed("");
            }

            @Override
            public void onAdPresent(ADBean bean) {//广告开始展示
                kpAdListener.onAdPresent();
//                if (bean != null && !TextUtils.isEmpty(bean.getAd_name())) {
//                    Map<String, String> map_ekv = new HashMap<String, String>();
//                    map_ekv.put("show", bean.getAd_name());
//                    MobclickAgent.onEvent(context, "kp_count", map_ekv);
//                }
            }

            @Override
            public void onAdClick(ADBean bean) {//广告被点击
                kpAdListener.onAdClick();
//                if (bean != null && !TextUtils.isEmpty(bean.getAd_name())) {
//                    Map<String, String> map_ekv = new HashMap<String, String>();
//                    map_ekv.put("click", bean.getAd_name());
//                    MobclickAgent.onEvent(context, "kp_count", map_ekv);
//                }
            }
        };
        SelfKPView selfKPView = new SelfKPView(context);
        selfKPView.setADListener(listener);
        adsParent.removeAllViews();
        adsParent.addView(selfKPView);
    }

    //初始化广点通退屏广告
    private static Boolean InitGDTTP(Context context) {
        if (CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_TP))//展示退屏广告
        {
//            String kpType = AppConfig.getTPType();//获取开屏广告类型，baidu，gdt，google
            String kpType = CacheUtils.getConfig(SysConfigEnum.AD_TP_TYPE);//获取开屏广告类型，baidu，gdt，google
            String kp_String = CacheUtils.getConfig(SysConfigEnum.AD_TP_ID);
            if (!TextUtils.isEmpty(kp_String)) {
                String[] a = kp_String.split(",");
                if (a.length == 2) {
                    String appid = a[0];
                    String adplaceid = a[1];
                    if ("gdt".equals(kpType)) {
                        GDTTuiPingDialog.Init(context, appid, adplaceid);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else//不展示开屏广告
        {
            return false;
        }

    }

    //初始化广点通退屏广告
    private static Boolean InitGDTMuBanTP(Context context) {
        if (CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_TP))//展示退屏广告
        {
//            String kpType = AppConfig.getTPType();//获取开屏广告类型，baidu，gdt，google
            String kpType = CacheUtils.getConfig(SysConfigEnum.AD_TP_TYPE);//获取开屏广告类型，baidu，gdt，google
            String kp_String = CacheUtils.getConfig(SysConfigEnum.AD_TP_ID);
            if (!TextUtils.isEmpty(kp_String)) {
                String[] a = kp_String.split(",");
                if (a.length == 2) {
                    String appid = a[0];
                    String adplaceid = a[1];
                    if ("gdtmb".equals(kpType)) {
                        GDTMuBanTuiPingDialog.Init(context, appid, adplaceid);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else//不展示开屏广告
        {
            return false;
        }

    }

    //初始化广点通退屏广告
    public static Boolean ShowTPAD(Context context) {
        if (CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_TP))//展示开屏广告
        {
//            String tpType = AppConfig.getTPType();//获取开屏广告类型，baidu，gdt，google
            String tpType = CacheUtils.getConfig(SysConfigEnum.AD_TP_TYPE);//获取开屏广告类型，baidu，gdt，google
//            String tp_String = AppConfig.configBean.ad_tp_idMap.get(tpType);
            String tp_String = CacheUtils.getConfig(SysConfigEnum.AD_TP_ID);
            if (!TextUtils.isEmpty(tpType) && "self".equals(tpType)) {//退屏类型为自家的
                SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context);
                sfCP.show();
                return false;
            } else if (!TextUtils.isEmpty(tp_String)) {//并非自家的，来自广点通，百度等，目前只有广点通
                String[] a = tp_String.split(",");
                if (a.length == 2) {
                    if ("gdt".equals(tpType)) {
                        GDTTuiPingDialog sfCP = new GDTTuiPingDialog(context);
                        sfCP.show();
                        return true;
                    } else if ("gdtmb".equals(tpType)) {
                        GDTMuBanTuiPingDialog sfCP = new GDTMuBanTuiPingDialog(context);
                        sfCP.show();
                        return true;
                    } else {//有两个id，但是又不是广点通
                        SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context, null);
                        sfCP.show();
                        return false;
                    }
                } else {//id没有两个，则暂时表示配置有问题，如果以后某个平台id只有一个，则重新写该方法
                    SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context, null);
                    sfCP.show();
                    return false;
                }
            } else {//如果返回的id为空，又不展示自家广告，这种情况可能是后台配置错误，则不展示广告
                SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context, null);
                sfCP.show();
                return true;
            }
        } else { //不展示退屏广告
            SelfTuiPingDialog sfCP = new SelfTuiPingDialog(context, null);
            sfCP.show();
            return false;
        }

    }

    public static void ShowKp(Context context, RelativeLayout adsParent, View skipView, final KPAdListener kpAdListener) {
        if (CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_KP))//展示开屏广告
        {
//            String kpType = AppConfig.getKPType();//获取开屏广告类型，baidu，gdt，google
            String kpType = CacheUtils.getConfig(SysConfigEnum.AD_KP_TYPE);//获取开屏广告类型，baidu，gdt，google
//            String kp_String = AppConfig.configBean.ad_kp_idMap.get(kpType);
            String kp_String = CacheUtils.getConfig(SysConfigEnum.AD_KP_ID);
            if (!TextUtils.isEmpty(kp_String)) {
                String[] a = kp_String.split(",");
                if (a.length == 2) {
                    String appid = a[0];
                    String adplaceid = a[1];
                    if ("baidu".equals(kpType)) {
                        ShowSelfKP(context, adsParent, kpAdListener);
                    } else if ("gdt".equals(kpType)) {
                        ShowGDTKP(context, adsParent, skipView, kpAdListener, appid, adplaceid);
                    } else {
                        kpAdListener.onAdFailed("其他不支持广告类型" + kp_String);
                    }
                } else {
                    kpAdListener.onAdFailed("后台获取开屏广告的id为" + kp_String);
                }
            } else {
                ShowSelfKP(context, adsParent, kpAdListener);
            }
        } else//不展示开屏广告
        {
            kpAdListener.onAdFailed("后台不展示开屏广告");
        }

    }


    private static void ShowGDTCP(Context context, String appid, String adplaceid) {


//        SplashAd.setAppSid(context, appid);// 其中的debug需改为您的APPSID
        final InterstitialAD interAd = new InterstitialAD((Activity) context, appid, adplaceid);
        interAd.setADListener(new InterstitialADListener() {
            @Override
            public void onADReceive() {
                interAd.show();
            }

            @Override
            public void onNoAD(AdError adError) {
                lastshowadTime = 0;
            }


            @Override
            public void onADOpened() {

            }

            @Override
            public void onADExposure() {

            }

            @Override
            public void onADClicked() {

            }

            @Override
            public void onADLeftApplication() {

            }

            @Override
            public void onADClosed() {

            }
        });
        interAd.loadAD();
    }

    private static UnifiedInterstitialAD interAd;
    private static String mPosId = "";

    private static void ShowGDTCP2(Activity context, String appid, String adplaceid) {

        interAd = getIAD(context, appid, adplaceid, new UnifiedInterstitialADListener() {
            @Override
            public void onADReceive() {
                interAd.show();
            }

            @Override
            public void onVideoCached() {

            }

            @Override
            public void onNoAD(AdError adError) {
                lastshowadTime = 0;
            }


            @Override
            public void onADOpened() {

            }

            @Override
            public void onADExposure() {

            }

            @Override
            public void onADClicked() {

            }

            @Override
            public void onADLeftApplication() {

            }

            @Override
            public void onADClosed() {

            }
        });

        interAd.loadAD();
    }

    private static UnifiedInterstitialAD getIAD(Activity context, String appid, String posId, UnifiedInterstitialADListener listener) {
//        if (interAd != null && mPosId.equals(posId)) {
//            return interAd;
//        }
        if (interAd != null) {
            interAd.close();
            interAd.destroy();
            interAd = null;
        }
        if (interAd == null) {
            interAd = new UnifiedInterstitialAD(context, appid, posId, listener);
        }
        mPosId = posId;
        return interAd;
    }

    private static void ShowSelfCP(final Context context) {

        SelfCPDialog sfCP = new SelfCPDialog(context);
        sfCP.setADListener(new SelfBannerAdListener() {
            @Override
            public void onAdClick(ADBean adBean) {
            }

            @Override
            public void onAdFailed() {

            }

            @Override
            public void onADReceiv(ADBean adBean) {

            }
        });
        sfCP.show();

    }

    /**
     * 展示插屏广告
     *
     * @param context context
     */
    public static void ShowCp(Activity context) {
        if (GDTTuiPingDialog.adItems == null || GDTTuiPingDialog.adItems.size() == 0 || System.currentTimeMillis() - GDTTuiPingDialog.initTime > 45 * 60 * 1000) {
            InitGDTTP(context);
        }
        if (GDTMuBanTuiPingDialog.adItems == null || GDTMuBanTuiPingDialog.adItems.size() == 0 || System.currentTimeMillis() - GDTMuBanTuiPingDialog.initTime > 45 * 60 * 1000) {
            InitGDTMuBanTP(context);
        }
        if (CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_CP))//展示开屏广告
        {
            if (System.currentTimeMillis() - lastshowadTime < showadTimeDuration) {
                System.out.println("广告时间没到" + (System.currentTimeMillis() - lastshowadTime));
                return;
            }
            lastshowadTime = System.currentTimeMillis();
            lastshowHaopingTime = lastshowadTime - showadTimeDuration + divideTime;
            String cpType = CacheUtils.getConfig(SysConfigEnum.AD_CP_TYPE);//获取开屏广告类型，baidu，gdt，google
            String kp_String = CacheUtils.getConfig(SysConfigEnum.AD_CP_ID);
            if (!TextUtils.isEmpty(kp_String)) {
                String[] a = kp_String.split(",");
                if (a.length == 2) {
                    String appid = a[0];
                    String adplaceid = a[1];
                    if ("baidu".equals(cpType)) {
                        ShowSelfCP(context);
                    } else if ("gdt2".equals(cpType)) {
                        ShowGDTCP2(context, appid, adplaceid);
                    } else if ("gdt".equals(cpType)) {
                        ShowGDTCP(context, appid, adplaceid);
                    } else if ("self".equals(cpType)) {
                        ShowSelfCP(context);
                    } else {
                        // kpAdListener.onAdFailed("其他不支持广告类型" + kp_String);
                    }
                } else {
                    // kpAdListener.onAdFailed("后台获取开屏广告的id为" + kp_String);
                }
            } else {
                ShowSelfCP(context);
            }
        } else//不展示开屏广告
        {
//              kpAdListener.onAdFailed("后台不展示开屏广告");
        }

    }


    private void addGDTBanner(final LinearLayout lyt, final Activity context, String appid, String adplaceid) {
        if (bannerView != null) {
            lyt.removeAllViews();
            bannerView.destroy();
        }
        try {
            bannerView = new BannerView(context, ADSize.BANNER, appid, adplaceid);
            // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
            // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
            bannerView.setRefresh(30);
            bannerView.setADListener(new BannerADListener() {

                @Override
                public void onADClicked() {
                    System.out.println("广点通广告被点击");
                }

                @Override
                public void onADLeftApplication() {

                }

                @Override
                public void onADOpenOverlay() {

                }

                @Override
                public void onADCloseOverlay() {

                }

                @Override
                public void onNoAD(AdError adError) {
                    addSelfBanner(lyt, context);
                }

                @Override
                public void onADReceiv() {

                }

                @Override
                public void onADExposure() {

                }

                @Override
                public void onADClosed() {

                }
            });
            lyt.addView(bannerView);
            bannerView.loadAD();

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private void addGDTBanner2(final LinearLayout lyt, final Activity context, String appid, String adplaceid) {
        if (unifiedBannerView != null) {
            lyt.removeAllViews();
            unifiedBannerView.destroy();
        }
        try {
            unifiedBannerView = new UnifiedBannerView(context, appid, adplaceid, new UnifiedBannerADListener() {
                @Override
                public void onNoAD(AdError adError) {
                    addSelfBanner(lyt, context);
                }

                @Override
                public void onADReceive() {

                }

                @Override
                public void onADExposure() {

                }

                @Override
                public void onADClosed() {

                }

                @Override
                public void onADClicked() {
                    System.out.println("广点通广告被点击");
                }

                @Override
                public void onADLeftApplication() {

                }

                @Override
                public void onADOpenOverlay() {

                }

                @Override
                public void onADCloseOverlay() {

                }
            });
            lyt.addView(unifiedBannerView, getUnifiedBannerLayoutParams(context));
            // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
            unifiedBannerView.loadAD();

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /**
     * banner2.0规定banner宽高比应该为6.4:1 , 开发者可自行设置符合规定宽高比的具体宽度和高度值
     *
     * @return
     */
    private static LinearLayout.LayoutParams getUnifiedBannerLayoutParams(Context context) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        return new LinearLayout.LayoutParams(screenWidth, Math.round(screenWidth / 6.4F));
    }

    private void addGoogleBanner(final LinearLayout lyt, final Activity context, String appid, String adplaceid) {
        lyt.removeAllViews();
    }

    private void addSelfBanner(LinearLayout lyt, final Activity context) {
        lyt.removeAllViews();
        try {
            SelfBannerView bv = new SelfBannerView(context);
            bv.setADListener(new SelfBannerAdListener() {
                @Override
                public void onAdClick(ADBean adBean) {
//                    if (adBean != null && !TextUtils.isEmpty(adBean.getAd_name())) {
//                        Map<String, String> map_ekv = new HashMap<String, String>();
//                        map_ekv.put("click", adBean.getAd_name());
//                        MobclickAgent.onEvent(context, "banner_count", map_ekv);
//                        System.out.println("广告被点击:"+adBean.getAd_name());
//                    }
                }

                @Override
                public void onAdFailed() {

                }

                @Override
                public void onADReceiv(ADBean adBean) {
//                    if (adBean != null && !TextUtils.isEmpty(adBean.getAd_name())) {
//                        Map<String, String> map_ekv = new HashMap<String, String>();
//                        map_ekv.put("show", adBean.getAd_name());
//                        MobclickAgent.onEvent(context, "banner_count", map_ekv);
//                        System.out.println("广告被展示:"+adBean.getAd_name());
//                    }
                }
            });
            lyt.addView(bv);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private BannerView bannerView;
    private UnifiedBannerView unifiedBannerView;

    /**
     * 显示banner广告
     *
     * @param lyt     banner广告位置
     * @param context context
     */
    public void addBannerAd(LinearLayout lyt, Activity context) {
        ShowCp(context);
        homeGet5Score(context);
        if (GDTTuiPingDialog.adItems == null || GDTTuiPingDialog.adItems.size() == 0 || System.currentTimeMillis() - GDTTuiPingDialog.initTime > 45 * 60 * 1000) {
            InitGDTTP(context);
        }
        if (GDTMuBanTuiPingDialog.adItems == null || GDTMuBanTuiPingDialog.adItems.size() == 0 || System.currentTimeMillis() - GDTMuBanTuiPingDialog.initTime > 45 * 60 * 1000) {
            InitGDTMuBanTP(context);
        }
        if (CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_BANNER))//展示广告条广告
        {
            String bannerType = CacheUtils.getConfig(SysConfigEnum.AD_BANNER_TYPE);//获取开屏广告类型，baidu，gdt，google
            String banner_String = CacheUtils.getConfig(SysConfigEnum.AD_BANNER_ID);
            if (!TextUtils.isEmpty(banner_String)) {
                String[] a = banner_String.split(",");
                if (a.length == 2) {
                    String appid = a[0];
                    String adplaceid = a[1];
                    if ("baidu".equals(bannerType)) {
                        addSelfBanner(lyt, context);
                    } else if ("gdt2".equals(bannerType)) {
                        addGDTBanner2(lyt, context, appid, adplaceid);
                    } else if ("gdt".equals(bannerType)) {
                        addGDTBanner(lyt, context, appid, adplaceid);
                    } else if ("google".equals(bannerType)) {
                        addGoogleBanner(lyt, context, appid, adplaceid);
                    } else if ("self".equals(bannerType)) {
                        addSelfBanner(lyt, context);
                    } else {
//                        kpAdListener.onAdFailed("其他不支持广告类型" + kp_String);
                    }
                } else {

//                    kpAdListener.onAdFailed("后台获取开屏广告的id为" + kp_String);
                }
            } else {
                addSelfBanner(lyt, context);
            }
        } else//不展示banner
        {
//            kpAdListener.onAdFailed("后台不展示开屏广告");
        }

    }

    public static void setISGiveHaoping(Context context, Boolean isgivehaoping) {
        ISGiveHaoping = isgivehaoping;
        SharedPreferences mSettings = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE); //
        Editor editor = mSettings.edit();
        editor.putBoolean("ISGiveHaoping", true);
        editor.apply();
    }

    public static boolean getIsGiveHaoping(Context context) {
        SharedPreferences mSettings = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ISGiveHaoping = mSettings.getBoolean("ISGiveHaoping", false);
        return ISGiveHaoping;
    }

    private void homeGet5Score(final Activity context) {
        if (getIsGiveHaoping(context)) {
            return;
        }
        if (!CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_HAOPING)) {
            return;
        }

        if (isonshow)
            return;

        if (System.currentTimeMillis() - lastshowHaopingTime < showadTimeDuration) {
            Log.i("广告时间没到", (System.currentTimeMillis() - lastshowHaopingTime) + "");
            return;
        }

        lastshowHaopingTime = System.currentTimeMillis();
        isonshow = true;

        new AlertDialog.Builder(context).setTitle("申请开放极速抢红包")
                .setMessage("\t\t在市场给5星好评,24小时内审核即可自动切换到极速抢红包模式！")
                .setPositiveButton("给个好评", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setISGiveHaoping(context, true);
                        goodPinglun(context);
                        isonshow = false;
                    }
                }).setNegativeButton("以后再说", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isonshow = false;
            }
        }).setCancelable(false).show();
    }

    /*
     * 加入QQ群的代码
     * */
    public static boolean joinQQGroup(Context context) {
        try {
            String key = CacheUtils.getConfig(SysConfigEnum.QQ_KEY);
            if (key == null || "".equals(key)) {
                Toast.makeText(context, "请手工添加QQ群:286239217", Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent();
            intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(context, "请手工添加QQ群:286239217", Toast.LENGTH_SHORT).show();
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static void goodPinglun(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "异常", Toast.LENGTH_SHORT).show();
        }

    }


    public static String Channel = "";


    public static boolean update(Context context) {

//        if (AppConfig.isShowUpdate()) {
        if (CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_UPDATE)) {
            int currentVersion = 0;
            try {
                // ---get the package info---
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                currentVersion = pi.versionCode;
                if (currentVersion != CacheUtils.getConfigInt(SysConfigEnum.UPDATE_VERSION)) {
                    UpdateDialog dg = new UpdateDialog(context);
                    dg.show();
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                Log.e("VersionInfo", "Exception", e);
            }
            return false;

        } else {
            return false;
        }
    }

    public void updateLiveVersion() {
    }

    public boolean isLiveUrlUpdate(Activity context) {
        return true;
    }


    static class Config implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = -700152136951314809L;
        String version = "";

        public Config(String version) {
            this.version = version;
        }
    }
}
