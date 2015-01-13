package com.example.app;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Graph {
    private Context context;
    XYMultipleSeriesDataset dataset;
    XYMultipleSeriesRenderer renderer;
    int greater;
    public static boolean ClickEnabled = true;
    public Graph(Context context) {
        this.context = context;
    }

    public void initData(ConcurrentLinkedQueue<AccelData> sensorDatas){    
    	long t = sensorDatas.peek().getTimestamp();
    	
    
        XYSeries xSeries = new XYSeries("X");
        
        
        	for(AccelData data : sensorDatas){
        //	Log.d("sensordatas", "sensor: "+sensorDatas);
        	xSeries.add(data.getTimestamp() - t, data.getX());
        }
        	 greater = sensorDatas.size();
        	 Log.d("sensordatas", "greaterr: "+ greater);
        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(xSeries);
      
        renderer = new XYMultipleSeriesRenderer();
    }
    public void setProperties(){

        //renderer.setClickEnabled(ClickEnabled);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setApplyBackgroundColor(true);
        renderer.setXAxisMin(0);
        if(greater < 10){
            renderer.setXAxisMax(10);
            }
        else{
            renderer.setXAxisMin(greater-10);
            renderer.setXAxisMax(greater);
        }
    
        renderer.setChartTitle("AccelerometerData");
        renderer.setGridColor(Color.DKGRAY);
        renderer.setShowGrid(true);
        renderer.setXTitle("Tiempo ms");
        renderer.setXLabels(10);
        
        renderer.setBackgroundColor(Color.BLACK);
       
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setAxesColor(Color.BLACK);
		renderer.setLabelsColor(Color.RED);
		renderer.setZoomButtonsVisible(true);
	
        XYSeriesRenderer renderer1 = new XYSeriesRenderer();
        renderer1.setColor(Color.RED);
        
		renderer1.setPointStyle(PointStyle.CIRCLE);
		renderer1.setFillPoints(true);
		renderer1.setLineWidth(1);
		renderer1.setDisplayChartValues(false);
        renderer.addSeriesRenderer(renderer1);
        
       /* XYSeriesRenderer renderer2 = new XYSeriesRenderer();
        renderer2.setColor(Color.GREEN);
        renderer.addSeriesRenderer(renderer2);
        XYSeriesRenderer renderer3 = new XYSeriesRenderer();
        renderer3.setColor(Color.BLUE);
        renderer.addSeriesRenderer(renderer3);*/
    }


    public GraphicalView getGraph(){    
  
        return ChartFactory.getLineChartView(context, dataset, renderer);
        
    }
}