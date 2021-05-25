/*
 * The MIT License
 *
 * Copyright 2018 Miguel Presinal.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.presinal.tradingbot.market.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType;
import com.presinal.tradingbot.market.client.security.PayloadSigner;


/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public abstract class AbstractMarketClient implements MarketClient {

    protected String apiKey;
    protected String secretKey;
    protected WebTarget baseTarget;
    protected String apiURL;

    private PayloadSigner signer;
    private Client client;
    private Gson gson;

    private boolean clientInitialized = false;
    
    public AbstractMarketClient(String apiURL, String apiKey, String secretKey) throws MarketClientException {
        this.apiURL = apiURL;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        initClient();
    }
    
    public abstract void registerTypeDeserializers(GsonBuilder builder);
    

    private void initClient() throws MarketClientException {
        
        if (!clientInitialized) {
            
            try {
                GsonBuilder builder = new GsonBuilder();
                registerTypeDeserializers(builder);
                gson = builder.create();
                
                client = ClientBuilder.newClient();
                baseTarget = client.target(apiURL);

                if (signer == null) {
                    signer = new PayloadSigner(secretKey);
                }
                
                clientInitialized = true;
                
            } catch (Exception ex) {
                Logger.getLogger(AbstractMarketClient.class.getName()).log(Level.SEVERE, "Error initializing client", ex);
                throw new MarketClientException("Error initializing client. "+ex.getMessage(), ex);
            }
        }
    }

    protected String signPayload(String payload) throws MarketClientException {

        if (signer != null) {
            
            try {

                return signer.sign(payload);

            } catch (Exception ex) {
                Logger.getLogger(AbstractMarketClient.class.getName()).log(Level.SEVERE, "Error signing payload", ex);
                throw new MarketClientException("Error signing payload", ex);
            }

        } else {
            throw new MarketClientException("Signer has not been initialized");
        }     
        
    }
    
    protected String doGetRequest(String endPoint, Map<String, Object> requestParam) throws MarketClientException {        
        
        
        try {
            Response response = addQueryParam(baseTarget.path(endPoint), requestParam)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            Status responseCode = Status.fromStatusCode(response.getStatus());
            String strResponse = null;
            if (Status.OK == responseCode) {
                strResponse = response.readEntity(String.class);
            } else {
                throw new MarketClientException("Error resquesting the target: " + endPoint + ". " + responseCode.getStatusCode() + " " + responseCode.getReasonPhrase());
            }
            return strResponse;
        } catch (javax.ws.rs.ProcessingException e) {
            throw new MarketClientException("Error resquesting the target: " + endPoint + ". "+e.getMessage(), e );
        }        
    }
    
    private WebTarget addQueryParam(WebTarget target, Map<String, Object> params)  {
        
        if(params == null || params.isEmpty()){
            return target;
        }
        
        WebTarget webTarget = target;
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
        }
        
        return webTarget;
    }
    
    protected final String createQueryParams(Map<String, Object> params) {
        StringBuilder buffer = new StringBuilder();
        int i = 1;
        int end = params.size();
        
        for(Map.Entry<String, Object> entry : params.entrySet()) {
           buffer.append(entry.getKey()).append("=").append(entry.getValue()); 
           if(i < end)
                buffer.append("&");
           ++i;
        }
        
        return buffer.toString();
    }

    public Client getClient() {
        return client;
    }

    public Gson getGson() {
        return gson;
    }
    
}
