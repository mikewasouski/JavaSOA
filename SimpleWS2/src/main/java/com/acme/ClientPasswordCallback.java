package com.acme;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.*;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

public class ClientPasswordCallback implements CallbackHandler {
	 public void handle(Callback[] callbacks) throws IOException, 
     UnsupportedCallbackException {

     WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

     if (pc.getIdentifier().equals("mw")) {
         // set the password on the callback. This will be compared to the
         // password which was sent from the client.
         pc.setPassword("pwd");
     }
 }
}