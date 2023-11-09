/**
 *
 * @author KLM
 */

package lexia.player;

import static lexia.Lexia.prefix;
    
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.object.entity.Message;
import discord4j.voice.AudioProvider;
import static java.lang.Integer.parseInt;

public class Player {
    
    public static AudioProvider provider;
    public static AudioPlayer player;
    public static AudioPlayerManager playerManager;
    public static TrackScheduler scheduler;
    
    public Player() {
        playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        player = playerManager.createPlayer();
        provider = new LavaPlayerAudioProvider(player);
        scheduler = new TrackScheduler(player);
        //player.addListener(scheduler);
        player.setVolume(25);
    }
        
        public void play(Message m){
            String a = m.getContent().substring(5);
            while(0x20 == a.charAt(0)){ // detect if space is left behind on begin
                a = a.substring(1);
            }
            playerManager.loadItem(a, scheduler);
        }
        
        public void queue(Message m){
            String a = m.getContent().substring(5);
            while(0x20 == a.charAt(0)){ // detect if space is left behind on begin
                a = a.substring(1);
            }
            
        }
        
        public boolean setVol(Message m) {
            String a = m.getContent();
            a = a.replace((prefix + "vol"), "");
            while(0x20 == a.charAt(0)){ // detect if space is left behind on begin
                a = a.substring(1);
            }
            int val = parseInt(a);
            if(val >= 0 && val <= 100) {
                player.setVolume(val);
                return true;
            }
            return false;
        }
           
        public void pause(boolean p) {
            boolean status = player.isPaused();
            if((!p && status) || (p && !status)) {
                player.setPaused(!status);
            }
        }
        
        public void skip() {
            player.stopTrack();
            // Get new AudioTrack
            //playerManager.loadItem(a, scheduler);
        }
        
        public void stop() {
            player.stopTrack();
        }
        
        public boolean isPlaying() {
            AudioTrack nowPlaying = player.getPlayingTrack();
            System.out.println(nowPlaying);
            if(nowPlaying != null) {
                return true;
            }
            return false;
        }
}
