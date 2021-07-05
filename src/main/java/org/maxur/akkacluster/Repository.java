package org.maxur.akkacluster;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;


/**
 * @author Evgeny Bubnov
 * @version 1.0 05.07.2021
 */
public class Repository extends UntypedActor {

    private ActorRef sender;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            save(message.toString());
            sender.tell(message, sender());
        }
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("Start Repository");
        sender = context().actorOf(Props.create(Sender.class));
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("Stop Repository");
    }

    private void save(String response) throws InterruptedException {
        // Имитация бурной деятельности
        Thread.sleep(100);
    }
}
