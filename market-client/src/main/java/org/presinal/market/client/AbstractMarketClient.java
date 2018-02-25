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
package org.presinal.market.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.presinal.market.client.security.PayloadSigner;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public abstract class AbstractMarketClient implements MarketClient {

    protected String apiKey;
    protected String secretKey;
    protected WebTarget baseTarget;
    protected String apiURL;

    private PayloadSigner signer;
    private Client client;

    private boolean clientInitialized = false;
    
    public AbstractMarketClient(String apiURL, String apiKey, String secretKey) {
        this.apiURL = apiURL;
        this.apiKey = apiKey;
        this.secretKey = secretKey;

    }

    protected void initClient() {
        
        if (!clientInitialized) {
            
            try {
                client = ClientBuilder.newClient();
                baseTarget = client.target(apiURL);

                if (signer == null) {
                    signer = new PayloadSigner(secretKey);
                }
                
                clientInitialized = true;
                
            } catch (Exception ex) {
                Logger.getLogger(AbstractMarketClient.class.getName()).log(Level.SEVERE, "Error signing payload", ex);
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
}
