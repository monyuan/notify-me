package org.xiqi.notifyme.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.xiqi.notifyme.domain.NotifyMe;
import org.xiqi.notifyme.domain.PushDo;
import org.xiqi.notifyme.event.NotifyBaseEvent;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Post;
import java.util.Optional;
import run.halo.app.extension.ExtensionClient;
import org.xiqi.notifyme.util.PushUtil;

@Component
@RequiredArgsConstructor
public class CommentStrategy implements NotifyStrategy {
    private final ExtensionClient client;
    private final PushUtil pushUtil;

    @Override
    public void process(NotifyBaseEvent event, NotifyMe setting) {
        Comment commentInfo = (Comment) event.getExtension();
        Comment.CommentSpec commentSpec = commentInfo.getSpec();
        if (!commentSpec.getOwner().getName().equals("admin")) {
            // 不是管理员的话就推送通知
            String postMetaName = commentSpec.getSubjectRef().getName();
            Optional<Post> postInfo = client.fetch(Post.class, postMetaName);
            postInfo.ifPresent(post -> {
                if (!commentSpec.getApproved()) { //等待审核的
                    audits(post, setting, commentSpec);
                } else {
                    publish(post, setting, commentSpec);
                }
            });

        }
    }

    private void publish(Post post, NotifyMe setting, Comment.CommentSpec commentSpec) { // 评论发布通知
        if (setting.getCommentStatus()) {
            String title = String.format("《%s》上有新评论", post.getSpec().getTitle());
            String content = String.format("%s说: %s \n\n[查看评论](%s)",
                commentSpec.getOwner().getDisplayName(),
                commentSpec.getContent(),
                setting.getSiteUrl() + post.getStatus().getPermalink()
            );
            push(title, content, setting);
        }
    }

    private void audits(Post post, NotifyMe setting, Comment.CommentSpec commentSpec) { // 评论审核通知
        if (setting.getCommentAuditsStatus()) {
            String title = String.format("《%s》上有新评论等待审核", post.getSpec().getTitle());
            String content = String.format("%s说: %s \n\n[现在去审核](%s)",
                commentSpec.getOwner().getDisplayName(),
                commentSpec.getContent(),
                setting.getSiteUrl() + "/console/comments"
            );
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
