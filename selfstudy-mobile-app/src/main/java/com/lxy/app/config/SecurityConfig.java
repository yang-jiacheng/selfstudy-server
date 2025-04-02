package com.lxy.app.config;

import com.lxy.app.security.filter.StatelessAuthenticationFilterUser;
import com.lxy.app.security.handle.AuthenticationEntryPointUserImpl;
import com.lxy.app.security.service.impl.UserDetailsServiceImpl;
import com.lxy.common.constant.CommonConstant;
import com.lxy.common.enums.LogUserType;
import com.lxy.framework.security.encoder.MinePasswordEncoder;
import com.lxy.framework.security.filter.StatelessPermitFilter;
import com.lxy.framework.security.serviice.LoginStatusService;
import com.lxy.system.service.BusinessConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.annotation.Resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/13 9:34
 * @Version: 1.0
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

    @Resource
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private BusinessConfigService businessConfigService;
    @Resource
    private LoginStatusService loginStatusService;


    private final static String[] PERMIT_URL = {
            "/druid/**","/","/token/**","/upload/**",
            "/userAgreement/**","/personalCenter/updatePassword","/version/**"
    };

    private final static String[] AUTH_URL = {
            "/catalog/**","/feedBack/**","/home/**","/personalCenter/getUserInfo","/personalCenter/updateUserInfo",
            "/personalCenter/getUserInfoById","/studyRecord/**","/studyStatistics/**",
            "/resources/upload","/resources/uploadApp","/resources/generateImage"
    };

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainAuth(HttpSecurity http) throws Exception {
        http
                // 会话管理（无状态）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // securityMatcher 限定此过滤器链仅处理 AUTH_URL 的请求
                .securityMatcher(AUTH_URL)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated())
                .addFilterBefore(
                        new StatelessAuthenticationFilterUser(businessConfigService, loginStatusService),
                        UsernamePasswordAuthenticationFilter.class
                )
                // 配置异常处理
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new AuthenticationEntryPointUserImpl()))
                //关闭csrf //允许跨域
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(configurationSource()))
                //X-Frame-Options 页面只能被本站页面嵌入到iframe或者frame中
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChainPermit(HttpSecurity http) throws Exception {
        http
                // 会话管理（无状态）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // securityMatcher 限定此过滤器链仅处理 PERMIT_URL 的请求
                .securityMatcher(PERMIT_URL)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
                //添加过滤器
                .addFilterBefore(
                        new StatelessPermitFilter(LogUserType.USER.type,loginStatusService),
                        UsernamePasswordAuthenticationFilter.class
                )
                //关闭csrf //允许跨域
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(configurationSource()))
                //X-Frame-Options 页面只能被本站页面嵌入到iframe或者frame中
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new MinePasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }


    // 配置 CORS
    @Bean
    CorsConfigurationSource configurationSource() {
        List<String> list = Collections.singletonList("*");
        List<String> methods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(list);
        corsConfiguration.setAllowedMethods(methods);
        /*
         * 当 setAllowCredentials(true) 设置为 true 时，setAllowedOrigins 不能包含通配符 *
         * 因为这会导致响应头 Access-Control-Allow-Origin 的值为 *，而这与携带凭证的请求不兼容。
         * 所以用 setAllowedOriginPatterns 代替 setAllowedOrigins
         */
        //corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        corsConfiguration.setAllowedOriginPatterns(list);
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
