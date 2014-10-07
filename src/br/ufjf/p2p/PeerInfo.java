package br.ufjf.p2p;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeerInfo implements Serializable {

    private String ip;
    private int port;
    private List<String> files;

    public PeerInfo(String ip) {
        this(ip, 0);
    }

    public PeerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
        files = new ArrayList<String>();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public void addFile(String file) {
        this.files.add(file);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PeerInfo) {
            return ip.equals(((PeerInfo) obj).ip);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    @Override
    public String toString() {
        return "Peer: " + ip + "\nFiles: " + files;
    }
}
