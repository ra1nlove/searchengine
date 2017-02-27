package com.buptnsrc.search.utils;


import com.buptnsrc.search.resource.Apk;
import com.buptnsrc.search.resource.Sites;
import net.sf.json.JSONObject;
import java.net.URL;

public class JsonUtils {


	public static String objectToJson(Object o){
		JSONObject jsonObj = JSONObject.fromObject(o);
		return jsonObj.toString();
	}

	
	/**
	 * generate the data in a certain format
	 * @param agentId
	 * @param channelId
	 * @param header
	 * @param status
	 * @param apk
	 * @return
	    agentId : agent_id
        channelId : channel_id
        header : header
        status : status // status 可能是running=3，done=4，也可能是failed=0
        details : {
            // 一个渠道中有n个应用，n=0
            appName : app_name
            appVersion : app_version
            appVenderName : app_vender_name
            appPackageName : app_package_name
            appType : app_type
            appSize : app_size		//String data = "{\"agentId\":\"" + agentId + "\",\"channelId\":\"" + channelId + "\",";
            appTsChannel : app_ts_channel // app上传时间
            osPlatform : os_platform // 适合的操作系统
            appDetailsUrl : app_details_url
            cookie : cookie // 一个应用一个cookie
            appDownloadUrl : app_download_url
        }
	 */
	public static String getSenderData(Apk apk) throws Exception{
		URL pageurl = new URL(apk.getAppMetaUrl().toLowerCase());
		String host = pageurl.getHost();
		String data = "{\"agentId\":\"searcher\",\"channelId\":\"" + Sites.channelId.get(host) + "\"," +"\"header\":\"\"," +"\"status\":\"4\"," + "\"details\":";
		String appsData = objectToJson(apk);
		appsData = appsData.replace("appType\":\"\"", "appType\":\"APK\"");
		data = data + appsData + "}";
		return data;
	}


}
