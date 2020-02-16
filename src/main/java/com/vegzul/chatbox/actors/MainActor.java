package com.vegzul.chatbox.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class MainActor extends AbstractBehavior<MainActor.Question> {

    public static class Question {
        public final String question;

        public Question(String question) {
            this.question = question;
        }
    }

    private final ActorRef<Question> searchActor;

    public static Behavior<Question> create() {
        return Behaviors.setup(MainActor::new);
    }

    @Override
    public Receive<Question> createReceive() {
        return newReceiveBuilder().onMessage(Question.class, this::onDoQuestion).build();
    }

    private MainActor(ActorContext<Question> context) {
        super(context);
        // create search actor
        searchActor = context.spawn(SearchActor.create(), "search");
    }

    private Behavior<Question> onDoQuestion(Question command) {
        searchActor.tell(new Question(command.question));
        return this;
    }
}
