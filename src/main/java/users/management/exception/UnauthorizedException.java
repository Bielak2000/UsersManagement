package users.management.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Slf4j
public class UnauthorizedException extends RuntimeException {
    private String message;

    public UnauthorizedException(String message) {
        this.message = message;
        log.info(this.message);
    }
}
