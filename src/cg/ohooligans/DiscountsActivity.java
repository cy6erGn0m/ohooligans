package cg.ohooligans;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author Sergey Mashkov
 */
public class DiscountsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discounts);
        
        ((CheckBox)findViewById(R.id.businessLunch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                updateModel();
            }
        });
        ((RadioGroup)findViewById(R.id.discountsGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                updateModel();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateView();
    }

    private void updateView() {
        int currentDiscount = MainActivity.getMgr().getDiscount();
        boolean businessLunch = MainActivity.getMgr().isBusinessLunch();

        ((CheckBox)findViewById(R.id.businessLunch)).setChecked(businessLunch);
        int id;
        if (currentDiscount == 0) {
            id = R.id.card_none;
        } else if (currentDiscount == 10) {
            id = R.id.card_ten;
        } else if (currentDiscount == 20) {
            id = R.id.card_twenty;
        } else {
            throw new IllegalStateException("Unsupported discount card!");
        }

        ((RadioButton)findViewById(id)).setChecked(true);
    }

    private void updateModel() {
        boolean businessLunch = ((CheckBox)findViewById(R.id.businessLunch)).isChecked();
        int currentDiscount = 0;
        
        switch (((RadioGroup)findViewById(R.id.discountsGroup)).getCheckedRadioButtonId()) {
            case R.id.card_none:
                currentDiscount = 0;
                break;
            case R.id.card_ten:
                currentDiscount = 10;
                break;
            case R.id.card_twenty:
                currentDiscount = 20;
                break;
        }

        MainActivity.getMgr().setBusinessLunch(businessLunch);
        MainActivity.getMgr().setDiscount(currentDiscount);
    }
}