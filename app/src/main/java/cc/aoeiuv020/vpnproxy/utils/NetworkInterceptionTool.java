package cc.aoeiuv020.vpnproxy.utils;

import android.app.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.aoeiuv020.vpnproxy.MyApplication;
import cc.aoeiuv020.vpnproxy.core.LocalVpnService;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlockingMaliciousWebSitesBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.InterceptMaliciousStatusBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.KeywordAddressWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.VpnProtectStatusBean;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.BlacklistOfWebsitesCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.BlacklistOrAutomaticInterceptionNumberCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.BlockingMaliciousWebSitesCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.InterceptMaliciousStatusCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.KeywordAddressWebsitesCtrls;
import cc.aoeiuv020.vpnproxy.greendao.ctrls.VpnProtectStatusCtrls;

import static cc.aoeiuv020.vpnproxy.utils.ConstantUtils.FLIP_WINDOW;
import static cc.aoeiuv020.vpnproxy.utils.ConstantUtils.FLIP_WINDOW_BLACK_OR_INTERCEPT;

/*
 Created by Little Q on date
 Page:
 Notes:网络拦截工具类
 Date: 2019/8/19
 Time: 20:38
 */
public class NetworkInterceptionTool {

    public static boolean isInterceptionTool = false;
    public static boolean isFirst = true;
    public static boolean isInterceptionFirst = true;
    /**
     * 判断拦截  以百度为例：www.baidu.com
     */
    public static boolean isJudgingInterception(String domainName){
        boolean isInterception = false;

        //判断拦截是否打开
        VpnProtectStatusBean vpnProtectStatus = VpnProtectStatusCtrls.getVpnProtectStatus();
        if(vpnProtectStatus != null && 0 == vpnProtectStatus.getVpnProtectsSatus()){//如果关闭则不进行拦截处理
           return isInterception;
        }

//        if(!HasDigit(domainName)){
//            domainName = domainName.substring(domainName.indexOf(".")+1);
//        }

        LogUtils.e("domainName==============="+domainName);

        //网站黑白名单的拦截
        List<BlacklistOfWebsitesBean> blacklistOfWebsitesBeans = BlacklistOfWebsitesCtrls.getAllBlacklistOfWebsitesBean();
        for(BlacklistOfWebsitesBean bean:blacklistOfWebsitesBeans){
            if(domainName.contains(getHost(bean.getWebsiteUrl()))){//判断是包含
                BlacklistOrAutomaticInterceptionNumberCtrls.addBlacklistOne();
                isInterception = true;

                if(isInterceptionFirst && CustomDialog.getInstance().getmView() == null){
                    isInterceptionFirst = false;
                    //去跳转页弹窗
                    MyApplication.getActivity().sendEmptyMessageDelayed(FLIP_WINDOW_BLACK_OR_INTERCEPT,500);
                }
            }
        }

        //判断是否开启状态
        InterceptMaliciousStatusBean interceptMaliciousStatusBean = InterceptMaliciousStatusCtrls.getVpnProtectStatus();
        if(interceptMaliciousStatusBean != null && 1 == interceptMaliciousStatusBean.getVpnProtectsSatus()){
            //自动拦截恶意网址
            List<BlockingMaliciousWebSitesBean> blockingMaliciousWebSitesBeans = BlockingMaliciousWebSitesCtrls.getAllBlacklistOfWebsitesBean();
            for(BlockingMaliciousWebSitesBean bean:blockingMaliciousWebSitesBeans){
                if(domainName.contains(getHost(bean.getWebsiteUrl()))){//判断是包含
                        if(isFirst && CustomDialog.getInstance().getmView() == null){
                            isFirst = false;
                            //去跳转页弹窗
                            MyApplication.getActivity().sendEmptyMessageDelayed(FLIP_WINDOW,500);
                            try {
                                synchronized (LocalVpnService.Instance){
                                    //阻塞，等待用户操作
                                    LocalVpnService.Instance.wait();
                                }
//                            LocalVpnService.isBlock = true;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    //默认拦截，
                    if(!isInterceptionTool){
                        return false;
                    }

                    BlacklistOrAutomaticInterceptionNumberCtrls.addAutomaticInterceptionOne();
                    isInterception = true;
                }
            }
        }

        //关键字过滤
        List<KeywordAddressWebsitesBean> keywordAddressWebsitesBeans =  KeywordAddressWebsitesCtrls.getAllBlacklistOfWebsitesBean();
        for(KeywordAddressWebsitesBean bean:keywordAddressWebsitesBeans){
            if(domainName.contains(getHost(bean.getWebsiteUrl()))){//判断是包含
                isInterception = true;
            }
        }

        return isInterception;

    }

    // 判断一个字符串是否含有数字
    public static boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }


    private static String getHost(String host){


        if(!HasDigit(host)){
            host = host.substring(host.indexOf(".")+1);
        }

        if(host.contains("www.")){
            host = host.replaceAll("www.","");
        }


        LogUtils.e("domainName2==============="+host);
        return host;
    }

}
