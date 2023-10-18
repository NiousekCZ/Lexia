/**
 *
 * @author KLM
 */

package lexia;

import lexia.commands.SlashAbuse;
import lexia.commands.SlashRandom;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;
import lexia.commands.SlashBadge;

public class SlashHandler {
    
    private static GatewayDiscordClient gateway;
    private static String server;
    
    SlashHandler(GatewayDiscordClient gw, String srv){
        gateway = gw;
        server = srv;
        //Register commands
        
        
        SlashBadge.init(gateway, server);
        //SlashAbuse.init(gateway, server);
        //SlashRandom.init(gateway, server);
        //rewrites ???
    }
    
    public InteractionApplicationCommandCallbackReplyMono sendSlash(String arg, ChatInputInteractionEvent e){
        // Run Slash commands
        if(arg.equals("random")){
            return SlashRandom.send(e);
        } else if(arg.equals("abuse")){
            return SlashAbuse.send(e);
        } else if(arg.equals("badge")){
            return SlashBadge.send(e, gateway);
        } else {
            return null;
        }
    }
}
