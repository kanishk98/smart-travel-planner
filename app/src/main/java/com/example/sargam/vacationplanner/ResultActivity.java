package com.example.sargam.vacationplanner;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class ResultActivity extends AppCompatActivity {

    String[] resultA=new String[1];
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultA = getIntent().getStringArrayExtra("params");
        listview=findViewById(R.id.lvMain);
        class GetPreds extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                String predictionsRead = null;
                String url = "http://18.188.218.137/predict?quarter=1&age=20&gender=male&duration=40&budget="+resultA[2];
                try {
                    URLConnection connection = new URL(url).openConnection();
                    InputStream response = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(response));
                    predictionsRead = br.readLine();
                    Log.d("predictions", predictionsRead);
                    predictionsRead = predictionsRead.substring(1, predictionsRead.length() - 1);
                    StringTokenizer st = new StringTokenizer(predictionsRead, ",");
                    int i = 0;
                    while(st.hasMoreTokens()) {
                        resultA[i] = st.nextToken();
                        Log.d("predictions", resultA[i]);
                        i = i + 1;
                    }
                    Log.d("ResultActivity", predictionsRead);
                } catch (IOException ioException) {
                    Log.d("RA", ioException.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setupList();
            }
        }
        new GetPreds().execute();
        setupList();
    }

    public void setupList()
    {
        ResultActivity.SimpleAdapter sa= new SimpleAdapter(ResultActivity.this,resultA);
        listview.setAdapter(sa);
    }


    public class SimpleAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater layoutinflater;
        TextView code;
        String[] result;







        public SimpleAdapter(Context context, String[] result_Array)
        {
            mContext=context;
            result=result_Array;

            layoutinflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            return result[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)
            {
                convertView=layoutinflater.inflate(R.layout.search_result_layout,null);
            }
            code=convertView.findViewById(R.id.code);

            code.setText(result[position]);


            return convertView;



        }
    }
}
