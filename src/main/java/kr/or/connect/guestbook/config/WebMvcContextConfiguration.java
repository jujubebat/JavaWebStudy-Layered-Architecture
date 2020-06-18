package kr.or.connect.guestbook.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import kr.or.connect.guestbook.argumentresolver.HeaderMapArgumentResolver;
import kr.or.connect.guestbook.interceptor.LogInterceptor;

//디스패처 서블릿이 읽어드리는 설정 클래스

@Configuration // congfig 설정 
@EnableWebMvc // 기본적인 mvc 설정
@ComponentScan(basePackages = { "kr.or.connect.guestbook.controller" })
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter{ //원하는 설정을 하기위해 WebMvcConfigurerAdapter을 상속받음 
    
    // 특정 URL 요청을 특정 폴더로 매핑해준다. 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }
 
    // default servlet handler를 사용하게 해준다. 
    // 매핑정보가 없는 URL정보는 스프링의  default servlet http request handle이 처리하도록 해준다. 
    // 매핑이 없는 URL이 넘어왔을때 WAS의  default servlet이 스태틱한 자원을 읽어서 보여줄 수 있겠끔 설정하는 것
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
   
    // 특정 URL에 대한 처리를 Controller 필요없이 보여주는 설정
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
            System.out.println("addViewControllers가 호출됩니다. ");
        registry.addViewController("/").setViewName("index");
    }
    
    // 뷰의 위치와 파일 형식을 설정 
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    // 인터셉터 등록
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
    		registry.addInterceptor(new LogInterceptor());
	}
    
    @Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    		System.out.println("아규먼트 리졸버 등록..");
		argumentResolvers.add(new HeaderMapArgumentResolver());
	}
    
    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760); // 1024 * 1024 * 10
        return multipartResolver;
    }
    
 
    
}