package cc.aoeiuv020.vpnproxy.core;

public class NatSession {
    public int RemoteIP;
    public short RemotePort;
    public String RemoteHost;
    public int BytesSent;
    public int PacketSent;
    public long LastNanoTime;


    @Override
    public String toString() {
        return "NatSession{" +
                "RemoteIP=" + RemoteIP +
                ", RemotePort=" + RemotePort +
                ", RemoteHost='" + RemoteHost + '\'' +
                ", BytesSent=" + BytesSent +
                ", PacketSent=" + PacketSent +
                ", LastNanoTime=" + LastNanoTime +
                '}';
    }
}
