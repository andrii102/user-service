package dre.userservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dre.subscriptionservice.exception.EntityNotFound;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.util.Map;

public class FeignErrorDecoder implements ErrorDecoder {

    private static final ErrorDecoder defaultErrorDecoder = new Default();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            try {
                // Try to parse the error response body
                String body = response.body().asInputStream() != null 
                    ? new String(response.body().asInputStream().readAllBytes()) 
                    : "";
                
                if (!body.isEmpty()) {
                    Map<String, Object> errorMap = mapper.readValue(body, Map.class);
                    String message = (String) errorMap.getOrDefault("message", "Client not found");
                    return new EntityNotFound(message);
                }
            } catch (Exception e) {
                // Fall through to default message
            }
            return new EntityNotFound("Client not found");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
