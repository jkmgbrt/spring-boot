package jkmgbrt.springboot.graphql;

import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class TraceIdController {

    private final Tracer tracer;

    public TraceIdController(Tracer tracer) {
        this.tracer = tracer;
    }

    @QueryMapping
    public String traceId() {
        log.info("Returning traceId");
        return tracer.currentSpan().context().traceId();
    }
}