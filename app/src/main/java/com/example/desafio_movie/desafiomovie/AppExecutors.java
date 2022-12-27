package com.example.desafio_movie.desafiomovie;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors instace;

    public static  AppExecutors getInstance(){
        if(instace == null){
            instace = new AppExecutors();
        }
        return  instace;
    }

    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);
    public  ScheduledExecutorService networkIO(){
        return mNetworkIO;
    }

}
