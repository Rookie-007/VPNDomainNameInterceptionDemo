package cc.aoeiuv020.vpnproxy.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BlacklistOfWebsitesBean {
    @Id
    Long id;
    String websiteUrl;

    @Generated(hash = 448151876)
    public BlacklistOfWebsitesBean(Long id, String websiteUrl) {
        this.id = id;
        this.websiteUrl = websiteUrl;
    }


    @Generated(hash = 1282416515)
    public BlacklistOfWebsitesBean() {
    }



    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}