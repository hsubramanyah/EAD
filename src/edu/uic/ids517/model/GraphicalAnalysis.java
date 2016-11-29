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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;

public class GraphicalAnalysis {

	private DBAccessBean dBAccessBean;
	private FacesContext context;
	private MessageBean messageBean;
	private InstructorActionBean instructorActionBean;
	private List<String> listGraph = new ArrayList<String>(
			Arrays.asList("Pie Chart", "Bar Graph", "Histogram", "Regression", "X-Y Series"));
	private List<String> ScoreList;
	private String graphTypeSelected;
	private String perdictorData;
	private String responseData;
	private boolean renderXScoreList = false;
	private boolean renderYScoreList = false;

	public boolean isRenderYScoreList() {
		return renderYScoreList;
	}

	public void setRenderYScoreList(boolean renderYScoreList) {
		this.renderYScoreList = renderYScoreList;
	}

	private String scoreXSelected;
	private String scoreYSelected;

	public String getScoreYSelected() {
		return scoreYSelected;
	}

	public void setScoreYSelected(String scoreYSelected) {
		this.scoreYSelected = scoreYSelected;
	}

	private ResultSet rs;
	private DefaultPieDataset pieDataset = new DefaultPieDataset();
	private DefaultCategoryDataset catDataset = new DefaultCategoryDataset();
	private HistogramDataset histDataset = new HistogramDataset();
	private XYSeries xYSeries;
	double total;
	List<Double> values;
	List<Double> valuesY;
	private boolean renderPieChart = false;
	private boolean renderBarChart = false;
	private boolean renderHistChart = false;
	private String histchartPath;

	public boolean isRenderHistChart() {
		return renderHistChart;
	}

	public void setRenderHistChart(boolean renderHistChart) {
		this.renderHistChart = renderHistChart;
	}

	public String getHistchartPath() {
		return histchartPath;
	}

	public void setHistchartPath(String histchartPath) {
		this.histchartPath = histchartPath;
	}

	public boolean isRenderPieChart() {
		return renderPieChart;
	}

	public void setRenderPieChart(boolean renderPieChart) {
		this.renderPieChart = renderPieChart;
	}

	public boolean isRenderBarChart() {
		return renderBarChart;
	}

	public void setRenderBarChart(boolean renderBarChart) {
		this.renderBarChart = renderBarChart;
	}

	public String getScoreXSelected() {
		return scoreXSelected;
	}

	public void setScoreXSelected(String scoreXSelected) {
		this.scoreXSelected = scoreXSelected;
	}

	public List<String> getScoreList() {
		return ScoreList;
	}

	public void setScoreList(List<String> scoreList) {
		ScoreList = scoreList;
	}

	public boolean isrenderXScoreList() {
		return renderXScoreList;
	}

	public void setrenderXScoreList(boolean renderXScoreList) {
		this.renderXScoreList = renderXScoreList;
	}

	private boolean renderGraphList = false;
	private String piechartPath;
	private String barchartPath;

	public String getBarchartPath() {
		return barchartPath;
	}

	public void setBarchartPath(String barchartPath) {
		this.barchartPath = barchartPath;
	}

	public String getPiechartPath() {
		return piechartPath;
	}

	public void setPiechartPath(String piechartPath) {
		this.piechartPath = piechartPath;
	}

	public boolean isRenderGraphList() {
		return renderGraphList;
	}

	public void setRenderGraphList(boolean renderGraphList) {
		this.renderGraphList = renderGraphList;
	}

	public String getGraphTypeSelected() {
		return graphTypeSelected;
	}

	public void setGraphTypeSelected(String graphTypeSelected) {
		this.graphTypeSelected = graphTypeSelected;
	}

	public String getPerdictorData() {
		return perdictorData;
	}

	public void setPerdictorData(String perdictorData) {
		this.perdictorData = perdictorData;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public List<String> getListGraph() {
		return listGraph;
	}

	public void setListGraph(List<String> listGraph) {
		this.listGraph = listGraph;
	}

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
									+ instructorActionBean.getCourseSelected() + "' and test_id ='" + scoreXSelected
									+ "' order by uin;";

						}

						if (dBAccessBean.execute(sqlIndQuery).equals("SUCCESS")) {
							rs = dBAccessBean.getResultSet();
							valuesY = new ArrayList<Double>(dBAccessBean.getNumOfRows());
							if (rs != null) {
								while (rs.next()) {

									valuesY.add(rs.getDouble(1));

								}
							}
						}
						generateDataset();
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
		} else if (graphTypeSelected.equals("X-Y Series_" + scoreXSelected + "_" + scoreYSelected)) {
			xYSeries = new XYSeries("X-Y Series_" + scoreXSelected + "_" + scoreYSelected);
			for (int i = 0; i < values.size(); i++) {
				xYSeries.add(values.get(i), valuesY.get(i));
			}
		}
	}

}
