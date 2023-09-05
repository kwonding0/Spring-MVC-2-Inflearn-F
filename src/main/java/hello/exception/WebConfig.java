package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*@Bean*/
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        //DispatcherType이 request, error일때 호출이 됨. 기본값은 DispatcherType.REQUEST
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST/*, DispatcherType.ERROR*/);
        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //인터셉터는 filter처럼 setDispatcherTypes 할 수 없음. 그래서 excludePathPatterns에 error관련 url 추가
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") //전체 url이 filter와 다르게 표시
                .excludePathPatterns("/*.ico", "/error", "/css/**", "/error-page/**"); //해당 url은 체크 제외
    }
}
