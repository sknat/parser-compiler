package shared;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Stack;

public class MarkRandomAccessFile {
	RandomAccessFile file;
	long smallmark;
	Stack<Long> marks;
	
	public void seek(long pos) throws IOException {file.seek(pos);}
	
	public void write(String s) throws IOException {
		file.setLength(0);
		for (int i=0;i<s.length();i++) {file.write( (int) s.charAt(i) );}
	}
	
	public MarkRandomAccessFile(String s, String mode) throws FileNotFoundException {
		this.file=new RandomAccessFile(s,mode);		
		marks=new Stack<Long>();
	}	
	
	public int read() throws IOException {return file.read();} // reads a single character from the file
	
	// A simple marking system to be used in the lexer and to replace the former BufferedReader Mechanism
	public void mark() throws IOException {smallmark = file.getFilePointer();}
	public void reset() throws IOException {file.seek(smallmark);} 
	
	// A Stackable Marking system, to be used in the Parser, remember to Drop the marks you didn't rewind to
	public void stackMark() throws IOException {
		marks.push(file.getFilePointer());
		}
	
	public void stackDrop() throws IOException {
		if (marks.isEmpty()) {throw new IOException("Mark Stack is empty");}
		marks.pop();
	}
	public void stackReset() throws IOException {
		if (marks.isEmpty()) {throw new IOException("Mark Stack is empty");}
		file.seek(marks.pop());
	}	
	public boolean stackIsEmpty() {
		return marks.isEmpty();
	}		
}
