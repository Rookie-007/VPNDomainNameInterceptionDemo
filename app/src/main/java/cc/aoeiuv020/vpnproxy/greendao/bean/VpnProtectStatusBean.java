package cc.aoeiuv020.vpnproxy.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/*
 Created by Little Q on date
 Page:
 Notes:
 Date: 2019/8/14
 Time: 15:12
*/

@Entity
public class VpnProtectStatusBean {
    @Id
    Long id;
    int vpnProtectsSatus;//防护状态
    String vpnProtectsSatusPormpt;//提示
    @Generated(hash = 734603268)
    public VpnProtectStatusBean(Long id, int vpnProtectsSatus,
            String vpnProtectsSatusPormpt) {
        this.id = id;
        this.vpnProtectsSatus = vpnProtectsSatus;
        this.vpnProtectsSatusPormpt = vpnProtectsSatusPormpt;
    }
    @Generated(hash = 819354755)
    public VpnProtectStatusBean() {
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