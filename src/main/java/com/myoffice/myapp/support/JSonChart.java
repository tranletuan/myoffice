package com.myoffice.myapp.support;

import java.util.ArrayList;
import java.util.List;

public class JSonChart {

	private String type;
	private String title;
	private String subtitle;
	
	private List<String> xAxisCategories = new ArrayList<String>();
	private String xAxisTitle;
	
	private String yAxisTitle;
	private String yAxislabels;
	
	private String legendLayout = "horizontal";
	private String legendAlign = "center";
	private String legendVerticalAlign = "bottom";
	private int legendX = 0; 
	private int legendY = 0;
	private boolean legendFloating = false;
	private int legendBorderWidth = 2;
	private boolean legendShadow = true;
	
	private String valueSuffix;
	
	List<JSSeries> series = new ArrayList<JSSeries>();

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public List<String> getxAxisCategories() {
		return xAxisCategories;
	}

	public String getxAxisTitle() {
		return xAxisTitle;
	}

	public String getyAxisTitle() {
		return yAxisTitle;
	}

	public String getValueSuffix() {
		return valueSuffix;
	}

	public List<JSSeries> getSeries() {
		return series;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public void setxAxisCategories(List<String> xAxisCategories) {
		this.xAxisCategories = xAxisCategories;
	}

	public void setxAxisTitle(String xAxisTitle) {
		this.xAxisTitle = xAxisTitle;
	}

	public void setyAxisTitle(String yAxisTitle) {
		this.yAxisTitle = yAxisTitle;
	}

	public void setValueSuffix(String valueSuffix) {
		this.valueSuffix = valueSuffix;
	}

	public void setSeries(List<JSSeries> series) {
		this.series = series;
	}

	public String getyAxislabels() {
		return yAxislabels;
	}

	public String getLegendLayout() {
		return legendLayout;
	}

	public String getLegendAlign() {
		return legendAlign;
	}

	public void setyAxislabels(String yAxislabels) {
		this.yAxislabels = yAxislabels;
	}

	public void setLegendLayout(String legendLayout) {
		this.legendLayout = legendLayout;
	}

	public void setLegendAlign(String legendAlign) {
		this.legendAlign = legendAlign;
	}

	public String getLegendVerticalAlign() {
		return legendVerticalAlign;
	}

	public void setLegendVerticalAlign(String legendVerticalAlign) {
		this.legendVerticalAlign = legendVerticalAlign;
	}

	public int getLegendX() {
		return legendX;
	}

	public int getLegendY() {
		return legendY;
	}

	public boolean isLegendFloating() {
		return legendFloating;
	}

	public int getLegendBorderWidth() {
		return legendBorderWidth;
	}

	public boolean isLegendShadow() {
		return legendShadow;
	}

	public void setLegendX(int legendX) {
		this.legendX = legendX;
	}

	public void setLegendY(int legendY) {
		this.legendY = legendY;
	}

	public void setLegendFloating(boolean legendFloating) {
		this.legendFloating = legendFloating;
	}

	public void setLegendBorderWidth(int legendBorderWidth) {
		this.legendBorderWidth = legendBorderWidth;
	}

	public void setLegendShadow(boolean legendShadow) {
		this.legendShadow = legendShadow;
	}
}

