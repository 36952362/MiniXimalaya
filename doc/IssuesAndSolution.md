#常见问题及解决

#[首页](./../README.md)

#1. Android高版本联网失败报错

为保证用户数据和设备的安全，Google针对下一代 Android 系统(Android P) 的应用程序，将要求默认使用加密连接，这意味着 Android P 将禁止 App 使用所有未加密的连接，因此运行 Android P 系统的安卓设备无论是接收或者发送流量，未来都不能明码传输，需要使用下一代(Transport Layer Security)传输层安全协议。

解决方案有以下几种方式:

##1.1 APP使用https方式发送请求
这就要求自己的项目工程能够修改请求方式，如果使用的第三方库并且不支持https,或者服务器方不支持https方式，这种方式就无效

##1.2. targetSdkVersion降到27以下
如果这样，有些NDK的新特性就不能使用 

##1.3. 更改网络安全配置

###1.3.1 在res文件夹下创建一个xml文件夹，然后创建一个network_security_config.xml文件

	<?xml version="1.0" encoding="utf-8"?>
	<network-security-config>
	    <base-config cleartextTrafficPermitted="true" />
	</network-security-config>
###1.3.2 修改清单文件

	<application
	...
	 android:networkSecurityConfig="@xml/network_security_config"
	...
	 />
