# About

1. This application is a Board Game where this server is the logic handling players requests. 

# Design

1. This application is a board where this server is the logic handling players requests. 

2. 2 players interact with a command prompt to send request to this server. 
Requests from client can be add player, get status, send a token to make a move and exit cleanly. 
Players take turn. A win is having a consecutive 5 rows in any direction. a draw is when all boxes have been filled.

3. 2 clients are represented by a ArrayList, the Board for the game is represented by a List of Stacks

4. BoardService encapsulates and handles all operations for the board game 

5. GameService encapsulates and handles all operations of the Game, it reflects the controller operations.  

6. PlayersContainer handles all operations on the List of Players, gives the option to use another Collection if need be without impacting much. 

7. ServerResponse is the communication model between clients and the server. It hold all information required for the game to function.  

8. Player class is a model holding the Player name and token, the token does not change for the duration of the game and will be used to determine winer and loser as well as draw. 

9. C5Token is the representation of a player on the board. It is allocated positions as moves from the player. 

10. GameController is the interface between the server and the client. the server exposes its service via the controller Restful endful endpoint.

# Technology

1. Java 11

2. Server side: SpringBoot, Eclipse IDE, Tomcat, Jococo, Junit 5, Mockito, Hamcrest, Rest Api, Stack data structure and List for the board game

3. Client side: exec maven plugin, Http client apache Api

5. The Controller class exposes Restful endpoints used by the clients to communicate via Http 

# Test

1. Mockito, jUnit 5

2.  run => "mvn clean verify" then go to "/target/site/jacoco/index.html" to see the test report, this apply only to the server as the project is backend project. the client are supplied as utility.

# Start game server

1. Run the server first, from the server folder Open a command prompt to run the command below

2. Run => "mvn spring-boot:run" to run the code and have the server ready



