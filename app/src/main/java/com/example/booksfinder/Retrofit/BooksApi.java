package com.example.booksfinder.Retrofit;

import com.example.booksfinder.Model.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BooksApi {
    @GET("volumes")
    Call<BookResponse> getBooks(@Query("key")String apiKey, @Query("q") String query, @Query("maxResults") int maxResults);
}
