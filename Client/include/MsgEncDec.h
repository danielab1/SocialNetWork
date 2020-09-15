//
// Created by sagl@wincs.cs.bgu.ac.il on 1/1/19.
//

#ifndef ASSIGNMENT3_MSGENCDEC_H
#define ASSIGNMENT3_MSGENCDEC_H

#include <string>
#include "connectionHandler.h"

class MsgEncDec{
public:

    bool getMessageString(std::string& line, ConnectionHandler& other);

private:
    ConnectionHandler *handler;
};
#endif //ASSIGNMENT3_MSGENCDEC_H
