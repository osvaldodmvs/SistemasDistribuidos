/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufp.inf.sd.rabbitmqservices._advancewars.server;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.engine.Game;
import edu.ufp.inf.sd.rabbitmqservices.util.RabbitUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Round-robin dispatching:
 *  One of the advantages of using a Task Queue is the ability to easily
 *  parallelise work. If we are building up a backlog of work, we can just add
 *  workers and scale easily.
 * 
 *  Run 2 worker instances at the same time (one on each shell).
 *  They will both get messages from the queue, since, by default, RabbitMQ will
 *  send each message to the next client (in sequence).
 *  On average, every client will get the same number of messages. This way of
 *  distributing messages is called round-robin (try this out with 3+ workers).
 * 
 * 
 * Message acknowledgment:
 *  With current channel queue, once RabbitMQ delivers a message to the customer it
 *  immediately marks it for deletion. In this case, if you kill a worker we
 *  will lose the message it was just processing. We also lose all the messages
 *  that were dispatched to this particular worker but were not yet handled.
 *
 *  To not lose any tasks (in case a worker dies) and deliver them to another worker,
 *  RabbitMQ supports message acknowledgments, i.e., an ack is sent back by the client
 *  to tell RabbitMQ that a particular message has been received, processed and
 *  that RabbitMQ is free to delete it.
 *
 *  If a client dies (i.e., channel is closed, connection is closed, or
 *  TCP connection is lost) without sending an ack, RabbitMQ will understand
 *  that a message was not processed fully and will re-queue it.
 *  If there are other consumers online at the same time, it will then quickly
 *  re-deliver it to another client. That way you can be sure that no message
 *  is lost, even if the workers occasionally die.
 *  There are no message timeouts and RabbitMQ will re-deliver the message when
 *  the client dies. It is fine even if processing a message takes a long time.
 *  "Manual message acknowledgments" are turned on by default (we may explicitly
 *  turned them off via the autoAck=true flag).
 * 
 * Forgotten acknowledgment:
 *  To debug lack of ack use rabbitmqctl to print the messages_unacknowledged field:
 *    - Linux/Mac:
 *     sudo rabbitmqctl list_queues name messages_ready messages_unacknowledged
 *    - Win:
 *     rabbitmqctl.bat list_queues name messages_ready messages_unacknowledged
 * 
 * Message durability:
 *  Messages/Tasks will be lost if RabbitMQ server stops, because when RabbitMQ
 *  quits or crashes it will forget the queues and messages unless you tell it not to.
 *
 *  Two things are required to make sure that messages are not lost, i.e., mark both
 *  the queue and messages as durable:
 *      1) declare the queue as *durable* (so RabbitMQ will never lose the queue);
 *      2) mark messages as persistent by setting MessageProperties.PERSISTENT_TEXT_PLAIN.
 *
 *  NB: persistence guarantees ARE NOT strong, i.e., may be cached and
 *  not immediately saved/persisted.
 * 
 * Fair dispatch:
 *  RabbitMQ dispatches a message when the message enters the queue. It does not
 *  look at the number of unacknowledged messages for a client. It just blindly
 *  dispatches every n-th message to the n-th client. Hence, a worker could get
 *  all heavy tasks while another the light ones.
 *
 *  To guarantee fairness use basicQos() method for setting prefetchCount = 1.
 *  This tells RabbitMQ not to give more than one message to a worker at a time,
 *  i.e. do not dispatch new message to a worker until it has not processed and
 *  acknowledged the previous one. Instead, dispatch it to the next worker
 *  that is not still busy.
 *
 * Challenge:
 *  1. Create a LogWorker for appending the message to a log file;
 *  2. Create a MailWorker for sending an email (use javamail API
 *  <https://javaee.github.io/javamail/>)
 * 
 * @author rui
 */

public class Server {

    private static final String id = UUID.randomUUID().toString();

    private static DBMockup db=new DBMockup();

    public static String find_room_by_user(String user){
        for (GameLobby g : db.getGames()) {
            for (String s: g.getPlayers()) {
                if(s.compareTo(user)==0){
                    return g.getId();
                }
            }
        }
        return null;
    }

    public static GameLobby find_gl_by_user(String user){
        for (GameLobby g : db.getGames()) {
            for (String s: g.getPlayers()) {
                if(s.compareTo(user)==0){
                    return g;
                }
            }
        }
        return null;
    }

    public static int get_index_by_user(String user){
        int i = 0;
        for (GameLobby g : db.getGames()) {
            for (String s: g.getPlayers()) {
                if(s.compareTo(user)==0){
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public static void main(String[] argv) throws Exception {
        try {
            RabbitUtils.printArgs(argv);

            //Read args passed via shell command
            String host = argv[0];
            int port = Integer.parseInt(argv[1]);
            String queueName = argv[2];
            String exchangeName = argv[3];
            String faultToleranceExchange = argv[4];

            /* Open a connection and a channel, and declare the queue from which to consume.
            Declare the queue here, as well, because we might start the client before the publisher. */
            Connection connection = RabbitUtils.newConnection2Server(host, port, "guest", "guest");
            Channel channel = RabbitUtils.createChannel2Server(connection);

            /* Declare a queue as Durable (queue won't be lost even if RabbitMQ restarts);
            NB: RabbitMQ doesn't allow to redefine an existing queue with different
            parameters, need to create a new one */
            boolean durable = true;
            //channel.queueDeclare(Send.QUEUE_NAME, false, false, false, null);
            //TODO : FILA PARA ONDES CLIENTES ENVIAM MENSAGENS
            channel.queueDeclare(queueName, durable, false, false, null);
            System.out.println(" [*] Waiting for client messages on queue '" + queueName + "'. To exit press CTRL+C");
            //TODO : FILA PARA ONDES SERVIDORES ENVIAM MENSAGENS DE DB
            /* The server pushes messages asynchronously, hence we provide a DefaultConsumer callback
            that will buffer the messages until ready to use them. */
            //Set QoS: accept only one unacked message at a time; and force dispatch to next worker that is not busy.
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            System.out.println(" [*] Declared exchange '" + exchangeName + "'.");
            channel.exchangeDeclare(faultToleranceExchange, BuiltinExchangeType.FANOUT);
            System.out.println(" [*] Declared exchange '" + faultToleranceExchange + "'.");
            String faultToleranceQueue = channel.queueDeclare().getQueue();
            channel.queueBind(faultToleranceQueue, faultToleranceExchange, "");
            System.out.println(" [*] Bound queue '" + faultToleranceQueue + "' to exchange '" + faultToleranceExchange + "'.");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] <advancewars>  Received '" + message + "'");
                //TODO doWork é o metodo que envia (publish) mensagem para o exchange
                try {
                    if(message.contains(";")){
                        System.out.println("Found a new or join game message ! ");
                        String[] split = message.split(";"); //0user 1newOrJoin 2room 3map 4commander
                        if(split[1].compareTo("new")==0 && !db.check_if_exists(split[2])){
                            GameLobby g = new GameLobby(split[0],split[2],Integer.parseInt(split[3]),Integer.parseInt(split[4]));
                            db.addGame(g);
                            message="Game created, waiting for players!";
                            System.out.println("Binding key is : "+split[2]);
                            channel.basicPublish(exchangeName,split[2],null,message.getBytes("UTF-8"));
                            message="CREATED "+split[0]+" "+split[2]+" "+split[3]+" "+split[4]+" "+id;
                            System.out.println("Sending CREATED message to fault tolerance exchange : "+message);
                            channel.basicPublish(faultToleranceExchange,"",null,message.getBytes("UTF-8"));
                        }
                        else{
                            GameLobby g = db.getGame(split[2]);
                            int valid=g.addToGameLobby(split[0],Integer.parseInt(split[4]));
                            if(valid==-1){
                                message="Game lobby is full!";
                                System.out.println("Binding key is : "+split[2]);
                                channel.basicPublish(exchangeName,split[2],null,message.getBytes("UTF-8"));
                            }
                            else if(g.getPlayers().size()==g.getMaxPlayers()){
                                message="Game is ready, starting game!";
                                System.out.println("Binding key is : "+split[2]);
                                channel.basicPublish(exchangeName,split[2],null,message.getBytes("UTF-8"));
                                message="Start "+g.getMap()+" "+g.returnCommanders()+" "+g.getId();
                                channel.basicPublish(exchangeName,split[2],null,message.getBytes("UTF-8"));
                                message="STARTED "+split[0]+" "+split[2]+" "+split[3]+" "+split[4]+" "+id;
                                System.out.println("Sending STARTED message to fault tolerance exchange : "+message);
                                channel.basicPublish(faultToleranceExchange,"",null,message.getBytes("UTF-8"));
                                //TODO start game
                            }
                            else{
                                message="Player joined, waiting for more players!";
                                System.out.println("Binding key is : "+split[2]);
                                channel.basicPublish(exchangeName,split[2],null,message.getBytes("UTF-8"));
                                message="JOINED "+split[0]+" "+split[2]+" "+split[3]+" "+split[4]+" "+id;
                                System.out.println("Sending JOINED message to fault tolerance exchange : "+message);
                                channel.basicPublish(faultToleranceExchange,"",null,message.getBytes("UTF-8"));
                            }
                        }
                    }
                    else if (message.contains("/")) {
                        if (message.startsWith("BUY-")) {
                            System.out.println("IM IN BUY UNIT SV");
                            System.out.println(message);
                            //BUY-UNIT/ type x y currentplayer sala
                            String[] split_buy = message.split(" ");
                            String routing = find_room_by_user(split_buy[5]);
                            int current = Integer.parseInt(split_buy[4]);
                            GameLobby gg = find_gl_by_user(split_buy[5]);
                            if (gg.getJwts().get(current).compareTo(gg.getJwts().get(get_index_by_user(split_buy[5]))) == 0) {
                                System.out.println("New Message : " + message);
                                channel.basicPublish(exchangeName, routing, null, message.getBytes("UTF-8"));
                                System.out.println(" [x] Sent '" + message + "'" + " to exchange '" + exchangeName + "' with routing key = " + routing);
                                channel.basicPublish(exchangeName, routing, null, message.getBytes("UTF-8"));
                            }
                        } else {
                            String[] split = message.split("/");
                            message = split[0];
                            String routingKey = find_room_by_user(split[1]);
                            int currplayer = Integer.parseInt(split[2]);
                            GameLobby g = find_gl_by_user(split[1]);
                            if (g.getJwts().get(currplayer).compareTo(g.getJwts().get(get_index_by_user(split[1]))) == 0) {
                                System.out.println("New Message : " + message);
                                channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
                                System.out.println(" [x] Sent '" + message + "'" + " to exchange '" + exchangeName + "' with routing key = " + routingKey);
                            } else {
                                message = "Wrong player tried to play!";
                                System.out.println("New Message : " + message);
                                channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
                                System.out.println(" [x] Sent '" + message + "'" + " to exchange '" + exchangeName + "' with routing key = " + routingKey);
                            }
                        }
                    }

                    //channel.basicPublish(exchangeName,"nothing really",null,message.getBytes("UTF-8"));
                } catch (NoSuchAlgorithmException e) {
                    System.out.println(e);
                    throw new RuntimeException(e);
                } finally {
                    System.out.println(" [x] Done processing task <advancewars>");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            //boolean autoAck = true; //When true disables "Manual message acknowledgments"
            //Set flag=false for worker to send proper ack (once it is done with a task).
            boolean autoAck = false;
            //Register handler deliverCallback()
            channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {
            });
            DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] <faulttolerance> Received '" + message + "'");
                try{
                    //0created/started/joined 1user 2room 3map 4commander 5id
                    String[] split = message.split(" ");
                    if(split[5].compareTo(id)==0){
                        Logger.getAnonymousLogger().log(Level.INFO, Thread.currentThread().getName()+": Fault tolerance message received by the same worker that sent i" + "t, ignoring it.\"");
                        for (GameLobby gl : db.getGames()) {
                            {
                                System.out.println("IGNORED Game: " + gl.getId() + " " + gl.getId() + " " + gl.getPlayers() + " " + gl.getCommanders());
                            }
                        }
                    }
                    else {
                        if (split[0].startsWith("CREATED")) {
                            GameLobby g = new GameLobby(split[1], split[2], Integer.parseInt(split[3]), Integer.parseInt(split[4]));
                            db.addGame(g);
                            //print the game info: users, number of players and room, use this db to do it, using a for each
                            for (GameLobby gl : db.getGames()) {
                                {
                                    System.out.println("CREATED Game: " + gl.getId() + " " + gl.getId() + " " + gl.getPlayers() + " " + gl.getCommanders());
                                }
                            }
                        } else {
                            GameLobby g = db.getGame(split[2]);
                            g.addToGameLobby(split[1], Integer.parseInt(split[4]));
                            for (GameLobby gl : db.getGames()) {
                                {
                                    System.out.println("ADDED Game: " + gl.getId() + " " + gl.getId() + " " + gl.getPlayers() + " " + gl.getCommanders());
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println(e);
                    throw new RuntimeException(e);
                }finally{
                    System.out.println(" [x] Done processing task <faulttolerance>");
                    //Worker must Manually ack each finalised task, hence, even if worker is killed
                    //(CTRL+C) while processing a message, nothing will be lost.
                    //Soon after the worker dies all unacknowledged messages will be redelivered.
                    //Ack must be sent on the same channel message it was received,
                    // otherwise raises exception (channel-level protocol exception).
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            //Register handler deliverCallback()
            channel.basicConsume(faultToleranceQueue, autoAck, deliverCallback2, consumerTag -> {
            });
        } catch (Exception e) {
            //Logger.getLogger(Recv.class.getName()).log(Level.INFO, e.toString());
            e.printStackTrace();
        }

    }

}