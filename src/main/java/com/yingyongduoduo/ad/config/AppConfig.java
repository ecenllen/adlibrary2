package com.yingyongduoduo.ad.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yingyongduoduo.ad.bean.ConfigBean;
import com.yingyongduoduo.ad.bean.PublicConfigBean;
import com.yingyongduoduo.ad.utils.DownLoaderAPK;
import com.yingyongduoduo.ad.utils.HttpUtil;
import com.yingyongduoduo.ad.utils.PackageUtil;
import com.yydd.net.net.CacheUtils;
import com.yydd.net.net.adbean.ADBean;
import com.yydd.net.net.adbean.MainListBean;
import com.yydd.net.net.adbean.SearchListBean;
import com.yydd.net.net.adbean.ShowListBean;
import com.yydd.net.net.adbean.VideoBean;
import com.yydd.net.net.adbean.WXGZHBean;
import com.yydd.net.net.adbean.ZiXunItemBean;
import com.yydd.net.net.adbean.ZiXunListItemBean;
import com.yydd.net.net.constants.SysConfigEnum;
import com.yydd.net.net.help.ADHelp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * 保存软件的配置信息
 * Created by yuminer on 2017/3/16.
 */
public class AppConfig {

    /**
     * index.html主页路径，从后台下载到手机本地的index.html 路径
     */
    public static String INDEX_HTML_LOCAL_PATH;
    /**
     * index.html本地路径：前缀加了file://，直接提供给webView使用
     */
    public static String URL_INDEX_HTML;
    /**
     * start.html本地路径
     */
    public static String START_HTML_LOCAL_PATH;
    /**
     * start.html本地路径：前缀加了file://，直接提供给webView使用
     */
    public static String URL_START_HTML;

    private static String play_qingxidu = "1";//清晰度 0,1,2 对应标清，高清，超清
    private static String download_qingxidu = "1";
    public static String qhblibPath; //抢红包本地路径
    private static final String QHB_VERSION_SP_KEY = "qhbsourceVersion";
    public static String youkulibPath;
    public static boolean isshowHDPicture = true;
    public static String GZHPath;
    public static float decimal = 0.05f;//广告内容百分比
    public static String[] qingxiduArray = new String[]{"标清", "高清", "超清", "1008P"};

    public static String getDownload_qingxidu(Context context) {
        SharedPreferences mSettings = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        AppConfig.download_qingxidu = mSettings.getString("download_qingxidu", "2");
        return AppConfig.download_qingxidu;
    }

    public static String getDownload_qingxidu() {
        return AppConfig.download_qingxidu;
    }

    public static void setDownload_qingxidu(Context context, String qingxidu) {
        SharedPreferences mSettings = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("download_qingxidu", qingxidu);
        editor.commit();
        AppConfig.download_qingxidu = qingxidu;
    }

    public static String getPlay_Qingxidu(Context context) {
        SharedPreferences mSettings = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        AppConfig.play_qingxidu = mSettings.getString("play_qingxidu", "2");
        return AppConfig.play_qingxidu;
    }

    public static String getPlay_Qingxidu() {
        return AppConfig.play_qingxidu;
    }

    public static void setPlay_Qingxidu(Context context, String qingxidu) {
        SharedPreferences mSettings = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("play_qingxidu", qingxidu);
        editor.apply();
        AppConfig.play_qingxidu = qingxidu;
    }

    //    public static List<DownloadInfo> downloadInfos = new ArrayList<DownloadInfo>();
    //    public static String ConfigJson = "";
//    public static String VideoJson = "";
//    public static String SelfadJson = "";
//    public static String wxgzhJson = "";
    public static String versioncode = "";
    public static String Channel = "";
    public static String APPKEY = "";


    private final static String baseURL1 = "http://47.106.9.254/resource/yinqing/";
    public final static String baseURL2 = "http://videodata.gz.bcebos.com/daohang/";
    public final static String baseURL3 = "http://www.yingyongduoduo.com/daohang/";
    private final static String configbaseURL1 = baseURL1 + "%s/";
    private final static String configbaseURL2 = baseURL2 + "%s/";
    private final static String configbaseURL3 = baseURL3 + "%s/";

    public static ConfigBean configBean;
    public static PublicConfigBean publicConfigBean;
    public static List<VideoBean> videoBeans = new ArrayList<VideoBean>();
    public static List<ADBean> selfadBeans = new ArrayList<ADBean>();
    public static List<ZiXunItemBean> ziXunBeans = new ArrayList<>();
    public static List<WXGZHBean> wxgzhBeans = new ArrayList<WXGZHBean>();


