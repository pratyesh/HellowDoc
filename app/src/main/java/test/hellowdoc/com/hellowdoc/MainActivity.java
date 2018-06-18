package test.hellowdoc.com.hellowdoc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity implements
        MyAsyncTask.MyAsyncTaskListener, OnBottomReachedListener {

    private String url = "https://hn.algolia.com/api/v1/search?query=";
    private String page = "&page=";
    private String query = "sports";

    static final String QUERY = "QUERY";

    private RecyclerView mRecyclerView;
    private MyAsyncTask mMyAsyncTask;
    private MyAdapter mMyAdapter;

    private int currentPage;

    private boolean reloadOnTextSearch;
    private int scrolledPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY);
        }

        final SearchView simpleSearchView = findViewById(R.id.search);

        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String typedQuery) {
                query = typedQuery;
                currentPage = 0;
                reloadOnTextSearch = true;
                fetchAndLoadData(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mRecyclerView = findViewById(R.id.list_item);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchAndLoadData(url);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(QUERY, query);
        super.onSaveInstanceState(outState);
    }

    private void fetchAndLoadData(String url) {
        final String completeUrl = url.concat(query).concat(page).concat(String.valueOf(1 + currentPage));
        mMyAsyncTask = new MyAsyncTask(this, completeUrl, this);
        mMyAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        mMyAsyncTask.cancelActive();
        mMyAsyncTask = null;
        mRecyclerView = null;
        mMyAdapter = null;
        currentPage = 0;
        super.onDestroy();
    }

    @Override
    public void onTaskCompleted(ResponseDetails dataModel) {

        if (dataModel == null) {
            return;
        }

        if (mMyAdapter == null || reloadOnTextSearch) {
            mMyAdapter = new MyAdapter(dataModel.getHits());
            mMyAdapter.setOnBottomReachedListener(this);
        } else {
            mMyAdapter.updateList(dataModel.getHits());
            mRecyclerView.scrollToPosition(scrolledPosition);
        }

        reloadOnTextSearch = false;
        currentPage = TextUtils.isEmpty(dataModel.getPage()) ? 0 : Integer.valueOf(dataModel.getPage());
        mRecyclerView.setAdapter(mMyAdapter);
    }

    @Override
    public void onBottomReached(int position) {
        scrolledPosition = position;
        fetchAndLoadData(url);
    }
}
