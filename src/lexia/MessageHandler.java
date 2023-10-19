/**
 *
 * @author KLM
 */

package lexia;

import static lexia.Lexia.prefix;

import static lexia.db.CommandsDB.db;
import lexia.db.node;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;


public class MessageHandler {
    MessageHandler(){
         
    }
    
    public void resolve(Message msg){
        if(isForMe(msg)) {
            if(getOK(msg.getContent())){
                sendout(msg, getReply(msg.getContent()));
            }
        }
    }
    
    //Send message
    private void sendout(Message msg, String in){
        MessageChannel channel = msg.getChannel().block();
        channel.createMessage(in).block();
    }
    
    //Reads prefix
    private boolean isForMe(Message m){
        if(m.getContent().contains(prefix)) {
            return true;
        }
        return false;
    }
    
    //returns node reply for asked arg
    public static String getReply(String k){
        for(node a : db){
            if((prefix + a.arg).equals(k)){
                return(a.reply);
            }
        }
        return(null);
    }
    
    //returns true if there is asked arg registered in db
    public static boolean getOK(String k){
        for(node a : db){
            if((prefix + a.arg).equals(k)){
                return(true);
            }
        }
        return(false);
    }
}
