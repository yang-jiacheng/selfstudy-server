package com.lxy.app.config;

import com.lxy.app.security.filter.StatelessAuthenticationFilterUser;
import com.lxy.app.security.handle.AccessDeniedHandlerImpl;
import com.lxy.app.security.handle.AuthenticationEntryPointUserImpl;
import com.lxy.app.security.service.impl.UserDetailsServiceImpl;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.security.encoder.MinePasswordEncoder;
import com.lxy.common.security.filter.StatelessPermitFilter;
import com.lxy.common.service.BusinessConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

    private final static String[] PERMIT_URL = {"/swagger-ui.html","/swagger-resources/**","/webjars/**","/v2/**","/api/**","/csrf",
            "/static/**","/druid/**","/","/token/**","/upload/**","/userAgreement/**","/personalCenter/updatePassword","/version/**"};

    private final static String[] AUTH_URL = {
            "/catalog/**","/feedBack/**","/home/**","/personalCenter/getUserInfo","/personalCenter/updateUserInfo",
            "/personalCenter/getUserInfoById","/studyRecord/**","/studyStatistics/**",
            "/error","/resources/upload","/resources/uploadApp","/resources/generateImage"
    };

    static CorsConfigurationSource configurationSource() {
        List<String> list = Collections.singletonList("*");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(list);
        corsConfiguration.setAllowedMethods(list);
        //corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        corsConfiguration.setAllowedOriginPatterns(list);
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Configuration
    @Order(1)
    static class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter{

        @Resource
        private UserDetailsServiceImpl userDetailsService;
        @Resource
        private BusinessConfigService businessConfigService;
        @Resource
        private CommonRedisService commonRedisService;

        @Resource
        private AuthenticationEntryPointUserImpl authenticationEntryPoint;

        @Resource
        private AccessDeniedHandlerImpl accessDeniedHandler;

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new MinePasswordEncoder();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    //不通过Session获取SecurityContext
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .requestMatchers()
                    .antMatchers(AUTH_URL)
                    .and()
                    //添加过滤器,
                    .addFilterBefore(
                            new StatelessAuthenticationFilterUser(businessConfigService, commonRedisService),
                            UsernamePasswordAuthenticationFilter.class
                    )
                    .authorizeRequests()
                    // 对于以下接口 鉴权认证
                    .antMatchers(AUTH_URL).authenticated()
                    .anyRequest().permitAll();



            //配置异常处理器
            http
                    .exceptionHandling()
                    //配置认证失败处理器
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);

            //关闭csrf //允许跨域
            http.csrf().disable().cors().configurationSource(configurationSource());
        }

        /**
         * 解决AuthenticationManager无法在其他service注入
         */
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }

    @Configuration
    @Order(2)
    static class DefaultWebSecurityConfig2 extends WebSecurityConfigurerAdapter{

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    //不通过Session获取SecurityContext
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .requestMatchers()
                    .antMatchers(PERMIT_URL)
                    .and()
                    //添加过滤器,
                    .addFilterBefore(
                            new StatelessPermitFilter(),
                            UsernamePasswordAuthenticationFilter.class
                    )
                    .authorizeRequests()
                    // 对于以下接口 放行
                    .antMatchers(PERMIT_URL).permitAll()
                    // 除上面外的所有请求全部放行
                    .anyRequest().permitAll();


            //关闭csrf //允许跨域
            http.csrf().disable().cors().configurationSource(configurationSource());
        }

    }


}



//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//    @Autowired
//    private BusinessConfigService businessConfigService;
//    @Autowired
//    private CommonRedisService commonRedisService;
//
//    @Autowired
//    private AuthenticationEntryPointUserImpl authenticationEntryPoint;
//
//    @Autowired
//    private AccessDeniedHandlerImpl accessDeniedHandler;
//
//    private final static String[] PERMIT_URL = {"/swagger-ui.html","/swagger-resources/**","/webjars/**","/v2/**","/api/**","/csrf",
//            "/static/**","/druid/**","/","/token/**","/upload/**","/userAgreement/**","/personalCenter/updatePassword","/version/**"};
//
//    private final static String[] AUTH_URL = {
//            "/catalog/**","/feedBack/**","/home/**","/personalCenter/getUserInfo","/personalCenter/updateUserInfo",
//            "/personalCenter/getUserInfoById","/studyRecord/**","/studyStatistics/**",
//            "/error","/resources/upload","/resources/uploadApp"
//    };
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new MinePasswordEncoder();
//    }
//
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                //不通过Session获取SecurityContext
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .requestMatchers()
//                .antMatchers(AUTH_URL)
//                .and()
//                //添加过滤器,
//                .addFilterBefore(
//                        new StatelessAuthenticationFilterUser(businessConfigService, commonRedisService),
//                        UsernamePasswordAuthenticationFilter.class
//                )
//                .authorizeRequests()
//                // 对于以下接口 鉴权认证
//                .antMatchers(AUTH_URL).authenticated()
//                // 除上面外的所有请求全部放行
//                .anyRequest().permitAll();
//
//        //配置异常处理器
//        http.exceptionHandling()
//                //配置认证失败处理器
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler);
//
//        //关闭csrf //允许跨域
//        http.csrf().disable().cors().configurationSource(configurationSource());
//    }
//
////    @Override
////    public void configure(WebSecurity web)  {
////        //以下路径不启用过滤器
////        web.ignoring()
////                .antMatchers(
////                        PERMIT_URL
////                );
////    }
//
//    /**
//     * 解决AuthenticationManager无法在其他service注入
//     */
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    CorsConfigurationSource configurationSource() {
//        List<String> list = Collections.singletonList("*");
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedHeaders(list);
//        corsConfiguration.setAllowedMethods(list);
//        //corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
//        corsConfiguration.setAllowedOriginPatterns(list);
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return source;
//    }
//
//}
