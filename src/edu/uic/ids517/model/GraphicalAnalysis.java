package edu.uic.ids517.model;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphicalAnalysis {

	private DBAccessBean dBAccessBean;
	private FacesContext context;
	private MessageBean messageBean;
	private InstructorActionBean instructorActionBean;
	private List<String> listGraph = new ArrayList<String>(
			Arrays.asList("Pie Chart", "Bar Graph", "Histogram", "X-Y Series"));
	private List<String> ScoreList;
	private String graphTypeSelected = "";
	private boolean renderXScoreList = false;
	private boolean renderYScoreList = false;
	private boolean renderNumAnalysis = false;
	private double[] xArrayValues;
	private double[] yArrayValues;
	private double intercept;
	private double slope;
	private double rSqr;
	private double interceptStdError;
	private double slopeStdError;
	private double rSignificance;
	private String rEquation;
	private double minValue;
	private double maxValue;
	private double mean;
	private double variance;
	private double std;
	private double median;
	private double q1;
	private double q3;
	private double iqr;
	private double range;
	private String scoreXSelected = "";
	private String scoreYSelected = "";
	private ResultSet rs;
	private DefaultPieDataset pieDataset = new DefaultPieDataset();
	private DefaultCategoryDataset catDataset = new DefaultCategoryDataset();
	private HistogramDataset histDataset;
	private XYSeries xYSeries;
	private XYSeriesCollection xYSeriesCollection;
	private double total;
	private List<Double> values;
	private List<Double> valuesY;
	private boolean renderPieChart = false;
	private boolean renderBarChart = false;
	private boolean renderHistChart = false;
	private boolean renderXYChart = false;
	private String xYchartPath;
	private boolean renderRegAnalysis = false;
	private boolean renderGraphList = false;
	private String piechartPath;
	private String barchartPath;
	private String histchartPath;

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dBAccessBean = (DBAccessBean) m.get("dBAccessBean");
		messageBean = (MessageBean) m.get("messageBean");
		instructorActionBean = (InstructorActionBean) m.get("instructorActionBean");
		instructorActionBean.listCourse();

	}

	public void renderFalse() {
		renderPieChart = false;
		renderBarChart = false;
		renderHistChart = false;
		renderXYChart = false;
		renderNumAnalysis = false;
		renderRegAnalysis = false;
	}

	public String listScoresDataforAnalysis() {
		messageBean.resetAll();
		renderXScoreList = false;
		renderYScoreList = false;
		if (instructorActionBean.getCourseSelected().isEmpty()) {
			messageBean.setErrorMessage("Please select Course Name from the list");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		} else {
			if (instructorActionBean.listTest().equals("SUCCESS")) {
				ScoreList = instructorActionBean.getTestList();
				ScoreList.add("Total");
				renderXScoreList = true;
				renderYScoreList = true;
				renderGraphList = true;
				return "SUCCESS";
			}
			return "FAIL";
		}
	}

	public String listGraphs() {
		messageBean.resetAll();
		if (instructorActionBean.getCourseSelected().isEmpty()) {

			messageBean.setErrorMessage("Please select Course Name from the list");
			messageBean.setRenderErrorMessage(true);
			renderGraphList = false;
			return "FAIL";

		} else if (scoreXSelected.isEmpty()) {
			messageBean.setErrorMessage(
					"Please List available Scores and select Score Name from 'X / Independent(Predictor) Values'");
			messageBean.setRenderErrorMessage(true);
			renderGraphList = false;
			return "FAIL";
		} else {
			renderGraphList = true;
			return "SUCCESS";
		}
	}

	public String generateGraph() {
		messageBean.resetAll();
		renderFalse();
		context = FacesContext.getCurrentInstance();
		JFreeChart chart;
		if (instructorActionBean.getCourseSelected().isEmpty()) {

			messageBean.setErrorMessage("Please select Course Name from the list");
			messageBean.setRenderErrorMessage(true);
			renderGraphList = false;
			return "FAIL";

		} else if (scoreXSelected.isEmpty()) {
			messageBean.setErrorMessage("Please List available Scores and select Score Name from 'X Values'");
			messageBean.setRenderErrorMessage(true);
			renderGraphList = false;
			return "FAIL";
		} else if (graphTypeSelected.isEmpty()) {
			messageBean.setErrorMessage("Please select Graph Type from the list");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		try {
			String path = context.getExternalContext().getRealPath("/ChartImages");
			File outChart;
			long date = new Date().getTime();
			String sqlIndQuery, SqlTotalQuery;
			if (scoreXSelected.equalsIgnoreCase("Total")) {
				sqlIndQuery = "select sum(score) from f16g321_scores where  code ='"
						+ instructorActionBean.getCourseSelected() + "' group by uin order by uin;";

				SqlTotalQuery = "select sum(total) from f16g321_test where code = '"
						+ instructorActionBean.getCourseSelected() + "'";
			} else {
				sqlIndQuery = "Select score from f16g321_scores where  code ='"
						+ instructorActionBean.getCourseSelected() + "' and test_id ='" + scoreXSelected
						+ "' order by uin;";

				SqlTotalQuery = "select total from f16g321_test where code = '"
						+ instructorActionBean.getCourseSelected() + "' and test_id ='" + scoreXSelected + "';";
			}
			if (dBAccessBean.execute(SqlTotalQuery).equals("SUCCESS")) {
				rs = dBAccessBean.getResultSet();
				if (rs != null && rs.next()) {

					total = rs.getDouble(1);

				}
			}
			if (dBAccessBean.execute(sqlIndQuery).equals("SUCCESS")) {
				rs = dBAccessBean.getResultSet();
				values = new ArrayList<Double>(dBAccessBean.getNumOfRows());
				if (rs != null) {
					while (rs.next()) {

						values.add(rs.getDouble(1));

					}

					switch (graphTypeSelected) {
					case ("Pie Chart"):

						generateDataset();
						chart = ChartFactory.createPieChart(scoreXSelected + "_Pie Chart", pieDataset, true, true,
								false);

						outChart = new File(path + "/" + date + "_PieGraph.png");
						System.out.println(path);
						ChartUtilities.saveChartAsPNG(outChart, chart, 600, 450);
						piechartPath = "/ChartImages/" + date + "_PieGraph.png";
						renderPieChart = true;
						break;
					case ("Bar Graph"):
						generateDataset();
						chart = ChartFactory.createBarChart(scoreXSelected + "_Bar Chart", "Category", "Value",
								catDataset, PlotOrientation.VERTICAL, true, true, false);
						outChart = new File(path + "/" + date + "_BarGraph.png");
						renderBarChart = true;
						ChartUtilities.saveChartAsPNG(outChart, chart, 600, 450);
						barchartPath = "/ChartImages/" + date + "_BarGraph.png";
						break;
					case ("Histogram"):
						generateDataset();
						outChart = new File(path + "/" + date + "_HistGraph.png");
						chart = ChartFactory.createHistogram("Histogram", "Marks range", "Students Count.", histDataset,
								PlotOrientation.VERTICAL, true, true, false);
						ChartUtilities.saveChartAsPNG(outChart, chart, 600, 450);
						histchartPath = "/ChartImages/" + date + "_HistGraph.png";
						renderHistChart = true;
						break;
					case ("X-Y Series"):
						if (scoreYSelected.isEmpty()) {
							messageBean.setErrorMessage(
									"Please List available Scores and select Score Name from 'Y Values'");
							messageBean.setRenderErrorMessage(true);
							renderGraphList = false;
							return "FAIL";
						}
						if (scoreYSelected.equalsIgnoreCase("Total")) {
							sqlIndQuery = "select sum(score) from f16g321_scores where  code ='"
									+ instructorActionBean.getCourseSelected() + "' group by uin order by uin;";

						} else {
							sqlIndQuery = "Select score from f16g321_scores where  code ='"
									+ instructorActionBean.getCourseSelected() + "' and test_id ='" + scoreYSelected
									+ "' order by uin;";

						}

						if (dBAccessBean.execute(sqlIndQuery).equals("SUCCESS")) {
							rs = dBAccessBean.getResultSet();
							valuesY = new ArrayList<Double>(dBAccessBean.getNumOfRows());
							if (rs != null) {
								while (rs.next()) {

									valuesY.add(rs.getDouble(1));

								}
								generateDataset();
								outChart = new File(path + "/" + date + "_X-YSeriesGraph.png");
								chart = ChartFactory.createXYLineChart("X-Y Seires", scoreXSelected, scoreYSelected,
										xYSeriesCollection, PlotOrientation.VERTICAL, true, true, false);
								ChartUtilities.saveChartAsPNG(outChart, chart, 600, 450);
								xYchartPath = "/ChartImages/" + date + "_X-YSeriesGraph.png";
								renderXYChart = true;
							}
						}

						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "SUCCESS";

	}

	public void generateDataset() {
		double a = total * 0.9;
		double b = total * 0.8;
		double c = total * 0.7;
		double d = total * 0.6;
		int countA = 0;
		int countB = 0;
		int countC = 0;
		int countD = 0;
		int countE = 0;

		for (double temp : values) {
			if (temp >= a) {
				countA++;
			} else if (temp >= b) {
				countB++;
			} else if (temp >= c) {
				countC++;
			} else if (temp >= d) {
				countD++;
			} else {
				countE++;
			}
		}
		if (graphTypeSelected.equals("Pie Chart")) {
			pieDataset.clear();
			pieDataset.setValue("A Grade", countA);
			pieDataset.setValue("B Grade", countB);
			pieDataset.setValue("C Grade", countC);
			pieDataset.setValue("D Grade", countD);
			pieDataset.setValue("E Grade", countD);
		} else if (graphTypeSelected.equals("Bar Graph")) {
			catDataset.clear();
			catDataset.addValue(countA, "A Grade", "Category 1");
			catDataset.addValue(countB, "B Grade", "Category 2");
			catDataset.addValue(countC, "C Grade", "Category 3");
			catDataset.addValue(countD, "D Grade", "Category 4");
			catDataset.addValue(countE, "E Grade", "Category 5");
		} else if (graphTypeSelected.equals("Histogram")) {
			double[] temp = new double[values.size()];
			histDataset = new HistogramDataset();
			histDataset.setType(HistogramType.FREQUENCY);
			for (int i = 0; i < values.size(); i++) {
				temp[i] = values.get(i);
			}
			histDataset.addSeries("Histogram", temp, 15, 0, total);
		} else if (graphTypeSelected.equals("X-Y Series")) {
			xYSeries = new XYSeries("X-Y Series_" + scoreXSelected + "_" + scoreYSelected);
			for (int i = 0; i < values.size(); i++) {
				xYSeries.add(values.get(i), valuesY.get(i));
			}
			xYSeriesCollection = new XYSeriesCollection();
			xYSeriesCollection.addSeries(xYSeries);
		}
	}

	public String numericalAnalysis() {
		messageBean.resetAll();
		renderFalse();
		if (instructorActionBean.getCourseSelected().isEmpty()) {

			messageBean.setErrorMessage("Please select Course Name from the list");
			messageBean.setRenderErrorMessage(true);
			renderGraphList = false;
			return "FAIL";

		} else if (scoreXSelected.isEmpty()) {
			messageBean.setErrorMessage("Please List available Scores and select Score Name from 'X Values'");
			messageBean.setRenderErrorMessage(true);
			renderGraphList = false;
			return "FAIL";
		}
		try {
			String sqlQuery;

			if (scoreXSelected.equalsIgnoreCase("Total")) {
				sqlQuery = "select sum(score) from f16g321_scores where  code ='"
						+ instructorActionBean.getCourseSelected() + "' group by uin order by uin;";

			} else {
				sqlQuery = "Select score from f16g321_scores where  code ='" + instructorActionBean.getCourseSelected()
						+ "' and test_id ='" + scoreXSelected + "' order by uin;";

			}

			if (dBAccessBean.execute(sqlQuery).equals("SUCCESS")) {
				rs = dBAccessBean.getResultSet();
				xArrayValues = new double[dBAccessBean.getNumOfRows()];
				if (rs != null) {
					int i = 0;
					while (rs.next()) {

						xArrayValues[i] = (rs.getDouble(1));
						i++;

					}
					minValue = StatUtils.min(xArrayValues);
					maxValue = StatUtils.max(xArrayValues);
					mean = StatUtils.mean(xArrayValues);
					variance = StatUtils.variance(xArrayValues, mean);
					std = Math.sqrt(variance);
					median = StatUtils.percentile(xArrayValues, 50.0);
					q1 = StatUtils.percentile(xArrayValues, 25.0);
					q3 = StatUtils.percentile(xArrayValues, 75.0);
					iqr = q3 - q1;
					range = maxValue - minValue;
					renderNumAnalysis = true;
				} else {
					return "FAIL";
				}
			}
		} catch (Exception e) {
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String regressionAnalysis() {
		messageBean.resetAll();
		renderFalse();
		if (numericalAnalysis().equals("FAIL")) {
			return "FAIL";
		}
		if (scoreYSelected.isEmpty()) {
			messageBean.setErrorMessage("Please List available Scores and select Score Name from 'Y Values'");
			messageBean.setRenderErrorMessage(true);
			return "FAIL";
		}
		String sqlQuery;
		if (scoreYSelected.equalsIgnoreCase("Total")) {
			sqlQuery = "select sum(score) from f16g321_scores where  code ='" + instructorActionBean.getCourseSelected()
					+ "' group by uin order by uin;";

		} else {
			sqlQuery = "Select score from f16g321_scores where  code ='" + instructorActionBean.getCourseSelected()
					+ "' and test_id ='" + scoreYSelected + "' order by uin;";

		}
		try {
			SimpleRegression sr = new SimpleRegression();
			if (dBAccessBean.execute(sqlQuery).equals("SUCCESS")) {
				rs = dBAccessBean.getResultSet();
				yArrayValues = new double[dBAccessBean.getNumOfRows()];
				if (rs != null) {
					int i = 0;
					while (rs.next()) {

						yArrayValues[i] = (rs.getDouble(1));
						sr.addData(xArrayValues[i], yArrayValues[i]);
						i++;
					}
					intercept = sr.getIntercept();
					slope = sr.getSlope();
					rSqr = sr.getRSquare();
					interceptStdError = sr.getInterceptStdErr();
					slopeStdError = sr.getSlopeStdErr();
					rSignificance = sr.getSignificance();
					renderRegAnalysis = true;
					renderNumAnalysis = false;
					rEquation = scoreYSelected + " = " + round(intercept) + " + " + round(slope) + " * "
							+ scoreXSelected;
				} else {
					return "FAIL";
				}

			}
		} catch (Exception e) {

		}

		return "SUCCESS";
	}

	public double round(double d) {
		if (10 < Math.abs(d)) {
			d = Math.round(d * 100.0) / 100.0;
		} else {
			d = Math.round(d * 10000.0) / 10000.0;
		}
		return d;
	}

	public double getRange() {
		return range;
	}

	public String getrEquation() {
		return rEquation;
	}

	public double getIntercept() {
		return round(intercept);
	}

	public double getSlope() {
		return round(slope);
	}

	public double getrSqr() {
		return round(rSqr);
	}

	public double getInterceptStdError() {
		return round(interceptStdError);
	}

	public double getSlopeStdError() {
		return round(slopeStdError);
	}

	public double getrSignificance() {
		return round(rSignificance);
	}

	public boolean isRenderNumAnalysis() {
		return renderNumAnalysis;
	}

	public double getMinValue() {
		return round(minValue);
	}

	public double getMaxValue() {
		return round(maxValue);
	}

	public double getMean() {
		return round(mean);
	}

	public double getVariance() {
		return round(variance);
	}

	public double getStd() {
		return round(std);
	}

	public double getMedian() {
		return round(median);
	}

	public double getQ1() {
		return round(q1);
	}

	public double getQ3() {
		return round(q3);
	}

	public double getIqr() {
		return round(iqr);
	}

	public boolean isRenderYScoreList() {
		return renderYScoreList;
	}

	/*public void setRenderYScoreList(boolean renderYScoreList) {
		this.renderYScoreList = renderYScoreList;
	}*/

	public String getScoreYSelected() {
		return scoreYSelected;
	}

	public void setScoreYSelected(String scoreYSelected) {
		this.scoreYSelected = scoreYSelected;
	}

	public boolean isRenderRegAnalysis() {
		return renderRegAnalysis;
	}

	public boolean isRenderXYChart() {
		return renderXYChart;
	}

	/*public void setRenderXYChart(boolean renderXYChart) {
		this.renderXYChart = renderXYChart;
	}*/

	public String getxYchartPath() {
		return xYchartPath;
	}

	/*public void setxYchartPath(String xYchartPath) {
		this.xYchartPath = xYchartPath;
	}*/

	

	public boolean isRenderHistChart() {
		return renderHistChart;
	}

	/*public void setRenderHistChart(boolean renderHistChart) {
		this.renderHistChart = renderHistChart;
	}*/

	public String getHistchartPath() {
		return histchartPath;
	}

	/*public void setHistchartPath(String histchartPath) {
		this.histchartPath = histchartPath;
	}*/

	public boolean isRenderPieChart() {
		return renderPieChart;
	}

	/*public void setRenderPieChart(boolean renderPieChart) {
		this.renderPieChart = renderPieChart;
	}*/

	public boolean isRenderBarChart() {
		return renderBarChart;
	}

	/*public void setRenderBarChart(boolean renderBarChart) {
		this.renderBarChart = renderBarChart;
	}*/

	public String getScoreXSelected() {
		return scoreXSelected;
	}

	public void setScoreXSelected(String scoreXSelected) {
		this.scoreXSelected = scoreXSelected;
	}

	public List<String> getScoreList() {
		return ScoreList;
	}

	/*public void setScoreList(List<String> scoreList) {
		ScoreList = scoreList;
	}*/

	public boolean isrenderXScoreList() {
		return renderXScoreList;
	}

	/*public void setrenderXScoreList(boolean renderXScoreList) {
		this.renderXScoreList = renderXScoreList;
	}*/

	public String getBarchartPath() {
		return barchartPath;
	}

	/*public void setBarchartPath(String barchartPath) {
		this.barchartPath = barchartPath;
	}*/

	public String getPiechartPath() {
		return piechartPath;
	}

	/*public void setPiechartPath(String piechartPath) {
		this.piechartPath = piechartPath;
	}*/

	public boolean isRenderGraphList() {
		return renderGraphList;
	}

	/*public void setRenderGraphList(boolean renderGraphList) {
		this.renderGraphList = renderGraphList;
	}*/

	public String getGraphTypeSelected() {
		return graphTypeSelected;
	}

	public void setGraphTypeSelected(String graphTypeSelected) {
		this.graphTypeSelected = graphTypeSelected;
	}

	public List<String> getListGraph() {
		return listGraph;
	}

	/*
	 * public void setListGraph(List<String> listGraph) { this.listGraph =
	 * listGraph; }
	 */
}
