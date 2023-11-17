/**
 * Sets the bot's status
 *
 * @author KLM
 */

package lexia;

import static lexia.Lexia.owner;
import static lexia.MessageHandler.clr;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import java.util.Arrays;
import java.util.List;

public class Status {

    public static boolean TrySet(MessageCreateEvent e) {
        String idInput = e.getMember().get().getId().toString();
        idInput = idInput.replace("Snowflake{","");
        idInput = idInput.replace("}","");
        if(!idInput.equals(owner)){// Only owner can change bot's status.
            return false;
        }
        String a = e.getMessage().getContent();
        a = a.replace(";status", "");
        if(!a.equals("")){// Called without argument
            a = clr(a);
            String valid[] = {"on","online","away","idle","dist","disturb","inv","invis","invisible","off","offline"};
            List<String> list = Arrays.asList(valid);
            
            if(list.contains(a)){
                if(a.equals(valid[0]) || a.equals(valid[1])) { // Online
                    set(e.getClient(), "ONLINE");
                } else if(a.equals(valid[2]) || a.equals(valid[3])) { // Idle
                    set(e.getClient(), "IDLE");
                } else if(a.equals(valid[4]) || a.equals(valid[5])) { // Do not disturb
                    set(e.getClient(), "DISTURB");
                } else if(a.equals(valid[6]) || a.equals(valid[7]) || a.equals(valid[8]) || a.equals(valid[9]) || a.equals(valid[10])) { // Invisible
                    set(e.getClient(), "INVISIBLE");
                } else { // No valid option
                    return false;
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    // Generates random status
    public static void set() {
        
    }
    
    // Sets only presence
    public static void set(GatewayDiscordClient gateway, String Mode) {
        switch(Mode){
            case "ONLINE":
                gateway.updatePresence(ClientPresence.online()).block();
                break;
            case "IDLE":
                gateway.updatePresence(ClientPresence.idle()).block();
                break;
            case "DISTURB":
                gateway.updatePresence(ClientPresence.doNotDisturb()).block();
                break;
            case "INVISIBLE":
                gateway.updatePresence(ClientPresence.invisible()).block();
                break;
            default:
                gateway.updatePresence(ClientPresence.invisible()).block();
                break;
        }
    }
    
    // Sets presence with activity
    public static void set(GatewayDiscordClient gateway, String Mode, String acMode, String activity) {
        ClientActivity acty = null;
        boolean isset = false;
        
        if(!activity.isEmpty()) {
            isset = true;
            switch(acMode){
                case "WATCH":
                    acty = ClientActivity.watching(activity);
                    break;
                case "LISTEN":
                    acty = ClientActivity.listening(activity);
                    break;
                case "PLAY":
                    acty = ClientActivity.playing(activity);
                    break;
                case "STREAM":
                    acty = ClientActivity.streaming(activity, "URL");
                    break;
                case "COMPETE":
                    acty = ClientActivity.competing(activity);
                    break;
                default:
                    isset = false;
                    break;
            }
        } else {
            isset = false;
        }

        switch(Mode){
            case "ONLINE":
                if(isset) {
                    gateway.updatePresence(ClientPresence.online(acty)).block();
                } else {
                    gateway.updatePresence(ClientPresence.online()).block();
                }
                break;
            case "IDLE":
                if(isset) {
                    gateway.updatePresence(ClientPresence.idle(acty));
                } else {
                    gateway.updatePresence(ClientPresence.idle());
                }
                break;
            case "DISTURB":
                if(isset) {
                    gateway.updatePresence(ClientPresence.doNotDisturb(acty));
                } else {
                    gateway.updatePresence(ClientPresence.doNotDisturb());        
                }
                break;
            case "INVISIBLE":
                gateway.updatePresence(ClientPresence.invisible());
                break;
            default:
                gateway.updatePresence(ClientPresence.invisible());
                break;
        }
    }
}
