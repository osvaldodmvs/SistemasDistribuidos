package edu.ufp.inf.sd.rabbitmqservices._04_topics.consumer;

import com.rabbitmq.client.*;
import edu.ufp.inf.sd.rabbitmqservices.util.RabbitUtils;


public class ReceiveLogsTopic {

    /**
     * This consumer receives logging info, organised by topics.
     * <p>
     * Let us assume routing keys for logs with 2 words: "<facility>.<severity>".
     * - facility = {kern, cron, auth}
     * - severity = {info, warn, critical}
     * <p>
     * To receive ALL logs run client with key: "#":
     * $ runconsumer "#"
     * <p>
     * To receive ALL logs to facility: "kern":
     * $ runconsumer "kern.*"
     * <p>
     * To receive ALL "critical" logs from ALL facilities:
     * $ runconsumer "*.critical"
     * <p>
     * To receive multiple bindings (all severities from kernel, critical from all facilities):
     * $ runconsumer "kern.*" "*.critical"
     */
    public static void main(String[] args) throws Exception {
        RabbitUtils.printArgs(args);

        //Read args passed via shell command
        String host=args[0];
        int port=Integer.parseInt(args[1]);
        String exchangeName=args[2];

        //DO NOT USE try-with-resources HERE because closing resources (channel) will prevent receiving any messages.
        try {

            // TODO: Create a channel to RabbitMQ


            // TODO: Declare exchange of type TOPIC


            // TODO: Create a non-durable, exclusive, autodelete queue with a generated name
            String queueName="";


            System.out.println("main(): argv.length=" + args.length);

            if (args.length < 4) {
                System.err.println("Usage: ReceiveLogsTopic [HOST] [PORT] [EXCHANGE] [BindingKey1] [RoutingKey2]");
                System.exit(1);
            }

            //Bind to each routing key (received from args[3] upward)
            for (int i=3; i < args.length; i++) {
                String bindingKey = args[i];
                System.err.println("main(): add queue bind to queue = " + queueName + ", with bindingKey = " + bindingKey);

                // TODO: Create binding: tell exchange to send messages to a queue


            }

            System.out.println(" [*] Waiting for messages... to exit press CTRL+C");

            //Create callback that will receive messages from topic
            DeliverCallback deliverCallback=(consumerTag, delivery) -> {
                String message=new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            CancelCallback cancelCallback=(consumerTag) -> {
                System.out.println(" [x] Cancel callback activated: " + consumerTag);
            };

            // TODO: Consume with deliver and cancel callbacks


            //Current Thread waits till interrupted (avoids finishing try-with-resources which closes channel)
            //Thread.currentThread().join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
