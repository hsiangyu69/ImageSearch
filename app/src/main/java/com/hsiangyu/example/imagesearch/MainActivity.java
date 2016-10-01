package com.hsiangyu.example.imagesearch;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hsiangyu.example.imagesearch.adapter.ImageSearchAdapter;
import com.hsiangyu.example.imagesearch.pojo.PhotoInfo;
import com.hsiangyu.example.imagesearch.retrofit.ImageSearchService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private final String apiKey = "3415584-c459a413c65fde1b2673afd71";
    private RecyclerView recyclerView;
    private ArrayList<PhotoInfo> photoInfoArrayList = new ArrayList<>();
    private ImageSearchAdapter imageSearchAdapter;
    private Drawable searchDrawable;
    private SearchView searchView;
    private MenuItem searchItem;
    private MenuItem swichDisPlayItem;
    private boolean isGrid = false;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageSearchAdapter = new ImageSearchAdapter();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageSearchAdapter);
        recyclerView.setHasFixedSize(true);
        SpacesItemDecoration decoration = new SpacesItemDecoration(8);
        recyclerView.addItemDecoration(decoration);
        searchImagesResult("yellow+flowers");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Drawable searchDrawable = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_search);
        searchDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        searchItem = menu.findItem(R.id.search);
        swichDisPlayItem = menu.findItem(R.id.switch_display);
        if (isGrid) {
            swichDisPlayItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_view_list_black_24dp));
        } else {
            swichDisPlayItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_view_module_black_24dp));

        }
        searchItem.setIcon(searchDrawable);
        searchView = (SearchView) searchItem.getActionView();
        searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text).setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha);
        searchView.setQueryHint("輸入關鍵字搜尋圖片，可以用空白鍵隔開");
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.performClick();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                searchView.onActionViewExpanded();
                break;
            case R.id.switch_display:
                isGrid = !isGrid;
                invalidateOptionsMenu();
                imageSearchAdapter.isGrid = isGrid;
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

                recyclerView.setLayoutManager(isGrid ? staggeredGridLayoutManager : linearLayoutManager);
                recyclerView.setAdapter(imageSearchAdapter);
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    private void searchImagesResult(String keyword) {
        progressBar.setVisibility(View.VISIBLE);
        photoInfoArrayList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImageSearchService imageSearchService = retrofit.create(ImageSearchService.class);
        Call<ResponseBody> call = imageSearchService.getImageSearchResult(apiKey, keyword, "photo", "true");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                try {
                    if (response.body() == null) {
                        Toast.makeText(MainActivity.this, "No Result,Please input keyword again", Toast.LENGTH_LONG).show();

                    } else {
                        String jsonString = response.body().string();
                        jsonObject = new JSONObject(jsonString);
                        JSONArray jsonArray = jsonObject.getJSONArray("hits");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject resultObject = (JSONObject) jsonArray.get(i);
                            String tags = resultObject.getString("tags");
                            String previewURL = resultObject.getString("previewURL");
                            String webformatURL = resultObject.getString("webformatURL");
                            PhotoInfo photoInfo = new PhotoInfo();
                            photoInfo.setTags(tags);
                            photoInfo.setPreviewURL(previewURL);
                            photoInfo.setWebformatURL(webformatURL);
                            photoInfoArrayList.add(photoInfo);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageSearchAdapter.photoInfoArrayList = photoInfoArrayList;
                imageSearchAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            String keyword = query.replaceAll(" ", "+");
            searchImagesResult(keyword);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {

            return false;
        }
    };


}
