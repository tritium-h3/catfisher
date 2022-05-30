package com.catfisher.multiarielle.controller;

import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.controller.delta.EntityChangeDelta;
import com.catfisher.multiarielle.entity.Entity;
import com.catfisher.multiarielle.model.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
@RequiredArgsConstructor
public class AiController implements Runnable {
    private final ModelServer model;

    @Value
    private static class QueuedCall implements Comparable<QueuedCall> {
        Entity entityToCall;
        long wakeupTimeMillis;
        long period;

        @Override
        public int compareTo(QueuedCall o) {
            return Long.compare(wakeupTimeMillis, o.getWakeupTimeMillis());
        }
    }

    private PriorityQueue<QueuedCall> wakeupQueue = new PriorityQueue<>();

    public void registerWaiter(Entity e, long period) {
        synchronized (wakeupQueue) {
            long currentTime = System.currentTimeMillis();
            wakeupQueue.add(new QueuedCall(e, currentTime + period, period));
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                QueuedCall nextWakeup;
                synchronized (wakeupQueue) {
                    try {
                        log.debug("Pending next waiter");
                        nextWakeup = wakeupQueue.remove();
                    } catch (NoSuchElementException exn) {
                        Thread.sleep(10);
                        continue;
                    }
                }
                log.debug("Woke up {}", nextWakeup);
                while (nextWakeup.wakeupTimeMillis > System.currentTimeMillis()) {
                    long preWaitTime = System.currentTimeMillis();
                    if (nextWakeup.wakeupTimeMillis > preWaitTime) {
                        log.debug("Waiting {} to resume", nextWakeup.wakeupTimeMillis - preWaitTime);
                        Thread.currentThread().sleep(nextWakeup.getWakeupTimeMillis() - preWaitTime);
                    }
                }
                long pretendCurrentTime = nextWakeup.getWakeupTimeMillis();
                Entity entityToCall = nextWakeup.getEntityToCall();
                long period = nextWakeup.getPeriod();
                EntityChangeDelta delta = (entityToCall.update(model.getTrueModel()));
                if (delta != null) {
                    model.applyDelta(delta);
                }
                synchronized (wakeupQueue) {
                    QueuedCall nextQueue = new QueuedCall(entityToCall, pretendCurrentTime + period, period);
                    log.debug("Enqueuing {}", nextQueue);
                    wakeupQueue.add(nextQueue);
                }
            }
        } catch (InterruptedException exn) {
            log.warn("Interrupted AI wakeup thread", exn);
        }
    }
}
