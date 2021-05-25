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

package com.presinal.tradingbot.market.client.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class PayloadSigner {

    public static final String HMAC_SHA256 = "HmacSHA256";
    
    private String secret;
    private String algorithm;
    
    private Mac mac;
    private SecretKeySpec secretKeySpec;
    
    public PayloadSigner(String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        this(secret, HMAC_SHA256);
    }

    public PayloadSigner(String secret, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        this.secret = secret;
        this.algorithm = algorithm;
        
        mac = Mac.getInstance(algorithm);
        secretKeySpec = new SecretKeySpec(secret.getBytes(), algorithm);
        mac.init(secretKeySpec);
    }
    
    public String sign(String payload) {
        if (payload != null && payload.isEmpty()) {
            return new String(Hex.encodeHex(mac.doFinal(payload.getBytes())));
        }        
        return null;
    }
}
