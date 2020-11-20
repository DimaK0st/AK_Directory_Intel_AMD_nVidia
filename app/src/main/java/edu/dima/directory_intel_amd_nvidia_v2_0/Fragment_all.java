package edu.dima.directory_intel_amd_nvidia_v2_0;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class Fragment_all extends Fragment implements View.OnClickListener {
    final String LOG_TAG = "myLogs";
    //Объявление важных переменных
    private Button btn_search_all, btn_clear_all;
    private TextView tv_all, tv_info_search_all;
    private Spinner spin_marka_all, spin_platform_all, spin_god_all, spin_type_all, spin_firma_all;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    private MyAsyncTask myAsyncTask;
    private search_MyAsyncTask search_MyAsyncTask;

    private String[] arr_zapros = new String[5];
    private String[] arr_position = new String[5];

    private ArrayList<String> firma_all = new ArrayList<>();
    private ArrayList<String> type_all = new ArrayList<>();
    private ArrayList<String> platforma_all = new ArrayList<>();
    private ArrayList<String> marka_all = new ArrayList<>();
    private ArrayList<String> god_all = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);
        ImageView img = rootView.findViewById(R.id.imageView2);
        tv_all = (TextView) rootView.findViewById(R.id.tv_all);
        tv_info_search_all = (TextView) rootView.findViewById(R.id.tv_info_search_all);
        btn_clear_all = rootView.findViewById(R.id.btn_clear_all);
        btn_search_all = rootView.findViewById(R.id.btn_search_all);
        clear();
        god_all.add("Год");
        marka_all.add("Марка");
        platforma_all.add("Платформа");
        type_all.add("Тип");
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


        spin_god_all = (Spinner) rootView.findViewById(R.id.spin_god_all);
        ArrayAdapter<String> adapter_god_all = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, god_all);
        spin_god_all.setAdapter(adapter_god_all);
        spin_god_all.setPrompt("Год");
        spin_god_all.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    arr_position[0] = god_all.get(position);
                    arr_zapros[0] = "AND strftime('%Y', processor_intel.date)='" + god_all.get(position) + "'";
                    tv_info_search_all.setText("Фирма: " + arr_position[4] + "\nМарка: " + arr_position[1] + "\n" + "Платформа: " + arr_position[2] + "\n" + "Тип: " + arr_position[3] + "    Год: " + arr_position[0]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spin_marka_all = (Spinner) rootView.findViewById(R.id.spin_marka_all);
        ArrayAdapter<String> adapter_marka_all = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, marka_all);
        spin_marka_all.setAdapter(adapter_marka_all);
        spin_marka_all.setPrompt("Марка");

        spin_marka_all.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                if (position != 0) {
                    arr_position[1] = marka_all.get(position);
                    arr_zapros[1] = "AND marka.marka='" + marka_all.get(position) + "'";
                    tv_info_search_all.setText("Фирма: " + arr_position[4] + "\nМарка: " + arr_position[1] + "\n" + "Платформа: " + arr_position[2] + "\n" + "Тип: " + arr_position[3] + "    Год: " + arr_position[0]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spin_platform_all = (Spinner) rootView.findViewById(R.id.spin_platform_all);
        ArrayAdapter<String> adapter_platforma_all = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, platforma_all);
        spin_platform_all.setAdapter(adapter_platforma_all);
        spin_platform_all.setPrompt("Платформа");
        spin_platform_all.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                if (position != 0) {
                    arr_position[2] = platforma_all.get(position);
                    arr_zapros[2] = "AND platform.platform='" + platforma_all.get(position) + "'";
                    tv_info_search_all.setText("Фирма: " + arr_position[4] + "\nМарка: " + arr_position[1] + "\n" + "Платформа: " + arr_position[2] + "\n" + "Тип: " + arr_position[3] + "    Год: " + arr_position[0]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spin_type_all = (Spinner) rootView.findViewById(R.id.spin_type_all);
        ArrayAdapter<String> adapter_type_all = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type_all);
        spin_type_all.setAdapter(adapter_type_all);
        spin_type_all.setPrompt("Платформа");
        spin_type_all.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                if (position != 0) {
                    arr_position[3] = type_all.get(position);
                    arr_zapros[3] = "AND type.type='" + type_all.get(position) + "'";
                    tv_info_search_all.setText("Фирма: " + arr_position[4] + "\nМарка: " + arr_position[1] + "\n" + "Платформа: " + arr_position[2] + "\n" + "Тип: " + arr_position[3] + "    Год: " + arr_position[0]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

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
                    arr_position[4] = firma_all.get(position);
                    arr_zapros[4] = "AND firmi.firma='" + firma_all.get(position) + "'";
                    tv_info_search_all.setText("Фирма: " + arr_position[4] + "\nМарка: " + arr_position[1] + "\n" + "Платформа: " + arr_position[2] + "\n" + "Тип: " + arr_position[3] + "    Год: " + arr_position[0]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spin_god_all.setSelection(0);
        spin_marka_all.setSelection(0);
        spin_platform_all.setSelection(0);
        spin_type_all.setSelection(0);
        spin_firma_all.setSelection(0);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_search_all:
                //Выводит информацию согласно критериям поиска
                search_MyAsyncTask = new search_MyAsyncTask();
                search_MyAsyncTask.execute();
                tv_info_search_all.setText("Фирма: " + arr_position[4] + "\nМарка: " + arr_position[1] + "\n" + "Платформа: " + arr_position[2] + "\n" + "Тип: " + arr_position[3] + "    Год: " + arr_position[0]);

                break;


            case R.id.btn_clear_all:

                for (int i = 0; i < 5; i++) {
                    arr_position[i] = "";
                    arr_zapros[i] = "";
                }
                tv_info_search_all.setText("");
                spin_god_all.setSelection(0);
                spin_marka_all.setSelection(0);
                spin_platform_all.setSelection(0);
                spin_type_all.setSelection(0);
                spin_firma_all.setSelection(0);
                tv_all.setText("");
                break;


        }
    }

    private void clear() {
        god_all.clear();
        marka_all.clear();
        platforma_all.clear();
        type_all.clear();
        firma_all.clear();
        for (int i = 0; i < 5; i++) {
            arr_position[i] = "";
            arr_zapros[i] = "";
        }
        tv_info_search_all.setText("");
        tv_all.setText("");
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

    // Метод, который выполняет какие-то действия в фоновом режиме.
    private void backgroundThreadProcessing() {
        String sqlQuery_god_all = "SELECT DISTINCT strftime('%Y', processor_intel.date) as Date FROM processor_intel WHERE processor_intel.kod_platform>0 ORDER by Date";
        create_drop_info(sqlQuery_god_all, "Date", god_all);

        String sqlQuery_marka_all = "SELECT DISTINCT marka.marka as Marka FROM marka INNER JOIN processor_intel ON marka.kod_marki=processor_intel.kod_marki ORDER BY marka.marka";
        create_drop_info(sqlQuery_marka_all, "Marka", marka_all);

        String sqlQuery_platforma_all = "SELECT DISTINCT platform.platform as Platforma FROM platform INNER JOIN processor_intel ON platform.kod_platform=processor_intel.kod_platform GROUP BY platform.platform";
        create_drop_info(sqlQuery_platforma_all, "Platforma", platforma_all);

        String sqlQuery_type_all = "SELECT DISTINCT type as Type FROM type";
        create_drop_info(sqlQuery_type_all, "Type", type_all);

        String sqlQuery_firma_all = "SELECT DISTINCT firmi.firma as Firma FROM firmi";
        create_drop_info(sqlQuery_firma_all, "Firma", firma_all);

    }


    public class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            backgroundThreadProcessing();
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
            String sqlQuery__all;
            String sql_first = "SELECT firmi.firma as Firma, marka.marka as Marka, processor_intel.name as Name,  processor_intel.info as Info,processor_intel.date as Date, platform.platform as Platform , type.type as Type FROM processor_intel  INNER JOIN platform ON processor_intel.kod_platform= platform.kod_platform INNER JOIN marka ON processor_intel.kod_marki =marka.kod_marki  INNER JOIN firmi on processor_intel.kod_firmi = firmi.kod_firmi INNER JOIN type ON processor_intel.kod_type= type.kod_type ";
            String sql_last = "ORDER BY firmi.kod_firmi, type.kod_type, processor_intel.name";
            if (arr_position[0] == "" && arr_position[1] == "" && arr_position[2] == "" && arr_position[3] == "" && arr_position[4] == "") {

                sqlQuery__all = sql_first + "WHERE  processor_intel.kod_platform>0\n" + sql_last;

            } else {
                sqlQuery__all = sql_first +
                        "WHERE  processor_intel.kod_platform>0 " + arr_zapros[0] + arr_zapros[1] + arr_zapros[2] + arr_zapros[3] + arr_zapros[4] + sql_last;
            }
            try{
            Cursor cursor__all = mDb.rawQuery(sqlQuery__all, null);
            if (cursor__all.moveToFirst()) {
                int firma = cursor__all.getColumnIndex("Firma");
                int marka = cursor__all.getColumnIndex("Marka");
                int name = cursor__all.getColumnIndex("Name");
                int info = cursor__all.getColumnIndex("Info");
                int date = cursor__all.getColumnIndex("Date");
                int platform = cursor__all.getColumnIndex("Platform");
                int type = cursor__all.getColumnIndex("Type");
                do {
                    vivod_temp += " <b>Фирма: </b>" + cursor__all.getString(firma) +
                            " <br>" + " <b>Марка: </b>" + cursor__all.getString(marka) +
                            "<br> <b>Серия: </b>" + cursor__all.getString(name) +
                            "<br> <b>Информация: </b>" + cursor__all.getString(info) +
                            "<br> <b>Дата выхода: </b>" + cursor__all.getString(date) +
                            "<br> <b>Платформа: </b>" + cursor__all.getString(platform) +
                            "<br> <b>Тип: </b>" + cursor__all.getString(type) +
                            "<br>-------------------------------------<br><br>";
                } while (cursor__all.moveToNext());
            } else
                Log.d("mLog", "0 rows");
            cursor__all.close();}
            catch (Exception e){}
            return null;
        }
    }
}
