package com.acme;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.cxf.attachment.AttachmentDeserializer;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class CxfInterceptor extends AbstractPhaseInterceptor<Message> {
	public CxfInterceptor() {
        super(Phase.RECEIVE);
        System.out.println("in the interceptor");
    }
 
    public void handleMessage(Message message) {
        String contentType = (String) message.get(Message.CONTENT_TYPE);
        System.out.println("coooontent type: "+contentType);
        if (contentType != null && contentType.toLowerCase().indexOf("multipart/related") != -1) {
            AttachmentDeserializer ad = new AttachmentDeserializer(message);
            try {
                ad.initializeAttachments();
            } catch (IOException e) {
                throw new Fault(e);
            }
        }
        
        /*
        Map<String, List<?>> headers = (Map<String, List<?>>) message.get(Message.PROTOCOL_HEADERS);

        String authString = configDao.getUsername() + ":" + config.getPassword();
        headers.put("Authorization", Collections.singletonList("Basic " + new String(Base64.encodeBase64(authString.getBytes()))));
        */
        
    }
 
    public void handleFault(Message messageParam) {
    }

}
