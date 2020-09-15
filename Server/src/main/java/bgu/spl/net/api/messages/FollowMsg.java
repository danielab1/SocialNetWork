package bgu.spl.net.api.messages;

import bgu.spl.net.Message;
import bgu.spl.net.api.*;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

public class FollowMsg extends ClientMessage {

    private int numOfUsers;
    private int follow;
    private String[] usersNameList;

    public FollowMsg(int follow, int numOfUsers){
        super((short) 4);
        this.follow = follow;
        this.numOfUsers = numOfUsers;
        usersNameList= new String[numOfUsers];
    }

    public FollowMsg(byte[] bytes){
        super((short) 4);
        this.follow = bytes[0];
        byte[] numOfUsersbytes = {bytes[1],bytes[2]};
        this.numOfUsers = BytesShortConv.getInstance().bytesToShort(numOfUsersbytes);
    }

    @Override
    public int getNumOfArguments() {
        return numOfUsers;
    }

    @Override
    public void setArguments(String[] s) {
        usersNameList = s;
    }

    /** Process The follow Msg:
     * checks if the user that is tagged is register to the system by the function followOrUnfollow
     */

    @Override
    public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int conId) {
        Message res = new ErrorMsg(this);

        short numOfUsers = 0;
        String namesList = "";
        User user;
        if ((user = db.getActiveUser(conId)) != null){
            for (String s : usersNameList) {
                User userToDo = db.getRegisteredUser(s);
                if (userToDo != null && user.followOrUnfollow(follow, userToDo)) {
                    numOfUsers++;
                    namesList += s + " ";
                }
            }

            if (numOfUsers > 0) {
                res = new AckMsg(this, namesList);
                ((AckMsg)res).addShorts(new short[]{numOfUsers});
            }
        }

        connections.send(conId, res);
    }
}