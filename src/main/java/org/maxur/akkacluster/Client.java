package org.maxur.akkacluster;


import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


import static akka.actor.ActorRef.noSender;

/**
 * @author Evgeny Bubnov
 * @version 1.0 05.07.2021
 */
public class Client extends UntypedActor {

    private ActorSelection worker;

    public static void main(String[] args) throws Exception {
        startSystem();
    }

    private static void startSystem() {
        final Config config = ConfigFactory.load().getConfig("client");
        ActorSystem system = ActorSystem.create("ClientSystem", config);
        system.actorOf(Props.create(Client.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            System.out.println(message);
        }
    }

    @Override
    public void preStart() {
        final String path = "akka.tcp://WorkerSystem@127.0.0.1:2550/user/worker";
        worker = context().system().actorSelection(path);
        run();
    }

    @Override
    public void postStop() {
        worker.tell(PoisonPill.getInstance(), noSender());
        context().system().shutdown();
    }

    private void run() {
        try {
            for (int i = 0; i < 100; i++) {
                worker.tell(makeRequest(), self());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String makeRequest() {
        return "Hello";
    }
}
