package com.lxy.framework.security.domain;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 无状态用户
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/22 17:33
 */
public class StatelessUser implements UserDetails {

    @Serial
    private static final long serialVersionUID = 6565570798263006714L;

    private Long userId;

    private String password;

    private String username;

    private List<String> permissions;

    @JsonIgnore
    private List<SimpleGrantedAuthority> authorities;

    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date loginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date endTime;

    public StatelessUser() {}

    public StatelessUser(Long userId, String password, String username) {
        this.userId = userId;
        this.password = password;
        this.username = username;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
        if (CollUtil.isNotEmpty(this.permissions)) {
            this.authorities = this.permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        } else {
            this.authorities = null;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authorities != null) {
            return this.authorities;
        }
        if (CollUtil.isNotEmpty(this.permissions)) {
            this.authorities = this.permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return this.authorities;
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
