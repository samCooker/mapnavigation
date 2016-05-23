var exec = require('cordova/exec');

//定义导航对象 与 方法
var MapNavigation = {
  navigation:function(key, successCallback, errorCallback) {
    	exec(successCallback, errorCallback, 'MapNavigation', 'navigate', [key])
	},
  //调用百度地图 仅android
  navigationBaiduMap:function(key, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'MapNavigation', 'navigationBaiduMap', [key])
  },
  //调用高德地图 仅android
  navigationMiniMap:function(key, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'MapNavigation', 'navigationMiniMap', [key])
  },
  //调用百度地图 仅android
  markerBaiduMap:function(key, successCallback, errorCallback) {
    exec(successCallback, errorCallback, 'MapMarker', 'mapMarker', [key])
  }

};

module.exports= MapNavigation;

