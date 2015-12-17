package com.lacapm.excercise.test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.lacapm.excercise.StockCumuReturnPlotter;
import com.lacapm.excercise.StockQuery;

public class StockCumuReturnPlotterTest {
	StockCumuReturnPlotter plotter = null;			
	
	@Test
	public void testLoadIniFile() {
		plotter = new StockCumuReturnPlotter("config.ini");
		System.out.println("Loading plot settings from config.ini:");
		assertEquals(plotter.loadIniFile().size(), 3);
	}
	
	@Test
	public void testLoadEmptyIniFile() {
		plotter = new StockCumuReturnPlotter("empty.ini");
		System.out.println("Loading empty plot settings from empty.ini:");
		assertEquals(plotter.loadIniFile().size(), 0);
	}
	
	@Test
	public void testLoadIniFileWithSingleStock() {
		plotter = new StockCumuReturnPlotter("single.ini");
		System.out.println("Loading single plot setting from single.ini:");
		assertEquals(plotter.loadIniFile().size(), 1);
	}
	
	@Test
	public void testCalculateCumuReturnsEmptyQuery() {
		List<StockQuery> list = new ArrayList<StockQuery>();
		plotter = new StockCumuReturnPlotter("config.ini");
		System.out.println("Calculate cumulative returns with empty query: Expected return null");
		assertNull(plotter.calculateCumuReturns(list));
	}
	
	@Test
	public void testCalculateCumuReturnsSingle() {
		StockQuery stockQuery = new StockQuery("SPY", "20151201", "20151209");
		List<StockQuery> list = new ArrayList<StockQuery>();
		list.add(stockQuery);
		plotter = new StockCumuReturnPlotter("config.ini");
		System.out.println("Calculate cumulative returns with empty query: Expected return null");
		assertNotNull(plotter.calculateCumuReturns(list));
		assertEquals(plotter.calculateCumuReturns(list).getSeriesCount(), 1);
	}
	
	@Test
	public void testCalculateCumuReturnsMultiple() {
		StockQuery stockQuery1 = new StockQuery("SPY", "20151201", "20151209");
		StockQuery stockQuery2 = new StockQuery("UCO", "20151101", "20151109");
		StockQuery stockQuery3 = new StockQuery("QQQ", "20151201", "20151209");
		List<StockQuery> list = new ArrayList<StockQuery>();
		list.add(stockQuery1);
		list.add(stockQuery2);
		list.add(stockQuery3);
		
		plotter = new StockCumuReturnPlotter("config.ini");
		System.out.println("Calculate cumulative returns with empty query: Expected return null");
		assertNotNull(plotter.calculateCumuReturns(list));
		assertEquals(plotter.calculateCumuReturns(list).getSeriesCount(), 3);
	}

}
