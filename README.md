# navigation
一个提供调用原生地图的Cordova导航插件
# Installation
    cordova plugin add https://github.com/samCooker/mapnavigation.git
# Features
1、支持百度地图 or 高德地图  
2、支持android ，ios(暂时不支持)
# Example

Javascript API:  

      //导航设置
          var options = {
            originLat:,//起点纬度
            originLng:,//起点经度
            originName:,//起点名称（用于显示）
            destLat:,//目标经度
            destLng:,//目标纬度
            destName:,//目标名称（用于显示）
          };
      
      //调用百度地图 
        MapNavigation.navigationBaiduMap(options, function (msg) {
          tipMsg.alertMsg(msg);
        }, function (error) {
          tipMsg.alertMsg(error);
        });
      //调用高德地图
        MapNavigation.navigationMiniMap(options, function (msg) {
          tipMsg.alertMsg(msg);
        }, function (error) {
          tipMsg.alertMsg(error);
        });
      //先调用百度地图，若百度地图不存在，则调用高德地图
        MapNavigation.navigation(options, function (msg) {
          tipMsg.alertMsg(msg);
        }, function (error) {
          tipMsg.alertMsg(error);
        });
      

