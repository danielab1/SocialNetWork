package bgu.spl.net.api;

import bgu.spl.net.Message;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

public class BGSProtocol implements BidiMessagingProtocol<Message> {
    private boolean shouldTerminate;
    private int connectionId;
    private Connections<Message> connections;
    private Database db;

    public BGSProtocol(Database db){
        this.db = db;
    }

    public Database getDb(){
        return db;
    }


    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
        shouldTerminate = false;
    }

    @Override
    public void process(Message msg) {
        ((ClientMessage)msg).process(this, connections, db, connectionId);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public Connections<Message> getConnections() { return connections; }
}
