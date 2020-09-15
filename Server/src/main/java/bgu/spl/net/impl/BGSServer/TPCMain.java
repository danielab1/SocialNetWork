package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.ActionsEncDec;
import bgu.spl.net.api.BGSProtocol;
import bgu.spl.net.api.Database;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args){
        Database db = new Database();
        int port = Integer.parseInt(args[0]);
        Server s = Server.threadPerClient(port,()->new BGSProtocol(db),()->new ActionsEncDec());
        s.serve();
    }
}

