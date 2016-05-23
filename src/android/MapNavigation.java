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
 * Created by Cookie on 2016/5/18.
 */
public class MapNavigation extends CordovaPlugin{

    @Override
    public boolean execute(String action, JSONArray jsonArray, CallbackContext callbackContext) throws JSONException {
        //导航
        if (action.equals("navigate")){
            return this.navigate4Both(jsonArray,callbackContext);
        }
        //使用百度地图导航
        if (action.equals("navigationBaiduMap")){
            return this.navigate4Baidu(jsonArray, callbackContext);
        }
        //使用高德地图导航
        if (action.equals("navigationMiniMap")){
            return this.navigate4Mini(jsonArray,callbackContext);
        }
        return false;
    }

    /**
     * 使用高德地图导航
     * @param jsonArray
     * @param callbackContext
     * @return
     */
    private boolean navigate4Mini(JSONArray jsonArray, CallbackContext callbackContext) {
        try {
            Activity activity = this.cordova.getActivity();
            //导航选项
            Options options = new Options().fromJson(jsonArray);
            Intent intent = null;
            if(appInstalled("com.autonavi.minimap")){
                //如果有高德地图 uri详情：http://lbs.amap.com/api/uri-api/
                intent = Intent.parseUri("androidamap://route?sourceApplication=softname" +options.getOrigin4Mini()+options.getDestination4Mini()+options.getModel4Mini(),0);
                activity.startActivities(new Intent[]{intent});
                callbackContext.success("成功进行导航。");
            }else {
                callbackContext.error("您还没有安装高德地图。");
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            callbackContext.error(e.toString());
        }
        return false;
    }

    /**
     * 使用百度地图导航
     * @param jsonArray
     * @param callbackContext
     * @return
     */
    private boolean navigate4Baidu(JSONArray jsonArray, CallbackContext callbackContext) {
        try {
            Activity activity = this.cordova.getActivity();
            //导航选项
            Options options = new Options().fromJson(jsonArray);
            Intent intent = null;
            if(appInstalled("com.baidu.BaiduMap")){
                //如果有百度地图 uri详情：http://lbsyun.baidu.com/index.php?title=uri/api/android
                intent = Intent.parseUri("intent://map/direction?"
                        + "origin="+options.getOrigin4Baidu()
                        + "&destination="+options.getDestination4Baidu()
                        + "&mode="+options.getModel4Baidu()
                        + "&coord_type=gcj02&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end",0);
                activity.startActivity(intent);
                callbackContext.success("成功进行导航。");
            } else {
                callbackContext.error("您还没有安装百度地图。");
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            callbackContext.error(e.toString());
        }
        return false;
    }

    /**
     * 使用百度地图或高德地图导航
     * @param jsonArray
     * @param callbackContext
     * @return
     */
    private boolean navigate4Both(JSONArray jsonArray, CallbackContext callbackContext) {
        Activity activity = this.cordova.getActivity();
        //导航选项
        Options options = new Options().fromJson(jsonArray);
        try {
            Intent intent = null;
            if(appInstalled("com.baidu.BaiduMap")){
                //如果有百度地图 uri详情：http://lbsyun.baidu.com/index.php?title=uri/api/android
                intent = Intent.parseUri("intent://map/direction?"
                        + "origin="+options.getOrigin4Baidu()
                        + "&destination="+options.getDestination4Baidu()
                        + "&mode="+options.getModel4Baidu()
                        + "&coord_type=gcj02&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end",0);
                activity.startActivity(intent);
                callbackContext.success("成功进行导航。");
            }else if(appInstalled("com.autonavi.minimap")){
                //如果有高德地图 uri详情：http://lbs.amap.com/api/uri-api/
                intent = Intent.parseUri("androidamap://route?sourceApplication=softname" +options.getOrigin4Mini()+options.getDestination4Mini()+options.getModel4Mini(),0);
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
        return false;
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

    /**
     * 配置数据实体
     */
    private final class Options {

        private String originLat;
        private String originLng;
        private String originName;
        private String destLat;
        private String destLng;
        private String destName;
        //transit、driving、walking，分别表示公交、驾车和步行
        private String model;

        private String src;

        public Options() {

        }

        public Options fromJson(JSONArray data){
            JSONObject options = data.optJSONObject(0);
            this.originLat = options.optString("originLat","");
            this.originLng = options.optString("originLng","");
            this.originName = options.optString("originName","");

            this.destLat = options.optString("destLat","");
            this.destLng = options.optString("destLng","");
            this.destName = options.optString("destName","");
            //默认步行
            this.model = options.optString("model");

            this.src = options.optString("src","");

            return this;
        }
        //百度地图获取出发地信息
        public String getOrigin4Baidu(){
            if(this.isNotBlank(this.originName)&&this.isNotBlank(this.originLat)&&this.isNotBlank(this.originLng)){
                return "latlng:"+this.originLat+","+this.originLng+"|name:"+this.originName;
            }else if(this.isNotBlank(this.originName)){
                return "name:"+this.originName;
            }else if(this.isNotBlank(this.originLat)&&this.isNotBlank(this.originLng)){
                return "latlng:"+this.originLat+","+this.originLng;
            }
            return "";
        }
        //百度地图获取目的地信息
        public String getDestination4Baidu(){
            if(this.isNotBlank(this.destName)&&this.isNotBlank(this.destLat)&&this.isNotBlank(this.destLng)){
                return "latlng:"+this.destLat+","+this.destLng+"|name:"+this.destName;
            }else if(this.isNotBlank(this.destName)){
                return "name:"+this.destName;
            }else if(this.isNotBlank(this.destLat)&&this.isNotBlank(this.destLng)){
                return "latlng:"+this.destLat+","+this.destLng;
            }
            return "";
        }
        public String getModel4Baidu(){
            if(!this.isNotBlank(this.model)){
              this.model="walking";//默认步行
            }
            return this.model;
        }

        //高德地图获取出发地信息
        public String getOrigin4Mini(){
            if(this.isNotBlank(this.originName)&&this.isNotBlank(this.originLat)&&this.isNotBlank(this.originLng)){
                return "&slat="+this.originLat+"&slon="+this.originLng+"&sname="+this.originName;
            }else if(this.isNotBlank(this.originLat)&&this.isNotBlank(this.originLng)){
                return "&slat="+this.originLat+"&slon="+this.originLng;
            }
            return "";
        }
        //高德地图获取目的地信息
        public String getDestination4Mini(){
            if(this.isNotBlank(this.destName)&&this.isNotBlank(this.destLat)&&this.isNotBlank(this.destLng)){
                return "&dlat="+this.destLat+"&dlon="+this.destLng+"&dname="+this.destName;
            }else if(this.isNotBlank(this.destLat)&&this.isNotBlank(this.destLng)){
                return "&dlat="+this.destLat+"&dlon="+this.destLng;
            }
            return "";
        }

        public String getModel4Mini(){
            if(!this.isNotBlank(this.model)){
              this.model="4";//默认步行
            }
            return "&dev=0&t="+this.model+"&m=0";
        }

        public boolean isNotBlank(String s){
            return s!=null&&!"".equals(s.trim());
        }
    }

}
