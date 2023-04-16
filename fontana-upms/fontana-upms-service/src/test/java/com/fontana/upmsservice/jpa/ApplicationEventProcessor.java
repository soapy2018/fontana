package com.fontana.upmsservice.jpa;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @className: ApplicationEventProcessor
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/2/22 15:09
 */
@Component
public class ApplicationEventProcessor {

    @EventListener(condition = "#jpaUserEvent.getState().toString() == 'SUCCEED'")
    public void jpaUserCreated(JpaUser.JpaUserEvent jpaUserEvent) {
        System.err.println("JpaUserEvent-event succeed0:" + jpaUserEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, condition = "#jpaUserEvent.getState().toString() == 'SUCCEED'")
    public void jpaUserCreatedAfter(JpaUser.JpaUserEvent jpaUserEvent) {
        System.err.println("JpaUserEvent-event succeed1:" + jpaUserEvent);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, condition = "#jpaUserEvent.getState().toString() == 'SUCCEED'")
    public void jpaUserCreatedBefore(JpaUser.JpaUserEvent jpaUserEvent) {
        System.err.println("JpaUserEvent-event succeed2:" + jpaUserEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void jpaUserCreatedFailed(JpaUser.JpaUserEvent jpaUserEvent) {
        System.err.println("JpaUserEvent-event failed:" + jpaUserEvent);
    }
}


