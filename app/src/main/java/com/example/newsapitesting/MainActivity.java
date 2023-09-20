package com.example.newsapitesting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(null, this);
        recyclerView.setAdapter(newsAdapter);

        // Fetch and display news articles
        fetchNewsArticles();
    }

    private void fetchNewsArticles() {
        progressBar.setVisibility(View.VISIBLE);

        // Create a Retrofit instance
        NewsApiClient.NewsApiService newsApiService = NewsApiClient.getNewsApiService();

        // Make a network request to get top headlines
        Call<NewsResponse> call = newsApiService.getTopHeadlines("in", NewsApiClient.getApiKey());

//        Call<NewsResponse> call = newsApiService.getTopHeadlines("us", NewsApiClient.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    List<NewsArticle> newsArticles = response.body().getArticles();
                    newsAdapter = new NewsAdapter(newsArticles, MainActivity.this);
                    recyclerView.setAdapter(newsAdapter);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                // Handle the failure here
            }
        });
    }
}
