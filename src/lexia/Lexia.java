/**
 * Lexia 
 * discord bot
 *
 * @author NiousekCZ
 * @author KLM
 */

package lexia;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.ClientPresence;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class Lexia {
    
    public static final String filepath = "config.txt";
    protected static String token;
    public static String owner;
    protected static String prefix;
    protected static String server;

    protected static MessageHandler Handler;
    protected static SlashHandler Slash;
    
    public static void main(String[] args){
        //Load configuration
        Config.Load();
        
        //Login to Discord
        DiscordClient client = DiscordClient.create(token);
        GatewayDiscordClient gateway = client.login().block();
        
        //Set Presence
        
        //gateway.updatePresence(ClientPresence.doNotDisturb()).block();
        
        Status.set(gateway, "DISTURB");

        //Initialize handlers
        Handler = new MessageHandler(prefix);
        Slash = new SlashHandler(gateway, server);
        
        //Bot commands
        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            Handler.resolve(event.getMessage());
            Status.set(gateway, "ONLINE");
        });
       
        //Discord integrated commands
        gateway.on(new ReactiveEventAdapter() {
            @Override
            public Publisher<?> onChatInputInteraction(ChatInputInteractionEvent event) {
                if (!event.getCommandName().isEmpty()){
                    if(Slash.sendSlash(event.getCommandName(), event) != null) {
                        return Slash.sendSlash(event.getCommandName(), event); //Run event
                    } else {
                        return Mono.empty(); //Command is not valid
                    }
                } else {
                    return Mono.empty(); //Command is not set
                }
            }
        }).blockLast();
        
        
        gateway.onDisconnect().block();
        
    }  
}
