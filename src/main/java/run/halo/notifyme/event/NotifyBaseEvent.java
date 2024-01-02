package run.halo.notifyme.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import run.halo.app.extension.Extension;

@Getter
public class NotifyBaseEvent extends ApplicationEvent {
    private final Extension extension;

    public NotifyBaseEvent(Object source, Extension extension) {
        super(source);
        this.extension = extension;
    }


    public static NotifyBaseEvent getEx(Object o, Extension extension) {
        return new NotifyBaseEvent(o,  extension);
    }

}