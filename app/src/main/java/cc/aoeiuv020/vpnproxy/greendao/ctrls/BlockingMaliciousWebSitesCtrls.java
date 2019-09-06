package cc.aoeiuv020.vpnproxy.greendao.ctrls;

import java.util.ArrayList;
import java.util.List;

import cc.aoeiuv020.vpnproxy.MyApplication;
import cc.aoeiuv020.vpnproxy.greendao.BlacklistOfWebsitesBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.BlockingMaliciousWebSitesBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlockingMaliciousWebSitesBean;
import cc.aoeiuv020.vpnproxy.utils.LogUtils;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/13
 Time: 17:59
 */public class BlockingMaliciousWebSitesCtrls {

    //插入或替换黑名单
     public static void insterBlacklistOfWebsitesBean(String websiteurl){
         BlockingMaliciousWebSitesBean bean = null;
         BlockingMaliciousWebSitesBeanDao dao = MyApplication.getInstance().getDaoSession().getBlockingMaliciousWebSitesBeanDao();
         List<BlockingMaliciousWebSitesBean> lists = dao.queryBuilder().where(BlockingMaliciousWebSitesBeanDao.Properties.WebsiteUrl.like(websiteurl)).list();
         if(lists == null || lists.size()<= 0){
             bean = new BlockingMaliciousWebSitesBean();
             bean.setWebsiteUrl(websiteurl);
         }else{
             bean  = lists.get(0);
             bean.setWebsiteUrl(websiteurl);
         }
         dao.insertOrReplace(bean);
     }

     //获取所有的网址数据黑名单
     public static List<BlockingMaliciousWebSitesBean> getAllBlacklistOfWebsitesBean(){
         List<BlockingMaliciousWebSitesBean> list = null;
         BlockingMaliciousWebSitesBeanDao dao = MyApplication.getInstance().getDaoSession().getBlockingMaliciousWebSitesBeanDao();
         list=dao.queryBuilder().build().list();

         if(list == null){
             list = new ArrayList<>();
         }
         return list;
     }

    //删除网站黑名单数据
    public static void deleteBlacklistOfWebsitesBean(String websiteurl){
        BlockingMaliciousWebSitesBean bean = null;
        BlockingMaliciousWebSitesBeanDao dao = MyApplication.getInstance().getDaoSession().getBlockingMaliciousWebSitesBeanDao();
        List<BlockingMaliciousWebSitesBean> lists = dao.queryBuilder().where(BlacklistOfWebsitesBeanDao.Properties.WebsiteUrl.like(websiteurl)).list();
        if(lists == null || lists.size()<= 0){
            bean = new BlockingMaliciousWebSitesBean();
        }else{
            bean = lists.get(0);
            LogUtils.e("删除恶意网址数据:"+bean.getWebsiteUrl());
        }
        dao.delete(bean);
    }
}
