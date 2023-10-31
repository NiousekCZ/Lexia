/**
 *
 * @author KLM
 */

package lexia.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.interaction.GuildCommandRegistrar;
import java.util.Collections;
import java.util.Random;
import reactor.core.publisher.Mono;

public class SlashRandom {
    
    public static InteractionApplicationCommandCallbackReplyMono send(ChatInputInteractionEvent event){
        //SEND
        final Random random = new Random();
        String result = result(random, event.getInteraction().getCommandInteraction().get());
        return event.reply(result);
    }

    public static void init(GatewayDiscordClient gateway, long server, long id){
        //BUILD
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
        
        //REGISTER
         gateway.getRestClient().getApplicationService()
            .createGuildApplicationCommand( id, server, randomCommand)
            .subscribe();
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

