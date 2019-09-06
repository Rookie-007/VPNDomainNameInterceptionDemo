package cc.aoeiuv020.vpnproxy.bean;

/*
 Created by Little Q on date
 Page:
 Notes: 教育资讯实体类
 Date: 2019/8/19
 Time: 10:57
 */
public class EducationalInformationBean {

    private String title;//标题
    private String imageUrl;//图片
    private String time;//时间
    private String source;//来源
    private String detailsUrl;//详情


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}
