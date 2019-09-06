package cc.aoeiuv020.vpnproxy.greendao.ctrls;

import java.util.ArrayList;
import java.util.List;

import cc.aoeiuv020.vpnproxy.MyApplication;
import cc.aoeiuv020.vpnproxy.greendao.BlacklistOfWebsitesBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.BlacklistOrAutomaticInterceptionNumberBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOfWebsitesBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.BlacklistOrAutomaticInterceptionNumberBean;
import cc.aoeiuv020.vpnproxy.utils.LogUtils;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/13
 Time: 17:59
 */public class BlacklistOrAutomaticInterceptionNumberCtrls {

    //黑名单++
     public static void addBlacklistOne(){
         BlacklistOrAutomaticInterceptionNumberBean bean = null;
         BlacklistOrAutomaticInterceptionNumberBeanDao dao = MyApplication.getInstance().getDaoSession().getBlacklistOrAutomaticInterceptionNumberBeanDao();
         List<BlacklistOrAutomaticInterceptionNumberBean> lists = dao.queryBuilder().build().list();
         if(lists == null || lists.size()<= 0){
             bean = new BlacklistOrAutomaticInterceptionNumberBean();
             bean.setBlacklistNumber(1);
         }else{
             bean  = lists.get(0);
           int size =  bean.getBlacklistNumber();
           size++;
           bean.setBlacklistNumber(size);
         }
         dao.insertOrReplace(bean);
     }

    //自动拦截数据++
    public static void addAutomaticInterceptionOne(){
        BlacklistOrAutomaticInterceptionNumberBean bean = null;
        BlacklistOrAutomaticInterceptionNumberBeanDao dao = MyApplication.getInstance().getDaoSession().getBlacklistOrAutomaticInterceptionNumberBeanDao();
        List<BlacklistOrAutomaticInterceptionNumberBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
            bean = new BlacklistOrAutomaticInterceptionNumberBean();
            bean.setAutomaticInterceptionNumber(1);
        }else{
            bean  = lists.get(0);
            int size =  bean.getAutomaticInterceptionNumber();
            size++;
            bean.setAutomaticInterceptionNumber(size);
        }
        dao.insertOrReplace(bean);
    }

    //获取自动拦截次数
    public static int getAutomaticInterceptionOne(){
         int size = 0;
        BlacklistOrAutomaticInterceptionNumberBean bean = null;
        BlacklistOrAutomaticInterceptionNumberBeanDao dao = MyApplication.getInstance().getDaoSession().getBlacklistOrAutomaticInterceptionNumberBeanDao();
        List<BlacklistOrAutomaticInterceptionNumberBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
           return size;
        }else{
            bean  = lists.get(0);
           size =  bean.getAutomaticInterceptionNumber();
        }
        return size;
    }

    //获取黑名单拦截次数
    public static int getBlacklistNumber(){
         int size = 0;
        BlacklistOrAutomaticInterceptionNumberBean bean = null;
        BlacklistOrAutomaticInterceptionNumberBeanDao dao = MyApplication.getInstance().getDaoSession().getBlacklistOrAutomaticInterceptionNumberBeanDao();
        List<BlacklistOrAutomaticInterceptionNumberBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
            return  size;
        }else{
            bean  = lists.get(0);
           size =  bean.getBlacklistNumber();
        }
       return  size;
    }


}
