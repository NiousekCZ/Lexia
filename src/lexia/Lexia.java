/**
 * Lexia 
 * discord bot
 *
 * @author NiousekCZ
 * @author KLM
 */

package lexia;

import lexia.player.Player;
import static lexia.commands.CmndPrune.Prune;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import java.io.IOException;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class Lexia {
    
    private static final String fp_cfg = "cfg.txt";
    private static final String fp_cmd = "cmd.txt";
    protected static String token;
    public static String owner;
    public static String prefix;
    protected static String server;
    public static String appId;

    protected static MessageHandler Handler;
    protected static SlashHandler Slash;
    public static Player player;
    
    //Logger for this class
    protected static final Logger log = LoggerFactory.getLogger(Lexia.class);
    
    public static void main(String[] args) throws IOException, Exception{
        
        InitializeLogger();
        
        //Load configuration
        Config.Load(fp_cfg);
        Config.LoadCommands(fp_cmd);
        
        //Login to Discord
        DiscordClient client = DiscordClient.create(token);
        GatewayDiscordClient gateway = client.login().block();
        
        if (gateway == null ) {
            log.error("Failed to connect to gateway!");
            return;
        }
        
        gateway.on(ReadyEvent.class).doOnNext(e -> log.info("Logged in as: " + e.getSelf().getTag())).then().block();
        
        //Set Presence
        Status.set(gateway, "DISTURB");
        
        //Initialize LavaPlayer
        player = new Player();
        
        //Initialize handlers
        Handler = new MessageHandler();
        Slash = new SlashHandler(gateway, server);

        //Bot commands - activates even on slashes
        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            Handler.resolve(event);
            if (event.getMessage().getContent().equals((prefix + "prune"))) { // Voice commands but no in VC
                Prune(gateway, event, 1);
            }
        });
       
        //Discord integrated commands - slashes
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
        /*
        gateway.on(new messageDeleteBulk() {
        
        });*/
        
        gateway.onDisconnect().block(); //Manual -> MessageHandler.shutdown       
    }  
    private static void InitializeLogger(){
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            configurator.doConfigure("slf4j-logback.xml");
        } catch (JoranException je) {
            throw new RuntimeException("Failed to configure loggers, shutting down...", je);
        }
    }
}
