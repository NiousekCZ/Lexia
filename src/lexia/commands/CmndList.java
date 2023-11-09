/**
 * Returns list of registered commands
 *
 * @author KLM
 */

package lexia.commands;

import static lexia.db.CommandsDB.db;
import lexia.db.command;
import static lexia.Lexia.prefix;

public class CmndList {
    
    public static String show(){
        StringBuilder builder = new StringBuilder("Ok!\r\nHere are my secrets, if you really need to know:\r\n\r\n");
        for(command a : db){
            if(!a.arg.contains("_")){ // remove system commands
                builder.append((prefix + a.arg + "\r\n"));
            }
        }
        
        builder.append(("\r\nYou want to talk to me ? What do you think about yourself ?\r\n"));
        builder.append(("\r\n" + prefix + "join\r\n"));
        builder.append((prefix + "leave\r\n"));
        
        builder.append(("\r\n" + prefix + "play\r\n"));
        
        builder.append(("\r\n" + prefix + "vol\r\n"));
        
        builder.append(("\r\n" + prefix + "stop\r\n"));
        builder.append((prefix + "skip - not working -> stop\r\n"));
        builder.append((prefix + "pause\r\n"));
        builder.append((prefix + "resume\r\n"));
        
        builder.append(("\r\nAre you happy now ?"));
        
        return builder.toString();
    }
}
