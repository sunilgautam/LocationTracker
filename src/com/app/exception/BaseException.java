package com.app.exception;

public class BaseException extends Exception
{
    private static final long serialVersionUID = 1L;
    private String errorMessage = null;

    public BaseException()
    {
	super();
    }

    public BaseException(String errorMessage)
    {
	super(errorMessage);
    }
    
    public BaseException(int ErrorNo)
    {
	super();
	setErrorMessage(getErrorMessageByNo(ErrorNo));
    }
    
    public String getErrorMessageByNo(int ErrorNo)
    {
	String errorMessage = null;
	switch (ErrorNo)
	{
	    case 103:
		errorMessage = "Unable to fetch location information.\nPlease enter correct location.";
		break;
	    case 102:
		errorMessage = "Unable to init map.";
		break;
	    case 101:
		errorMessage = "Error occurred while opening activity.";
		break;
	    
	    case 100:
		errorMessage = "Error occurred.";
		break;

	    default:
		errorMessage = "Error occurred.";
		break;
	}
	
	return errorMessage;
    }

    public BaseException(Exception ex, int ErrorNo)
    {
	super(ex);
	setErrorMessage(getErrorMessageByNo(ErrorNo));
    }

    public String getErrorMessage()
    {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
	this.errorMessage = errorMessage;
    }
}
