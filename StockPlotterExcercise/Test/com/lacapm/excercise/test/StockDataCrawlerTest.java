package com.lacapm.excercise.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.lacapm.excercise.StockDataCrawler;
import com.lacapm.excercise.StockQuery;

public class StockDataCrawlerTest {	
	String startDate = "20151201";
	String endDate = "20151209";
	
	StockQuery stockQuery = new StockQuery("SPY", startDate, endDate);
	StockDataCrawler sdc = new StockDataCrawler(stockQuery);
	
	StockQuery stockQueryEmpty = null;
	StockDataCrawler sdcEmpty = new StockDataCrawler(stockQueryEmpty);	
	
	@Test
	public void testStockURL() {		
		String correctURL = "http://ichart.finance.yahoo.com/table.csv?s=SPY&a=11&b=01&c=2015&d=11&e=09&f=2015&g=d&ignore=.csv";
		System.out.println("Query URL of stock data of SPY from 12/01/2015 to 12/09/2015:");
		assertEquals(correctURL, sdc.stockURL());
	}
	
	@Test
	public void testEmptyStockURL() {
		System.out.println("Empty query:");
		assertEquals(sdcEmpty.stockURL().length(), 0);
	}
	
	@Test
	public void testIncompleteStockURL() {
		StockQuery stockQuery1 = new StockQuery("", "20151201", "20151209");
		StockQuery stockQuery2 = new StockQuery("SPY", "", "20151209");
		StockQuery stockQuery3 = new StockQuery("SPY", "20151201", "");
		System.out.println("Incomplete queries:");
		assertEquals(new StockDataCrawler(stockQuery1).stockURL().length(), 0);
		assertEquals(new StockDataCrawler(stockQuery2).stockURL().length(), 0);
		assertEquals(new StockDataCrawler(stockQuery3).stockURL().length(), 0);
	}
	
	@Test
	public void testCrawleStockData() {
		Double[] data = {210.679993, 208.529999, 205.610001, 209.619995, 208.350006, 206.949997, 205.339996};
		List<Double> stockData = Arrays.asList(data);
		System.out.println("Query stock data of SPY from 12/01/2015 to 12/09/2015.");
		assertEquals(stockData, sdc.crawleStockData());
	}
	
	@Test
	public void testCrawleStockDataEmptyQuery() {
		System.out.println("Empty Query:");
		assertNull(sdcEmpty.crawleStockData());
	}

}
