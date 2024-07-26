package com.example.meritmatch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Field;

public interface ApiService {
    @POST("/users/")
    Call<User> createUser(@Body UserCreate userCreate);

    @POST("/users/login")
    Call<User> loginUser(@Body UserAuth userAuth);

    @GET("/tasks/all")
    Call<List<Task>> getAllTasks();

    @POST("/tasks/choose")
    Call<Task> chooseTask(@Body TaskChooseRequest taskChooseRequest);

    @POST("/transactions/history")
    Call<List<Transaction>> getTransactionHistory(@Query("username") String username);

    @POST("/tasks/")
    Call<Task> createTask(@Body TaskCreateRequest request);

    @POST("/tasks/complete")
    Call<Task> completeTask(@Body TaskCompleteRequest request);

    @GET("/messages/{userId}/{otherUserId}")
    Call<List<Message>> getMessages(@Path("userId") int userId, @Path("otherUserId") int otherUserId);

    @POST("/messages/")
    Call<Message> sendMessage(@Body Message message);


}

