/**
 * This class reads commands and does what it requires.
 *
 * @author NiousekCZ
 * @author KLM
 */

package lexia;

import static lexia.Lexia.prefix;

import static lexia.db.CommandsDB.db;
import lexia.db.command;

import lexia.commands.CmndList;

import static lexia.Lexia.player;
import static lexia.player.Player.provider;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.VoiceConnection;

import static java.lang.Integer.parseInt;
import java.util.Random;

public class MessageHandler {
    
    private static boolean isInVC;
    private VoiceConnection VCn;
       
    // Constructor
    MessageHandler(){
         isInVC = false;
    }
    
    // Main method which decides what to do with normal commands. When anything is executed, returns.
    public void resolve(MessageCreateEvent event){
        Message msg = event.getMessage();
        if ("!ping".equals(msg.getContent())) {
            // Test command - ping
            sendout(msg, "Pong!");
            return;
        } else if (msg.getContent().equals((prefix + "commands"))) {
            // Command list
            sendout(msg, CmndList.show());
            ret(event);
            return;
        } else if(isForMe(msg)) {
            // Voice commands
            if(isVCCmd(msg)) {
                if(isInVC) {
                    // Leave, Volume, Pause, Resume, Stop
                    voiceCmd(event);
                    // Play
                    if(isPlayCmd(msg)) {
                        playCmd(msg);
                    }
                } else {
                    // Join
                    voiceCmd(event);
                    // Play Not
                    if(isPlayCmd(msg)) {
                        sendout(event.getMessage(), getReplyNP("_play_no_vc"));
                        ret(event);
                        return;
                    }
                }
                ret(event);
                return;
            }
            // Mentionable commands
            if (msg.getContent().contains("<@") && msg.getContent().contains(">")) {
                getMentionable(event);
                ret(event);
                return;
            }
            // Status change commands
            if (msg.getContent().contains((prefix + "status"))) {
                if(Status.TrySet(event)) {
                    sendout(msg, "Status updated.\r\nAre you happy from this little useless feature ?");
                } else {
                    sendout(msg, "Wait, You don't have permission to do that.");
                }
                ret(event);
                return;
            }
            // Just reply commands. Leave on the end of Handler.
            if(getOK(msg.getContent())){
                // Have valid reply
                sendout(msg, getReply(msg.getContent()));
            } else {
                // Prefix detected - no valid reply option.
                sendout(msg, "Hmm ?\r\nDo you want something?\r\nIf no, then dont use `" + prefix + "`.");
            }
            ret(event);
            return;
        } else {
            // Command was not for me. :(
        }
        // Debug place - keep clear
        
    }
    
    // Deletes command which was issued from channel. Use before 'return' statement.
    private void ret(MessageCreateEvent e) {
        e.getClient().getRestClient().getChannelService().deleteMessage(e.getMessage().getChannelId().asLong(), e.getMessage().getId().asLong(), "Lexia Automatic Command Cleanup").block();
    }
    
    // Send message
    private static void sendout(Message msg, String in){
        MessageChannel channel = msg.getChannel().block();
        channel.createMessage(in).block();
    }
    
    // Reads prefix
    private static boolean isForMe(Message m){
        if(!m.getAuthor().get().isBot()) {// Otherwise breaks when sending command list -> tries to execute them with invalid values
            return m.getContent().contains(prefix);
        }
        return false;
    }
    
    // Returns node reply for asked argument.
    private static String getReply(String k){
        for(command a : db){
            if((prefix + a.arg).equals(k)){
                return(a.reply);
            }
        }
        return(null);
    }
    
    // Returns command reply for asked internal command - without prefix.
    private static String getReplyNP(String k){
        for(command a : db){
            if((a.arg).equals(k)){
                return(a.reply);
            }
        }
        return(null);
    }
    
    // Returns true if there is asked argument registered in db.
    private static boolean getOK(String k){
        for(command a : db){
            if((prefix + a.arg).equals(k)){
                return(true);
            }
        }
        return(false);
    }
    
    // Break down play %link command.
    private static boolean isPlayCmd(Message m){
        String match;
        try {
            match = m.getContent().substring(0, 5);
        } catch(Exception e) {
            return false;
        }
        if(match.equals(prefix + "play")){
            // Bot is not in VC
            if (!isInVC) {
                return true;
            }
            // Resolve no argument
            try {
                String a = m.getContent().substring(6);
                if(a.isEmpty()){
                    sendout(m, getReplyNP("_play_empty"));
                    return false; // has no argument
                }
                return true;
            } catch(Exception e) {
                sendout(m, getReplyNP("_play_empty"));
                return false; // has no argument
            }
        }
        return false; // has no valid syntax
    }
    
    // Makes request for audio player.
    private static void playCmd(Message m){
        String a = m.getContent().replace((prefix + "play"),""); // remove command
        while(0x20 == a.charAt(0)){ // detect if space is left behind on begin
            a = a.substring(1);
        }
        
        // Controls, if bot is using Lavaplayer suitable Java version.
        if(!jdkVersion(m)){
            return;
        }
        
        // Parse requested path and decide source.
        if(a.matches("([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?")) { // Local file regex
            //sendout(m, getReplyNP("_play_local"));
            player.play(m);
        } else if (a.contains("youtube")) {
            //sendout(m, getReplyNP("_play_youtube"));
            player.play(m);
        } else if (a.contains("spotify")) {
            //sendout(m, getReplyNP("_play_spotify"));
            sendout(m, "Spotify !NOT IMPLEMENTED!");
            //player.play(m);
        } else if (a.contains("apple")) {
            //sendout(m, getReplyNP("_play_apple"));
            sendout(m, "Apple Music !NOT IMPLEMENTED!");
            //player.play(m);
        } else {
            sendout(m, getReplyNP("_play_bad"));
        }
    }

