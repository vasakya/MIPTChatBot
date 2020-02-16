package com.vegzul.chatbox.actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class SearchActor extends AbstractBehavior<MainActor.Question> {

    @Override
    public Receive<MainActor.Question> createReceive() {
        return newReceiveBuilder().onMessage(MainActor.Question.class, this::onSearch).build();
    }

    public static Behavior<MainActor.Question> create() {
        return Behaviors.setup(SearchActor::new);
    }

    private SearchActor(ActorContext<MainActor.Question> context) {
        super(context);
    }

    private Behavior<MainActor.Question> onSearch(MainActor.Question command) {
        System.out.println(command.question);
        System.out.println("Ask next question? Y/n");
        return this;
    }
}
