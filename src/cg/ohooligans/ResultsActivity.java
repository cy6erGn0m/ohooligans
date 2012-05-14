package cg.ohooligans;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Sergey Mashkov
 */
public class ResultsActivity extends Activity implements UpdatableActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        update();
    }

    @Override
    public void update() {
        ((TextView)findViewById(R.id.summary)).setText(getMainActivity().getMgr().computeSum() + " р.");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        update();
    }

    private MainActivity getMainActivity() {
        return (MainActivity) getParent();
    }
}
