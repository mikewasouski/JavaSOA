package com.acme;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Map;

import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.SecurityConstants;

public abstract class BasicAuthAuthorizationInterceptor extends AbstractOutDatabindingInterceptor {
	public BasicAuthAuthorizationInterceptor() {
		super(Phase.WRITE);
		}

		@Override
		public void handleMessage(Message outMessage) throws Fault {
			System.out.println(SecurityConstants.USERNAME);
			//outMessage.getContextualProperty(SecurityConstants.USERNAME, getUsername());
			//outMessage.getContextualProperty(SecurityConstants.PASSWORD, getPassword());
		}

		public abstract String getUsername();

		public abstract String getPassword();
}
