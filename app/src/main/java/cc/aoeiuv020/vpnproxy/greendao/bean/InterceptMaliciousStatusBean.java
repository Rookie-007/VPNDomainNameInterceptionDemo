package cc.aoeiuv020.vpnproxy.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/14
 Time: 15:12
*/

@Entity
public class InterceptMaliciousStatusBean {
    @Id
    Long id;
    int vpnProtectsSatus;//防护状态
    String vpnProtectsSatusPormpt;//提示
    @Generated(hash = 1928483334)
    public InterceptMaliciousStatusBean(Long id, int vpnProtectsSatus,
            String vpnProtectsSatusPormpt) {
        this.id = id;
        this.vpnProtectsSatus = vpnProtectsSatus;
        this.vpnProtectsSatusPormpt = vpnProtectsSatusPormpt;
    }
    @Generated(hash = 1050236172)
    public InterceptMaliciousStatusBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getVpnProtectsSatus() {
        return this.vpnProtectsSatus;
    }
    public void setVpnProtectsSatus(int vpnProtectsSatus) {
        this.vpnProtectsSatus = vpnProtectsSatus;
    }
    public String getVpnProtectsSatusPormpt() {
        return this.vpnProtectsSatusPormpt;
    }
    public void setVpnProtectsSatusPormpt(String vpnProtectsSatusPormpt) {
        this.vpnProtectsSatusPormpt = vpnProtectsSatusPormpt;
    }

}