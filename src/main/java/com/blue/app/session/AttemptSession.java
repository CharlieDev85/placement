package com.blue.app.session;

import com.blue.app.model.Attempt;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class AttemptSession {
    private Attempt attempt;

    public Attempt getAttempt() {
        return attempt;
    }

    public void setAttempt(Attempt attempt) {
        this.attempt = attempt;
    }

    public void clear() {
        attempt = null;
    }
    public boolean isStarted() {
        return attempt != null;
    }
    public boolean isCompleted() {
        return attempt != null && attempt.getCompletedAt() != null;
    }
}
