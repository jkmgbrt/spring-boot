Sample Spring Boot 3 application for highlighting an issue with Micrometer Tracing and Open Zipkin Brave when used in a Spring for GraphQL app. The issue is that the same traceId ends up being used for all requests handled by the same Tomcat worker thread (for illustrative purposes, server.tomcat.threads.max is set to 1). For comparison, the same issue does not occur in a Spring Web RESTful app.

First, to illustrate the expected behaviour when calling a RESTful endpoint:
1. Start **RestApplication.java** (listens on port 8082)
2. Send multiple GET requests to http://localhost:8082/trace-id, which returns the traceId used for the request and also logs a simple message in the console together with traceId and spanId from MDC
3. The traceId used is unique for each request handled by the same thread, as expected

Sample log:
```
2022-12-30 13:05:15.802 +01:00 [http-nio-8082-exec-1] [traceId=63aed3fbc9e74208444057b7b660535b, spanId=444057b7b660535b] INFO  j.s.r.TraceIdController - Returning traceId
2022-12-30 13:05:16.434 +01:00 [http-nio-8082-exec-1] [traceId=63aed3fca8360a78848e041f41ce496d, spanId=848e041f41ce496d] INFO  j.s.r.TraceIdController - Returning traceId
2022-12-30 13:05:17.039 +01:00 [http-nio-8082-exec-1] [traceId=63aed3fd595a312c6961b1cc3e460a93, spanId=6961b1cc3e460a93] INFO  j.s.r.TraceIdController - Returning traceId
```
Now, to illustrate the faulty behaviour when sending a GraphQL query:
1. Start **GraphQlApplication.java** (listens on port 8081)
2. Send multiple requests using the GraphQL query below to http://localhost:8081/graphql, which returns the traceId used for the request and also logs a simple message in the console together with traceId and spanId from MDC
3. The traceId used is the same for each request handled by the same thread, expected is that traceId should be unique

Sample log:
```
2022-12-30 13:05:09.954 +01:00 [http-nio-8081-exec-1] [traceId=63aed3f5c2ae118eb36fdcbae567ee69, spanId=d7176567e4a72035] INFO  j.s.g.TraceIdController - Returning traceId
2022-12-30 13:05:10.704 +01:00 [http-nio-8081-exec-1] [traceId=63aed3f5c2ae118eb36fdcbae567ee69, spanId=6efdadb976e85418] INFO  j.s.g.TraceIdController - Returning traceId
2022-12-30 13:05:11.307 +01:00 [http-nio-8081-exec-1] [traceId=63aed3f5c2ae118eb36fdcbae567ee69, spanId=41a564c587c396f1] INFO  j.s.g.TraceIdController - Returning traceId
```

GraphQL query

```graphql
query {
  traceId
}
```
