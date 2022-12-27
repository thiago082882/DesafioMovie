package com.example.desafio_movie.desafiomovie.utils;

import com.example.desafio_movie.desafiomovie.models.MovieModel;
import com.example.desafio_movie.desafiomovie.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    //Search for movies
    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );


    // https://api.themoviedb.org/3/movie/popular?api_key=d8def18d2c8a0453638cfcd6f96adc29&language=en-US&page=1
    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") int page

    );



    //Fazendo pesquisa com ID
    // https://api.themoviedb.org/3/movie/550?api_key=d8def18d2c8a0453638cfcd6f96adc29
    //Lembre-se que movie_id= 550 é para o filme Fight Club
@GET("3/movie/{movie_id}?")

    Call<MovieModel> getMovie(

            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key


    );
}
