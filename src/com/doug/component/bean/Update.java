package com.doug.component.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description:
 * @author: Liu wei
 * @mail: i@liuwei.co
 * @date: 2014-3-12
 */
public class Update {

    protected int versionCode;//版本号

    protected String versionName;//版本名称

    protected String downloadURL;//下载地址

    protected String versionDesc;//版本描述
    
    protected boolean forceUpdate = false;//版本描述
    
    public Update() {
	}

	public Update(JSONObject o) throws JSONException {
        super();
        //产品信息部分
        versionCode = o.getInt("androidVersion");
        versionName = o.getString("androidVersionName");
        downloadURL = o.getString("androidDownLink");
        versionDesc = o.getString("androidVersionDes");
        try {
			forceUpdate = "true".equals(o.getString("force"));
		} catch (Exception e) {
			forceUpdate = false;
			e.printStackTrace();
		}
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }
    
	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

    @Override
    public String toString() {
        return "Update [versionCode=" + versionCode + ", versionName="
                + versionName + ", downloadURL=" + downloadURL
                + ", versionDesc=" + versionDesc + "force=" + forceUpdate + "]";
    }




}
