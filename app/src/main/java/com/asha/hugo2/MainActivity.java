package com.asha.hugo2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;

import hugo.weaving.internal.StackPrinter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long nano = System.nanoTime();
                long ts = System.currentTimeMillis();
                StackPrinter.shared().printIn(nano, ts, "halo0");
                StackPrinter.shared().printOut(nano, ts, "halo0");
            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long nano = System.nanoTime();
                long ts = System.currentTimeMillis();
                StackPrinter.shared().printIn(nano, ts, "halo0");
                StackPrinter.shared().printOut(nano, ts,"halo0");
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StackPrinter.shared().dump(new File(v.getContext().getExternalCacheDir(), "dump.txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
