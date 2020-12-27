package com.badr.mockapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    public static RecyclerView recyclerView;
    private ViewAdapter viewAdapter;
    public static List<DataItem> itemsList;
    private View bottomHorizontalDivider;

    private LineChart lineChartRSRP;
    private LineChart lineChartRSRQ;
    private LineChart lineChartSINR;

    LinearLayout linearLayoutData, internetInfo;

    List<Entry> entryRSRPList;
    List<Entry> entryRSRQList;
    List<Entry> entrySINRList;

    int selectedChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        internetInfo = (LinearLayout) findViewById(R.id.internetInfo);
        linearLayoutData = (LinearLayout) findViewById(R.id.linearLayoutData);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        bottomHorizontalDivider = (View) findViewById(R.id.bottomHorizontalDivider);

        lineChartRSRP = (LineChart) findViewById(R.id.lineChartRSRP);
        lineChartRSRQ = (LineChart) findViewById(R.id.lineChartRSRQ);
        lineChartSINR = (LineChart) findViewById(R.id.lineChartSINR);

        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new com.badr.mockapp.DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.horizontal_divider)));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        itemsList = new ArrayList<DataItem>();

        entryRSRPList = new ArrayList<Entry>();
        entryRSRQList = new ArrayList<Entry>();
        entrySINRList = new ArrayList<Entry>();

        selectedChart = 0;
        setVisibleChart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Timer timer = new Timer();
        int timeInterval = 2000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                startLooper(selectedChart);
                if (!InternetStatus.isConnect(getApplicationContext()))
                    timer.cancel();
            }
        }, 0, timeInterval);

        setupChart(selectedChart, lineChartRSRP);
    }

    private void startLooper(final int mode) {

        if (InternetStatus.isConnect(getApplicationContext())) {

            if (linearLayoutData.getVisibility() == View.GONE) linearLayoutData.setVisibility(View.VISIBLE);
            if (internetInfo.getVisibility() == View.VISIBLE) internetInfo.setVisibility(View.GONE);

            getLiveData(new Callback() {

                private float xAxis = 0.0f;
                private String label;

                @Override
                public void onCallback(String data) {

                    try {

                        JSONObject jsonObject = new JSONObject(data);

                        double RSRP = Double.parseDouble(String.valueOf(jsonObject.get("RSRP")));
                        double RSRQ = Double.parseDouble(String.valueOf(jsonObject.get("RSRQ")));
                        double SINR = Double.parseDouble(String.valueOf(jsonObject.get("SINR")));

                        String currentTime = new SimpleDateFormat("mm:ss").format(new Date(System.currentTimeMillis()));

                        addDataToRecyclerView(new DataItem(RSRP, RSRQ, SINR, currentTime));

                        xAxis = Float.parseFloat(currentTime.replaceAll(":", "."));
                        float yAxisRSRP = (float) RSRP;
                        float yAxisRSRQ = (float) RSRQ;
                        float yAxisSINR = (float) SINR;

                        Log.i("info -> Time: ", currentTime + " | " + yAxisRSRP + " | " + yAxisRSRQ + " | " + yAxisSINR);

                        if (mode == 0) {

                            label = "RSRP Data";

                        } else if (mode == 1) {

                            label = "RSRQ Data";

                        } else if (mode == 2) {

                            label = "SINR Data";
                        }

                        // set RSRP data to lineChartRSRP
                        Entry entryRSRP = new Entry(xAxis, yAxisRSRP);
                        entryRSRPList.add(entryRSRP);
                        LineDataSet lineDataSetRSRP = new LineDataSet(entryRSRPList, label);
                        lineDataSetRSRP.setAxisDependency(YAxis.AxisDependency.LEFT);
                        lineDataSetRSRP.setColor(ColorTemplate.getHoloBlue());
                        lineDataSetRSRP.setCircleColor(Color.BLUE);
                        lineDataSetRSRP.setLineWidth(2f);
                        lineDataSetRSRP.setCircleRadius(4f);
                        lineDataSetRSRP.setFillAlpha(65);
                        lineDataSetRSRP.setFillColor(ColorTemplate.getHoloBlue());
                        lineDataSetRSRP.setHighLightColor(Color.rgb(244, 117, 117));
//                        lineDataSet.setValueTextColor(Color.RED);
//                        lineDataSet.setValueTextSize(9f);
                        lineDataSetRSRP.setDrawValues(false);

                        // use the interface ILineDataSet
                        List<ILineDataSet> dataSetsRSRP = new ArrayList<ILineDataSet>();
                        dataSetsRSRP.add(lineDataSetRSRP);

                        LineData lineDataRSRP = new LineData(dataSetsRSRP);
                        lineChartRSRP.setData(lineDataRSRP);
                        lineChartRSRP.invalidate(); // refresh
                        lineChartRSRP.notifyDataSetChanged();

                        // set RSRQ data to lineChartRSRQ
                        Entry entryRSRQ = new Entry(xAxis, yAxisRSRQ);
                        entryRSRQList.add(entryRSRQ);
                        LineDataSet lineDataSetRSRQ = new LineDataSet(entryRSRQList, label);
                        lineDataSetRSRQ.setAxisDependency(YAxis.AxisDependency.LEFT);
                        lineDataSetRSRQ.setColor(ColorTemplate.getHoloBlue());
                        lineDataSetRSRQ.setCircleColor(Color.BLUE);
                        lineDataSetRSRQ.setLineWidth(2f);
                        lineDataSetRSRQ.setCircleRadius(4f);
                        lineDataSetRSRQ.setFillAlpha(65);
                        lineDataSetRSRQ.setFillColor(ColorTemplate.getHoloBlue());
                        lineDataSetRSRQ.setHighLightColor(Color.rgb(244, 117, 117));
                        lineDataSetRSRQ.setDrawValues(false);
                        List<ILineDataSet> dataSetsRSRQ = new ArrayList<ILineDataSet>();
                        dataSetsRSRQ.add(lineDataSetRSRQ);
                        LineData lineDataRSRQ = new LineData(dataSetsRSRQ);
                        lineChartRSRQ.setData(lineDataRSRQ);
                        lineChartRSRQ.invalidate(); // refresh
                        lineChartRSRQ.notifyDataSetChanged();

                        // set SINR data to lineChartSINR
                        Entry entrySINR = new Entry(xAxis, yAxisSINR);
                        entrySINRList.add(entrySINR);
                        LineDataSet lineDataSetSINR = new LineDataSet(entrySINRList, label);
                        lineDataSetSINR.setAxisDependency(YAxis.AxisDependency.LEFT);
                        lineDataSetSINR.setColor(ColorTemplate.getHoloBlue());
                        lineDataSetSINR.setCircleColor(Color.BLUE);
                        lineDataSetSINR.setLineWidth(2f);
                        lineDataSetSINR.setCircleRadius(4f);
                        lineDataSetSINR.setFillAlpha(65);
                        lineDataSetSINR.setFillColor(ColorTemplate.getHoloBlue());
                        lineDataSetSINR.setHighLightColor(Color.rgb(244, 117, 117));
                        lineDataSetSINR.setDrawValues(false);
                        List<ILineDataSet> dataSetsSINR = new ArrayList<ILineDataSet>();
                        dataSetsSINR.add(lineDataSetSINR);
                        LineData lineDataSINR = new LineData(dataSetsSINR);
                        lineChartSINR.setData(lineDataSINR);
                        lineChartSINR.invalidate(); // refresh
                        lineChartSINR.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        } else {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Please check internet connection!", Toast.LENGTH_SHORT).show();
                }
            });
            linearLayoutData.setVisibility(View.GONE);
            internetInfo.setVisibility(View.VISIBLE);
        }
    }

    private void getLiveData(final Callback callback) {

        String apiUrl = "http://51.195.89.92:6000/random";

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        callback.onCallback(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error != null)
                        Log.i("appInfo: ", error.getMessage());
                } catch (Exception e) {
                    return;
                }
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    private void addDataToRecyclerView(DataItem dataItem) {

        itemsList.add(dataItem);

        viewAdapter = new ViewAdapter(itemsList, getApplication());
        recyclerView.setAdapter(viewAdapter);

        if (bottomHorizontalDivider.getVisibility() == View.GONE)
            bottomHorizontalDivider.setVisibility(View.VISIBLE);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }

    private void setupChart(int mode, LineChart chart) {

        chart.setOnChartValueSelectedListener(MainActivity.this);
        // enable description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);
        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY);
        // init lineData
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);
        // add empty data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend legend = chart.getLegend();
        // modify the legend ...
        legend.setForm(Legend.LegendForm.LINE);
        //legend.setTypeface(tfLight);
        legend.setTextColor(Color.BLUE);

        XAxis xl = chart.getXAxis();
        //xl.setTypeface(tfLight);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.BLACK);

        // NOTE:: This is deprecated now!, USE Min & Max
        //chart.getAxisLeft().setStartAtZero(false);

        if (mode == 0) {

            chart.getAxisLeft().setAxisMinimum(-150);
            chart.getAxisLeft().setAxisMaximum(-50);

        } else if (mode == 1) {

            chart.getAxisLeft().setAxisMinimum(-50);
            chart.getAxisLeft().setAxisMaximum(0);

        } else if (mode == 2) {

            chart.getAxisLeft().setAxisMinimum(-10);
            chart.getAxisLeft().setAxisMaximum(30);
        }

        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setVisibleChart() {

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_action_multiline_chart);
        floatingActionButton.setImageDrawable(myFabSrc);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (InternetStatus.isConnect(getApplicationContext())) {

                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.rsrp_chart: {
                                    selectedChart = 0;
                                    setupChart(selectedChart, lineChartRSRP);
                                    lineChartRSRP.setVisibility(View.VISIBLE);
                                    lineChartRSRQ.setVisibility(View.GONE);
                                    lineChartSINR.setVisibility(View.GONE);
                                }
                                break;

                                case R.id.rsrq_chart: {
                                    selectedChart = 1;
                                    setupChart(selectedChart, lineChartRSRQ);
                                    lineChartRSRQ.setVisibility(View.VISIBLE);
                                    lineChartRSRP.setVisibility(View.GONE);
                                    lineChartSINR.setVisibility(View.GONE);
                                }
                                break;

                                case R.id.sinr_chart: {
                                    selectedChart = 2;
                                    setupChart(selectedChart, lineChartSINR);
                                    lineChartSINR.setVisibility(View.VISIBLE);
                                    lineChartRSRP.setVisibility(View.GONE);
                                    lineChartRSRQ.setVisibility(View.GONE);
                                }
                                break;
                            }

                            return false;
                        }
                    });

                    popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu menu) {
                            return;
                        }
                    });

                    popupMenu.inflate(R.menu.realtime_chart);
                    popupMenu.show();

                } else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onValueSelected(Entry entry, Highlight h) {

        Toast.makeText(this, entry.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

        Log.i("appInfo", "Nothing selected.");
    }

}
