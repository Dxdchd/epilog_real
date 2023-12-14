package com.example.epilog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ChooseFeelWeatherDialog extends Dialog {

    private CustomListener customListener;
    private Context context;

    public void setCustom(CustomListener customListener) {
        this.customListener = customListener;
    }

    public ChooseFeelWeatherDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feel_weather);

        LayoutInflater inflater = getLayoutInflater();
        View par = inflater.inflate(R.layout.activity_makeblog, null);
        ImageView wimg = par.findViewById(R.id.weather);
        ImageView fimg = par.findViewById(R.id.feel);
        Log.d("test", "onCreate: "+wimg.getDrawable());
        getWindow().setLayout((int) (350 * getContext().getResources().getDisplayMetrics().density), ViewGroup.LayoutParams.WRAP_CONTENT);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageButton btn = findViewById(R.id.wfdone);

        RadioGroup feelgroup = findViewById(R.id.dialogfeel);
        RadioGroup weathergroup = findViewById(R.id.dialogweather);

        btn.setOnClickListener(view -> {
            RadioButton feelchecked = findViewById(feelgroup.getCheckedRadioButtonId());
            RadioButton weatherchecked = findViewById(weathergroup.getCheckedRadioButtonId());
            customListener.doneClicked(feelchecked.getId(), weatherchecked.getId());
            dismiss();
        });
    }
}
