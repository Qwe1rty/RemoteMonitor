# RemoteMonitor
A LAN activity monitoring program. The utility requires two parts:
  > RemoteMonitorServer - This runs on the server, collecting data from any computer within a LAN that runs the client part.
  > RemoteMonitorClient - This runs on the monitored computer, sending data to the server depending on what is requested.
  
To run, the server must be run first with a specified key. The monitored computer should then run the client, and enter the server's internal IP address (the server will give this to you) along with the corresponding key. If the hashes generated from the client and server match, the TCP connection will be maintained and the server/client will start communications. Otherwise, the connection is cut.

As of release v1.1, the program currently has two functions: monitor key input, and capturing screenshots. The computer hosting the server component can request a screenshot of any client's screen, alongside passively collecting keystroke data

**Disclaimer: I am not liable or responsible for unauthorized/inappropriate use of this utility. This program is meant to be a debugging tool, tech demonstration, and a proof of concept. Various warnings are included in the program to ensure that the user(s) use the program responsibly.**
