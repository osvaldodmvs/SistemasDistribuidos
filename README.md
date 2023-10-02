# AdvanceWars Distributed Game Management System

Developed for the class of Sistemas Distribuídos (Distributed Systems), this project consists on turning a local, singleplayer strategy game (AdvanceWars) into a multiplayer game.

## **Grade: 17/20**


### Description
Written in Java, this multiplayer feature is implemented with two different methods (for learning and comparison purposes) : 

* Java RMI
* RabbitMQ messaging

Use of sign-up/login and identity verification system using JWT, implementation of several servers listening in round-robin and synchronizing with each other to add fault tolerance.
Supports two modes : 1v1 (2 players) and 1v1v1v1 (4 players).
Both servers and clients run via execution of .bat scripts.
