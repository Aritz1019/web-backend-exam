package domain.advertisment.model;

import java.util.Locale;


public class Advertisment {

    private int advertismentId = 0;
    private String title = null;
    private String url = null;
    private String src = null;
    private Locale local = null;

    public int getAdvertismentId() {
        return advertismentId;
    }
    public Locale getLocale() {
        return local;
    }
    public void setLocale(Locale locale) {
        this.local = locale;
    }
    public String getSrc() {
        return src;
    }
    public void setSrc(String src) {
        this.src = src;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAdvertismentId(int advertismentId) {
        this.advertismentId = advertismentId;
    }
    
}
