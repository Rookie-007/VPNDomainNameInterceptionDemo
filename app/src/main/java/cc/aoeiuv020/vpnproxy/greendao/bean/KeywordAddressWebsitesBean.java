package cc.aoeiuv020.vpnproxy.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class KeywordAddressWebsitesBean {
    @Id
    Long id;
    String websiteUrl;
    @Generated(hash = 376098423)
    public KeywordAddressWebsitesBean(Long id, String websiteUrl) {
        this.id = id;
        this.websiteUrl = websiteUrl;
    }
    @Generated(hash = 1024425925)
    public KeywordAddressWebsitesBean() {
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