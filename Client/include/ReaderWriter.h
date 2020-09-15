//
// Created by sagl@wincs.cs.bgu.ac.il on 1/2/19.
//

#ifndef ASSIGNMENT3_READERWRITER_H
#define ASSIGNMENT3_READERWRITER_H

#include "connectionHandler.h"

class Reader{
public:
    Reader(ConnectionHandler& handler);
    void run();
    bool getMessage(std::string& frame);
    bool createAckMsg(std::string& frame);
    bool createErrorMsg(std::string& frame);
    bool createNotificationMsg(std::string& frame);
    int getNextShort();

private:
    ConnectionHandler *handler;
    bool shouldTerminate;
};

class Writer{
public:
    Writer(ConnectionHandler& handler);
    void run();
private:
    ConnectionHandler *handler;
};
#endif //ASSIGNMENT3_READERWRITER_H
