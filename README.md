# VpnProxy 

首页说一下过程是多么的千辛万苦，前期不知道什么是vpn代理，找了很多的相关博客， 去补充了解相关知识点
然后是....找各种博客写好的demo准备膜拜，说走咋就走...然后三天过去了，找下来的都是零星半点的demo，要么是直说原理，要么是写到一半，总之没有找到是一个可以用的成品，然后在询问同行情况下，终于在github上面找到了一个 下面附上链接地址：https://github.com/M66B/NetGuard

也不理想，不是我想要的，上面是拦截整个app的网络，但是我需要的是拦截指定的IP或域名，在通过N多次的百度

最终在gihub上面找到一个


https://img-blog.csdnimg.cn/20190903174402613.gif


好了废话不多说，先看修改后的效果：









自动拦截设置里面已把 www.baidu.com的网址，所以只要是匹配到响应的域名就会弹窗提示，还有黑名单里已经添加了www.360.cn域名，所以也会进行拦截提醒。

下面来说下原理：
首先是通过用户开启VPN代理权限，当开启了APP代理，所有的请求数据包都会传到我们APP。
VpnService这个就是我们继承的代理服务，并且实现了Runnable接口，主要用来轮询判断是否有数据包，需要进行处理对这个类对于VpnService没有印象的朋友请自行搜索了解！

看下继承VpnService的LocalVpnService类的准备：注意IP、TCP、UDP，DNS初始化都是以m_Packet字节数组对象创建。



我们先看下Runnable接口的 run() 方法:



能看到的是这部分，主要是进行初始化的操作，第221行添加了代理地址，第223行是进行了ip区域初始化，下面继续看到

第226行的runVPN() 方法：






这部分很有意思，请看242行我们拿到了代理，在往下看通过这个文件拿到输入流和输出流，然后开启了轮询

