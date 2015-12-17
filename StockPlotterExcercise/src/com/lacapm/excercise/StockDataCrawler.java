package com.lacapm.excercise;


import java.util.List;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;


/*
 * This program takes a stock query and crawls stock price data from Yahoo.com
 */

public class StockDataCrawler {
	private final String urlPrefix = "http://ichart.finance.yahoo.com/table.csv";
	private final String urlPostfix = "&g=d&ignore=.csv";
	private StockQuery stockQuery;
  
	public StockDataCrawler(StockQuery stockQuery) {
		this.stockQuery = stockQuery;   
	}
	
	
	/* This method resolves the complete query URL for Yahoo API 
	 * 
	 * @return String : the complete query URL
     */	
	public String stockURL() {
		// check invalid cases
		if (stockQuery == null || stockQuery.getStockName() == null || stockQuery.getStartDate() == null || stockQuery.getEndDate() == null
				|| stockQuery.getStockName().length() == 0 || stockQuery.getStartDate().length() == 0 || stockQuery.getEndDate().length() == 0)  {
			
			return new String();
		}
		
		// obtain query stock name
		String urlStockName = "?s=" + stockQuery.getStockName();
		
		// obtain query start date
		String start = stockQuery.getStartDate();
		String urlStartDate = "&a=" + (Integer.parseInt(start.substring(4, 6))-1) + "&b=" + start.substring(6, 8) + "&c=" + start.substring(0, 4);
		
		// obtain query end date
		String end = stockQuery.getEndDate();
		String urlEndDate = "&d=" + (Integer.parseInt(end.substring(4, 6))-1) + "&e=" + end.substring(6, 8) + "&f=" + end.substring(0, 4);
		
		// return complete query URL
		return urlPrefix + urlStockName + urlStartDate + urlEndDate + urlPostfix;
	}
  
	/*
	 * This method fetches stock data through Yahoo API and return 
	 * as a list of double values
	 * 
	 * @return List<Double> : a list of double values of stock prices
	 */
	
	public List<Double> crawleStockData() {

		// Obtain the full stock query URL
		String stockUrl = stockURL();
		System.out.println("Query url: " + stockUrl);
		// check invalid URLs
		if (stockUrl == null || stockUrl.length() == 0) {
			return null;
		}
		
		List<Double> stockData = new ArrayList<Double>();
		
		// Initiate a crawling connection
		URL stockURL;
		
		try {
			stockURL = new URL(stockUrl);
			BufferedReader in = null;
			
			try {
				// read incoming response
				in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
				// skip first line, no valid data
				String line = in.readLine();
				
				// read line by line and obtain the last value
				while ((line = in.readLine()) != null) {
					// separate each line into different parameters using comma as separator
					String[] entities = line.split(",");
          	
					// store the stock data in reversed order
					stockData.add(0, Double.parseDouble(entities[entities.length-1]));

				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
				// clean the IO stream
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (MalformedURLException e) {
			System.out.println("Invalid URL !!!");
			e.printStackTrace();
		}
		
		return stockData;
	} 
	
	// test case, can be commented
	public static void main(String args[]) {

		String startDate = "20151201";
		String endDate = "20151209";
		
		System.out.println("Start Date: " + startDate);
		System.out.println("End Date: " + endDate);
		
		StockQuery stockQuery = new StockQuery("SPY", startDate, endDate);
		StockDataCrawler sdc = new StockDataCrawler(stockQuery);
		
		List<Double> stockData = sdc.crawleStockData();
		
		for (double data : stockData) {
			System.out.print(data + " ");
		}
	}
}
