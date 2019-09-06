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
public class BlacklistOrAutomaticInterceptionNumberBean {
    @Id
    Long id;
    int automaticInterceptionNumber;//恶意拦截次数
    int blacklistNumber;//黑名单拦截次数
    @Generated(hash = 1922165154)
    public BlacklistOrAutomaticInterceptionNumberBean(Long id,
            int automaticInterceptionNumber, int blacklistNumber) {
        this.id = id;
        this.automaticInterceptionNumber = automaticInterceptionNumber;
        this.blacklistNumber = blacklistNumber;
    }
    @Generated(hash = 392038159)
    public BlacklistOrAutomaticInterceptionNumberBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getAutomaticInterceptionNumber() {
        return this.automaticInterceptionNumber;
    }
    public void setAutomaticInterceptionNumber(int automaticInterceptionNumber) {
        this.automaticInterceptionNumber = automaticInterceptionNumber;
    }
    public int getBlacklistNumber() {
        return this.blacklistNumber;
    }
    public void setBlacklistNumber(int blacklistNumber) {
        this.blacklistNumber = blacklistNumber;
    }



}