//
// Created by sagl@wincs.cs.bgu.ac.il on 12/31/18.
//

#ifndef ASSIGNMENT3_MESSAGE_H
#define ASSIGNMENT3_MESSAGE_H

#include <vector>
#include <string>
#include "connectionHandler.h"

class Message{
public:
    Message(std::string s);
    void static shortToBytes(short num, char* bytesArr);
    short static bytesToShort(char* bytesArr);
    virtual ~Message();
    std::string getNextArgument();
    void setPostMsg();
    void setPmMsg();
    void uniteFromInd(int ind);
    bool isDone();


private:
    int lastInd;
    std::vector<std::string> args;

};


#endif //ASSIGNMENT3_MESSAGE_H
