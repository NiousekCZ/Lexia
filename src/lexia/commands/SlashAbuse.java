/**
 *
 * @author KLM
 */

package lexia.commands;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.User;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import java.util.List;
import reactor.core.publisher.Mono;

public class SlashAbuse {
    
    public static InteractionApplicationCommandCallbackReplyMono send(ChatInputInteractionEvent event) {
        //SEND
        return event.reply(getTarget(event));
        //"Dear " + event.getInteraction().getUser().getUsername() + ", you are moron!"
        //"Hey, " + %user + "!\r\n" + %initiator + " says that you are dumb. And you know what ? I have to agree with him."
        //"Ehm, " + %user + ", I always wondered how it feels to be idiot. Dont you want to share your knowledge ?"
        //In case I have been naughty, go cry to `niousekcz`.
    }
    
    public static void init(GatewayDiscordClient gateway, long server, long id) {    
        //BUILD
        ApplicationCommandRequest abuseCommand = ApplicationCommandRequest.builder()
                .name("abuse")
                .description("Screw You!")
                /*.addOption(ApplicationCommandOptionData.builder()
                        .name("Target")
                        .description("aaaa")
                        .type(ApplicationCommandOption.Type.USER.getValue())
                        .required(false)
                        .build())*/
                .build();
        
        //REGISTER
        gateway.getRestClient().getApplicationService()
            .createGuildApplicationCommand( id, server, abuseCommand)
            .subscribe();   
    }
    
    private static String getTarget(ChatInputInteractionEvent e) {
        
        //ApplicationCommandInteraction acid = e.getInteraction().getCommandInteraction().get();
        //acid.getOption("Target");
        return("Dear " + e.getInteraction().getUser().getUsername() + ", you are moron!");
    }
    
}
