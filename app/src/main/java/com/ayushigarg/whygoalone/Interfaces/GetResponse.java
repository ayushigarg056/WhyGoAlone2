package com.ayushigarg.whygoalone.Interfaces;

import com.ayushigarg.whygoalone.Models.Restaurant;

import java.util.ArrayList;



public interface GetResponse {

    public void onSuccess(ArrayList<Restaurant> restList);
    public void onError();

}
