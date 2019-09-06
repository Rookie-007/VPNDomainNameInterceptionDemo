package cc.aoeiuv020.vpnproxy.greendao.ctrls;

import java.util.ArrayList;
import java.util.List;

import cc.aoeiuv020.vpnproxy.MyApplication;
import cc.aoeiuv020.vpnproxy.greendao.BlacklistOfWebsitesBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.WhiteListOfWebsitesBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.WhiteListOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.utils.LogUtils;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/13
 Time: 17:59
 */public class WhitelistOfWebsitesCtrls {

    //插入或替换黑名单
    public static void insterBlacklistOfWebsitesBean(String oldWebiterUrl,String websiteurl){
         WhiteListOfWebsitesBean bean = null;
         WhiteListOfWebsitesBeanDao dao = MyApplication.getInstance().getDaoSession().getWhiteListOfWebsitesBeanDao();
         List<WhiteListOfWebsitesBean> lists = dao.queryBuilder().where(WhiteListOfWebsitesBeanDao.Properties.WebsiteUrl.like(oldWebiterUrl)).list();
         if(lists == null || lists.size()<= 0){
             bean = new WhiteListOfWebsitesBean();
             bean.setWebsiteUrl(websiteurl);
         }else{
             bean  = lists.get(0);
             bean.setWebsiteUrl(websiteurl);
         }
         dao.insertOrReplace(bean);
     }

     //获取所有的网址数据黑名单
     public static List<WhiteListOfWebsitesBean> getAllBlacklistOfWebsitesBean(){
         List<WhiteListOfWebsitesBean> list = null;
         WhiteListOfWebsitesBeanDao dao = MyApplication.getInstance().getDaoSession().getWhiteListOfWebsitesBeanDao();
         list=dao.queryBuilder().build().list();

         if(list == null){
             list = new ArrayList<>();
         }
         return list;
     }

    //删除网站黑名单数据
    public static void deleteBlacklistOfWebsitesBean(String websiteurl){
        WhiteListOfWebsitesBean bean = null;
        WhiteListOfWebsitesBeanDao dao = MyApplication.getInstance().getDaoSession().getWhiteListOfWebsitesBeanDao();
        List<WhiteListOfWebsitesBean> lists = dao.queryBuilder().where(WhiteListOfWebsitesBeanDao.Properties.WebsiteUrl.like(websiteurl)).list();
        if(lists == null || lists.size()<= 0){
            bean = new WhiteListOfWebsitesBean();
        }else{
            bean = lists.get(0);
            LogUtils.e("删除白名单数据:"+bean.getWebsiteUrl());
        }
        dao.delete(bean);
    }
}
