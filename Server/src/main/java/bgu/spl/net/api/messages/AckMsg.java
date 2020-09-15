package bgu.spl.net.api.messages;
import bgu.spl.net.Message;
import bgu.spl.net.api.BytesShortConv;
import bgu.spl.net.api.ServerMessage;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class AckMsg extends ServerMessage {

    private short msgOpcode;
    private List<Short> shortsList;
    private String namesList;
    private Message msg;


    public AckMsg(Message msg, String namesList){
        super((short) 10);
        this.msgOpcode = msg.getOpCode();
        this.shortsList = new LinkedList<>();
        this.msg = msg;
        this.namesList = namesList;
    }

    public AckMsg(Message msg){
        super((short) 10);
        this.msgOpcode = msg.getOpCode();
        this.shortsList = new LinkedList<>();
        this.msg = msg;
        this.namesList = "";
    }

    /**
     * @return A new byte Array of The Message encode
     */

    @Override
    public byte[] encode() {
        byte[] opCodeBytes = BytesShortConv.getInstance().shortToBytes(getOpCode());
        byte[] msgCodeBytes = BytesShortConv.getInstance().shortToBytes(msgOpcode);
        int namesLen = namesList.getBytes(StandardCharsets.UTF_8).length;

        byte[] wholeMsg = new byte[opCodeBytes.length + msgCodeBytes.length+  (shortsList.size()*2) + namesLen];

        wholeMsg[0] = opCodeBytes[0];
        wholeMsg[1] = opCodeBytes[1];
        wholeMsg[2] = msgCodeBytes[0];
        wholeMsg[3] = msgCodeBytes[1];

        for(int i = 0; i < shortsList.size(); i++) {
            byte[] arr = BytesShortConv.getInstance().shortToBytes(shortsList.get(i));
            wholeMsg[(i*2)+4] = arr[0];
            wholeMsg[(i*2)+5] = arr[1];
        }

        if(namesLen > 0){
            String[] namesListArray = namesList.split(" ");
            int lastInd = wholeMsg.length - namesLen;
            for(int i = 0; i<namesListArray.length; i++){
                lastInd = addName(wholeMsg, lastInd, namesListArray[i]);
            }
        }

        return wholeMsg;
    }
    /**
     * Add all the Nums that The Message Contains
     */

    public void addShorts(short[] nums){
        for(int i = 0; i<nums.length; i++)
            shortsList.add(nums[i]);
    }
    /**
     * Add all the UsersList that The Message Contains
     * @return num of the index where The last friend was add
     */

    private int addName(byte[] wholeMsg, int lastInd, String name){
        byte[] nameAsBytes = name.getBytes(StandardCharsets.UTF_8);
        for(int i=0; i<nameAsBytes.length; i++)
            wholeMsg[i+lastInd] = nameAsBytes[i];

        wholeMsg[lastInd + nameAsBytes.length] = '\0';
        return lastInd + nameAsBytes.length + 1;
    }

}
