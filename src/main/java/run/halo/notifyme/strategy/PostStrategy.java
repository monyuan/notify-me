package run.halo.notifyme.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ExtensionClient;
import run.halo.notifyme.domain.NotifyMe;
import run.halo.notifyme.domain.PushDo;
import run.halo.notifyme.event.NotifyBaseEvent;
import run.halo.notifyme.util.PushUtil;
import java.util.Optional;


@Component
@Slf4j
@RequiredArgsConstructor
public class PostStrategy implements NotifyStrategy {
    private final ExtensionClient client;

    private final PushUtil pushUtil;

    @Override
    public void process(NotifyBaseEvent event, NotifyMe setting) {
        Extension extension = event.getExtension();

        try {
            Thread.sleep(1000);  //这里有个问题，通知是halo先发的，内部还没有完成文章发布，导致获取的是空的，只能先等待1秒
            Optional<Post> postInfo = client.fetch(Post.class, extension.getMetadata().getName());
            postInfo.ifPresent(post -> {
                if (post.getStatus() == null) {
                    process(event, setting);
                } else {
                    if (post.getStatus().getPhase().equals("DRAFT") && !post.getSpec().getOwner()
                        .equals("admin")) { // 管理员的不管
                        audits(post, setting);
                    }
                    if (post.getStatus().getPhase().equals("PUBLISHED")) {
                        publish(post, setting);
                    }
                }
            });
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }

    }

    private void publish(Post post, NotifyMe setting) { // 文章发布通知
        if (setting.getPostStatus()) {
            String title = "有新文章: " + post.getSpec().getTitle();
            String content = "您的站点有新文章发布，文章链接: " + String.format("[文章链接](%s)",
                setting.getSiteUrl() + post.getStatus().getPermalink());
            push(title, content, setting);
        }
    }

    private void audits(Post post, NotifyMe setting) { // 文章审核通知
        if (setting.getPostAuditsStatus()) {
            String title = "有新文章等待审核: " + post.getSpec().getTitle();
            String content = "您的站点有新文文章等待审核，\n\n " + String.format("[立刻审核](%s)",
                setting.getSiteUrl() + "/console/posts");
            push(title, content, setting);
        }
    }

    private void push(String title, String content, NotifyMe setting) { // 推送
        PushDo pushDo = new PushDo();
        pushDo.setTitle(title);
        pushDo.setContent(content);
        pushUtil.sendRequest(pushDo, setting);
    }
}

