package com.cosmin.cartracking;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cosmin.cartracking.http.RetrofitFactory;
import com.cosmin.cartracking.security.Security;
import com.cosmin.cartracking.security.TokenParser;

public class AbstractActivity extends AppCompatActivity {
    protected Security security;
    protected RetrofitFactory retrofitFactory;
    protected TokenParser tokenParser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        security = new Security(getApplicationContext());
        retrofitFactory = new RetrofitFactory(security);
        tokenParser = new TokenParser();
    }
}
