/**
 * Loads configuration file.
 *
 * @author KLM
 */

package lexia;

import java.io.BufferedReader;
import static lexia.Lexia.owner;
import static lexia.Lexia.prefix;
import static lexia.Lexia.token;
import static lexia.Lexia.server;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import static lexia.db.CommandsDB.db;
import lexia.db.node;

public class Config {
    public static void Load(String filepath){
        try{
            Properties p = new Properties();
            p.load(new FileInputStream(filepath));
            token = p.getProperty("token");
            owner = p.getProperty("owner");
            prefix = p.getProperty("prefix");
            server = p.getProperty("server");
        }catch(Exception e){
            throw new Error("Could not load configuration.", e);
        }
    }
    
    public static void LoadCommands(String filepath) throws IOException, Exception{
        String cmd = IStoSTR(filepath);
        regexCMD(cmd);
    }
    
    private static void regexCMD(String in) throws Exception{
        //===[ HEAD ]===
        String part = in.substring(0,7);
        if(!part.equals("<begin>")){
            throw new Exception();
        }
        String nxt = in.replace("<begin>","");
        
        //===[ DATA ]===
        part = nxt.substring(0,6);
        
        //number of nodes
        int lastIndex = 0;
        int count = 0;
        while(lastIndex != -1){
            lastIndex = in.indexOf("<node>",lastIndex);
            if(lastIndex != -1){
                count++;
                lastIndex += 6;
            }
        }
        int p = nxt.indexOf("<node>", nxt.indexOf("<node>") + 1);
        String nwp = nxt.substring(0,p);//Node workplace

        int op = 0;
        int ed = 0;               
        String value = null;
        
         //resolve each node
        for(/*NIC*/;count > 0;count--){
            node tmp = new node();//Pro každou iteraci znovu inicializovat node (pokud ne, pøenášejí se hodnoty)
            if(part.equals("<node>")){
                nwp = nwp.replace("<node>","");
                
                //ARG
                op = nwp.indexOf("arg=");
                ed = nwp.indexOf(";"); 
                value = nwp.substring(op+4, ed);
                tmp.arg = value;
                value = nwp.substring(0,ed+1);
                nwp = nwp.replace(value,"");
                //REPLY
                op = nwp.indexOf("reply=");
                ed = nwp.indexOf(";"); 
                value = nwp.substring(op+6, ed);
                tmp.reply = value;
                value = nwp.substring(0,ed+1);
                nwp = nwp.replace(value,"");
                
                db.add(tmp);
                
                nwp = nxt.substring(0,p);
                nxt = nxt.replace(nwp,"");
                
                if(nxt.equals("<end>")){
                    return;
                }else{
                    p = nxt.indexOf("<node>", nxt.indexOf("<node>") + 1);
                    if(p == -1){
                        p = nxt.indexOf("<end>", nxt.indexOf("<node>") + 1);
                    }
                    nwp = nxt.substring(0,p);
                }
            }
        }
    }
    
    private static String IStoSTR(String filepath) throws FileNotFoundException, IOException{
        InputStream is = new FileInputStream(filepath);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String str;
        while((str = reader.readLine())!= null){
            sb.append(str);
        }
        return sb.toString();
    }
}
