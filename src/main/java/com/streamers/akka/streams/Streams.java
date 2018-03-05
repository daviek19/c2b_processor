package com.streamers.akka.streams;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Framing;
import akka.stream.javadsl.FramingTruncation;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import scala.concurrent.duration.FiniteDuration;

public class Streams {

    public static void main(String[] argv) throws InterruptedException, Exception {
        ActorSystem _system = ActorSystem.create("c2b_processor");
        final Materializer materializer = ActorMaterializer.create(_system);
        final List<String> strings = new ArrayList<>();

        final String newLine = "\n";
        final ByteString newLineBytes = ByteString.fromString(newLine);

        Source<ByteString, NotUsed> source = FileTailSource
                .create(
                        new File("input.txt").toPath(),
                        1,
                        0,
                        FiniteDuration.fromNanos(1000 * 5)
                )
                .via(Framing.delimiter(newLineBytes, 100, FramingTruncation.ALLOW));

        source.runForeach(
                x -> {
                    System.out.println( x.utf8String());
                }, materializer
        );
    }
}
