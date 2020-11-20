package edu.dima.directory_intel_amd_nvidia_v2_0;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn_history, btn_model;
    private Fragment_start fragment_start;
    private Fragment_all fragment_all;
    private Fragment_history fragment_history;
    private FragmentTransaction fTrans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_history = findViewById(R.id.btn_history);
        btn_model = findViewById(R.id.btn_model);

        btn_history.setOnClickListener(this);
        btn_model.setOnClickListener(this);


        fragment_all = new Fragment_all();
        fragment_history = new Fragment_history();
        fragment_start = new Fragment_start();


        fTrans = getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
        fTrans.replace(R.id.fragment1, fragment_start);
        fTrans.commit();
    }


    @Override
    public void onClick(View view) {

        fTrans = getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);

        try {

            System.out.println("onClick");
            switch (view.getId()) {

                case R.id.btn_model:

                    fTrans.replace(R.id.fragment1, fragment_all);
                    btn_model.setBackgroundColor(getResources().getColor(R.color.bg_btn_check_bottom));
                    btn_history.setBackgroundColor(getResources().getColor(R.color.bg_btn_bottom));
                    break;


                case R.id.btn_history:
                    fTrans.replace(R.id.fragment1, fragment_history);
                    btn_model.setBackgroundColor(getResources().getColor(R.color.bg_btn_bottom));
                    btn_history.setBackgroundColor(getResources().getColor(R.color.bg_btn_check_bottom));
                    break;

            }

            fTrans.commit();
        } catch (Exception e) {
        }
    }


}