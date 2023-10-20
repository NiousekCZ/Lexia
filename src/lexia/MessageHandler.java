/**
 *
 * @author KLM
 */

package lexia;

import static lexia.Lexia.prefix;

import static lexia.db.CommandsDB.db;
import lexia.db.command;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;


public class MessageHandler {
    MessageHandler(){
         
    }
    
    public void resolve(Message msg){
        if ("!ping".equals(msg.getContent())) {
            sendout(msg, "Pong!");
        } else if(isPlayCmd(msg)) {
            playCmd(msg);
        } else if(isForMe(msg)) {
            if(getOK(msg.getContent())){
                sendout(msg, getReply(msg.getContent()));
            }
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
                    sendout(m, getReplyNP("play_empty"));
                    return false; // has no argument
                }
                return true;
            } catch(Exception e) {
                sendout(m, getReplyNP("play_empty"));
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
            sendout(m, getReplyNP("play_local"));
        } else if (a.contains("youtube")) {
            sendout(m, getReplyNP("play_youtube"));
        } else if (a.contains("spotify")) {
            sendout(m, getReplyNP("play_spotify"));
        } else if (a.contains("apple")) {
            sendout(m, getReplyNP("play_apple"));
        } else {
            sendout(m, getReplyNP("play_bad"));
        }
        
    }
    
}
