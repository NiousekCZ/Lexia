/**
 * Delete requested messages.
 *
 * @author KLM
 */
package lexia.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.retriever.EntityRetrievalStrategy;
import discord4j.discordjson.json.gateway.ImmutableMessageDeleteBulk;
import discord4j.discordjson.json.gateway.MessageDeleteBulk;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import reactor.core.publisher.Mono;

public class CmndPrune {
    
    public static void Prune(GatewayDiscordClient gateway, MessageCreateEvent e, int cnt) {
        /*
        List<Long> idlist = null;
        
        
        ImmutableMessageDeleteBulk bulk = MessageDeleteBulk.builder()
                //.addAllIds(idlist)
                .addAllIds(idlist)
                .channelId(e.getMessage().getChannelId().asString())
                .build();
        */
        Mono<Channel> a = gateway.getChannelById(Snowflake.of(e.getMessage().getChannelId().asString()));
        Mono<GuildMessageChannel> b = a.ofType(GuildMessageChannel.class);
        
        GuildMessageChannel channel = null;
        channel.getMessagesBefore(Snowflake.of(Instant.now()))
           .take(cnt)
           .map(Message::getId)
           .transform(channel::bulkDelete);
    }
}
