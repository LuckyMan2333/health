package com.itheima.service;

import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //假设从数据库查询 使用全限定名 user
        com.itheima.health.pojo.User userInDb = findByUsername(username);
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


            //使用用户名 密码 和权限集合创建User对象并返回
            //使用明文
            //User userDetail = new User(username, "{noop}"+userInDb.getPassword(), authorities);

            //使用密文,写死加密方式
            //User userDetail = new User(username, "{bcrypt}"+userInDb.getPassword(), authorities);

            //使用密文,使用bean注册方式
            User userDetail = new User(username, userInDb.getPassword(), authorities);


            return userDetail;
        }

        return null;
    }


    /**
     * 这个用户admin/admin, 有ROLE_ADMIN角色，角色下有ADD_CHECKITEM权限
     * 假设从数据库查询
     * @param username
     * @return
     */
    private com.itheima.health.pojo.User findByUsername (String username){
        if("admin".equals(username)) {
            com.itheima.health.pojo.User user = new com.itheima.health.pojo.User();
            user.setUsername("admin");
            // 使用密文，删除{noop}
            user.setPassword("$2a$10$geO7ljXXXZERKmB/jFzdw.OhMbll9RkKLp8DY6yGMh1DtztjlgaR.");
            //不使用密文
           // user.setPassword("admin");

            // 角色
            Role role = new Role();
            role.setKeyword("ROLE_ADMIN");

            // 权限
            Permission permission = new Permission();
            permission.setKeyword("ADD_CHECKITEM");

            // 给角色添加权限
            role.getPermissions().add(permission);

            // 把角色放进集合
            Set<Role> roleList = new HashSet<Role>();
            roleList.add(role);

            //添加 ABC 角色
            role = new Role();
            role.setKeyword("ABC");
            roleList.add(role);

            // 设置用户的角色
            user.setRoles(roleList);
            return user;
        }
        return null;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //加密密码
        //System.out.println(bCryptPasswordEncoder.encode("1234"));
        //校验密码
        System.out.println(bCryptPasswordEncoder.matches("1234", "$2a$10$geO7ljXXXZERKmB/jFzdw.OhMbll9RkKLp8DY6yGMh1DtztjlgaR."));
        System.out.println(bCryptPasswordEncoder.matches("1234", "$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a"));

    }


}
