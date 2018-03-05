package com.streamers.akka.streams;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import java.io.File;
import scala.concurrent.duration.FiniteDuration;

public class Streams {

    public static void main(String[] argv) throws InterruptedException, Exception {
        ActorSystem _system = ActorSystem.create("c2b_processor");
        final Materializer materializer = ActorMaterializer.create(_system);

        Source<ByteString, NotUsed> source = FileTailSource
                .create(
                        new File("input.txt").toPath(),
                        1,
                        0,
                        FiniteDuration.fromNanos(1000 * 5)
                );
        source.runForeach(
                x -> {
                    char c = x.utf8String().charAt(0);
                    
                    System.out.println(x + " END");
                }, materializer
        );
    }
}
