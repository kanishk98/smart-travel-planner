package com.example.sargam.vacationplanner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    String[] resultA={"Trail Entry"};
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        listview=findViewById(R.id.lvMain);

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
