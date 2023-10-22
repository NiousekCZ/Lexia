/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexia.player;

/**
 *
 * @author KLM
 */


    
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.object.entity.Message;
import discord4j.voice.AudioProvider;


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
        player.setVolume(100);
    }
        
        public void play(Message m){
            String a = m.getContent().substring(5);
            while(0x20 == a.charAt(0)){ // detect if space is left behind on begin
                a = a.substring(1);
            }
 
            playerManager.loadItem(a, scheduler);
        }
           
}
