package com.lacapm.excercise;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.List;
import java.util.ArrayList;
import java.io.*;


/*
 * This program loads a configuration file for stocks to be queried,
 * calculates the cumulative return for each query and plot all data
 * in a 2D XY plot
 */

public class StockCumuReturnPlotter extends ApplicationFrame {
	private static final long serialVersionUID = 1L;
	
	// file path of the configuration ini file
	private String iniFilePath;
  
	public StockCumuReturnPlotter(String iniFilePath) {
		// Application title
		super("Cumulative Return Plot");
		this.iniFilePath = iniFilePath;
	}
  
	/*
	 * This method loads the local configuration file and return a list
	 * of StockQuery objects
	 * 
	 * @return List<StockQuery> : a list of stock queries
	 */
	
	public List<StockQuery> loadIniFile() {
	  
		List<StockQuery> stockQueries = new ArrayList<StockQuery>();
		
		// check invalid file paths
		if (iniFilePath == null || iniFilePath.length() == 0) {
			return stockQueries;
		}
		
		Ini stockIni;
		try {
			// loads the ini file and stores the content as a Map object
			stockIni = new Ini(new File(iniFilePath));
			
			// iterate through all entries and construct respective Stock Query object
			for (String key : stockIni.keySet()) {
				Ini.Section section = stockIni.get(key);
				String stockName = section.get("Ticker");
				String startDate = section.get("From");
				String endDate = section.get("To");
				
				// skip incomplete stock query
				if (stockName == null || stockName.length() == 0 || startDate == null || 
					startDate.length() == 0 || endDate == null || endDate.length() == 0) {
					
					System.out.println("Incomplete query information, skipped.");
					continue;
				}
				// add to the list to be returned
				stockQueries.add(new StockQuery(stockName, startDate, endDate));
  		  	}
			
		} catch (InvalidFileFormatException e) {
			System.out.println("Invalid file format !!!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Can not find file !!!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Can not read ini file !!!");
			e.printStackTrace();
		}
		return stockQueries;
  }
  
  /*
   * This method calculates the cumulative returns for each query in the 
   * ini file and plot all series in the same XY plot
   */
	
  public void plotCumuReturn() {  
	  List<StockQuery> stockQueries = loadIniFile();
	  
	  if (stockQueries == null || stockQueries.size() == 0) {
		  return;
	  }
	  // obtain all series of the cumulative returns
	  XYSeriesCollection cumuReturnsCollection = calculateCumuReturns(stockQueries);
	  
	  // create a XY 2D plot
	  JFreeChart chart = ChartFactory.createXYLineChart(
			  // Plot title
			  "Stock Cumulative Return",
			  // Plot X title
			  "Day", 
			  // plot Y title
			  "Cumulative Return", 
			  cumuReturnsCollection,
			  PlotOrientation.VERTICAL,
			  true,
			  true,
			  false
	  );
	  
	  // generate the XY plot in a window
	  ChartPanel chartPanel = new ChartPanel(chart);
	  chartPanel.setPreferredSize(new java.awt.Dimension(1000, 540));
	  setContentPane(chartPanel);
	  this.pack();
	  RefineryUtilities.centerFrameOnScreen(this);
	  this.setVisible(true);
  }
  
  /*
   * This method calculates the cumulative returns for each stock query 
   * and return a collection of XY series
   * 
   * @param  stockQueries: a list of StockQuery objects
   * @return XYSeriesCollection: a collection of all XY series
   */
  
  public XYSeriesCollection calculateCumuReturns(List<StockQuery> stockQueries) {
	  // check invalid input
	  if (stockQueries == null || stockQueries.size() == 0) {
		  return null;
	  }
    
	  XYSeriesCollection cumuReturnsCollection = new XYSeriesCollection();    
	  
	  // for each stock query, obtain data from Yahoo.com and calculate the cumulative returns
	  for (StockQuery stockQuery : stockQueries) {
		  // obtain stock data from Yahoo.com
		  List<Double> stockData = new StockDataCrawler(stockQuery).crawleStockData();
		  // create a new XYseries object
		  XYSeries stockSeries = new XYSeries(stockQuery.getStockName());

		  // calculate the cumulative return for each day
		  for (int index = 0; index < stockData.size(); index++) {
			  // cumulative return = (current - original) / original;
			  double cumuReturn = (stockData.get(index) - stockData.get(0)) / stockData.get(0);
			  stockSeries.add(index+1, cumuReturn);
		  }
		  
		  // add to the collection
		  cumuReturnsCollection.addSeries(stockSeries);
	  }
    
	  return cumuReturnsCollection;
  }
  
  // test case, can be commented
  public static void main(String[] args) {
	  	StockCumuReturnPlotter plotter = new StockCumuReturnPlotter("config.ini");	  	
	  	plotter.plotCumuReturn();
	}
  
}

