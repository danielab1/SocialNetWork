//
// Created by sagl@wincs.cs.bgu.ac.il on 12/31/18.
//

#include <iterator>
#include <iostream>
#include <sstream>
#include <algorithm>
#include "../include/Message.h"


void Message::shortToBytes(short num, char* bytesArr){
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

short Message::bytesToShort(char* bytesArr){
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

Message::Message(std::string s):lastInd(0), args(){
    std::stringstream ss(s);
    std::istream_iterator<std::string> begin(ss);
    std::istream_iterator<std::string> end;
    std::vector<std::string> vstrings(begin, end);
    args = vstrings;
}

std::string Message::getNextArgument(){
    return args[lastInd++];
};

void Message::setPostMsg(){
    uniteFromInd(1);
}

void Message::setPmMsg(){
    uniteFromInd(2);
}

void Message::uniteFromInd(int ind){
    std::string content;
    int argsSize = args.size();
    for(int i=ind; i<argsSize-1; i++){
        content += args[i] + " ";
    }
    content += args[args.size()-1];
    args[ind] = content;
    args.resize(ind+1);
}


bool Message::isDone() {
    int argsSize = args.size();
    return argsSize<=lastInd;
}


Message::~Message(){

};




