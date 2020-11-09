package com.example.booksfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.booksfinder.Fragments.FragmentBookDetails;
import com.example.booksfinder.Fragments.FragmentSearchResults;
import com.example.booksfinder.Model.BookResponse;
import com.example.booksfinder.Model.Item;
import com.example.booksfinder.Retrofit.BooksApi;
import com.example.booksfinder.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BookAdapter.OnItemClickListener {

    private final static String BACKSTACK_TAG = "backStackTag";
    public final static String ITEM_KEY = "itemKey";

    private SearchView searchView;
    private List<Item> volumeInfoList;
    private ProgressBar progressBar;
    private BooksApi booksApi;
    private FragmentManager fragmentManager;

    private final static int MAX_RESULT = 40;
    private final static String API_KEY = "AIzaSyCKtqaB98XwGktcOJF-4hNOT5SCgwg9ndI";

    @Override
    public void onItemClickListener(Item item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM_KEY, item);
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment instanceof FragmentSearchResults) {
            fragment = new FragmentBookDetails();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).addToBackStack(BACKSTACK_TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }
    }

    public interface OnActivityDataListener {
        void onActivityDataListener(List<Item> volumeInfoList);
    }

    OnActivityDataListener activityDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        booksApi = RetrofitClient.getApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        int options = searchView.getImeOptions();
        searchView.setImeOptions(options| EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                openFragmentSearchResults();
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                searchBooks(s);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void openFragmentSearchResults() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new FragmentSearchResults();
            activityDataListener = (OnActivityDataListener) fragment;
            fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).addToBackStack(BACKSTACK_TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }
    }

    private void searchBooks(String s) {
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment instanceof FragmentBookDetails) {
            fragmentManager.beginTransaction().remove(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
        booksApi.getBooks(API_KEY, s, MAX_RESULT).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(retrofit2.Call<BookResponse> call, Response<BookResponse> response) {
                if(response.isSuccessful()) {
                    volumeInfoList = response.body().getItems();
                    activityDataListener.onActivityDataListener(volumeInfoList);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, R.string.wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
