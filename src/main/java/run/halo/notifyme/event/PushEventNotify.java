package run.halo.notifyme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.Watcher;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushEventNotify implements Watcher, InitializingBean {

    private final ExtensionClient client;
    private final ApplicationEventPublisher eventPublisher;

    private Runnable disposeHook;
    private volatile boolean disposed = false;

    @Override
    public void onAdd(Extension extension) {
        eventPublisher.publishEvent(NotifyBaseEvent.getEx(this, extension));
    }

    @Override
    public void registerDisposeHook(Runnable dispose) {
        this.disposeHook = dispose;
    }

    @Override
    public boolean isDisposed() {
        return this.disposed;
    }

    @Override
    public void afterPropertiesSet() {
        client.watch(this);
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        this.disposed = true;
        if (this.disposeHook != null) {
            this.disposeHook.run();
        }
    }
}
