package com.foxfail.appium_bell;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.openqa.selenium.remote.DesiredCapabilities;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DesiredCapabilities capabilities = new DesiredCapabilities();

    }
}
