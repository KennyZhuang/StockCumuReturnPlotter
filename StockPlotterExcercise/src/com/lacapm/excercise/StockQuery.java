package com.lacapm.excercise;


public class StockQuery {
	private String stockName;
	private String startDate;
	private String endDate;  
  
	public StockQuery(String stockName, String startDate, String endDate) {
		this.stockName = stockName;		
		this.startDate = startDate;
		this.endDate = endDate;    
	}
  
	public String getStockName() {
		return stockName;
	}
  
	public String getStartDate() {
		return startDate;
	}
  
	public String getEndDate() {
		return endDate;
	}
}
