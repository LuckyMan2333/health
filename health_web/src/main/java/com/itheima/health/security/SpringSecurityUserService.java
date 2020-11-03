package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    //订阅服务
    @Reference
    private UserService userService;

    /**
     * 提供登陆用户信息  username password 权限集合 authorities
     *
     * @param
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //通过用户名查询数据,如果为空则报错
        com.itheima.health.pojo.User userInDb = userService.findByUserName(username);
        //如果查询到了用户,就进行授权: 用户名 密码 角色 权限
        if (userInDb != null) {
            //创建用户需要的权限集合
            List<GrantedAuthority> authorities = new ArrayList<>();
            //创建权限集合的泛型,添加数据,并存入到权限集合中
            GrantedAuthority sga = null;
            //遍历用户拥有的角色和权限
            Set<Role> roles = userInDb.getRoles();
            //如果角色列表不为空,就授予角色和权限
            if (roles != null) {
                //遍历角色列表
                for (Role role : roles) {
                    //授予角色
                    sga = new SimpleGrantedAuthority(role.getKeyword());
                    authorities.add(sga);

                    //获取当前角色下所拥有的权限
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions != null) {
                        for (Permission permission : permissions) {
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sga);
                        }
                    }
                }
            }

            User userDetail = new User(username, userInDb.getPassword(), authorities);
            return userDetail;
        }

        return null;
    }
}
