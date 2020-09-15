package bgu.spl.net.api;
import bgu.spl.net.Message;
import bgu.spl.net.api.messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ActionsEncDec implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10];
    private boolean readArguments = false;
    private int len = 0;
    private short op = 0;
    private int zeros = -1;
    private ClientMessage ms;

    @Override
    public Message decodeNextByte(byte nextByte) {
        pushByte(nextByte);

        if(!readArguments && op == 0 && len == 2){
            op = BytesShortConv.getInstance().bytesToShort(bytes);
            popString();
        }

        if(!readArguments && op != 0 && evaluateSettings(op)) {
            ms = (ClientMessage)createNewMessage(op);
            zeros = ms.getNumOfArguments();
            readArguments = true;
        } else if(nextByte == '\0' && op != 0 && readArguments)
            zeros--;

        if(zeros == 0 && op != 0){
            String[] list = (popString().split("\0"));
            ms.setArguments(list);
            readArguments = false;
            op = 0;
            zeros = -1;
            return ms;
        }

        return null; //not a message yet
    }

    @Override
    public byte[] encode(Message message) {
        return ((ServerMessage)message).encode();
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private boolean evaluateSettings(int op){
        switch(op){
            case 1: return (len == 0);
            case 2: return (len == 0);
            case 3: return (len == 0);
            case 4: return (len == 3);
            case 5: return (len == 0);
            case 6: return (len == 0);
            case 7: return (len == 0);
            case 8: return (len == 0);
        }
        return false;
    }

    private Message createNewMessage(int op){
        Message ms = null;
        switch(op){
            case 1: ms = new RegisterMsg();
                break;
            case 2: ms = new LoginMsg();
                break;
            case 3: ms = new LogOutMsg();
                break;
            case 4: ms = new FollowMsg(bytes);
                break;
            case 5: ms = new PostMsg();
                break;
            case 6: ms = new PmMsg();
                break;
            case 7: ms = new UserListMsg();
                break;
            case 8: ms = new StatMsg();
                break;
        }
        popString();
        return ms;
    }

}
