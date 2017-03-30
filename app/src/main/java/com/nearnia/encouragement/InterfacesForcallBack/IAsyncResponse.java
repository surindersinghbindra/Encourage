package com.nearnia.encouragement.InterfacesForcallBack;

public interface IAsyncResponse {

	/******* Response return By Network Call *******/
	void onRestIntractionResponse(String response);

	/******* Error Response return By Network Call *******/
	void onRestIntractionError(String message);

}
