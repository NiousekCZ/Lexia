/**
 *
 * @author KLM
 */

package lexia.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler implements AudioLoadResultHandler {
    
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    
    public TrackScheduler(final AudioPlayer player) {
        this.player = player;
    }
    
    public void queue(AudioTrack track) {
        if(!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    @Override
    public void trackLoaded(AudioTrack at) {
        player.playTrack(at);
    }

    @Override
    public void playlistLoaded(AudioPlaylist ap) {}
    @Override
    public void noMatches() {}
    @Override
    public void loadFailed(FriendlyException fe) {}
}
