package br.ufjf.p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MasterPeer {

    public static final String CONNECT_ACTION = "connect";
    public static final String ADD_FILES_ACTION = "add_files";
    public static final String SEARCH_FILE_ACTION = "search_file";

    public static int PORT = 5000;

    private List<PeerInfo> peers;

    public MasterPeer() {
        peers = new ArrayList<PeerInfo>();
    }

    public List<PeerInfo> getPeers() {
        return peers;
    }

    public void setPeers(List<PeerInfo> peers) {
        this.peers = peers;
    }

    public void addPeer(PeerInfo peer) {
        this.peers.add(peer);
    }

    public void start() throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        while (true) {
            final Socket client = server.accept();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(client.getInputStream()));
                        String line = in.readLine();
                        String[] data = line.split("=");
                        String action = data[0];
                        String msn = data[1];

                        if (CONNECT_ACTION.equals(action)) {
                            connectPeer(msn);
                        } else if (ADD_FILES_ACTION.equals(action)) {
                            addFiles(msn);
                        } else if (SEARCH_FILE_ACTION.equals(action)) {
                            List<String> ips = searchFile(msn);
                            String result = "";
                            if (!ips.isEmpty()) {
                                for (String ip : ips) {
                                    result += ip + ",";
                                }
                                result = result.substring(0,
                                        result.length() - 1);
                            } else {
                                result = "empty";
                            }
                            PrintWriter out = new PrintWriter(client
                                    .getOutputStream());
                            out.println("{" + result + "}");
                            out.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void connectPeer(String msn) {
        String[] addr = msn.split(":");
        String ip = addr[0];
        int port = Integer.parseInt(addr[1]);
        PeerInfo info = new PeerInfo(ip, port);
        if (!peers.contains(info)) {
            peers.add(info);
            System.out.println("Added peer: " + MSNUtils.printPeerInfo(info));
        }
    }

    private void addFiles(String msn) {
        String ip = msn.substring(0, msn.indexOf("{"));
        String data = msn.substring(msn.indexOf("{") + 1, msn.indexOf("}"));
        String[] files = data.split(",");

        int index = peers.indexOf(new PeerInfo(ip));
        if (index != -1) {
            PeerInfo info = peers.get(index);
            info.setFiles(Arrays.asList(files));
            System.out.println("Added files [" + MSNUtils.printArray(files)
                    + "] to peer: " + MSNUtils.printPeerInfo(info));
        }
    }

    private List<String> searchFile(String file) {
        List<String> candidates = new ArrayList<String>();
        for (PeerInfo info : peers) {
            List<String> files = info.getFiles();
            if (files.contains(file)) {
                candidates.add(MSNUtils.printPeerInfo(info));
            }
        }
        return candidates;
    }
}
