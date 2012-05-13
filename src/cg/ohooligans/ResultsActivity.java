package cg.ohooligans;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Sergey Mashkov
 */
public class ResultsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
    }

    public void update() {
        ((TextView)findViewById(R.id.summary)).setText(MainActivity.getMgr().computeSum() + " р.");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        update();
    }
}