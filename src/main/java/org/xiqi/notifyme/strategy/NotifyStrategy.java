package org.xiqi.notifyme.strategy;

import org.xiqi.notifyme.domain.NotifyMe;
import org.xiqi.notifyme.event.NotifyBaseEvent;

public interface NotifyStrategy {
    void process(NotifyBaseEvent event,
                 NotifyMe setting);
}
