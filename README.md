# School-Day-Simulation
Develop a monitor(s) that will synchronize the threads (principal, instructor, nurse, students) in the context of the story; Closely follow the details of the story and the synchronization requirements.

# First, compile and run Server side package -> MainServer.java. 
 Server side let MainServer as the process's main entry. 
 Whenever has new connection, it spawns new thread to create communication.
 After connection close, main server still keep listening, waiting for new connection.

# Then, compile and run Client side package -> Client.java.  

In order to make easier for testing the result, each character as a thread, connect with the server.

In reality, client side every character also can be independent project to run. 
Even can run in different location of the world.
The way to changing the code is, duplicating Client.java as many as you want. 
Then combine with each character's characterClient.java as project.
Then make sure that every copy has the character declaration and starting run in the main function.
Let server be one project ,run first.
Then run as following order : student project -> nurse project -> teacher project -> principal project. 
