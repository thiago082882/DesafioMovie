package com.example.desafio_movie.desafiomovie.response;

    //this  is for single movie request

import com.example.desafio_movie.desafiomovie.models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieResponse {

    //find the movie object


    @SerializedName("results")
    @Expose

    private MovieModel movie;
    public MovieModel getMovieModel(){
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movieModel=" + movie +
                '}';
    }
}
