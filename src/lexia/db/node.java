/**
 *
 * @author KLM
 */

package lexia.db;

public class node {
    public String arg;
    public String reply;
    
    @Override 
    public String toString() {
        return(String.format("%s %s",this.arg,this.reply));
    }
}
