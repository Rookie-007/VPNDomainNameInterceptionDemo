package cc.aoeiuv020.vpnproxy.greendao.ctrls;

import java.util.List;

import cc.aoeiuv020.vpnproxy.MyApplication;
import cc.aoeiuv020.vpnproxy.greendao.InterceptMaliciousStatusBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.VpnProtectStatusBeanDao;
import cc.aoeiuv020.vpnproxy.greendao.bean.InterceptMaliciousStatusBean;
import cc.aoeiuv020.vpnproxy.greendao.bean.VpnProtectStatusBean;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/14
 Time: 16:07
 */
public class InterceptMaliciousStatusCtrls {

    //插入或替换Vpn防护状态
    public static void insterInterceptMaliciousStatus(int vpnStatus,String vpnProtectsSatusPormpt){
        InterceptMaliciousStatusBean bean = null;
        InterceptMaliciousStatusBeanDao dao = MyApplication.getInstance().getDaoSession().getInterceptMaliciousStatusBeanDao();
        List<InterceptMaliciousStatusBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
            bean = new InterceptMaliciousStatusBean();
            bean.setVpnProtectsSatus(vpnStatus);
            bean.setVpnProtectsSatusPormpt(vpnProtectsSatusPormpt);
        }else{
            bean  = lists.get(0);
            bean.setVpnProtectsSatus(vpnStatus);
            bean.setVpnProtectsSatusPormpt(vpnProtectsSatusPormpt);
        }
        dao.insertOrReplace(bean);
    }

    //获取防护Vpn开启状态
    public static InterceptMaliciousStatusBean getVpnProtectStatus(){
        InterceptMaliciousStatusBeanDao dao = MyApplication.getInstance().getDaoSession().getInterceptMaliciousStatusBeanDao();
        List<InterceptMaliciousStatusBean> lists = dao.queryBuilder().build().list();
        if(lists == null || lists.size()<= 0){
            return null;
        }
        return lists.get(0);
    }


}
