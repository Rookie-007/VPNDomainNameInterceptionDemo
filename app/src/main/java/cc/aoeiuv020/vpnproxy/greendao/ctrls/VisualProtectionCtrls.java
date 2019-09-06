package cc.aoeiuv020.vpnproxy.greendao.ctrls;

import java.util.List;

import cc.aoeiuv020.vpnproxy.MyApplication;
import cc.aoeiuv020.vpnproxy.greendao.VisualProtectionBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.VpnProtectStatusBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.bean.VisualProtectionBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.VpnProtectStatusBean;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/14
 Time: 16:07
 */
public class VisualProtectionCtrls {

    //设置打开状态
    public static void setVisualProtectionOpenStatus(boolean isOpen){
        VisualProtectionBean bean = null;
        VisualProtectionBeanDao dao = MyApplication.getInstance().getDaoSession().getVisualProtectionBeanDao();
        List<VisualProtectionBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
            bean = new VisualProtectionBean();
            bean.setIsOpen(isOpen);
        }else{
            bean  = lists.get(0);
            bean.setIsOpen(isOpen);
        }
        dao.insertOrReplace(bean);
    }

    //设置使用时长
    public static void setUseTime(String userTime){
        VisualProtectionBean bean = null;
        VisualProtectionBeanDao dao = MyApplication.getInstance().getDaoSession().getVisualProtectionBeanDao();
        List<VisualProtectionBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
            bean = new VisualProtectionBean();
            bean.setUseTime(userTime);
        }else{
            bean  = lists.get(0);
            bean.setUseTime(userTime);
        }
        dao.insertOrReplace(bean);
    }

    //设置锁屏时长
    public static void setLckScreenLength(String lckScreenLength){
        VisualProtectionBean bean = null;
        VisualProtectionBeanDao dao = MyApplication.getInstance().getDaoSession().getVisualProtectionBeanDao();
        List<VisualProtectionBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
            bean = new VisualProtectionBean();
            bean.setLockScreenLength(lckScreenLength);
        }else{
            bean  = lists.get(0);
            bean.setLockScreenLength(lckScreenLength);
        }
        dao.insertOrReplace(bean);
    }


    //获取视力保护实体
    public static VisualProtectionBean getVisualProtection(){
        VisualProtectionBeanDao dao = MyApplication.getInstance().getDaoSession().getVisualProtectionBeanDao();
        List<VisualProtectionBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
            return null;
        }
        return lists.get(0);
    }


}
