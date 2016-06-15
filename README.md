# RemoteMonitor
A basic LAN activity monitoring program. The utility requires two parts:
  > RemoteMonitorServer - This runs on the server, collecting data from computers within a LAN that run the client part.
  > RemoteMonitorClient - This runs on the monitored computer, sending data to the server depending on what is requested.
  
To run, the server must be run first with a specified key. The monitored computer should then run the client, and enter the server's internal IP address (the server will give this to you) along with the corresponding key. If the hashes generated from the client and server match, the TCP connection will be maintained and the server/client will start communications. Otherwise, the connection is cut.

As of release v1.1, the program only has two functions: monitor key input, and capturing screenshots.
