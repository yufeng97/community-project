package com.nowcoder.community.component;

import com.nowcoder.community.vo.LoginUser;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，用于代替session对象
 */
@Component
public class UserContext {
    private ThreadLocal<LoginUser> loginUser = new ThreadLocal<>();

    public void setUser(LoginUser loginUser) {
        this.loginUser.set(loginUser);
    }

    public LoginUser getUser() {
        return loginUser.get();
    }

    public void clear() {
        loginUser.remove();
    }
}
