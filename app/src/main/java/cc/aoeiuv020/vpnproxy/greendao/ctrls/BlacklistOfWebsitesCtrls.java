package cc.aoeiuv020.vpnproxy.greendao.ctrls;

import java.util.ArrayList;
import java.util.List;

import cc.aoeiuv020.vpnproxy.MyApplication;
import cc.aoeiuv020.vpnproxy.greendao.BlacklistOfWebsitesBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.utils.LogUtils;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/13
 Time: 17:59
 */public class BlacklistOfWebsitesCtrls {

    //插入或替换黑名单
     public static void insterBlacklistOfWebsitesBean(String oldWebiterUrl,String websiteurl){
         BlacklistOfWebsitesBean bean = null;
         BlacklistOfWebsitesBeanDao dao = MyApplication.getInstance().getDaoSession().getBlacklistOfWebsitesBeanDao();
         List<BlacklistOfWebsitesBean> lists = dao.queryBuilder().where(BlacklistOfWebsitesBeanDao.Properties.WebsiteUrl.like(oldWebiterUrl)).list();
         if(lists == null || lists.size()<= 0){
             bean = new BlacklistOfWebsitesBean();
             bean.setWebsiteUrl(websiteurl);
         }else{
             bean  = lists.get(0);
             bean.setWebsiteUrl(websiteurl);
         }
         dao.insertOrReplace(bean);
     }

     //获取所有的网址数据黑名单
     public static List<BlacklistOfWebsitesBean> getAllBlacklistOfWebsitesBean(){
         List<BlacklistOfWebsitesBean> list = null;
         BlacklistOfWebsitesBeanDao dao = MyApplication.getInstance().getDaoSession().getBlacklistOfWebsitesBeanDao();
         list=dao.queryBuilder().build().list();

         if(list == null){
             list = new ArrayList<>();
         }
         return list;
     }

    //删除网站黑名单数据
    public static void deleteBlacklistOfWebsitesBean(String websiteurl){
        BlacklistOfWebsitesBean bean = null;
        BlacklistOfWebsitesBeanDao dao = MyApplication.getInstance().getDaoSession().getBlacklistOfWebsitesBeanDao();
        List<BlacklistOfWebsitesBean> lists = dao.queryBuilder().where(BlacklistOfWebsitesBeanDao.Properties.WebsiteUrl.like(websiteurl)).list();
        if(lists == null || lists.size()<= 0){
            bean = new BlacklistOfWebsitesBean();
        }else{
            bean = lists.get(0);
            LogUtils.e("删除黑名单数据:"+bean.getWebsiteUrl());
        }
        dao.delete(bean);
    }
}
