package net.darchangel.shoppingTweeter.util;

import twitter4j.Status;

public class TweetTaskStatus {

	private Status tweetStatus = null;
	private Exception exception = null;

	public TweetTaskStatus() {
		// do nothing
	}

	public boolean isSuccess() {
		if (tweetStatus != null && exception == null) {
			return true;
		} else {
			return false;
		}
	}

	public void setTweetStatus(Status status) {
		tweetStatus = status;
	}

	public Status getTweetStatus() {
		return tweetStatus;
	}

	public void setException(Exception e) {
		exception = e;
	}

	public Exception getException() {
		return exception;
	}
}