    // Checks, if requested command is related to Voice chat.
    private static boolean isVCCmd(Message msg) {
        if(msg.getContent().contains((prefix + "join"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "leave"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "skip"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "vol"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "resume"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "pause"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "queue"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "stop"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "play"))) {
            return true;
        } else {
            return false;
        }
    }
    
    // Decides, what to do with voice commands, which are not play.
    private void voiceCmd(MessageCreateEvent event) {
        Message msg = event.getMessage();
        if(msg.getContent().equals((prefix + "join"))) {
            if(!isInVC) {
                joinVC(event);
            }
        } else if(msg.getContent().equals((prefix + "leave"))) {
            if(isInVC) {
                leaveVC();
            }
        } else if(msg.getContent().contains((prefix + "vol"))) {
            if(!player.setVol(msg)) {
                sendout(msg, "Volume: < 0; 100 >");
            }
        } else if(msg.getContent().contains((prefix + "resume"))) {
            player.pause(false);
        } else if(msg.getContent().contains((prefix + "pause"))) {
            player.pause(true);
        } else if(msg.getContent().contains((prefix + "stop"))) {
            player.stop();
        } else if(msg.getContent().contains((prefix + "skip"))) {
            player.skip();
        } else if(msg.getContent().contains((prefix + "queue"))) {
            player.queue(msg);
        } else {
            
        }
    }
    
    // Tries to join Voice chat.
    private void joinVC(MessageCreateEvent event) {
        final Member member = event.getMember().orElse(null);
        if (member != null) {
            final VoiceState voiceState = member.getVoiceState().block();
            if (voiceState != null) {
                final VoiceChannel channel = voiceState.getChannel().block();
                if (channel != null) {
                    //System.out.println(channel.getName().toString());
                    VCn = channel.join(spec -> spec.setProvider(provider)).block();
                    isInVC = true;
                }
            } else {
                // User is not in VC
                sendout(event.getMessage(), getReplyNP("_join_no_vc"));
            }
        }
    }
    
    // Disconnect from Voice chat.
    private void leaveVC() {
        VCn.disconnect().block();
        isInVC = false;
    }
    
    // NOT SURE Disconnect bot from gateway.
    public void shutdown(MessageCreateEvent event) {
        event.getClient().onDisconnect().block();
    }
    
    // Proprietal check, if Java version is supported by Lavaplayer - no other use.
    private static boolean jdkVersion(Message m) {
        String version = System.getProperty("java.version");

        int index1 = version.indexOf(".");
        int index2 = version.indexOf(".", version.indexOf(".") + 1);

        int major = parseInt(version.substring(0, index1));
        int minor = parseInt(version.substring((index1 + 1), index2));

        if(major >= 17) {
            return true;
        } else {
            sendout(m, ("Sorry.\r\nLavaplayer needs Java 17, I have only Java " + major + "." + minor + ".\r\nWill you upgrade me ?\r\nPretty please.\r\n:pleading_face:"));
            return false;
        }
    }
    
    // After removing command, removes spaces between command and argument.
    public static String clr(String a) {
        while(0x20 == a.charAt(0)){ // detect if space is left behind on begin
            a = a.substring(1);
        }
        return a;
    }
    
    // Returns random int, with specifided number of digits.
    public static int rndm(int digits) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            result.append(random.nextInt(10));
        }
        int q = parseInt(result.toString());
        return q;
    }
    
    private static void getMentionable(MessageCreateEvent e) {
        String a = e.getMessage().getContent();
        
        if(a.contains((prefix + "abuse"))) {
            a = a.replace(";abuse", "");
            a = clr(a);
            abuseUser(e, a);
        } else if(a.contains((prefix + "greet"))) {
            a = a.replace(";greet", "");
            a = clr(a);
            greetUser(e, a);
        } else {
            
        }
    }
    
    private static void abuseUser(MessageCreateEvent e, String a) {
        int q = rndm(3);
        
        if (q <= 200) {
            sendout(e.getMessage(), ("I am speechless.\r\nYou do not see something like " + a + " every day"));
        } else if (q <= 400) {
            sendout(e.getMessage(), ("Ehm, " + a + ", I always wondered how it feels to be idiot. Dont you want to share your knowledge ?"));
        } else if (q <= 600) {
            sendout(e.getMessage(), ("Hey, " + a + "!\r\n" + e.getMember().get().getMention() + " says that you are dumb. And you know what ? I have to agree with him."));
        } else if (q <= 800) {
            sendout(e.getMessage(), ("Hello, " + a + ".\r\nI sincerely hope, you are in immerse pain now.\r\nIf not, I can cause it. Just for you.:kissing_heart:"));      
        } else {
            sendout(e.getMessage(), ("Dear " + a + ", you are moron!")); 
        }
    }
    
    private static void greetUser(MessageCreateEvent e, String a) {
        int q = rndm(1);
        
        if (q <= 2) {
            sendout(e.getMessage(), ("Hi, " + a + ", I just learn how to ping. Do you like it ?\r\n:blush:"));
        } else if (q <= 4) {
            sendout(e.getMessage(), ("Welcome to hell, dear " + a + ".\r\nEnjoy your stay here, because you won't be leaving anytime soon. Everyone has anticipated you.:heart_on_fire:"));
        } else {
            sendout(e.getMessage(), ("Hello, " + a + ".\r\n:blush:"));
        }
    }
    
}
