package cc.aoeiuv020.vpnproxy.greendao.bean;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by silver on 2018/8/10.
 */

@Entity
public class EducationalConsultingList implements Comparable<EducationalConsultingList>{
    @Id
    Long id;
    String consultingClassification;//资讯分类
    String consultingTitle;//资讯标题    String time;//资讯时间
    String consultationDetals;//资讯详情

    @Generated(hash = 1045828029)
    public EducationalConsultingList(Long id, String consultingClassification,
            String consultingTitle, String consultationDetals) {
        this.id = id;
        this.consultingClassification = consultingClassification;
        this.consultingTitle = consultingTitle;
        this.consultationDetals = consultationDetals;
    }

    @Generated(hash = 1461719861)
    public EducationalConsultingList() {
    }

    @Override
    public int compareTo(@NonNull EducationalConsultingList educationalConsultingList) {
        return 0;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsultingClassification() {
        return this.consultingClassification;
    }

    public void setConsultingClassification(String consultingClassification) {
        this.consultingClassification = consultingClassification;
    }

    public String getConsultingTitle() {
        return this.consultingTitle;
    }

    public void setConsultingTitle(String consultingTitle) {
        this.consultingTitle = consultingTitle;
    }

    public String getConsultationDetals() {
        return this.consultationDetals;
    }

    public void setConsultationDetals(String consultationDetals) {
        this.consultationDetals = consultationDetals;
    }
}
