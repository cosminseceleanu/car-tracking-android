package com.cosmin.cartracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmin.cartracking.common.TaskUpdater;
import com.cosmin.cartracking.http.endpoints.TaskEndpoint;
import com.cosmin.cartracking.model.PagedResponse;
import com.cosmin.cartracking.model.Task;
import com.cosmin.cartracking.model.TaskListResponse;
import com.cosmin.cartracking.model.User;
import com.cosmin.cartracking.service.LocationService;
import com.cosmin.cartracking.ui.TasksListAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AbstractActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TasksListAdapter adapter;
    private int currentPage = 0;
    private int totalTasks = 0;
    private final static  int SIZE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskUpdater taskUpdater = new TaskUpdater(retrofitFactory, security.get());
        adapter = new TasksListAdapter(this, R.layout.task_list_item, taskUpdater);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupMenu(navigationView.getHeaderView(0));
        ListView listView = (ListView) findViewById(R.id.tasks_list);
        listView.setAdapter(adapter);
        loadTasks();
        startLocationService();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list_tasks) {
            adapter.clear();
            loadTasks();
        } else if (id == R.id.nav_logout) {
            security.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadTasks() {
        Log.d("main", "load tasks");
        Call<PagedResponse<TaskListResponse>> call = retrofitFactory.create()
                .create(TaskEndpoint.class)
                .get(security.get().getId(), currentPage, SIZE);

        call.enqueue(new Callback<PagedResponse<TaskListResponse>>() {
            @Override
            public void onResponse(Call<PagedResponse<TaskListResponse>> call, Response<PagedResponse<TaskListResponse>> response) {
                PagedResponse<TaskListResponse> pagedResponse = response.body();
                totalTasks = pagedResponse.getPage().getTotalElements();
                if (pagedResponse.getData() == null) {
                    return;
                }
                for (Task task : pagedResponse.getData().getTasks()) {
                    adapter.add(task);
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<TaskListResponse>> call, Throwable t) {
                String msg = "Lista de task-uri nu a putut fi incarcata";
                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void setupMenu(View view) {
        User user = security.get();
        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView userEmail = (TextView) view.findViewById(R.id.userEmail);
        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
    }

    private void startLocationService() {
        Intent intent = new Intent(this, LocationService.class);
//        startService(intent);
    }
}
