package com.ted.resonance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class MyLocaleResolver implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        return new MyResolver();
    }
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename("messages");
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

}

class MyResolver extends AcceptHeaderLocaleResolver {
    List<Locale> LOCALES = Arrays.asList(
            new Locale("en"),
            new Locale("ko"),
            new Locale("zh")
    );

    /**
     * 适配别的项目的国际化
     * @param request
     * @return
     */

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLang = request.getHeader("language");
        if(headerLang == null || "".equals(headerLang)) {
            return Locale.CHINESE;
        }
        if("krw".equals(headerLang.toLowerCase())){
            return Locale.KOREA;
        }else if("en-us".contains(headerLang.toLowerCase())){
            return Locale.ENGLISH;
        } else {
            return Locale.CHINESE;
        }
//        return headerLang == null || headerLang.isEmpty()
//                ? Locale.getDefault()
//                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
    }
}
