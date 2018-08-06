package com.JBibtexParser.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import com.JBibtexParser.util.exceptions.BlockProviderException;

public class JBibtexStringReader implements IBlocksProvider{

	private String content;
    private BufferedReader br;
    private String appendToNextEntry="";

    /**
     * @param string to parse
     */
    public void setContent(String content){
        this.content = content;
    }
    
    /**
     * Opens the file
     *
     * @throws BlockProviderException  if a file does not exist or can not be read
     */

    @Override
    public void openProvider() throws BlockProviderException {
    	br = new BufferedReader(new StringReader(content));
    }
    
    /**
     * Provides a block containing one bibtex entry from file
     *
     * @return A block containing next entry
     * @throws BlockProviderException on inner IOException
     */

    public String nextEntry() throws BlockProviderException {
        StringBuilder sb = new StringBuilder();
        sb.append(appendToNextEntry);
        appendToNextEntry="";

            int bracketCounter=0;
            boolean currentEntry=true;
        try {
            while (br.ready() && currentEntry) {

                char r = (char) br.read();
                if (r == '{') {
                    bracketCounter++;
                }
                if (r == '}') {
                    bracketCounter--;
                    if(bracketCounter==0)
                    {
                        currentEntry=false;
                    }
                }
                if (r == '@') {
                    currentEntry=false;
                    appendToNextEntry="@";
                }else {
                    sb.append(r);
                }
            }
        } catch (IOException e) {
            throw new BlockProviderException("Failed to open File " + e.getMessage());
        }

        return sb.toString();
    }
    /**
     * @return A value indicating if there are any more entries in the opened file.
     * @throws BlockProviderException on exception while reading blocks
     */
    public boolean hasNextEntry() throws BlockProviderException {
        try {
            return br.ready();
        } catch (IOException e) {
            throw new BlockProviderException("Failed to read some entries from file" + e.getMessage());
        }
    }

    @Override
    public void closeProvider() {
        try {
            br.close();
        } catch (IOException e) {
            //assume file to be closed
        }
    }

}