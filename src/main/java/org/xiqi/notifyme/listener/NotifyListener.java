package org.xiqi.notifyme.listener;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.xiqi.notifyme.domain.NotifyMe;
import org.xiqi.notifyme.event.NotifyBaseEvent;
import org.xiqi.notifyme.strategy.CommentStrategy;
import org.xiqi.notifyme.strategy.NotifyStrategy;
import org.xiqi.notifyme.strategy.PostStrategy;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ExtensionClient;
import java.util.Optional;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class NotifyListener implements ApplicationListener<NotifyBaseEvent> {


    private final PostStrategy postStrategy;
    private final CommentStrategy commentStrategy;
    private final ExtensionClient client;


    @Override
    public void onApplicationEvent(@NonNull NotifyBaseEvent event) {
        NotifyStrategy strategy = getStrategyForExtension(event.getExtension());
        if (strategy != null) {
            Optional<NotifyMe> config = client.fetch(NotifyMe.class, "notify-config");
            config.ifPresent(c -> strategy.process(event, c));
        }
    }

    private NotifyStrategy getStrategyForExtension(Extension extension) {
        if (extension instanceof Post) {
            return postStrategy;
        }
        if (extension instanceof Comment) {
            return commentStrategy;
        }
        return null;
    }

}
