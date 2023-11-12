/**
 * Music player instance.
 *
 * @author KLM
 */

package lexia.player;

import static lexia.Lexia.prefix;

import static lexia.MessageHandler.clr;
    
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
    
    // Constructor
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
    
    // Tries to play requested path.
    public void play(Message m){
        String a = m.getContent().substring(5);
        a = clr(a);
        playerManager.loadItem(a, scheduler);
    }

    //TODO Adds requested track to queue.
    public void queue(Message m){
        String a = m.getContent().substring(5);
        a = clr(a);

    }

    // Sets volume on requested value.
    public boolean setVol(Message m) {
        String a = m.getContent();
        a = a.replace((prefix + "vol"), "");
        a = clr(a);
        int val = parseInt(a);
        if(val >= 0 && val <= 100) {
            player.setVolume(val);
            return true;
        }
        return false;
    }

    // Pause and Resume commands.
    public void pause(boolean p) {
        boolean status = player.isPaused();
        if((!p && status) || (p && !status)) {
            player.setPaused(!status);
        }
    }

    //TODO Tries to play next track in queue.
    public void skip() {
        player.stopTrack();
        // Get new AudioTrack
        //playerManager.loadItem(a, scheduler);
    }

    // Stops currently playing track.
    public void stop() {
        player.stopTrack();
    }

    //TODO Returns if player is currently playing. (return track? not sure what i wanted to do here)
    public boolean isPlaying() {
        AudioTrack nowPlaying = player.getPlayingTrack();
        System.out.println(nowPlaying);
        if(nowPlaying != null) {
            return true;
        }
        return false;
    }
}
