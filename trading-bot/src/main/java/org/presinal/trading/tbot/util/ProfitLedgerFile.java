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

package org.presinal.trading.tbot.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.presinal.trading.tbot.AssetLostProfit;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class ProfitLedgerFile {

    private static final String FILE_NAME_PREFIX = "profit_ledger_book_";
    private static final String EXTENSION = ".csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM/dd/yyyy h:mm:ss a");
    
    private String destinationDir;
    private BufferedWriter writer;
    
    private boolean headerGenerated = false;
    
    public ProfitLedgerFile(String destinationDir) throws IOException {
        this.destinationDir = destinationDir;
        Path destPath = Paths.get(destinationDir);
        Path path = destPath.resolve(Paths.get(generatedFileName()));
        
        if(!Files.exists(destPath)) {
            Files.createDirectories(destPath);            
            
        }
        
        if(!Files.exists(path)) { 
            Files.createFile(path);
            
        } else {
            headerGenerated = true;
        }
        
        writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    }
    
    private String generatedFileName() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("YYYY-MM-DD");
        return FILE_NAME_PREFIX+dateFormater.format(new Date())+EXTENSION;
    }
    
    private String generatedHeader() {
        return "Date,Asset,Buy Date, Sell Date,Buy Price,Sell Price,Profit,Profit %";
    }
    
    public void writeEntry(AssetLostProfit profit) throws IOException {
        if(!headerGenerated){ 
            writer.write(generatedHeader());
            headerGenerated = true;
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append(DATE_FORMAT.format(new Date())).append(",")
                .append(profit.getAsset().toSymbol()).append(",")
                .append(DATE_FORMAT.format(profit.getBuyDate())).append(",")
                .append(DATE_FORMAT.format(profit.getSellDate())).append(",")
                
                .append(profit.getBuyPrice()).append(",")
                .append(profit.getSellPrice()).append(",")
                .append(profit.getProfit()).append(",")
                .append(profit.getProfitPercentage()).append("\n");
        
        writer.write(builder.toString());
        writer.flush();
    }
    
    public void close() {
        
        try {
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
