package com.lxy.admin.config;

import com.lxy.admin.security.filter.StatelessAuthenticationFilterAdmin;
import com.lxy.admin.security.handle.AccessDeniedHandlerImpl;
import com.lxy.admin.security.handle.AuthenticationEntryPointAdminImpl;
import com.lxy.admin.security.service.impl.AdminDetailsServiceImpl;
import com.lxy.common.redis.service.CommonRedisService;
import com.lxy.common.security.encoder.MinePasswordEncoder;
import com.lxy.common.service.AdminInfoService;
import com.lxy.common.service.BusinessConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/23 21:05
 * @Version: 1.0
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminDetailsServiceImpl adminDetailsService;
    @Autowired
    private BusinessConfigService businessConfigService;
    @Autowired
    private CommonRedisService commonRedisService;
    @Autowired
    private AdminInfoService adminInfoService;

    @Autowired
    private AuthenticationEntryPointAdminImpl authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new MinePasswordEncoder();
    }


    private final static String[] PERMIT_URL = {"/webjars/**","/v2/**","/api/**","/csrf",
            "/static/**","/druid/**","/css/**","/fonts/**","/images/**","/js/**","/layui_v2.6.8/**","/multipleSel/**","/tinymce/**","/zoomify/**","/zTree/**",
            "/","/token/**","/upload/**","/login","/Kaptcha","/error403","/404"};

    private final static String[] AUTH_URL = {
            "/home/**","/adminManage/**","/businessConfigManage/**","/classifyManage/**","/feedBackManage/**","/home/**",
            "/personalManage/**","/roleManage/**","/studyRecord/**","/userAgreementManage/**","/userManage/**","/versionManage/**",
            "/error","/resources/upload","/resources/uploadApp","/resources/generateImage","/objectStorageManage/**"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminDetailsService).passwordEncoder(passwordEncoder());
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
                        new StatelessAuthenticationFilterAdmin(businessConfigService, commonRedisService, adminInfoService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeRequests()
                // 对于以下接口 鉴权认证
                .antMatchers(AUTH_URL).authenticated()
                // 除上面外的所有请求全部放行
                .anyRequest().permitAll();
        //配置异常处理器
        http.exceptionHandling()
                //配置认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                //权限不足过滤器
                .accessDeniedHandler(accessDeniedHandler);

        //关闭csrf //允许跨域
        http.csrf().disable().cors().configurationSource(configurationSource());
        //页面只能被本站页面嵌入到iframe或者frame中
        http.headers().frameOptions().sameOrigin();
    }

//    @Override
//    public void configure(WebSecurity web)  {
//        //以下路径不启用过滤器
//        web.ignoring()
//                .antMatchers(PERMIT_URL);
//    }

    /**
     * 解决AuthenticationManager无法在其他service注入
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    CorsConfigurationSource configurationSource() {
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

}
