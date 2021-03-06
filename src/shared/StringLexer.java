package shared;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;



public class StringLexer {
	private BufferedReader source; // The stream we are trying to analyze
	public StringLexer(String s) throws FileNotFoundException {source = new BufferedReader(new StringReader(s));}
	
	int pos;
	public boolean verbose = false;
	public final int EOF = -1; //endofstream
	public final int LF = 10; //line feed
	public final int CR = 13; //carriage return
	public final int SP = 32; //space
	
	public void assumeToken(String expected) throws IOException{
		if (!getWord().equals(expected)) {throw new SyntaxError("Expected "+expected);}
	}
	
	public int getInt() throws IOException{
		String s = getWord ();
		try {int i = Integer.decode(s); return i;} 
		catch (NumberFormatException e) {throw new SyntaxError("Isnt an Integer : "+s);}
	}
	
	public String getWordUntil(String mark,boolean withIt) throws IOException {
		StringWriter out = new StringWriter();
		StringWriter blanks = new StringWriter();
		
		int n = mark.length(); //string length
		BufferedReader markBuffReader = new BufferedReader(new StringReader(mark)); //string reader
		
		//read until finding a blank
		int chr=source.read();
		if (chr==EOF) {throw new SyntaxError("Trying to read above EOF - searching for "+mark);}
		while ((chr!=EOF) && (chr!=LF) && (chr!=CR)&& (chr!=SP)){
			out.write(chr);
			chr = source.read();
		}
		
		boolean check = false;//represents if we found what we were searching
		while (!check) {	
			//skip remaining blanks, pushing them to <blanks>
			source.mark(1);
			blanks.getBuffer().setLength(0);//clears the buffer
			while ((chr==LF) || (chr==CR) || (chr==SP)){
				source.mark(1);
				blanks.write(chr);
				chr=source.read();
			}
			source.reset();//we got just before next word
			
			//check if there is "mark" after the blanks
			source.mark(n);
			markBuffReader.mark(n);
			int i=0;
			check = true;//did we find it?
			while (check && (i<n)) {
				int c=source.read();
				blanks.write(c);
				check = check && (markBuffReader.read()==c);
				i++;
			}// if we found it check==true at this point
			markBuffReader.reset();
			
			if (check) {
				if (withIt) {
					out.write(blanks.toString());
					} 
				else {
					source.reset();
					}
				}
			else {out.write(blanks.toString());} //in the other case, we add the blanks to the buffer			
		}
		//then skip remaining blanks
		source.mark(1);
		chr=source.read();
		while ((chr==LF) || (chr==CR) || (chr==SP)){
			source.mark(1);
			chr=source.read();
		}
		source.reset();		
		if (verbose) System.out.println("read: "+out.toString()+" |searched until: "+mark+" |withit: "+withIt);
		return out.toString();
	}

	public String lookWord() throws IOException {
		StringWriter out = new StringWriter();
		//read until finding a blank
		source.mark(20);
		int chr=source.read();
		if (chr==EOF) {throw new SyntaxError("Trying to read above EOF");}
		while ((chr!=EOF) && (chr!=LF) && (chr!=CR)&& (chr!=SP)){
			out.write(chr);
			chr = source.read();
		}
		
		if (verbose) System.out.println("looked up: "+out.toString());
		source.reset();
		return out.toString();
	}
	
	public String getWord() throws IOException {
		StringWriter out = new StringWriter();
		//read until finding a blank
		int chr=source.read();
		if (chr==EOF) {throw new SyntaxError("Trying to read above EOF");}
		while ((chr!=EOF) && (chr!=LF) && (chr!=CR)&& (chr!=SP)){
			out.write(chr);
			chr = source.read();
		}
		//then skip remaining blanks
		source.mark(1);
		while ((chr==LF) || (chr==CR) || (chr==SP)){
			source.mark(1);
			chr=source.read();
		}
		source.reset();
		if (verbose) System.out.println("read: "+out.toString());
		return out.toString();
	}

	public boolean eof() throws IOException {
		boolean res = (source.read()==-1);
		source.reset();
		return res;
	}
}
