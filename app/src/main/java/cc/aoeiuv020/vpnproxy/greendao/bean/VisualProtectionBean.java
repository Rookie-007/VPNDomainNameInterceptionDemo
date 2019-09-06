package cc.aoeiuv020.vpnproxy.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class VisualProtectionBean {
    @Id
    Long id;
    String useTime;//使用时长
    String lockScreenLength;//锁屏时长
    boolean isOpen;//状态
    @Generated(hash = 1821750841)
    public VisualProtectionBean(Long id, String useTime, String lockScreenLength,
            boolean isOpen) {
        this.id = id;
        this.useTime = useTime;
        this.lockScreenLength = lockScreenLength;
        this.isOpen = isOpen;
    }
    @Generated(hash = 1787938113)
    public VisualProtectionBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUseTime() {
        return this.useTime;
    }
    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
    public String getLockScreenLength() {
        return this.lockScreenLength;
    }
    public void setLockScreenLength(String lockScreenLength) {
        this.lockScreenLength = lockScreenLength;
    }
    public boolean getIsOpen() {
        return this.isOpen;
    }
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }


}