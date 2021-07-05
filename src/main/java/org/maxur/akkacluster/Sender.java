package org.maxur.akkacluster;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static akka.actor.ActorRef.noSender;

/**
 * @author Evgeny Bubnov
 * @version 1.0 05.07.2021
 */
public class Sender extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            send(message.toString());
            sender().tell(message, noSender());
        }
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("Start Sender");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("Stop Sender");
    }

    private  void send(String response) throws InterruptedException {
        // Имитация бурной деятельности
        Thread.sleep(100);
    }
}
