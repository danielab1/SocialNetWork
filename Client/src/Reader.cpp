//
// Created by sagl@wincs.cs.bgu.ac.il on 1/2/19.
//

#include <connectionHandler.h>
#include <ReaderWriter.h>
#include <Message.h>

Reader::Reader(ConnectionHandler& handler):handler(), shouldTerminate(false){
    this->handler = &handler;
}

void Reader::run(){
    while(1){
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!getMessage(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
//        answer.resize(len-1);
        std::cout <<  answer << std::endl;
        if (shouldTerminate) {
            std::cout << "Exiting...\n" << std::endl;
            break;
        }
    }

}

bool Reader::getMessage(std::string& frame) {
    int opCode;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        opCode = getNextShort();
    } catch (std::exception & e){
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }

    if (opCode == 9) {
        createNotificationMsg(frame);
    } else if (opCode == 10) {
        createAckMsg(frame);
    } else if (opCode == 11) {
        createErrorMsg(frame);
    }

    return true;
}



bool Reader::createAckMsg(std::string& frame){
    int msgOpCode;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        msgOpCode = getNextShort();
        frame = "ACK " + std::to_string(msgOpCode);
        if(msgOpCode == 2)
            handler->setLoggedIn(true);
        if(msgOpCode == 3)
            this->shouldTerminate = true;
        if(msgOpCode == 4 || msgOpCode == 7){
            int numOfUsers = getNextShort();
            frame += " " + std::to_string(numOfUsers);
            std::string friendsName;
            for(int i=0; i<numOfUsers; i++){
                handler->getLine(friendsName);
                frame += " " + friendsName;
                friendsName = "";
            }
        } else if(msgOpCode == 8){
            std::string numOfPosts = std::to_string(getNextShort());
            std::string numFollowers = std::to_string(getNextShort());
            std::string numFollowing = std::to_string(getNextShort());
            frame += " " + numOfPosts + " " + numFollowers + " " + numFollowing;
        }
    } catch (std::exception & e){
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }

    return true;
}

bool Reader::createErrorMsg(std::string &frame){
    int opCode;

    try {
        opCode = getNextShort();
        frame = "ERROR " + std::to_string(opCode);
    } catch (std::exception & e){
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool Reader::createNotificationMsg(std::string &frame){
    char noteTypeArr[1];

    try {
        handler->getBytes(noteTypeArr, 1);
        char notificationType = noteTypeArr[0];
        if(notificationType == '\0')
            frame = "NOTIFICATION PM ";
        else frame = "NOTIFICATION Public ";
        std::string postingUser;
        handler->getLine(postingUser);
        frame += postingUser + " ";
        std::string content;
        handler->getLine(content);
        frame += content;
    } catch (std::exception & e){
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

int Reader::getNextShort(){
    int num;

    try {
        char opCodeArr(2);
        handler->getBytes(&opCodeArr, 2);
        num = Message::bytesToShort(&opCodeArr);
    } catch (std::exception & e){
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return -1;
    }
    return num;
}



