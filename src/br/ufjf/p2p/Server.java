package br.ufjf.p2p;

import java.io.IOException;

public class Server {

    public static void main(String[] args) {
        MasterPeer server = new MasterPeer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
