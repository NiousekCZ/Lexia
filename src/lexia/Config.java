/**
 * Loads configuration file.
 *
 * @author KLM
 */

package lexia;

import static lexia.Lexia.owner;
import static lexia.Lexia.prefix;
import static lexia.Lexia.token;
import static lexia.Lexia.server;

import java.io.FileInputStream;
import java.util.Properties;
import static lexia.Lexia.filepath;

public class Config {
    public static void Load(){
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
}
