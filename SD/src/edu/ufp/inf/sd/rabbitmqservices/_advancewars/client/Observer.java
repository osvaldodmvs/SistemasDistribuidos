package edu.ufp.inf.sd.rabbitmqservices._advancewars.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rabbitmqservices._04_topics.chatgui.ObserverGuiClient;
import edu.ufp.inf.sd.rabbitmqservices._advancewars.client.game.engine.Game;
import edu.ufp.inf.sd.rabbitmqservices._advancewars.server.GameLobby;
import edu.ufp.inf.sd.rabbitmqservices.util.RabbitUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create a chat room (like zoom chat private msg), supporting:
 * - open *general* messages to all users
 * - private messages to a specific *user*
 *
 * <p>
 * Each _05_observer will receive messages from its queue with 2 Binding keys:
 * - room1.general (public msg for general/all users) and room1.pedro (private msg for given user)
 *
 * <p>
 * Send message with specific Routing keys:
 * - routingKey="room1.general" (public to general/all users)
 * - routingKey="room1.pedro"   (private to specific user)
 *
 * <p>
 * Run _05_observer with 3 parameters <room> <user> <general>:
 * $ runobserver room1 pedro general
 *
 *
 * @author rui
 */
public class Observer {

    //Reference for gui
    //TODO trocar por Game(engine) e adicionar outras coisas se necessario
    private Game game;
    //Preferences for exchange...
    private final Channel channelToRabbitMq;
    private final String exchangeName;
    private final BuiltinExchangeType exchangeType;
    private final String exchangeBindingKeys;
    private final String messageFormat;
    //Settings for specifying topics
    private final String room;
    private final String user;
    private final String newOrJoin;

    private final String commander;
    private final int maxPlayers;

    //Store received message to be get by gui
    private String receivedMessage;


    public Observer(String host, int port, String brokerUser, String brokerPass, String user, String newOrJoin, String room, int maxPlayers, String commander, String exchangeName, BuiltinExchangeType exchangeType, String messageFormat) throws IOException, TimeoutException {
        this.game=new Game();
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, " going to attach advancewars_observer to host: " + host + "...");

        Connection connection=RabbitUtils.newConnection2Server(host, port, brokerUser, brokerPass);
        this.channelToRabbitMq=RabbitUtils.createChannel2Server(connection);

        this.exchangeName=exchangeName;
        this.exchangeType=exchangeType;

        this.user=user;
        this.newOrJoin=newOrJoin;
        this.room=room;
        this.maxPlayers=maxPlayers;
        this.commander=commander;
        // TODO: Set 2 binding keys (to receive msg from public room.general and private room.user
        String bindingKey=room;

        String map;

        if(maxPlayers==4)
            map="FourCorners";
        else
            map="SmallVs";

        this.exchangeBindingKeys=bindingKey;
        this.messageFormat=messageFormat;

        bindExchangeToChannelRabbitMQ();
        attachConsumerToChannelExchangeWithKey();
        String messageToSend = user + ";" + newOrJoin + ";" + room + ";" + map + ";" + commander;
        sendMessage(messageToSend);
    }

    /**
     * Declare exchange of specified type.
     */
    private void bindExchangeToChannelRabbitMQ() throws IOException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Declaring Exchange '" + this.exchangeName + "' with policy = " + this.exchangeType);

        // TODO: Declare exchange type
        // EXCHANGE DE ONDE RECEBE MENSAGENS
        channelToRabbitMq.exchangeDeclare(exchangeName,BuiltinExchangeType.TOPIC);
    }

    /**
     * Creates a Consumer associated with an unnamed queue.
     */
    private void attachConsumerToChannelExchangeWithKey() {
        try {
            // TODO: Create a non-durable, exclusive, autodelete queue with a generated name.
            // FILA QUE RECEBE MENSAGENS NO CLIENTE
            String queueName = channelToRabbitMq.queueDeclare().getQueue();

            // TODO: Bind to routing key
            System.err.println("main(): add queue bind to queue = " + queueName + ", with bindingKey = " + exchangeBindingKeys);

            // TODO: Create binding: tell exchange to send messages to a queue
            channelToRabbitMq.queueBind(queueName, exchangeName, exchangeBindingKeys);

            /* Use a DeliverCallback lambda function instead of DefaultConsumer to receive messages from queue;
               DeliverCallback is an interface which provides a single method:
                void handle(String tag, Delivery delivery) throws IOException; */
            DeliverCallback deliverCallback=(consumerTag, delivery) -> {
                String message=new String(delivery.getBody(), "UTF-8");
                setReceivedMessage(message);
                System.out.println(" [x] Received '" + message + "'");
                doWork(message);
            };
            CancelCallback cancelCallback=consumerTag -> {
                System.out.println(" [x] CancelCallback invoked");
            };

            // TODO: Consume with deliver and cancel callbacks
            channelToRabbitMq.basicConsume(queueName,true,deliverCallback,cancelCallback);

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Publish messages to existing exchange instead of the nameless one.
     * - Messages will be lost if no queue is bound to the exchange yet.
     * - User may be some 'username' or 'general' (for all)
     */
    public void sendMessage(String msgToSend) throws IOException {
        BasicProperties prop=MessageProperties.PERSISTENT_TEXT_PLAIN;
        //User maybe some <username> (private msg) or 'general' (public msg for all)

        // TODO: Publish message with routing key
        channelToRabbitMq.basicPublish(exchangeName, room, null, msgToSend.getBytes("UTF-8"));
    }

    /**
     * @return the receivedMessage
     */
    public String getReceivedMessage() {
        return receivedMessage;
    }

    /**
     * @param receivedMessage the receivedMessage to set
     */
    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage=receivedMessage;
    }

    public void doWork(String message){
        if (message.compareTo("Game created, waiting for players!")==0){
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, message);
            return;
        }
        else if(message.contains("START")){
            Game.Start(message);
        } else {
            Game.updateGUI(message);
        }
    }

    public static void main(String[] args) {
        try {
            RabbitUtils.printArgs(args);
            String host=args[0];
            int port=Integer.parseInt(args[1]);
            String exchangeName=args[2];
            String user=args[3];
            String newOrJoin=args[4];
            String room=args[5];
            int maxPlayers=Integer.parseInt(args[6]);
            String commander=args[7];
            new Observer(host, port, "guest","guest", user, newOrJoin, room, maxPlayers, commander, exchangeName, BuiltinExchangeType.TOPIC, "UTF-8");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
