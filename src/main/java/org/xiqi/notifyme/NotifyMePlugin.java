package org.xiqi.notifyme;

import org.pf4j.PluginWrapper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.xiqi.notifyme.domain.NotifyMe;
import run.halo.app.extension.Scheme;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.extension.SchemeManager;

@Component
@EnableAsync
public class NotifyMePlugin extends BasePlugin {
    private final SchemeManager schemeManager;

    public NotifyMePlugin(PluginWrapper wrapper, SchemeManager schemeManager) {
        super(wrapper);
        this.schemeManager = schemeManager;
    }

    @Override
    public void start() {
        schemeManager.register(NotifyMe.class);
        System.out.println("插件启动成功！");
    }

    @Override
    public void stop() {
        Scheme notifyMeScheme = schemeManager.get(NotifyMe.class);
        schemeManager.unregister(notifyMeScheme);
        System.out.println("插件停止！");
    }
}
