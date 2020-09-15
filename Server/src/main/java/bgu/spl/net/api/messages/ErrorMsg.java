package bgu.spl.net.api.messages;

import bgu.spl.net.Message;
import bgu.spl.net.api.BytesShortConv;
import bgu.spl.net.api.ServerMessage;

public class ErrorMsg extends ServerMessage {

    private short msgOpcode;

    public ErrorMsg(Message msg){
        super((short) 11);
        this.msgOpcode = msg.getOpCode();
    }

    /**
     * @return A new byte Array of The Message encode
     */

    @Override
    public byte[] encode() {
        byte[] opCodeBytes = BytesShortConv.getInstance().shortToBytes(getOpCode());
        byte[] msgCodeBytes = BytesShortConv.getInstance().shortToBytes(msgOpcode);

        byte[] wholeMsg = new byte[opCodeBytes.length + msgCodeBytes.length];
        wholeMsg[0] = opCodeBytes[0];
        wholeMsg[1] = opCodeBytes[1];
        wholeMsg[2] = msgCodeBytes[0];
        wholeMsg[3] = msgCodeBytes[1];

        return wholeMsg;
    }
}
