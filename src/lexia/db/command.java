/**
 * Structure of command and response
 *
 * @author KLM
 */

package lexia.db;

public class command {
    public String arg;
    public String reply;
    
    @Override 
    public String toString() {
        return(String.format("ARG: %s REPLY: %s", this.arg, this.reply));
    }
}
