package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
//@ControllerAdvice //RestControllerAdvice와 같은거임. ResponseBody가 붙냐 안붙냐 차이.
@RestControllerAdvice(/*annotations=RestController.class*/ basePackages = "hello.exception.api" /*assignableTypes = {ControllerInterface.class, AbstractController.class}*/) //@ResponseBody가 아래 함수마다 붙음
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e){
        log.error("[ExceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResult> userExHandler(UserException e){
        log.error("[UserException] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler()
    public ErrorResult exHandler(Exception e){
        log.error("[UserException] ex", e);
        return new ErrorResult("EX", "내부오류");
    }
}
