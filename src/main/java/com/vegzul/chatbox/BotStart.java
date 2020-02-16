package com.vegzul.chatbox;

import akka.Done;
import akka.actor.typed.ActorSystem;
import com.vegzul.chatbox.actors.MainActor;
import scala.concurrent.Future;

import java.util.Scanner;

public class BotStart {

    public static void main(String[] args) {
        // create system actor
        final ActorSystem<MainActor.Question> systemActor = ActorSystem.create(MainActor.create(), "MainActor");

        // for terminated
        final Future<Done> workDone = systemActor.whenTerminated();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, ask your question.");
        do {
            // wait message
            String message = scanner.nextLine();
            systemActor.tell(new MainActor.Question(message));
        } while (!workDone.isCompleted());
    }
}