读取数据包里面的信息，大家注意m_Packet这个字节数据，上面其实创建LocalVpnService的时候已经看到了，读取并写入到流中，判断当前是否有数据包数据没有，有的情况下才行进处理，继续看262行onIPPacketReceived()方法



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void onIPPacketReceived(IPHeader ipHeader, int size) throws IOException {
        //解析数据包
        switch (ipHeader.getProtocol()) {
            case IPHeader.TCP:
                TCPHeader tcpHeader = m_TCPHeader;
                tcpHeader.m_Offset = ipHeader.getHeaderLength();
//                    LogUtils.e("mmm   pHeader.getSourceIP() ================" + ipHeader.getSourceIP());
                if (ipHeader.getSourceIP() == LOCAL_IP) {
                    if (tcpHeader.getSourcePort() == m_TcpProxyServer.Port) {
                        NatSession session = NatSessionManager.getSession(tcpHeader.getDestinationPort());
                        if (session.RemoteHost != null) {
                            boolean isInterception = NetworkInterceptionTool.isJudgingInterception(session.RemoteHost);
                            if (isInterception) {
                                LogUtils.e("eee ==============拦截网络=====================" + session.RemoteHost);
                                return;
                            }
                        }

                        LogUtils.e("eee session.RemoteHost======================" + session.RemoteHost);
                        if (session != null) {
                            ipHeader.setSourceIP(ipHeader.getDestinationIP());
                            tcpHeader.setSourcePort(session.RemotePort);
                            ipHeader.setDestinationIP(LOCAL_IP);

                            CommonMethods.ComputeTCPChecksum(ipHeader, tcpHeader);
                            //数据写入虚拟网卡
                            m_VPNOutputStream.write(ipHeader.m_Data, ipHeader.m_Offset, size);
                            m_ReceivedBytes += size;
                        } else {
                            if (ProxyConfig.IS_DEBUG) {
                                Log.d(Constant.TAG, "NoSession: " +
                                        ipHeader.toString() + " " +
                                        tcpHeader.toString());
                            }
                        }
                    } else {
                        int portKey = tcpHeader.getSourcePort();
                        NatSession session = NatSessionManager.getSession(portKey);
                        if (session == null || session.RemoteIP != ipHeader.getDestinationIP() || session.RemotePort != tcpHeader.getDestinationPort()) {
                            session = NatSessionManager.createSession(portKey, ipHeader.getDestinationIP(), tcpHeader.getDestinationPort());
                        }

                        session.LastNanoTime = System.nanoTime();
                        session.PacketSent++;

                        int tcpDataSize = ipHeader.getDataLength() - tcpHeader.getHeaderLength();
                        if (session.PacketSent == 2 && tcpDataSize == 0) {
                            return;
                        }
                        if (session.BytesSent == 0 && tcpDataSize > 10) {
                            int dataOffset = tcpHeader.m_Offset + tcpHeader.getHeaderLength();
                            String host = HttpHostHeaderParser.parseHost(tcpHeader.m_Data, dataOffset, tcpDataSize);
                            LogUtils.e("eee host======================" + host);
                            if (host != null) {
                                session.RemoteHost = host;
                                boolean isInterception = NetworkInterceptionTool.isJudgingInterception(host);
                                if (isInterception) {
                                    LogUtils.e("eee ==============拦截网络=====================" + host);
                                    return;
                                }
                            }
                        }

                        ipHeader.setSourceIP(ipHeader.getDestinationIP());
                        ipHeader.setDestinationIP(LOCAL_IP);
                        tcpHeader.setDestinationPort(m_TcpProxyServer.Port);

                        CommonMethods.ComputeTCPChecksum(ipHeader, tcpHeader);
                        //数据写入虚拟网卡
                        m_VPNOutputStream.write(ipHeader.m_Data, ipHeader.m_Offset, size);
                        session.BytesSent += tcpDataSize;
                        m_SentBytes += size;
                    }
                }
                break;
            case IPHeader.UDP:
                UDPHeader udpHeader = m_UDPHeader;
                udpHeader.m_Offset = ipHeader.getHeaderLength();
                if (ipHeader.getSourceIP() == LOCAL_IP && udpHeader.getDestinationPort() == 53) {
                    m_DNSBuffer.clear();
                    m_DNSBuffer.limit(ipHeader.getDataLength() - 8);
                    DnsPacket dnsPacket = DnsPacket.FromBytes(m_DNSBuffer);
                    LogUtils.e("eee dnsPacket.getQuestions()[0]======================" + dnsPacket.getQuestions()[0].Domain);
                    boolean isInterception = NetworkInterceptionTool.isJudgingInterception(dnsPacket.getQuestions()[0].Domain);
                    if (isInterception) {
                        LogUtils.e("eee ==============拦截网络=====================" + dnsPacket.getQuestions()[0].Domain);
                        return;
                    }
                    if (dnsPacket != null && dnsPacket.Header.QuestionCount > 0) {
                        //设置参数发送数据包
                        m_DnsProxy.onDnsRequestReceived(ipHeader, udpHeader, dnsPacket);
                    }
                }
                break;
        }
    }

从上面可以看出是判断数据包发送类型：分别为TCP、UDP两种。两种根据数据类型，进行不一样的解析方式，两种方式都是通m_Packer字节数组解析数据包，从而得到发送数据包请求头，在从中拿到发送的目标IP地址或是域名。两种方式发送的数据方式也不一样,TCP是通过m_VPNOutputStream输入流写入到文件流，而UDP则是通过包装类DatagramSocket发送数据。好了，大致的介绍也差不多了，这个就是VPN开启拿到数据包，在进行转发的流程。

在说下数据拦截也是在上面，解析出域名的时候进行判断，因为已经代理了VPN，不代码往下走转发其实就是不进行数据包就是拦截，NetworkInterceptionTool.isJudgingInterception()方法：进行匹配黑名单/自动拦截恶意网址存在相同即进行拦截，否否则不进行处理。

在补充一下：因为是需要弹窗给用户提醒，然而我们LocalVpnService又是单线程轮询，所以弹窗时进行了线程阻塞，当用户操作了停止访问或是继续访问亦或者是十秒后则，进行线程释放。如果不进行释放可能会导致死锁。



参考源码地址：https://github.com/AoEiuV020/VpnProxy


通过用户开启VPN代理权限，当开启了APP代理，所有的请求数据包都会传到我们APP。
VpnService这个就是我们继承的代理服务，并且实现了Runnable接口，主要用来轮询判断是否有数据包，
需要进行处理对这个类对于VpnService没有印象的朋友请自行搜索了解！
具体一点的请看博客吧! https://blog.csdn.net/Rookie_or_beginner/article/details/100524020
 
