package com.example.desafio_movie.desafiomovie.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.desafio_movie.desafiomovie.AppExecutors;
import com.example.desafio_movie.desafiomovie.models.MovieModel;
import com.example.desafio_movie.desafiomovie.response.MovieSearchResponse;
import com.example.desafio_movie.desafiomovie.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private MutableLiveData<List<MovieModel>> mMovie;
    private MutableLiveData<List<MovieModel>> mMoviesPop ;
    private static MovieApiClient instance;

    //fazendo solicitação Executável

    private RetrieveMoviesRunnable retrieveMoviesRunnable;
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;


    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovie = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies() {
        return mMovie;
    }
        public LiveData<List<MovieModel>> getMoviesPop(){
            return mMoviesPop;
        }



    // 1- esse método que vamos chamar através da classe
    public void searchMovieApi(String query, int pageNumber) {

        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }
        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelando a chamada de retrofit
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);
    }

    public void searchMoviesPop(int pageNumber) {

        if (retrieveMoviesRunnablePop != null){
            retrieveMoviesRunnablePop = null;
        }

        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePop);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler2.cancel(true);

            }
        }, 1000, TimeUnit.MILLISECONDS);


    }

    //recuperando dados da API Rest pela classe Runnable
    //Nós temos 2 tipos de consulta : o ID e a Pesquisa por consultas

    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;

        }

        @Override
        public void run() {

//obtendo os objetos de resposta

            try {
                Response response = getMovies(query, pageNumber).execute();

                if (cancelRequest) {

                    return;
                }
                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) response.body()).getMovies());
                    if (pageNumber == 1) {
                        //enviando dados para dados ao vivo
                        //PostValue: usado para thread em segundo plano
                        //setValue: não para fundo

                        mMovie.postValue(list);

                    } else {
                        List<MovieModel> currentMovies = mMovie.getValue();
                        currentMovies.addAll(list);
                        mMovie.postValue(currentMovies);

                    }

                } else {

                    String error = response.errorBody().string();
                    Log.v("Tag", "Error" + error);
                    mMovie.postValue(null);

                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovie.postValue(null);
            }


        }
        //Pesquisa Método/Consulta

        private Call<MovieSearchResponse> getMovies(String query, int pagerNumber) {
            return Servicey.getMovieApi().searchMovie(

                    Credentials.API_KEY,
                    query,
                    pagerNumber

            );
        }

        private void CancelRequest() {
            Log.v("Tag", "cancelando solicitação de pesquisa");
            cancelRequest = true;
        }
    }
    private class RetrieveMoviesRunnablePop implements Runnable{

        private int pageNumber;
        boolean cancelRequest;


        public RetrieveMoviesRunnablePop(int pageNumber) {

            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects

            try{

                Response response2 = getPop(pageNumber).execute();

                if (cancelRequest) {

                    return;
                }
                if (response2.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if (pageNumber == 1){
                        // Sending data to live data
                        // PostValue: used for background thread
                        // setValue: not for background thread
                        mMoviesPop.postValue(list);



                    }else{
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);



                    }

                }else{
                    String error = response2.errorBody().string();
                    Log.v("Tagy", "Erroryy " +error);
                    mMoviesPop.postValue(null);

                }

            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop.postValue(null);

            }




        }

        // Search Method/ query
        private Call<MovieSearchResponse> getPop( int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber

            );

        }
        private void cancelRequest(){
            Log.v("Tag", "Cancelling Search Request" );
            cancelRequest = true;
        }





    }
}



