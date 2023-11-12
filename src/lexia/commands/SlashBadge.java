/**
 * Default command to keep 'Active developer badge'.
 *
 * @author KLM
 */

package lexia.commands;

import static lexia.Lexia.owner;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandRequest;

public class SlashBadge {
    
    public static InteractionApplicationCommandCallbackReplyMono send(ChatInputInteractionEvent event) {
        //SEND
        String idInput = event.getInteraction().getUser().getId().toString();
        idInput = idInput.replace("Snowflake{","");
        idInput = idInput.replace("}","");
        
        if(idInput.equals(owner)) {
            return event.reply("Yahaloo!");
        } else {
            return event.reply("Nope!");
        }
    }
    
    public static void init(GatewayDiscordClient gateway, long server, long id) {    
        //BUILD
        ApplicationCommandRequest badgeCommand = ApplicationCommandRequest.builder()
                .name("badge")
                .description("Claim Active developer's badge.")
                .build();
        
        //REGISTER
        gateway.getRestClient().getApplicationService()
            .createGuildApplicationCommand( id, server, badgeCommand)
            .subscribe();
    }
}