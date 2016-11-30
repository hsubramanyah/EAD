package edu.uic.ids517.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.*;

public class StudentLogin {
	private String userName;
	private FacesContext context;
	private DBAccessBean dbaseBean;
	private MessageBean messageBean;
	private DBAccessActionBean dBAccessActionBean;
	private Boolean errorMessage = false;

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		System.out.println(context);
		Map<String, Object> m = context.getExternalContext().getSessionMap();
		dbaseBean = (DBAccessBean) m.get("dBAccessBean");
		dBAccessActionBean = (DBAccessActionBean) m.get("dBAccessActionBean");
		if (dBAccessActionBean == null){
			dBAccessActionBean = new DBAccessActionBean();
		}
		messageBean = (MessageBean) m.get("messageBean");

	}

	public void getIp() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		System.out.println("ipAddress:" + ip);
		java.sql.Timestamp sqlDate = new java.sql.Timestamp(System.currentTimeMillis());

		String loginQuery = "update f16g321_student s set s.last_access='" + sqlDate + "',s.last_login_ip='" + ip
				+ "' where s.uin >0 and s.user_name='" + userName + "';";

		dbaseBean.execute(loginQuery);
		System.out.println(sqlDate.toString());
	}

	public String login() {
		messageBean.resetAll();
		String query = "Select count(*) from f16g321_student s where s.user_name='" + userName + "'";
		try {
			dbaseBean.execute(query);

			ResultSet result = dbaseBean.getResultSet();
			if (result == null) {
				messageBean.setErrorMessage("Incorrect UserName. Login Failed");
				errorMessage = true;
				return "Fail";
			}

			if (result.first()) {

				if (result.getInt(1) == 1) {

					getIp();
					dBAccessActionBean.setUsername(userName);
					return "Success";
				} else {
					messageBean.setErrorMessage("Incorrect UserName. Login Failed");
					errorMessage = true;
					return "Fail";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Fail";

	}

	public Boolean getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(Boolean errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
