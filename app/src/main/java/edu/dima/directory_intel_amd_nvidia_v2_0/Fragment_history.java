package edu.dima.directory_intel_amd_nvidia_v2_0;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.NoCopySpan;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class Fragment_history extends Fragment implements View.OnClickListener {
    final String LOG_TAG = "myLogs";
    //Объявление важных переменных
    private Button btn_search_all, btn_clear_all;
    private TextView tv_all, tv_info_search_all;
    private Spinner spin_firma_all;
    private EditText year_1, year_2;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private MyAsyncTask myAsyncTask;
    private search_MyAsyncTask search_MyAsyncTask;
    private String[] arr_zapros = new String[3];
    private String[] arr_position = new String[3];
    private  ArrayList<String> firma_all = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ImageView img = rootView.findViewById(R.id.imageView2);
        tv_all = (TextView) rootView.findViewById(R.id.tv_all);
        tv_info_search_all = (TextView) rootView.findViewById(R.id.tv_info_search_all);
        btn_clear_all = rootView.findViewById(R.id.btn_clear_all);
        btn_search_all = rootView.findViewById(R.id.btn_search_all);
        year_1 = rootView.findViewById(R.id.et_year_1);
        year_2 = rootView.findViewById(R.id.et_year_2);
        clear();
        year_2.setText("2020");
        firma_all.add("Фирма");
        btn_clear_all.setOnClickListener(this);
        btn_search_all.setOnClickListener(this);

        mDBHelper = new DatabaseHelper(getActivity());
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();


        spin_firma_all = (Spinner) rootView.findViewById(R.id.spin_firma_all);
        ArrayAdapter<String> adapter_firma_all = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, firma_all);
        spin_firma_all.setAdapter(adapter_firma_all);
        spin_firma_all.setPrompt("Платформа");
        spin_firma_all.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                if (position != 0) {
                    arr_position[0] = firma_all.get(position);
                    arr_zapros[0] = "AND firmi.firma='" + firma_all.get(position) + "'";
                    tv_info_search_all.setText("Фирма: " + arr_position[0] + "\n" + "Год: с " + arr_position[1] + " по " + arr_position[2]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spin_firma_all.setSelection(0);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_search_all:
                arr_position[1] = year_1.getText().toString();
                arr_position[2] = year_2.getText().toString();
                tv_info_search_all.setText("Фирма: " + arr_position[0] + "\n" + "Год: с " + arr_position[1] + " по " + arr_position[2]);

                //Выводит информацию согласно критериям поиска
                search_MyAsyncTask = new search_MyAsyncTask();
                search_MyAsyncTask.execute();

                break;


            case R.id.btn_clear_all:

                for (int i = 0; i < 3; i++) {
                    arr_position[i] = "";
                    arr_zapros[i] = "";
                }
                tv_info_search_all.setText("");
                year_1.setText("");
                spin_firma_all.setSelection(0);
                tv_all.setText("");
                break;


        }
    }

    private void clear() {
        firma_all.clear();
        for (int i = 0; i < 3; i++) {
            arr_zapros[i] = "";
            arr_position[i] = "";
        }

        tv_info_search_all.setText("");
        tv_all.setText("");
        year_1.setText("");
        year_2.setText("");
    }

    public void create_drop_info(String sql, String column, ArrayList arr_list) {
        try{
        Cursor cursor = mDb.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            int date = cursor.getColumnIndex(column);
            do {
                arr_list.add(cursor.getString(date));
            } while (cursor.moveToNext());
        }
        cursor.close();}
        catch (Exception e){}
    }


    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... strings) {
            String sqlQuery_firma_all = "SELECT DISTINCT firmi.firma as Firma FROM firmi";
            create_drop_info(sqlQuery_firma_all, "Firma", firma_all);
            return null;
        }
    }


    public class search_MyAsyncTask extends AsyncTask<Void, Void, Void> {
        String vivod_temp = "";

        @Override
        protected void onPostExecute(Void aVoid) {

            tv_all.setText("");
            tv_all.setText(Html.fromHtml(vivod_temp));
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String sqlQuery__all = "SELECT DISTINCT history.year as Year, firmi.firma as Firma, history.info as Info FROM history INNER JOIN firmi ON history.kod_firmi=firmi.kod_firmi \n";
            String temp = "";

            if (!arr_position[0].equals("")) {
                temp += arr_zapros[0];
            }
            if (!arr_position[1].equals("") && !arr_position[2].equals("")) {
                temp += "AND history.year BETWEEN " + year_1.getText().toString() + " AND " + year_2.getText().toString();
            }
            sqlQuery__all += "WHERE history.id_history>0 " + temp + "\n ORDER BY history.year";
            try{
            Cursor cursor__all = mDb.rawQuery(sqlQuery__all, null);
            if (cursor__all.moveToFirst()) {
                int Year = cursor__all.getColumnIndex("Year");
                int Firma = cursor__all.getColumnIndex("Firma");
                int Info = cursor__all.getColumnIndex("Info");

                do {
                    vivod_temp += "<b> Год: </b>" + cursor__all.getString(Year) +
                            "       <b>Фирма: </b>" + cursor__all.getString(Firma) +
                            " <br>" + "<b>Информация:</b> <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cursor__all.getString(Info) +
                            "<br>-----------------------------------------------------------<br><br>";
                } while (cursor__all.moveToNext());
            } else
                Log.d("mLog", "0 rows");
            cursor__all.close();}
            catch (Exception e){}
            return null;
        }
    }
}
