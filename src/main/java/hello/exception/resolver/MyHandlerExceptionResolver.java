package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try{
            if(ex instanceof IllegalArgumentException){
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                /***
                 * ExceptionResolver 가 ModelAndView 를 반환하는 이유는 마치 try, catch를 하듯이,
                 * Exception 을 처리해서 정상 흐름 처럼 변경하는 것이 목적이다. 이름 그대로 Exception 을 Resolver(해결)하는 것이 목적
                 */
                return new ModelAndView(); //빈값의 ModelAndView를 넘기면 return return 정상적인 흐름으로 WAS까지 호출됨.
            }
        } catch (IOException e) {
            log.info("resolver ex", e);
        }

        /**
         * 다음 ExceptionResolver 를 찾아서 실행한다. 만약 처리할 수 있는
         * ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다. WAS까지 Exception이 그대로 전달됨.
         */
        return null;
    }
}
