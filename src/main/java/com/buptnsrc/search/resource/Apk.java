package com.buptnsrc.search.resource;

import com.buptnsrc.search.utils.StringTool;

import java.net.URL;
import java.util.List;

/**
 * Created by rain on 17-2-23.
 */
public class Apk {

    private String appName;
    private String appMetaUrl;
    private String appDownloadUrl;
    private String osPlatform ;
    private String appVersion;
    private String appSize;
    private String appTsChannel;// app上传时间
    private String appType;
    private String cookie;
    private String appVenderName=null;
    private String appPackageName = null;
    private String appDownloadTimes = null;
    private String appDescription = null;
    private String appCommentUrl = null;
    private String appComment = null;
    private List<String> appScreenshot = null;
    private String appTag = null;//应用标签
    private String appCategory = null;//应用类别

    public Apk(String appName,String appMetaUrl,String appDownloadUrl,String osPlatform ,
               String appVersion,String appSize,String appTsChannel, String appType,String cookie){
        init();
        create(appName,appMetaUrl, appDownloadUrl, osPlatform,appVersion, appSize,appTsChannel, appType,cookie);
    }

    private void init(){
        //this.appCrawlTime = String.valueOf(System.currentTimeMillis());
    }

    public void create(String appName,String appMetaUrl,String appDownloadUrl,String osPlatform ,
                       String appVersion,String appSize,String appTsChannel, String appType,String cookie){
        if(appName != null)
            this.appName = appName.trim().replaceAll("[\n\r]", "");
        if(appVersion != null)
            this.appVersion = appVersion.toLowerCase().replaceAll("[v\\s]", "");
        if(appType != null)
            this.appType = appType.replaceAll("\\s", "").toUpperCase();
        if(appTsChannel != null)
            this.appTsChannel = appTsChannel.trim().replace("年", "-").replace("月", "-").replaceAll("[\n\r]", "");
        if(osPlatform != null)
            this.osPlatform = osPlatform.replaceAll("\\s", "");
        if(appDownloadUrl != null )
            this.appDownloadUrl = appDownloadUrl.replaceAll("\\s", "");
        if(appSize != null){
            this.appSize = appSize.toUpperCase().replaceAll("\\s", "");
            if(!this.appSize.endsWith("B")){
                this.appSize = this.appSize+"B";
            }
        }
        this.cookie = cookie;
        if(appMetaUrl != null)
            this.appMetaUrl = appMetaUrl.replaceAll("\\s", "");
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        if(appName != null)
            this.appName = appName.trim().replaceAll("[\n\r]", "");
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        if(appVersion != null)
            this.appVersion = appVersion.toLowerCase().replaceAll("[v\\s]", "");
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        if(appType != null )
            this.appType = appType.replaceAll("\\s", "").toUpperCase();
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        if(appSize != null && appSize.length()>1){
            this.appSize = appSize.toUpperCase().replaceAll("\\s", "");
            if(!this.appSize.endsWith("B")){
                this.appSize = this.appSize+"B";
            }
        }
    }

    public String getAppTsChannel() {
        return appTsChannel;
    }

    public void setAppTsChannel(String appTsChannel) {
        if(appTsChannel != null)
            this.appTsChannel = appTsChannel.trim().replace("年", "-").replace("月", "-");
    }

    public String getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(String osPlatform) {
        if(osPlatform != null)
            this.osPlatform = osPlatform.replaceAll("\\s", "");
    }

    public String getAppMetaUrl() {
        return appMetaUrl;
    }

    public void setAppMetaUrl(String appMetaUrl) {
        if(appMetaUrl != null)
            this.appMetaUrl = appMetaUrl.replaceAll("\\s", "");
    }

    public String getAppDownloadUrl() {
        return appDownloadUrl;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        if(appDownloadUrl != null )
            this.appDownloadUrl = appDownloadUrl.replaceAll("\\s","");
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getAppVenderName() {
        return appVenderName;
    }

    public void setAppVenderName(String appVenderName) {
        if(appVenderName != null)
            this.appVenderName = appVenderName.replaceAll("\\s","");
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        if(appPackageName != null)
            this.appPackageName = appPackageName.replaceAll("\\s","");
    }

    public String getAppDownloadTimes() {
        return appDownloadTimes;
    }

    public void setAppDownloadTimes(String appDownloadedTime) {
        if(appDownloadedTime != null)
            this.appDownloadTimes = appDownloadedTime.replace("次", "").replaceAll("\\s","");
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        if(appDescription != null)
            this.appDescription = appDescription.replaceAll("[\r\n]", "").replaceAll("\\s+", " ");
    }

    public String getAppCommentUrl() {
        return appCommentUrl;
    }

    public void setAppCommentUrl(String appCommentUrl) {
        if(appCommentUrl != null)
            this.appCommentUrl = appCommentUrl.replaceAll("\\s","");
    }

    public String getAppComment() {
        return appComment;
    }

    public void setAppComment(String appComment) {
        if(appComment != null)
            this.appComment = appComment.replaceAll("[\n\r]", "");
    }

    public List<String> getAppScreenshot() {
        return appScreenshot;
    }

    public void setAppScreenshot(List<String> appScreenshot) {
        this.appScreenshot = appScreenshot;
    }

    public String getAppTag() {
        return appTag;
    }

    public void setAppTag(String appTag) {
        if(appTag != null)
            this.appTag = appTag.trim().replaceAll("[\r\n]", "");;
    }

    public String getAppCategory() {
        return appCategory;
    }

    public void setAppCategory(String appCategory) {
        if(appCategory != null)
            this.appCategory = appCategory.trim().replaceAll("[\r\n]", "");
    }

    public String getApkHash() throws Exception{
        URL url = new URL(appMetaUrl);
        String host = url.getHost();
        String infoStr = appSize+"APK"+Sites.channelId.get(host)+appVersion+appName+appDownloadUrl;
        if(infoStr != null){
            return StringTool.getHash(infoStr);
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("name:" + 		this.appName+"\n");
        strBuilder.append("metaUrl:" + 		this.getAppMetaUrl()+"\n");
        strBuilder.append("osPlatform:" + 	this.getOsPlatform()+"\n");
        strBuilder.append("download url:" + this.getAppDownloadUrl()+"\n");
        strBuilder.append("Category:" + 	this.getAppCategory()+"\n");
        strBuilder.append("DownloadTimes:" +this.getAppDownloadTimes()+"\n");
        strBuilder.append("Description:" + 	this.getAppDescription()+"\n");
        strBuilder.append("Size:" + 		this.getAppSize()+"\n");
        strBuilder.append("Tag:" + 			this.getAppTag()+"\n");
        strBuilder.append("TsChannel:" + 	this.getAppTsChannel()+"\n");
        strBuilder.append("Type:" + 		this.getAppType()+"\n");
        strBuilder.append("VenderName:" + 	this.getAppVenderName()+"\n");
        strBuilder.append("Version:" + 		this.getAppVersion()+"\n");
        strBuilder.append("Screenshot:" + 	this.getAppScreenshot()+"\n");
        strBuilder.append("comment:" + 		this.getAppComment()+"\n");

        return strBuilder.toString();
    }
    @Override
    public boolean equals(Object o){
        Apk apk = (Apk)o;
        return (this.appName.equals(apk.appName) && this.appDownloadUrl.equals(apk.appDownloadUrl));
    }

}