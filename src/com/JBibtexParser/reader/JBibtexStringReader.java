package com.JBibtexParser.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import com.JBibtexParser.util.exceptions.BlockProviderException;

public class JBibtexStringReader implements IBlocksProvider {

	private String content = null;
	private BufferedReader br = null;
	private String appendToNextEntry = "";
	private int c = 1; 

	/**
	 * @param string to parse
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Opens the file
	 *
	 * @throws BlockProviderException if a file does not exist or can not be read
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
		appendToNextEntry = "";

		int bracketCounter = 0;
		boolean currentEntry = true;
		
		try {
			c = br.read();
			while (c != -1 && currentEntry) {
				char r = (char)c;
				if (r == '{') {
					bracketCounter++;
				}
				if (r == '}') {
					bracketCounter--;
					if (bracketCounter == 0) {
						currentEntry = false;
					}
				}
				if (r == '@') {
					currentEntry = false;
					appendToNextEntry = "@";
				} else {
					sb.append(r);
				}
				c = br.read();
			}
		} catch (IOException e) {
			throw new BlockProviderException("Failed to read string " + e.getMessage());
		}

		return sb.toString();
	}

	/**
	 * @return A value indicating if there are any more entries in the opened file.
	 * @throws BlockProviderException on exception while reading blocks
	 */
	public boolean hasNextEntry() {
		return c != -1;
	}

	@Override
	public void closeProvider() {
		System.out.println("closed called");
		try {
			br.close();
		} catch (IOException e) {
			br = null;
		}
	}

}