//
// Created by sagl@wincs.cs.bgu.ac.il on 1/2/19.
//

#include <Message.h>
#include "../include/ReaderWriter.h"

Writer::Writer(ConnectionHandler& handler):handler(){
    this->handler = &handler;
}

void Writer::run() {

    while(1){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);

        Message* ms = new Message(line);
        std::string mOp = ms->getNextArgument();
        short opCode = 0;

        if(mOp == "REGISTER")
            opCode = 1;
        else if(mOp == "LOGIN")
            opCode = 2;
        else if(mOp == "LOGOUT")
            opCode = 3;
        else if(mOp == "FOLLOW")
            opCode = 4;
        else if(mOp == "POST")
            opCode = 5;
        else if(mOp == "PM")
            opCode = 6;
        else if(mOp == "USERLIST")
            opCode = 7;
        else if(mOp == "STAT")
            opCode = 8;

        //SENDING OPCODE
        char opCodeBytes[2];
        Message::shortToBytes(opCode, opCodeBytes);
        handler->sendBytes(opCodeBytes, 2);

        //HANDLING REST OF THE MESSAGE
        if(opCode == 3 && handler->getLoggedIn()){
            delete(ms);
            break;
        }

        if(opCode == 4){
            char followInd = char(std::stoi(ms->getNextArgument()));
            char numOfUsersBytes[2];
            Message::shortToBytes((short)(std::stoi(ms->getNextArgument())), numOfUsersBytes);

            if(!handler->sendBytes(&followInd, 1) || !handler->sendBytes(numOfUsersBytes, 2)) {
                delete (ms);
                break;
            };
        }
        else if(opCode == 5)
            ms->setPostMsg();
        else if(opCode == 6)
            ms->setPmMsg();


        while(!ms->isDone()){
            if (!handler->sendFrameAscii(ms->getNextArgument(), '\0')){
                delete(ms);
                break;
            }
        }

        delete(ms);
    }

}
