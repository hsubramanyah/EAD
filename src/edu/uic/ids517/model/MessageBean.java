package edu.uic.ids517.model;

public class MessageBean {

	private String errorMessage;
	private boolean renderErrorMessage;
	private String successMessage;
	private boolean renderSuccessMessage;

	
	public void resetAll(){
		errorMessage = "";
		renderErrorMessage = false;
		successMessage ="";
		renderSuccessMessage = false;
	}
	public boolean isRenderErrorMessage() {
		return renderErrorMessage;
	}

	public void setRenderErrorMessage(boolean renderErrorMessage) {
		this.renderErrorMessage = renderErrorMessage;
	}

	public MessageBean() {
		super();
		errorMessage = "";
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public boolean isRenderSuccessMessage() {
		return renderSuccessMessage;
	}

	public void setRenderSuccessMessage(boolean renderSuccessMessage) {
		this.renderSuccessMessage = renderSuccessMessage;
	}
	
	

}
