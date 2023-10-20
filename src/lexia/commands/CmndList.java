/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexia.commands;

import discord4j.core.object.entity.Message;
import static lexia.db.CommandsDB.db;
import lexia.db.command;
import static lexia.Lexia.prefix;

/**
 *
 * @author KLM
 */
public class CmndList {
    
    public static String show(){
        StringBuilder builder = new StringBuilder("Ok!\r\nHere are my secrets, if you really need to know:\r\n\r\n");
        for(command a : db){
            if(!a.arg.contains("_")){ // remove system commands
                builder.append((prefix + a.arg + "\r\n"));
            }
        }
        builder.append(("\r\n" + prefix + "play\r\n"));
        builder.append(("\r\nAre you happy now ?"));
        return builder.toString();
    }
}
