# VpnProxy 
通过用户开启VPN代理权限，当开启了APP代理，所有的请求数据包都会传到我们APP。
VpnService这个就是我们继承的代理服务，并且实现了Runnable接口，主要用来轮询判断是否有数据包，
需要进行处理对这个类对于VpnService没有印象的朋友请自行搜索了解！
具体一点的请看博客吧! https://blog.csdn.net/Rookie_or_beginner/article/details/100524020
 
