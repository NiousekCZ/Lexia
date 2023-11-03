/**
 *
 * @author KLM
 */

package lexia;

import static lexia.Lexia.appId;

import lexia.commands.SlashAbuse;
import lexia.commands.SlashRandom;
import lexia.commands.SlashBadge;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.InteractionApplicationCommandCallbackReplyMono;

public class SlashHandler {
    
    private static GatewayDiscordClient gateway;
    private final Long AppID;
    private final Long server; 
        
    SlashHandler(GatewayDiscordClient gw, String srv){
        gateway = gw;
        server = Long.valueOf(srv);
        AppID = Long.valueOf(appId);
        
        //Register commands
        SlashBadge.init(gateway, server, AppID);
        SlashAbuse.init(gateway, server, AppID);
        SlashRandom.init(gateway, server, AppID);
    }
    
    public InteractionApplicationCommandCallbackReplyMono sendSlash(String arg, ChatInputInteractionEvent e){
        // Run Slash commands
        switch (arg) {
            case "random":
                return SlashRandom.send(e);
            case "abuse":
                return SlashAbuse.send(e);
            case "badge":
                return SlashBadge.send(e);
            default:
                return null;
        }
        /*
        if(arg.equals("random")){
        return SlashRandom.send(e);
        } else if(arg.equals("abuse")){
        return SlashAbuse.send(e);
        } else if(arg.equals("badge")){
        return SlashBadge.send(e);
        } else {
        return null;
        }
         */
    }
}
