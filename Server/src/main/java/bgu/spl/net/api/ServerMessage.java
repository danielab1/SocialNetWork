package bgu.spl.net.api;

import bgu.spl.net.Message;

public abstract class ServerMessage implements Message {
    /**
     *This Class returns a new instance of Message that been send by the Server
     */

    private short opCode;

    public short getOpCode(){return opCode;}

    public ServerMessage(short opCode){
        this.opCode = opCode;
    }

    public abstract byte[] encode();
}
