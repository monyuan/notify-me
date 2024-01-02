package run.halo.notifyme.listener;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ExtensionClient;
import run.halo.notifyme.domain.NotifyMe;
import run.halo.notifyme.event.NotifyBaseEvent;
import run.halo.notifyme.strategy.CommentStrategy;
import run.halo.notifyme.strategy.NotifyStrategy;
import run.halo.notifyme.strategy.PostStrategy;
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
