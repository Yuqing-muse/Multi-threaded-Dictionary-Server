# Multi-threaded-Dictionary-Server
Design and implement a multi-threaded dictionary server, which allows many clients to search, add, remove word concurrently. Multiple clients can connect to a (single) multi-threaded server and perform operations concurrently

This project designs and implements a multi-threaded dictionary server, which allows many clients to search, add, remove
word concurrently. Multiple clients can connect to a (single) multi-threaded server and perform operations concurrently.
The communication between server and clients is based on socket and thread.
This project is based on TCP and thread-per-connection mode. Once a client opens the GUI, it will connect to the
server and start a thread.
# Components
After running the server GUI program, the server will keep monitoring real-time network status and waiting connections
from clients. Clients will be automatically connected to the server once we open client’s GUI interface. Task from the client
will be added into waiting queue first. Extra free threads in the thread pool will be allocated to tasks. Otherwise, tasks
will wait in the queue. All connected clients’ socket information will be printed on server interface. We can dynamically
observe clients’ connections.
Before clients successfully connect to the server, all exceptions will be caught, and a suitable description of error will be
printed out. Once a client connects to server, a log file will be created and record current time and information of client.
During the connection, errors will be managed, and readable instructions will be printed on client interface. At the same
time, the reason for the exception is written to the log file in a user-readable mode. Each client can check the log file to
observe the operation of system.
The main functionalities of dictionary are the followings.
1) Query
Clients can search the meanings of words through entering the word. Then meanings will be shown in output field on
client interface. Only if the word has already existed in the dictionary, clients can query its meanings. Otherwise, output
field of GUI will print “Sorry, we can’t find this word”.
If a word has more than one meaning, each meaning will be printed on a separate line in output field.
2) Add
Clients should enter word in textField(smaller one) and meanings in textArea(larger one). Clients can add a new word
and its meanings into dictionary only if word does not exist already in the dictionary. Then dictionary should be updated.
The meaning of the words entered by the client needs to be separated by ‘;’. For instance, meaning format that client
must input is “fruit; red”.
Add a word without an associated meaning should result in a LackParameterException error, which is defined by my
own.
3) Remove
Remove a word and all its associated meanings from the dictionary. If the dictionary does not have the word, then no
action should be taken and print ”Sorry, we can’t find this word” in output field. If remove word successfully, update
dictionary.
4) Update
Clients can update associated meanings of a word from the dictionary. If multiple meanings exist, all of them should be
replaced by new meaning(s) provided by client. And update dictionary. If the word does not exist in the dictionary, then
no action should be taken and print ”Sorry, we can’t find this word” in output field. Besides, the meaning of the words
entered by the client needs to be separated by ‘;’.
5) Exit
Click on “Exit” button then program will be terminated on client side

# Design
**Thread-per-connection mode**:  To cache many resources and schedule threads in advance during the concurrency process, I implement my own thread pool and custom
blocking queue class. Blocking queue is a waiting queue and follows FIFIO principle (first in, first out).

**TCP and JSON Format**
**Log File**:A log file will be generated when client connects successfully. The log file will be saved locally on the client’s side(default
save in file of .jar). It contains summary of this project, current client’s socket information, interaction between the server
and the clients, and readable exceptions descriptions. Generate log file is important for monitoring, reporting, and
observing specific exception from client sides.

#  UML class diagram
![newfile](https://user-images.githubusercontent.com/62585203/131211020-6ddf0d3f-6af7-44b9-9874-01dedcf0493e.jpg)



