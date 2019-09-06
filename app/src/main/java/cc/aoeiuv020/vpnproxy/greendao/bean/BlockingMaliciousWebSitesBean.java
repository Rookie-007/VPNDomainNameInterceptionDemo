package cc.aoeiuv020.vpnproxy.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class BlockingMaliciousWebSitesBean {
    @Id
    Long id;
    String websiteUrl;
    @Generated(hash = 1628980322)
    public BlockingMaliciousWebSitesBean(Long id, String websiteUrl) {
        this.id = id;
        this.websiteUrl = websiteUrl;
    }
    @Generated(hash = 2097277433)
    public BlockingMaliciousWebSitesBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getWebsiteUrl() {
        return this.websiteUrl;
    }
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }




}