package com.example.android_supervisor.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.cncoderx.recyclerviewhelper.adapter.ArrayAdapter;
import com.example.android_supervisor.R;

/**
 * @author wujie
 */
public class TutorialActivity extends ListActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnableRefresh(false);
        setEnableLoadMore(false);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRefreshLayout.setEnabled(false);

        String[] sources = new String[6];
        sources[0] = getString(R.string.tutorial_title_1);
        sources[1] = getString(R.string.tutorial_title_2);
        sources[2] = getString(R.string.tutorial_title_3);
        sources[3] = getString(R.string.tutorial_title_4);
        sources[4] = getString(R.string.tutorial_title_5);
        sources[5] = getString(R.string.tutorial_title_6);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(R.layout.item_tutorial, sources);
        setAdapter(adapter);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        int textId = 0;
        switch (position) {
            case 0:
                textId = R.string.tutorial_content_1;
                break;
            case 1:
                textId = R.string.tutorial_content_2;
                break;
            case 2:
                textId = R.string.tutorial_content_3;
                break;
            case 3:
                textId = R.string.tutorial_content_4;
                break;
            case 4:
                textId = R.string.tutorial_content_5;
                break;
            case 5:
                textId = R.string.tutorial_content_6;
                break;
        }
        Intent intent = new Intent(TutorialActivity.this, TutorialSubActivity.class);
        intent.putExtra("textId", textId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