    public static void InitLocal(Context context) {
//        initConfigBean(context);
//        initPublicConfigBean(context);
//        initVideoBean(CacheUtils.getConfig(SysConfigEnum.AD_VIDEO_JSON));
//        initselfadBeans(CacheUtils.getConfig(SysConfigEnum.AD_SELFAD_JSON));
//        initZixunBeans(CacheUtils.getConfig(SysConfigEnum.AD_ZIXUN_JSON));
//        initwxgzhBeans(CacheUtils.getConfig(SysConfigEnum.AD_WXGZH_JSON));
        //initvideosourceVersion(context);这个没有必要初始化
    }


    public static List<VideoBean> getVideoBean(String videoJson) {
        List<VideoBean> beans = new ArrayList<VideoBean>();

        try {
            final JSONArray ja = new JSONArray(videoJson);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                VideoBean bean = new VideoBean();
                if (haveKey(jo, "platform") && haveKey(jo, "name") && haveKey(jo, "playonbroswer")) {
                    bean.platform = jo.getString("platform");
                    bean.name = jo.getString("name");
                    bean.playonbroswer = jo.getString("playonbroswer");
                    bean.noadVideowebBaseUrl = jo.getString("noadVideowebBaseUrl");
                    bean.imgUrl = jo.getString("imgUrl");
                    beans.add(bean);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }


    private static List<ZiXunItemBean> getZiXunBeans(String zixunJson) {
        List<ZiXunItemBean> beans = new ArrayList<>();

        try {
            final JSONArray ja = new JSONArray(zixunJson);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                JSONObject bottomTab = jo.optJSONObject("bottomTab");
                ZiXunItemBean ziXunItemBean = new ZiXunItemBean();
                ziXunItemBean.setTabName(bottomTab.optString("tabName"));
                ziXunItemBean.setIcon(bottomTab.optString("icon"));
                ziXunItemBean.setSelIcon(bottomTab.optString("selIcon"));
                JSONArray list = bottomTab.optJSONArray("list");
                if (list != null) {
                    List<ZiXunListItemBean> ziXunListItemBeans = new ArrayList<>();
                    for (int l = 0; l < list.length(); l++) {
                        JSONObject jsonObject = list.getJSONObject(l);
                        ZiXunListItemBean ziXunListItemBean = new ZiXunListItemBean();
                        ziXunListItemBean.setName(jsonObject.optString("name"));
                        ziXunListItemBean.setUrl(jsonObject.optString("url"));
                        ziXunListItemBeans.add(ziXunListItemBean);
                    }
                    ziXunItemBean.setList(ziXunListItemBeans);
                }

                beans.add(ziXunItemBean);
            }

        } catch (Exception e) {
        }
        return beans;
    }

    public static List<WXGZHBean> getWXGZHBeans(String wxgzhJson) {
        List<WXGZHBean> beans = new ArrayList<WXGZHBean>();

        try {
            final JSONArray ja = new JSONArray(wxgzhJson);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                WXGZHBean bean = new WXGZHBean();
                if (haveKey(jo, "displayName") && haveKey(jo, "introduction") && haveKey(jo, "url") && haveKey(jo, "id") && haveKey(jo, "thumb") && haveKey(jo, "type")) {
                    bean.displayName = jo.getString("displayName");
                    bean.id = jo.getString("id");
                    bean.type = jo.getString("type");
                    bean.introduction = jo.getString("introduction");
                    bean.thumb = jo.getString("thumb");
                    bean.url = jo.getString("url");
                    if (new File(GZHPath + bean.id + ".jpg").exists()) {
                        bean.isPicExist = true;
                    }
                    beans.add(bean);
                }
            }

        } catch (Exception e) {
        }
        return beans;
    }

