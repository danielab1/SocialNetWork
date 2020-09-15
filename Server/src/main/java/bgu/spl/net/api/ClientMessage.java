package bgu.spl.net.api;

import bgu.spl.net.Message;
import bgu.spl.net.api.bidi.Connections;

public abstract class ClientMessage implements Message {
    /**
     *This Class returns a new instance of Message that been send by the Client
     */

    private short opCode;
    public ClientMessage(short opCode){
        this.opCode = opCode;
    }

    public short getOpCode(){return opCode;}

    abstract public int getNumOfArguments();

    public void setArguments(String[] s){}

    abstract public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int connectionId);
}
