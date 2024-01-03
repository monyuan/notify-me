package org.xiqi.notifyme.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiqi.notifyme.domain.NotifyMe;
import org.xiqi.notifyme.domain.PushDo;
import org.xiqi.notifyme.event.NotifyBaseEvent;
import org.xiqi.notifyme.util.PushUtil;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ExtensionClient;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
@RequiredArgsConstructor
public class PostStrategy implements NotifyStrategy {
    private final ExtensionClient client;

    private final PushUtil pushUtil;

    private Integer counter = 0;

    @Override
    public void process(NotifyBaseEvent event, NotifyMe setting) {
        /*
        TODO 等halo出了文章发布后的事件，这里再改下
        这里其实是落库前的数据,目前是发布前的事件，是取不到文章链接的。
        放到异步队列里，3秒后执行，基本都会成功，不成功也无伤大雅
         */
        Extension extension = event.getExtension();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            Optional<Post> postInfo = client.fetch(Post.class, extension.getMetadata().getName());
            postInfo.ifPresent(post -> {
                if (post.getStatus().getPhase().equals("DRAFT") && !post.getSpec().getOwner()
                    .equals("admin")) { // 管理员的不管
                    audits(post, setting);
                }
                if (post.getStatus().getPhase().equals("PUBLISHED")) {
                    publish(post, setting);
                }

            });
        }, 4, TimeUnit.SECONDS);
        executorService.shutdown();
    }

    private void publish(Post post, NotifyMe setting) { // 文章发布通知
        if (setting.getPostStatus()) {
            String title = "有新文章发布";
            String content = "您的站点有新文章发布：" + String.format("[%s](%s)",
                post.getSpec().getTitle(),
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

