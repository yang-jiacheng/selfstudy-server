package com.lxy.common.domain;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxy.common.po.AdminInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/22 17:32
 * @Version: 1.0
 */
public class StatelessAdmin implements UserDetails {

    private AdminInfo adminInfo;

    private List<String> permissions;

    private List<SimpleGrantedAuthority> authorities;

    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date loginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale="zh", timezone="GMT+8")
    private Date endTime;

    public AdminInfo getAdminInfo() {
        return adminInfo;
    }

    public void setAdminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
        if (CollUtil.isNotEmpty(this.permissions)){
            this.authorities = this.permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }else {
            this.authorities = null;
        }
    }

    public StatelessAdmin() {
    }

    public StatelessAdmin(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.authorities!=null){
            return this.authorities;
        }
        if (CollUtil.isNotEmpty(this.permissions)){
            this.authorities = this.permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return this.authorities;
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
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
    public String getPassword() {
        return this.adminInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return this.adminInfo.getUsername();
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
