package org.maxur.akkacluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static akka.actor.ActorRef.noSender;
import static java.lang.String.format;

/**
 * @author Evgeny Bubnov
 * @version 1.0 05.07.2021
 */
public class Worker extends UntypedActor {

    private int count = 0;

    private ActorRef repository;

    public static void main(String[] args) throws Exception {
        final Config config = ConfigFactory.load().getConfig("worker");
        ActorSystem system = ActorSystem.create("WorkerSystem", config);
        system.actorOf(Props.create(Worker.class), "worker");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            final String response = format("%d: %s", count++, message);
            repository.tell(response, sender());
        }
    }

    @Override
    public void preStart() throws Exception {
        repository = context().actorOf(Props.create(Repository.class));
    }

    @Override
    public void postStop() throws Exception {
        repository.tell(PoisonPill.getInstance(), noSender());
    }

}
