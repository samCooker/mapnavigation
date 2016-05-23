/********* Navigation.h Cordova Plugin Header *******/

    #import <Cordova/CDV.h>
    @interface Navigation : CDVPlugin

    - (void)do:(CDVInvokedUrlCommand*)command;

    @end

    /********* Navigation.m Cordova Plugin Implementation *******/

    #import <Cordova/CDV.h>

    @implementation Navigation

    - (void)do:(CDVInvokedUrlCommand*)command
    {
        CDVPluginResult* pluginResult = nil;
        NSString* echo = [command.arguments objectAtIndex:0];
        
        if (echo != nil && [echo length] > 0) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
        } else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }
	
        if ([[UIApplication sharedApplication]canOpenURL:[NSURL URLWithString:@"baidumap://map/"]]){
            NSString *urlString = [[NSString stringWithFormat:@"baidumap://map/direction?origin=我的位置&destination=%@&mode=driving",echo]
                                   stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            
            [[UIApplication sharedApplication]openURL:[NSURL URLWithString:urlString]];
            
        }else if ([[UIApplication sharedApplication]canOpenURL:[NSURL URLWithString:@"iosamap://"]])
        {
             //NSString *urlString = [[NSString stringWithFormat:@"iosamap://navi?sourceApplication=applicationName&backScheme=applicationScheme&poiname=大梅沙海滨公园&poiid=BGVIS&lat=22.597811&lon=114.001424&dev=1&style=2"]
            [self failWithCallbackID:command.callbackId withMessage:@"未安装高德地图,无法导航!"];
            return ;
        }else
        {
            [self failWithCallbackID:command.callbackId withMessage:@"未安装百度地图或高德地图,无法导航!"];
            return ;
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }

    - (void)failWithCallbackID:(NSString *)callbackID withMessage:(NSString *)message
    {
        CDVPluginResult *commandResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
        [self.commandDelegate sendPluginResult:commandResult callbackId:callbackID];
    }
    @end
