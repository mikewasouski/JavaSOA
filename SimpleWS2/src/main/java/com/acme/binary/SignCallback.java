package com.acme.binary;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class SignCallback implements CallbackHandler {
	 @Override
	  public void handle(Callback[] callbacks)
	      throws IOException, UnsupportedCallbackException {
	    // Do nothing
	  }
}