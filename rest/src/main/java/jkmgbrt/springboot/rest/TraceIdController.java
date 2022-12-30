package jkmgbrt.springboot.rest;

import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TraceIdController {

    private final Tracer tracer;

    public TraceIdController(Tracer tracer) {
        this.tracer = tracer;
    }

    @GetMapping("/trace-id")
    public String traceId() {
        log.info("Returning traceId");
        return tracer.currentSpan().context().traceId();
    }
}