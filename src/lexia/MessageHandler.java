/**
 *
 * @author KLM
 */

package lexia;

import static lexia.Lexia.prefix;
import static lexia.db.CommandsDB.db;
import lexia.db.command;
import static lexia.Lexia.provider;
import lexia.commands.CmndList;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.VoiceConnection;

public class MessageHandler {
    
    private VoiceConnection VCn;
            
    MessageHandler(){
         
    }
    
    public void resolve(MessageCreateEvent event){
        Message msg = event.getMessage();
        if ("!ping".equals(msg.getContent())) {
            sendout(msg, "Pong!");
        } else if(isVCCmd(msg)) {
            voiceCmd(event);
        } else if(isPlayCmd(msg)) {
            playCmd(msg);
        } else if(isForMe(msg)) {
            if(getOK(msg.getContent())){
                sendout(msg, getReply(msg.getContent()));
            } else if (msg.getContent().equals((prefix + "commands"))) {
                sendout(msg, CmndList.show());
            }
        } else {
            
        }
    }
    
    //Send message
    private static void sendout(Message msg, String in){
        MessageChannel channel = msg.getChannel().block();
        channel.createMessage(in).block();
    }
    
    //Reads prefix
    private static boolean isForMe(Message m){
        if(m.getContent().contains(prefix)) {
            return true;
        }
        return false;
    }
    
    //returns node reply for asked arg
    private static String getReply(String k){
        for(command a : db){
            if((prefix + a.arg).equals(k)){
                return(a.reply);
            }
        }
        return(null);
    }
    
    //returns node reply for asked internal command - without prefix
    private static String getReplyNP(String k){
        for(command a : db){
            if((a.arg).equals(k)){
                return(a.reply);
            }
        }
        return(null);
    }
    
    //returns true if there is asked arg registered in db
    private static boolean getOK(String k){
        for(command a : db){
            if((prefix + a.arg).equals(k)){
                return(true);
            }
        }
        return(false);
    }
    
    //Break down ;play %link command
    private static boolean isPlayCmd(Message m){
        String match = null;
        try {
            match = m.getContent().substring(0, 5);
        } catch(Exception e) {
            return false;
        }
        if(match.equals(prefix + "play")){
            // resolve no argument
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
    
    private static void playCmd(Message m){
        String a = m.getContent().replace((prefix + "play"),""); // remove command
        while(0x20 == a.charAt(0)){ // detect if space is left behind on begin
            a = a.substring(1);
        }
        
        if(a.matches("([a-zA-Z]:)?(\\\\[a-zA-Z0-9_.-]+)+\\\\?")) { // Local file regex
            sendout(m, getReplyNP("_play_local"));
        } else if (a.contains("youtube")) {
            sendout(m, getReplyNP("_play_youtube"));
        } else if (a.contains("spotify")) {
            sendout(m, getReplyNP("_play_spotify"));
        } else if (a.contains("apple")) {
            sendout(m, getReplyNP("_play_apple"));
        } else {
            sendout(m, getReplyNP("_play_bad"));
        }
        
    }

    private static boolean isVCCmd(Message msg) {
        if(msg.getContent().contains((prefix + "join"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "leave"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "skip"))) {
            return true;
        } else if(msg.getContent().contains((prefix + "vol"))) {
            return true;
        } else {
            return false;
        }
    }
    
    private void voiceCmd(MessageCreateEvent event) {
        Message msg = event.getMessage();
        if(msg.getContent().contains((prefix + "join"))) {
            joinVC(event);
        } else if(msg.getContent().contains((prefix + "leave"))) {
            leaveVC();
        } else if(msg.getContent().contains((prefix + "skip"))) {
            
        } else if(msg.getContent().contains((prefix + "vol"))) {
            
        } else {
            
        }
    }
    
    private void joinVC(MessageCreateEvent event) {
        final Member member = event.getMember().orElse(null);
        if (member != null) {
            final VoiceState voiceState = member.getVoiceState().block();
            if (voiceState != null) {
                final VoiceChannel channel = voiceState.getChannel().block();
                if (channel != null) { // join returns a VoiceConnection which would be required if we were
                    // adding disconnection features, but for now we are just ignoring it.
                    //channel.join(spec -> spec.setProvider()).block();
                    //System.out.println(channel.getName().toString());
                    VCn = channel.join(spec -> spec.setProvider(provider)).block();
                    
                }
            }
        }
    }
    
    private void leaveVC() {
        VCn.disconnect().block();
    }
    
}
