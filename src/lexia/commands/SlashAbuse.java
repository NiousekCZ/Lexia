/**
 *
 * @author KLM
 */

package lexia.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.interaction.GuildCommandRegistrar;
import java.util.Collections;
import reactor.core.publisher.Mono;

public class SlashAbuse {
    
    public static InteractionApplicationCommandCallbackReplyMono send(ChatInputInteractionEvent event){
        //SEND
        return event.reply("Dear " + event.getInteraction().getUser().getUsername() + ", you are moron!");
    }
    
    public static void init(GatewayDiscordClient gateway, long server, long id){    
        //BUILD
        ApplicationCommandRequest abuseCommand = ApplicationCommandRequest.builder()
                .name("abuse")
                .description("Screw You")
                .build();
        //REGISTER
        gateway.getRestClient().getApplicationService()
            .createGuildApplicationCommand( id, server, abuseCommand)
            .subscribe();   
    }
}
