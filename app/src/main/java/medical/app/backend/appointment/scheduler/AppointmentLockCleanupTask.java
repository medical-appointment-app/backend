package medical.app.backend.appointment.scheduler;

import medical.app.backend.appointment.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Periodically cancels LOCKED appointments whose hold window has expired.
 * This is a safety net; the slot-availability query already ignores expired locks
 * in real time, so users see freed slots without waiting for this task.
 */
@Component
public class AppointmentLockCleanupTask {

    private static final Logger log = LoggerFactory.getLogger(AppointmentLockCleanupTask.class);

    private final AppointmentService appointmentService;

    public AppointmentLockCleanupTask(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Scheduled(fixedDelayString = "${app.appointment.lock-cleanup-interval-ms:60000}")
    public void releaseExpiredLocks() {
        int released = appointmentService.releaseExpiredLocks();
        if (released > 0) {
            log.info("Released {} expired appointment lock(s).", released);
        }
    }
}