    public static void initVideoBean(String videoJson) {
        if (videoJson == null) return;
        try {
            List<VideoBean> currentVideoBeans = getVideoBean(videoJson);
            if (currentVideoBeans.size() != 0) {
                videoBeans = currentVideoBeans;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取asset里的文件
     *
     * @param context  上下文
     * @param filename 文件名
     * @return
     */
    public static String getZixunJsonFromAssets(Context context, String filename) {
        String string = "";
        try {
            InputStream in = context.getResources().getAssets().open(filename);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            string = new String(buffer);

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    private static String getZixunJson(String url) {
        String SelfadJson = "";
        try {
            SelfadJson = HttpUtil.getJson(url);
            List<ZiXunItemBean> currentSelfAdBeans = getZiXunBeans(SelfadJson);
            if (currentSelfAdBeans.size() == 0) {
                SelfadJson = "";
            }
        } catch (IOException e) {
            SelfadJson = "";
        }
        return SelfadJson;
    }

//    private static void initselfadBeans(String selfJson) {
//        if (TextUtils.isEmpty(selfJson)) return;
//        try {
//            List<ADBean> currentSelfAdBeans = ADBeanUtil.getSelfAdBeans(selfJson);
//            if (currentSelfAdBeans != null && currentSelfAdBeans.size() != 0) {
//                selfadBeans = currentSelfAdBeans;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private static void initZixunBeans(String zixunJson) {
//        if (zixunJson == null) return;
//        try {
//            List<ZiXunItemBean> currentSelfAdBeans = getZiXunBeans(zixunJson);
//            if (currentSelfAdBeans.size() != 0) {
//                ziXunBeans = currentSelfAdBeans;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private static String getWXGZHJson(String url) {
        String wxgzhJson = "";
        try {
            wxgzhJson = new HttpUtil().getJson(url);
            List<WXGZHBean> currentSelfAdBeans = getWXGZHBeans(wxgzhJson);
            if (currentSelfAdBeans.size() == 0) {
                wxgzhJson = "";
            }
        } catch (IOException e) {
            wxgzhJson = "";
        }
        return wxgzhJson;
    }

    public static void initwxgzhJson(Context context) {
        if (context == null) return;
        SharedPreferences mSettings = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        if (publicConfigBean != null && !"".equals(publicConfigBean.wxgzhversion) && !publicConfigBean.wxgzhversion.equals(mSettings.getString("wxgzhversion", ""))) {//需要更新
            String wxgzhJson = getWXGZHJson(baseURL1 + "wxgzh/wxgzh.json");
            if (wxgzhJson.isEmpty()) {
                wxgzhJson = getWXGZHJson(baseURL2 + "wxgzh/wxgzh.json");
            }
            if (wxgzhJson.isEmpty()) {
                wxgzhJson = getWXGZHJson(baseURL3 + "wxgzh/wxgzh.json");
            }

            if (!wxgzhJson.isEmpty()) {
                List<WXGZHBean> currentSelfAdBeans = getWXGZHBeans(wxgzhJson);
                for (WXGZHBean bean : currentSelfAdBeans) {
                    initGZHPic(bean);
                }
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString("wxgzhJson", wxgzhJson);
                editor.putString("wxgzhversion", publicConfigBean.wxgzhversion);
                editor.apply();
            }
        }
        String wxgzhJson = mSettings.getString("wxgzhJson", "");
        List<WXGZHBean> currentSelfAdBeans = getWXGZHBeans(wxgzhJson);
        for (WXGZHBean bean : currentSelfAdBeans) {
            // Boolean isSuccess = true;//成功与否不重要，不成功的不用就是
            if (!new File(GZHPath + bean.id + ".jpg").exists()) {//如果文件不存在
                initGZHPic(bean);
            }
        }
//        initwxgzhBeans(context);//初始化之后需要判断图片是否存在
    }

    private static void initGZHPic(WXGZHBean bean) {

        try {
            downloadgzhjpg(bean, bean.thumb);
        } catch (Exception ethumb) {//这一步则表示下载失败
            deleteFile(GZHPath + bean.id + ".jpg");

            try {
                downloadgzhjpg(bean, baseURL1 + "wxgzh/" + bean.id + ".jpg");
            } catch (Exception e1) {
                deleteFile(GZHPath + bean.id + ".jpg");
                try {
                    downloadgzhjpg(bean, baseURL2 + "wxgzh/" + bean.id + ".jpg");
                } catch (Exception e2) {
                    deleteFile(GZHPath + bean.id + ".jpg");
                    try {
                        downloadgzhjpg(bean, baseURL3 + "wxgzh/" + bean.id + ".jpg");
                    } catch (Exception e3) {//这一步则表示下载失败
                        // isSuccess = false;
                        deleteFile(GZHPath + bean.id + ".jpg");
                    }
                }
            }
        }
    }

    public static void downloadgzhjpg(WXGZHBean bean, String jpgurl) throws Exception {
        deleteFile(GZHPath + bean.id + ".jpg");// 如果存在就先删除
        URL url = new URL(jpgurl);
        URLConnection con = url.openConnection();
        int contentLength = con.getContentLength();
        InputStream is = con.getInputStream();
        byte[] bs = new byte[1024];
        int len;
        OutputStream os = new FileOutputStream(GZHPath + bean.id + ".jpg");
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.close();
        is.close();

    }

    public static void initwxgzhBeans(String wxgzhJson) {
        if (TextUtils.isEmpty(wxgzhJson)) return;
        try {
            List<WXGZHBean> currentSelfAdBeans = getWXGZHBeans(wxgzhJson);
            if (currentSelfAdBeans.size() != 0) {
                wxgzhBeans = currentSelfAdBeans;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化抢红包JAR包
     *
     * @param context
     */
    public static void initQhbsourceVersion(Context context) {
        SharedPreferences mSettings = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        Boolean isneedUpdate = publicConfigBean != null && !"".equals(publicConfigBean.qhbsourceVersion) && !publicConfigBean.qhbsourceVersion.equals(mSettings.getString(QHB_VERSION_SP_KEY, ""));
        if (isneedUpdate || (!(new File(qhblibPath).exists()) && publicConfigBean != null && !"".equals(publicConfigBean.qhbsourceVersion))) {//需要更新videosourceVersion 或者没有在目录下找到该jar,但是获取
            Boolean isSuccess = true;
            try {
                downloadjar(baseURL1 + "video/libqhb.so", qhblibPath);
            } catch (Exception e1) {
                try {
                    downloadjar(baseURL2 + "video/libqhb.so", qhblibPath);
                } catch (Exception e2) {
                    try {
                        downloadjar(baseURL3 + "video/libqhb.so", qhblibPath);
                    } catch (Exception e3) {//这一步则表示下载失败
                        isSuccess = false;
                    }
                }
            }
            if (isSuccess) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(QHB_VERSION_SP_KEY, publicConfigBean.qhbsourceVersion);
                editor.apply();
            } else {
                deleteFile(qhblibPath);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(QHB_VERSION_SP_KEY, "");
                editor.apply();
            }
        }
    }


    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
        }

    }

    public static void downloadjar(String jarUrl, String savePath) throws Exception {
        deleteFile(savePath);// 如果存在就先删除
        URL url = new URL(jarUrl);
        URLConnection con = url.openConnection();
        int contentLength = con.getContentLength();
        InputStream is = con.getInputStream();
        byte[] bs = new byte[1024];
        int len;
        OutputStream os = new FileOutputStream(savePath);
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.flush();
        os.close();
        is.close();

    }

    private static boolean haveKey(JSONObject jo, String key) {
        return jo.has(key) && !jo.isNull(key);
    }

    //是否使用第三方链接对当前播放地址进行处理，针对vip等情况
    public static boolean isNoadVideoweb(String platform) {
        if (configBean == null || TextUtils.isEmpty(platform)) {
            return false;
        }
        for (String str : configBean.noadVideowebchannel.split(",")) {
            String[] a = str.split(":");
            if (a.length == 2) {
                String platformItem = a[0];
                String versionItem = a[1];
                if (platform.equals(platformItem) && versioncode.equals(versionItem)) {//平台与版本对应了，因为渠道已经选定了
                    return false;
                }
            }
        }
        return true;
    }

    //获取第三方去广告的基地址
    public static String getnoadVideowebBaseUrl(String platform, String url) {
        if (videoBeans == null || videoBeans.size() == 0 || TextUtils.isEmpty(platform)) {
            return url;
        }
        if (!isNoadVideoweb(platform)) {
            return url;
        }
        for (VideoBean bean : ADHelp.videoBeans) {
            if (bean.platform.equals(platform)) {
                if (bean.noadVideowebBaseUrl.contains("%s")) {
                    return String.format(bean.noadVideowebBaseUrl, url);
                } else {
                    return url;
                }
            }
        }
        return url;
    }

    //是否通过浏览器进行播放，这里涉及两个开关，一个在videojson里面（总开关），一个在config里面（渠道开关）
    public static boolean isPlayOnweb(String platform) {
        System.out.println("当前频道:" + platform);
        if (videoBeans == null || videoBeans.size() == 0 || TextUtils.isEmpty(platform)) {
            return false;
        }
        for (VideoBean bean : ADHelp.videoBeans) {
            if (bean.platform.equals(platform)) {
                if ("1".equals(bean.playonbroswer)) //为1则表示用web播放,为0表示总开关没有设置一定要用web播放
                {
                    return true;
                } else {
                    break;
                }
            }
        }
        if (configBean == null) {
            return false;
        }
        for (String str : configBean.playonwebchannel.split(",")) {
            String[] a = str.split(":");
            if (a.length == 2) {
                String platformItem = a[0];
                String versionItem = a[1];
                if (platform.equals(platformItem) && versioncode.equals(versionItem)) {//平台与版本对应了，因为渠道已经选定了
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Integer> GetRandomList(int size, int max) {
        Random r = new Random();
        List<Integer> list = new ArrayList<Integer>();
        int i;
        while (list.size() < size) {
            i = r.nextInt(max);
            if (!list.contains(i)) {
                list.add(i);
            }
        }
        Collections.sort(list);
        return list;
    }

    public static Boolean ISInistallSelfAD(String packageName) {
        for (ADBean selfad : selfadBeans) {//过滤掉已经安装了的app
            if (TextUtils.equals(packageName, selfad.getAd_packagename())) {
                return true;
            }

        }
        return false;

    }

    /*
     * 随机取广告，size表示取的数量
     * */
    public static List<ADBean> GetSelfADByCount(Context context, int size, String event_id) {
        List<ADBean> selfADs = new ArrayList<ADBean>();
        List<ADBean> ok_selfadBeans = new ArrayList<ADBean>();
        for (ADBean selfad : selfadBeans) {//过滤掉已经安装了的app
            if (!PackageUtil.isInstallApp(context, selfad.getAd_packagename())) {
                ok_selfadBeans.add(selfad);
            }

        }
        if (size >= ok_selfadBeans.size()) {
            selfADs.addAll(ok_selfadBeans);
        } else {
            //建立一个size大的0-selfadBeans.size()之间不重复的list
            List<Integer> positionList = GetRandomList(size, ok_selfadBeans.size());
            for (int i : positionList) {
                selfADs.add(ok_selfadBeans.get(i));
            }
        }
        for (ADBean bean : selfADs) {
            Map<String, String> map_ekv = new HashMap<String, String>();
            map_ekv.put("show", bean.getAd_name());
            MobclickAgent.onEvent(context, event_id, map_ekv);
        }
        return selfADs;
    }

    public static ShowListBean adToShowListBean(ADBean adbean) {
        ShowListBean videoDataBean = new ShowListBean();
        videoDataBean.setAd_name(adbean.getAd_name());
        videoDataBean.setAd_description(adbean.getAd_description());
        videoDataBean.setAd_iconurl(adbean.getAd_iconurl());
        videoDataBean.setAd_thumbnail(adbean.getAd_thumbnail());
        videoDataBean.setAd_iconscal(adbean.getAd_iconscal());
        videoDataBean.setAd_thumbnailscal(adbean.getAd_thumbnailscal());
        videoDataBean.setAd_apkurl(adbean.getAd_apkurl());
        videoDataBean.setAd_packagename(adbean.getAd_packagename());
        videoDataBean.setAd_isConfirm(adbean.isAd_isConfirm());
        videoDataBean.setAd_type(adbean.getAd_type());
        videoDataBean.setAd_versioncode(adbean.getAd_versioncode());
        videoDataBean.setAd_platform(adbean.getAd_platform());
        return videoDataBean;
    }

    public static MainListBean adToShowListBean2(ADBean adbean) {
        MainListBean videoDataBean = new MainListBean();
        videoDataBean.setAd_name(adbean.getAd_name());
        videoDataBean.setAd_description(adbean.getAd_description());
        videoDataBean.setAd_iconurl(adbean.getAd_iconurl());
        videoDataBean.setAd_thumbnail(adbean.getAd_thumbnail());
        videoDataBean.setAd_iconscal(adbean.getAd_iconscal());
        videoDataBean.setAd_thumbnailscal(adbean.getAd_thumbnailscal());
        videoDataBean.setAd_apkurl(adbean.getAd_apkurl());
        videoDataBean.setAd_packagename(adbean.getAd_packagename());
        videoDataBean.setAd_isConfirm(adbean.isAd_isConfirm());
        videoDataBean.setAd_type(adbean.getAd_type());
        videoDataBean.setAd_versioncode(adbean.getAd_versioncode());
        videoDataBean.setAd_platform(adbean.getAd_platform());
        return videoDataBean;
    }

    public static SearchListBean adToSearchListBean(ADBean adbean) {
        SearchListBean videoDataBean = new SearchListBean();
        videoDataBean.setAd_name(adbean.getAd_name());
        videoDataBean.setAd_description(adbean.getAd_description());
        videoDataBean.setAd_iconurl(adbean.getAd_iconurl());
        videoDataBean.setAd_thumbnail(adbean.getAd_thumbnail());
        videoDataBean.setAd_iconscal(adbean.getAd_iconscal());
        videoDataBean.setAd_thumbnailscal(adbean.getAd_thumbnailscal());
        videoDataBean.setAd_apkurl(adbean.getAd_apkurl());
        videoDataBean.setAd_packagename(adbean.getAd_packagename());
        videoDataBean.setAd_isConfirm(adbean.isAd_isConfirm());
        videoDataBean.setAd_type(adbean.getAd_type());
        videoDataBean.setAd_versioncode(adbean.getAd_versioncode());
        videoDataBean.setAd_platform(adbean.getAd_platform());
        return videoDataBean;
    }

    /*
     * 将自定义广告随机插入到showbeans里面
     * */
    public static List<SearchListBean> getMixSearchListBeans(Context context, List<SearchListBean> historyBeans) {
        List<SearchListBean> retshowBeans = new ArrayList<SearchListBean>();
        if (!CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_SELFAD)) {
            return historyBeans;
        }
        int adCount = (int) (historyBeans.size() * AppConfig.decimal);
        if (adCount == 0 && historyBeans.size() != 0) {
            adCount = 1;//如果获取到了节目数据至少保证一条
        }
        List<ADBean> selfADs = GetSelfADByCount(context, adCount, "yuansheng_count");//随机取adcount条广告

        List<Integer> adIntegerList = GetRandomList(selfADs.size(), historyBeans.size() + selfADs.size());
        int showBeanIndex = 0, selfADIndex = 0;
        for (int i = 0; i < historyBeans.size() + selfADs.size(); i++) {
            if (adIntegerList.contains(i)) {

                retshowBeans.add(AppConfig.adToSearchListBean(selfADs.get(selfADIndex++)));
            } else {

                retshowBeans.add(historyBeans.get(showBeanIndex++));
            }


        }
        return retshowBeans;
    }

    /*
     * 将自定义广告随机插入到集合
     * */
    public static List<MainListBean> getAdMIXShowListBeans(Context context, List<MainListBean> historyBeans) {
        List<MainListBean> retshowBeans = new ArrayList<>();
        if (!CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_SELFAD)) {
            return historyBeans;
        }
        int adCount = (int) (historyBeans.size() * AppConfig.decimal);
        if (adCount == 0 && historyBeans.size() != 0) {
            adCount = 1;//如果获取到了节目数据至少保证一条
        }
        List<ADBean> selfADs = GetSelfADByCount(context, adCount, "yuansheng_count");//随机取adcount条广告

        List<Integer> adIntegerList = GetRandomList(selfADs.size(), historyBeans.size() + selfADs.size());
        int showBeanIndex = 0, selfADIndex = 0;
        for (int i = 0; i < historyBeans.size() + selfADs.size(); i++) {
            if (adIntegerList.contains(i)) {

                retshowBeans.add(AppConfig.adToShowListBean2(selfADs.get(selfADIndex++)));
            } else {

                retshowBeans.add(historyBeans.get(showBeanIndex++));
            }


        }
        return retshowBeans;
    }

    public static List<ShowListBean> getMIXShowListBeans(Context context, List<ShowListBean> historyBeans) {
        List<ShowListBean> retshowBeans = new ArrayList<ShowListBean>();
        if (!CacheUtils.getConfigBoolean(SysConfigEnum.AD_SHOW_SELFAD)) {
            return historyBeans;
        }
        int adCount = (int) (historyBeans.size() * AppConfig.decimal);
        if (adCount == 0 && historyBeans.size() != 0) {
            adCount = 1;//如果获取到了节目数据至少保证一条
        }
        List<ADBean> selfADs = GetSelfADByCount(context, adCount, "yuansheng_count");//随机取adcount条广告

        List<Integer> adIntegerList = GetRandomList(selfADs.size(), historyBeans.size() + selfADs.size());
        int showBeanIndex = 0, selfADIndex = 0;
        for (int i = 0; i < historyBeans.size() + selfADs.size(); i++) {
            if (adIntegerList.contains(i)) {

                retshowBeans.add(AppConfig.adToShowListBean(selfADs.get(selfADIndex++)));
            } else {

                retshowBeans.add(historyBeans.get(showBeanIndex++));
            }


        }
        return retshowBeans;
    }
//
//    /*
//         * 将自定义广告随机插入到showbeans里面
//         * */
//    public static List<HistoryBean> getMIXHistoryBeans(Context context, List<HistoryBean> historyBeans) {
//        if (!isShowSelfAD())
//            return historyBeans;
//        int adCount = (int) (historyBeans.size() * AppConfig.decimal);
//        if (adCount == 0 && historyBeans.size() != 0)
//            adCount = 1;//如果获取到了节目数据至少保证一条
//        List<ADBean> selfADs = GetSelfADByCount(context, adCount, "yuansheng_count");//随机取adcount条广告
//        List<HistoryBean> retshowBeans = new ArrayList<HistoryBean>();
//        List<Integer> adIntegerList = GetRandomList(selfADs.size(), historyBeans.size() + selfADs.size());
//        int showBeanIndex = 0, selfADIndex = 0;
//        for (int i = 0; i < historyBeans.size() + selfADs.size(); i++) {
//            if (adIntegerList.contains(i)) {
//
//                retshowBeans.add(adToHistoryBean(selfADs.get(selfADIndex++)));
//            } else {
//
//                retshowBeans.add(historyBeans.get(showBeanIndex++));
//            }
//
//
//        }
//        return retshowBeans;
//    }
//
//    /*
//      * 将自定义广告随机插入到showbeans里面
//      * */
//    public static List<DownloadInfo> getMIXDownloadListBeans(Context context, List<DownloadInfo> downloadInfos) {
//        if (!isShowSelfAD())
//            return downloadInfos;
//        int adCount = (int) (downloadInfos.size() * AppConfig.decimal);
//        if (adCount == 0 && downloadInfos.size() != 0)
//            adCount = 1;//如果获取到了节目数据至少保证一条
//        List<ADBean> selfADs = GetSelfADByCount(context, adCount, "yuansheng_count");//随机取adcount条广告
//        List<DownloadInfo> retshowBeans = new ArrayList<DownloadInfo>();
//        List<Integer> adIntegerList = GetRandomList(selfADs.size(), downloadInfos.size() + selfADs.size());
//        int showBeanIndex = 0, selfADIndex = 0;
//        for (int i = 0; i < downloadInfos.size() + selfADs.size(); i++) {
//            if (adIntegerList.contains(i)) {
//
//                retshowBeans.add(adToDownloadInfo(selfADs.get(selfADIndex++)));
//            } else {
//
//                retshowBeans.add(downloadInfos.get(showBeanIndex++));
//            }
//
//
//        }
//        return retshowBeans;
//    }
//
//    /*
//    * 将自定义广告随机插入到showbeans里面
//    * */
//    public static List<VideoDataListBean> getMIXVideoDataListBeans(Context context, List<VideoDataListBean> showBeans) {
//        if (!isShowSelfAD())
//            return showBeans;
//        int adCount = (int) (showBeans.size() * AppConfig.decimal);
//        if (adCount == 0 && showBeans.size() != 0)
//            adCount = 1;//如果获取到了节目数据至少保证一条
//        List<ADBean> selfADs = GetSelfADByCount(context, adCount, "yuansheng_count");//随机取adcount条广告
//        List<VideoDataListBean> retshowBeans = new ArrayList<VideoDataListBean>();
//        List<Integer> adIntegerList = GetRandomList(selfADs.size(), showBeans.size() + selfADs.size());
//        int showBeanIndex = 0, selfADIndex = 0;
//        for (int i = 0; i < showBeans.size() + selfADs.size(); i++) {
//            if (adIntegerList.contains(i)) {
//
//                retshowBeans.add(adToVideoDataListBean(selfADs.get(selfADIndex++)));
//            } else {
//
//                retshowBeans.add(showBeans.get(showBeanIndex++));
//            }
//
//
//        }
//        return retshowBeans;
//    }

//    public static ADBean toADBean(VideoDataListBean videoDataListBean) {
//
//        ADBean bean = new ADBean();
//        bean.setAd_name(videoDataListBean.getName());
//        bean.setAd_versioncode(videoDataListBean.getVersioncode());
//        bean.setAd_apkurl(videoDataListBean.getApkurl());
//        bean.setAd_packagename(videoDataListBean.getPackagename());
//        return bean;
//    }

    public static void openAD(final Context context, final ADBean adbean, String tag) {//如果本条是广告
        if (context == null || adbean == null) return;
        Map<String, String> map_ekv = new HashMap<String, String>();
        map_ekv.put("click", adbean.getAd_name());
        MobclickAgent.onEvent(context, tag, map_ekv);

        int type = adbean.getAd_type();
        if (type == 1)//下载
        {
            if (PackageUtil.isInstallApp(context, adbean.getAd_packagename()))//已经安装直接打开
            {
                PackageUtil.startApp(context, adbean.getAd_packagename());
                return;
            }
            if (adbean.isAd_isConfirm()) {
                new AlertDialog.Builder(context).setTitle("确定下载：" + adbean.getAd_name() + "?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("下载", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                if (DownLoaderAPK.getInstance(context).addDownload(adbean)) {
                                    Toast.makeText(context, "开始下载:" + adbean.getAd_name(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, adbean.getAd_name() + " 已经在下载了:", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                        }).show();
            } else {

                if (DownLoaderAPK.getInstance(context).addDownload(adbean)) {
                    Toast.makeText(context, "开始下载:" + adbean.getAd_name(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, adbean.getAd_name() + " 已经在下载了:", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (type == 2)//打开网页
        {
            if (adbean.getAd_apkurl().contains(".taobao.com"))//是淘宝链接
            {
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW"); //
                    String url = "";
                    if (adbean.getAd_apkurl().startsWith("http://")) {
                        url = adbean.getAd_apkurl().replaceFirst("http://", "taobao://");
                    } else {
                        url = adbean.getAd_apkurl().replaceFirst("https://", "taobao://");
                    }
                    Uri uri = Uri.parse(url);
                    intent.setData(uri);
                    context.startActivity(intent);
                } catch (Exception ex) {
                    PackageUtil.qidongLiulanqi((Activity) context, adbean.getAd_apkurl());
                }
            } else if (adbean.getAd_apkurl().contains("item.jd.com/"))//是淘宝链接
            {
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW"); //
                    int begin = adbean.getAd_apkurl().indexOf("item.jd.com/") + "item.jd.com/".length();
                    int end = adbean.getAd_apkurl().indexOf(".html");
                    String id = adbean.getAd_apkurl().substring(begin, end);
                    String url = "openapp.jdmobile://virtual?params=%7B%22sourceValue%22:%220_productDetail_97%22,%22des%22:%22productDetail%22,%22skuId%22:%22" + id + "%22,%22category%22:%22jump%22,%22sourceType%22:%22PCUBE_CHANNEL%22%7D";

                    Uri uri = Uri.parse(url);
                    intent.setData(uri);
                    context.startActivity(intent);
                } catch (Exception ex) {
                    PackageUtil.qidongLiulanqi((Activity) context, adbean.getAd_apkurl());
                }
            } else {

                PackageUtil.qidongLiulanqi((Activity) context, adbean.getAd_apkurl());
            }
        } else if (type == 3)//打开公众号
        {
            WXGZHBean wxgzhbean = new WXGZHBean();
            if (AppConfig.wxgzhBeans != null && AppConfig.wxgzhBeans.size() > 0) {
                wxgzhbean.type = AppConfig.wxgzhBeans.get(0).type;
            } else {
                wxgzhbean.type = "pengyouquan,pengyou,putong";
            }

            wxgzhbean.thumb = adbean.getAd_thumbnail();
            wxgzhbean.displayName = adbean.getAd_name();
            wxgzhbean.id = adbean.getAd_packagename();
            wxgzhbean.url = adbean.getAd_apkurl();
            wxgzhbean.introduction = adbean.getAd_description();
//            Intent intent = new Intent(context, GZHAddActivity.class);
//            intent.putExtra("wxgzhbean", wxgzhbean);
//            context.startActivity(intent);
        } else {
            PackageUtil.qidongLiulanqi((Activity) context, adbean.getAd_apkurl());
        }

    }

}
