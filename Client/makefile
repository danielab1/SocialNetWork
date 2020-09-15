CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-pthread -lboost_system

all: BGSclient
	g++ -o bin/BGSclient bin/connectionHandler.o bin/echoClient.o bin/Message.o bin/Reader.o bin/Writer.o $(LDFLAGS) 

BGSclient: bin/connectionHandler.o bin/echoClient.o bin/Message.o bin/Reader.o bin/Writer.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
	
bin/Message.o: src/Message.cpp
	g++ $(CFLAGS) -o bin/Message.o src/Message.cpp
	
	
bin/Reader.o: src/Reader.cpp
	g++ $(CFLAGS) -o bin/Reader.o src/Reader.cpp
	
bin/Writer.o: src/Writer.cpp
	g++ $(CFLAGS) -o bin/Writer.o src/Writer.cpp
	
	
.PHONY: clean
clean:
	rm -f bin/*
