package rocks.javezki.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import rocks.javezki.MathTopics.MathTopics;
import rocks.javezki.MathTopics.Topics;

public class Start extends Command {

    private EventWaiter waiter;

    private static CommandEvent commandEvent;

    public Start(EventWaiter waiter) {
        this.waiter = waiter;
        this.name = "start";
        this.help = "Starts the math stuffs";
    }

    @Override
    protected void execute(CommandEvent event) {
        commandEvent = event;

        event.reply("Please select your topic:");
        event.reply(Topics.displayMathtopics());

        waiter.waitForEvent(MessageReceivedEvent.class, e -> e.getAuthor().equals(event.getAuthor())
                && e.getChannel().equals(event.getChannel())
                && !e.getMessage().equals(event.getMessage()),
                e -> {
                    String rawMessage = e.getMessage().getContentRaw().toLowerCase();

                    try {
                        Topics.getMathtopics().get(Integer.parseInt(rawMessage) - 1).run();
                    } catch (NumberFormatException ex) {
                        event.reply("Please list the number or the name of the topic!");
                    } catch (IndexOutOfBoundsException ex) {
                        event.reply("Not a valid option!");
                    }

                    for (MathTopics topic : Topics.getMathtopics()) {
                        if (rawMessage.equals(topic.getTitle())) {
                            topic.run();
                            return;
                        }
                    }
                });
    }

    public static CommandEvent getCommandEvent() {
        return commandEvent;
    }

}
