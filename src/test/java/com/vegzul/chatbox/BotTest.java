package com.vegzul.chatbox;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import com.vegzul.chatbox.actors.MainActor;
import com.vegzul.chatbox.actors.SearchActor;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.vegzul.chatbox.actors.SearchActor.*;

public class BotTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @ClassRule
    public static final TestKitJunitResource TEST_KIT = new TestKitJunitResource();

    @Test
    public void testSearchActor() {
        ActorRef<MainActor.Question> underTest = TEST_KIT.spawn(SearchActor.create(), "search");
        underTest.tell(new MainActor.Question("What you'r question?"));
        waitMessage();

        boolean check = outContent.toString().contains(NEXT_QUEST_IS_NEED);
        outContent.reset();
        Assert.assertTrue(check);

        underTest.tell(new MainActor.Question("Y"));
        waitMessage();

        check = outContent.toString().contains(NEW_QUEST);
        outContent.reset();
        Assert.assertTrue(check);
    }

    private void waitMessage() {
        while (outContent.toString().equals("")) {
            // bad code
        }
    }
}
