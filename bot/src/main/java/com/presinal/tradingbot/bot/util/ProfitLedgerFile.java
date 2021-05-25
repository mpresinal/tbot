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

package com.presinal.tradingbot.bot.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class ProfitLedgerFile {

    private static final String FILE_NAME_PREFIX = "profit_ledger_book_";
    private static final String EXTENSION = ".csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM/dd/yyyy h:mm:ss a");
    
    private Path destinationPath;
    
    private boolean headerGenerated = false;
    
    public ProfitLedgerFile(String destinationPath) throws IOException {
        this(Paths.get(destinationPath));
    }
    
    public ProfitLedgerFile(Path destinationPath) throws IOException {
        this.destinationPath = destinationPath;
    }
    
    private String generatedFileName() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("YYYY-MM-dd");
        return FILE_NAME_PREFIX+dateFormater.format(new Date())+EXTENSION;
    }
    
    private String generatedHeader() {
        return "Date,Asset,Buy Date, Sell Date,Buy Price,Sell Price,Profit,Profit %";
    }
    
    private BufferedWriter openFile() throws IOException{
        Path path = destinationPath.resolve(Paths.get(generatedFileName()));
        
        if(!Files.exists(destinationPath)) {
            Files.createDirectories(destinationPath);
        }
        
        if(!Files.exists(path)) { 
            Files.createFile(path);
            
        } else {
            headerGenerated = true;
        }
        
        BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        
        if(!headerGenerated) {
            writer.write(generatedHeader());
            writer.write("\n");
            headerGenerated = true;        
        }
        return writer;
    }
    
    public synchronized void writeEntry(AssetLostProfit profit) throws IOException {
        
        try ( BufferedWriter writer = openFile()){
        
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
        
    }
    
}
