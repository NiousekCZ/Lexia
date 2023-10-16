/**
 * Lexia 
 * discord bot
 *
 * @author NiousekCZ
 * @author KLM
 */

package lexia;


import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.interaction.GuildCommandRegistrar;
import java.util.Collections;
import java.util.Random;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;


public class Lexia {
    
    public static String fp = "config.txt";
    protected static String token;
    protected static String owner;
    protected static String prefix;
    protected static String server;

    //private final GatewayIntent[] = ;
    
    public static void main(String[] args){
        Config.Load();
        
        DiscordClient client = DiscordClient.create(token);
        GatewayDiscordClient gateway = client.login().block();
        
        
        gateway.on(MessageCreateEvent.class).subscribe(event -> {
        Message message = event.getMessage();
        if ("!ping".equals(message.getContent())) {
            MessageChannel channel = message.getChannel().block();
            channel.createMessage("Pong!").block();
            }
        if (";go".equals(message.getContent())) {
            MessageChannel channel = message.getChannel().block();
            channel.createMessage("A pořád nefunguju").block();
            }
        });
        
        
        // Slash commands
        ApplicationCommandRequest randomCommand = ApplicationCommandRequest.builder()
                .name("random")
                .description("Send a random number")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("digits")
                        .description("Number of digits (1-20)")
                        .type(ApplicationCommandOption.Type.INTEGER.getValue())
                        .required(false)
                        .build())
                .build();

        GuildCommandRegistrar.create(gateway.getRestClient(), Collections.singletonList(randomCommand))
                .registerCommands(Snowflake.of(server))
                .onErrorResume(e -> Mono.empty())
                .blockLast();

        gateway.on(new ReactiveEventAdapter() {

            private final Random random = new Random();

            @Override
            public Publisher<?> onChatInputInteraction(ChatInputInteractionEvent event) {
                if (event.getCommandName().equals("random")) {
                    String result = result(random, event.getInteraction().getCommandInteraction().get());
                    return event.reply(result);
                }
                return Mono.empty();
            }
        }).blockLast();
        
        gateway.onDisconnect().block();

    }
    
    private static String result(Random random, ApplicationCommandInteraction acid) {
        long digits = acid.getOption("digits")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong)
                .orElse(1L);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Math.max(1, Math.min(20, digits)); i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
    
}
