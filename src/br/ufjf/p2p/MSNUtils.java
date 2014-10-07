package br.ufjf.p2p;

import java.util.List;


public class MSNUtils {

    public static String printArray(String[] array) {
        String msn = "";
        if (array != null && array.length > 0) {
            for (String file : array) {
                msn += file + ",";
            }
            msn = msn.substring(0, msn.length() - 1);

        }
        return msn;
    }

    public static String printList(List<String> list) {
        if (list != null) {
            return printArray(list.toArray(new String[0]));
        }
        return "";
    }

    public static String printPeerInfo(PeerInfo info) {
        return info.getIp() + ":" + info.getPort();
    }

}
