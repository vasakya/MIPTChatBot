package com.vegzul.chatbox.actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class SearchActor extends AbstractBehavior<MainActor.Question> {

    public static final String NO_ANSWER = "N";
    public static final String NEXT_QUEST_IS_NEED = "Ask next question? Y/n";
    public static final String NEW_QUEST = "Well, please, ask your question.";
    public static final String EXIT = "Well, please press ENTER to exit.";

    private boolean isAsked = false;

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
        if (isAsked) {
            if (command.question.toUpperCase().equals(NO_ANSWER)) {
                getContext().getSystem().terminate();
                System.out.println(EXIT);
            } else {
                System.out.println(NEW_QUEST);
                isAsked = false;
            }
        } else {
            isAsked = true;
            searchRequest(command.question);
            System.out.println(NEXT_QUEST_IS_NEED);
        }

        return this;
    }

    // search answer from API
    private void searchRequest(String messege) {
        HttpPost post = new HttpPost("https://odqa.demos.ivoice.online/model");

        // add headers
        post.setHeader("User-Agent", "Mozilla");
        post.addHeader("content-type", "application/json");
        post.addHeader("accept", "application/json");

        // add request parameter, form parameters
        try {
            StringBuilder json = new StringBuilder();
            json.append("{")
                    .append("\"context\":\"")
                    .append(messege)
                    .append("\",")
                    .append("}");

            // send a JSON data
            post.setEntity(new StringEntity(json.toString()));
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)) {

                JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
                // TODO: fix to correct value
                System.out.println(jsonObject.getString("message"));
            }
        } catch (IOException ignore) {
            System.out.println("Sorry, didn't work out.");
        }
    }
}
