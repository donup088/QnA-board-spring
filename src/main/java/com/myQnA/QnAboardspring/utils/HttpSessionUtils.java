package com.myQnA.QnAboardspring.utils;

import com.myQnA.QnAboardspring.domain.User;

import javax.servlet.http.HttpSession;

public class HttpSessionUtils {
    public static final String USER_SESSION_KEY="sessionUser";

    public static boolean isLogin(HttpSession session){
        User sessionUser= (User) session.getAttribute(USER_SESSION_KEY);
        if(sessionUser==null){
            return false;
        }
        return true;
    }

    public static User getUserFromSession(HttpSession session){
        if(!isLogin(session)){
            return null;
        }

        return (User) session.getAttribute(USER_SESSION_KEY);
    }

}
