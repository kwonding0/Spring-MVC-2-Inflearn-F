package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /**
     * 예외가 터지면 ExceptionResolver에서 예외 해결시도를 하는데 첫번째로 시도하는 resolver가 ExceptionHandlerExceptionResolver이다.
     * 그리고 ExceptionHandlerExceptionResolver는 Controller에 @ExceptionHandler 어노테이션이 있는지 찾아보고, 있으면 해당 함수 실행
     * 그렇기 떄문에 @RestController, @ResponsBody와같은 어노테이션이 exception 처리에 다 반영이 되기 때문에 어렵게 responsBody에 값을 넣어주고 ModelAndView를 반환해주지 않고
     * 바로 api로 return을 해줄 수 있다.
     * @ExceptionHandler는 해당컨트롤러에서만 적용이됨.
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST) //@ExceptionHandler에서 잘 처리를 해주면 정상적인 흐름으로 처리되기 때문에 상태코드가 200이다. 그래서 ResponseStatus로 상태코드를 바꿔줌.
    //@ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e){
        log.error("[ExceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    //@ExceptionHandler(/*UserException*/) //변수랑 같을 경우에는 어노테이션에 값에서 생략가능
    public ResponseEntity<ErrorResult> userExHandler(UserException e){
        log.error("[UserException] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * 각 Exception들은 자식 Exception까지 처리가 됨.
     * 만약 ExceptionHandler로 선언이 되지 않은 Exception들은 최상위 Exception인 Exception처리 함수에 들어오게됨. (아래 함수)
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //@ExceptionHandler()
    public ErrorResult exHandler(Exception e){
        log.error("[UserException] ex", e);
        return new ErrorResult("EX", "내부오류");
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값"); //사용자가 bad라는 잘못된 입력값을 넘겼기 때문에, 에러코드를 400으로 던지고 싶음.
        }
        if(id.equals("user-ex")){
            throw new IllegalArgumentException("사용자 오류");
        }
        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
