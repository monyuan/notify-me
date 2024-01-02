package run.halo.notifyme.strategy;

import run.halo.notifyme.domain.NotifyMe;
import run.halo.notifyme.event.NotifyBaseEvent;

public interface NotifyStrategy {
    void process(NotifyBaseEvent event,
        NotifyMe setting);
}
