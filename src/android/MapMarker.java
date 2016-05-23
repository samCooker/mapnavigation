package cn.com.chaochuang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shicx on 2016/5/22.
 */
public class MapMarker extends CordovaPlugin{

    @Override
    public boolean execute(String action, JSONArray jsonArray, CallbackContext callbackContext) throws JSONException {
        Activity activity = this.cordova.getActivity();
        //导航选项
        Options options = new Options().fromJson(jsonArray);
        if ("mapMarker".equals(action)){
            try {
                if(appInstalled("com.baidu.BaiduMap")){
                    //如果有百度地图 uri详情：http://lbsyun.baidu.com/index.php?title=uri/api/android
                    Intent intent = Intent.parseUri(options.getUri4Baidu(),0);
                    activity.startActivity(intent);
                    callbackContext.success(options.getUri4Baidu());
                }else if(appInstalled("com.autonavi.minimap")){
                    //如果有高德地图 uri详情：http://lbs.amap.com/api/uri-api/
                    Intent intent = Intent.parseUri("",0);
                    activity.startActivities(new Intent[]{intent});
                    callbackContext.success("成功进行导航。");
                }else {
                    callbackContext.error("您还没有安装百度地图和高德地图。");
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
                callbackContext.error(e.toString());
            }
        }
        return false;
    }

    public final class Options{
        private String lat;
        private String lng;
        private String title;
        private String content;
        private String coordType;
        private String src;

        public Options() {
        }

        public Options fromJson(JSONArray data){
            JSONObject options = data.optJSONObject(0);
            this.lat = options.optString("lat","");
            this.lng = options.optString("lng","");
            this.title = options.optString("title","");
            this.content = options.optString("content","");
            this.coordType = options.optString("coordType","");

            this.src = options.optString("src","");

            return this;
        }

        /**
         * 返回百度标记uri
         * @return
         */
        public String getUri4Baidu(){
            return "intent://map/marker?location="+this.lat+","+this.lng+"&title="+this.title+"&content="+this.content+"&src="+this.src+"#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
        }
    }

    /**
     * 检查app是否已经安装
     * @param uri
     * @return
     */
    public boolean appInstalled(String uri) {
        Context ctx = this.cordova.getActivity().getApplicationContext();
        final PackageManager pm = ctx.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch(PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
