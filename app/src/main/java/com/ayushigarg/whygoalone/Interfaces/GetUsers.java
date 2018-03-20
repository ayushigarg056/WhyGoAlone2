package com.ayushigarg.whygoalone.Interfaces;

import com.ayushigarg.whygoalone.Models.User2;

import java.util.ArrayList;


public interface GetUsers {

    public void onSuccess(ArrayList<User2> userList);
    public void onError(String errorMsg);

}
