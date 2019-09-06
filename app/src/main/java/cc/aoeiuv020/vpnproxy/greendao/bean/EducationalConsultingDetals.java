package cc.aoeiuv020.vpnproxy.greendao.bean;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by silver on 2018/8/10.
 */

@Entity
public class EducationalConsultingDetals implements Comparable<EducationalConsultingDetals>{
    @Id
    Long id;
    String consultingClassification;//资讯分类
    String consultingTitle;//资讯标题
    String time;//资讯时间

    @Generated(hash = 166247017)
    public EducationalConsultingDetals(Long id, String consultingClassification,
            String consultingTitle, String time) {
        this.id = id;
        this.consultingClassification = consultingClassification;
        this.consultingTitle = consultingTitle;
        this.time = time;
    }

    @Generated(hash = 2094361195)
    public EducationalConsultingDetals() {
    }

    @Override
    public int compareTo(@NonNull EducationalConsultingDetals educationalConsultingDetals) {
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

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
