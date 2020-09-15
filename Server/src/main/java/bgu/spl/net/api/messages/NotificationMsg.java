package bgu.spl.net.api.messages;

import bgu.spl.net.api.BytesShortConv;
import bgu.spl.net.api.ServerMessage;

import java.nio.charset.StandardCharsets;

public class NotificationMsg extends ServerMessage {
    private int noteType;
    private String postingUser;
    private String content;

    public NotificationMsg(int noteType, String postingUser, String content){
        super((short) 9);
        this.noteType = noteType;
        this.postingUser = postingUser;
        this.content = content;
    }
    /**
     * @return A new byte Array of The Message encode
     */

    public byte[] encode() {
        byte[] opCodeBytes = BytesShortConv.getInstance().shortToBytes(getOpCode());
        String msg = postingUser + '\0' + content + '\0';
        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);

        byte[] wholeMsg = new byte[opCodeBytes.length + 1 + msgBytes.length];
        wholeMsg[0] = opCodeBytes[0];
        wholeMsg[1] = opCodeBytes[1];
        wholeMsg[2] = (byte) noteType;
        for(int i=0; i<msgBytes.length; i++)
            wholeMsg[i+3] = msgBytes[i];

        return wholeMsg;
    }
}
