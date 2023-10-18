/**
 *
 * @author KLM
 */

package lexia.commands;

import static lexia.Lexia.owner;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.interaction.GuildCommandRegistrar;
import java.util.Collections;
import reactor.core.publisher.Mono;

public class SlashBadge {
    public static InteractionApplicationCommandCallbackReplyMono send(ChatInputInteractionEvent event, GatewayDiscordClient gateway){
        //SEND
        //event.getInteraction().getUser().getDiscriminator();
        //event.getInteraction().getUser().getTag();//NAME#DISC
        //event.getInteraction().getUser().getUsername();
        //event.getInteraction().getUser().isBot();
        //event.getInteraction().getUser().getId().toString() //ID of user, who sent that message in format: Snowflake{496020325147082777}
        //gateway.getSelfId().toString(); //Bot Self ID = 929021388684152902
        
        String idInput = event.getInteraction().getUser().getId().toString();
        idInput = idInput.replace("Snowflake{","");
        idInput = idInput.replace("}","");
        
        if(idInput.equals(owner)) {
            return event.reply("Yahaloo!");
        } else {
            return event.reply("Nope!");
        }
    }
    
    public static void init(GatewayDiscordClient gateway, String server){    
        //BUILD
        ApplicationCommandRequest badgeCommand = ApplicationCommandRequest.builder()
                .name("badge")
                .description("Claim Active developer's badge")
                .build();
        //REGISTER
        GuildCommandRegistrar.create(gateway.getRestClient(), Collections.singletonList(badgeCommand))
                .registerCommands(Snowflake.of(server))
                .onErrorResume(e -> Mono.empty())
                .blockLast();
    
        /*
        //GLOBAL COMMAND
        gateway.getRestClient().getApplicationService()
            .createGlobalApplicationCommand(applicationId, greetCmdRequest)
            .subscribe();
        */
        /*
        //SERVER COMMAND
        gateway.getRestClient().getApplicationService()
            .createGuildApplicationCommand(applicationId, guildId, greetCmdRequest)
            .subscribe();
        */
    }
}
