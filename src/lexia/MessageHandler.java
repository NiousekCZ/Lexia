/**
 *
 * @author KLM
 */

package lexia;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class MessageHandler {
    MessageHandler(String p){
         
    }
    
    public void resolve(Message msg){
        if ("!ping".equals(msg.getContent())) {
            sendout(msg, "Pong!");
        } else if (";go".equals(msg.getContent())) {
            sendout(msg, "A poøád nefunguju");
        } else if (";ok".equals(msg.getContent())) {
            sendout(msg, "I am glad it works ^^");
        } else if (";degu".equals(msg.getContent())) {
            sendout(msg, "Degu žije!");
        } else if (";help".equals(msg.getContent())) {
            sendout(msg, "HAHAHAHA! Did you expect me to help you ?");
        }
    }
    
    private void sendout(Message msg, String in){
        MessageChannel channel = msg.getChannel().block();
        channel.createMessage(in).block();
    }
    
    private String removePrefix(Message m){
        m.getContent().replace(";","");
        return(null);
    }
}
