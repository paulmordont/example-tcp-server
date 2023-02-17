# example-tcp-server

The task is to implement a simple text TCP server in Java. It should be able to do the following:

1. Accept connections from several clients
2. Be able to process requests concurrently (no need to implement multiplexing per connecion though)
3. Connection with a client must be open until client termintates it

Server should accept data in text format and respond back in the following format:
```
Client $CLIENT_ID: $REQUEST_TEXT_IN_REVERSE
```

There are unit tests provided in order to test the desired behaviour. It also should be possible to test the server with `nc` command line tool. 
```
$ nc 0.0.0.0 12345 # establishing connection
Hello! # this is a request sent to the server
Client 1: !olleH # this is a server response
```
