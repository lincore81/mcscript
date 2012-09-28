package xde.lincore.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;

/**
 * Class that encapsulates a string and provides convenient methods for reading, writing and editing.
 * @author lincore
 *
 */
public class Text {
	private static int BUFFER_LEN = 256;
	private Charset charset;
	private ArrayList<Integer> lineBreaks;
	private String lineTerminator;
	private String text = "";

	public static final char CR = '\r';
	public static final char LF = '\n';

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public Text() {
		lineBreaks = new ArrayList<Integer>();
	}

	public Text(final String initialText) {
		this();
		setText(initialText);
	}

	/**
	 * Finds the character(s) that represent a line's end in this text.
	 * @param textStr The text to search the line terminator in.
	 * @return The character(s) used for EOL as String.
	 * The result depends on the operating system this text was created on,
	 * "\n" (LF) on Unixoids, like Linux, FreeBSD and Mac OS X,
	 * "\r\n" (CRLF) on Windows and
	 * "\r" (CR) on Mac OS 9 and below
	 */
	private String findLineTerminator(final String textStr) {
		for (int i = 0; i < textStr.length(); ++i) {
			final char c = textStr.charAt(i);
			if (c == LF) { // LF: Unix-like OSs including Mac OS X
				return String.valueOf(LF);
			}
			else if (c == CR && textStr.charAt(i + 1) == LF) { // CR-LF: Windows
				return String.valueOf(CR) + String.valueOf(LF);
			}
			else if (c == CR) { // CR: Mac OS
				return String.valueOf(CR);
			}
		}
		// text doesn't contain any line terminators. Try to get system default or use LF
		return System.getProperty("line.separator", "\n");
	}

	private void findLineBreaks() {
		lineBreaks = new ArrayList<Integer>();
		for (int i = 0; i < text.length(); ++i) {
			final char c = text.charAt(i);
			if (c == '\n') {
				lineBreaks.add(i);
			}
		}
	}

	public String get() {
		return text;
	}

	public Charset getCharset() {
		return charset;
	}

	/**
	 * Return a line of text.
	 * @param lineNumber Index of the line to return, starting at 0
	 * @param includeLineTerminator If true and the line to return is not the last line, the result includes the line terminator
	 * @return A string containing the line requested or null if the line doesn't exist.
	 */
	public String getLine(final int lineNumber, final boolean includeLineTerminator) {
		if (lineNumber > lineBreaks.size()) {
			return null;
		}

		final int from = (lineNumber == 0)? 0 : lineBreaks.get(lineNumber - 1) + 1;
		int to;
		if (lineNumber < lineBreaks.size()) { // not the last/only line?
			to = lineBreaks.get(lineNumber);
			if (includeLineTerminator) {
				to++;
			}
		}
		else {
			to = text.length();
		}
		return text.substring(from, to);
	}

	public String getLineAt(final int index, final boolean includeLineTerminator) {
		return getLine(getLineNumberOf(index), includeLineTerminator);
	}


	public int getLineCount() {
		return lineBreaks.size() + 1;
	}

	public String[] lines() {
		return text.split("\\n");
	}

	/**
	 * Return the line of the specified index
	 * @param index The char-index
	 * @return The line number the character with the specified index is in, starting at 0.
	 */
	public int getLineNumberOf(final int index) {
		for (int i = lineBreaks.size() - 1; i >= 0; --i) {
			final int currentLineBreak = lineBreaks.get(i);
			if (index > currentLineBreak) {
				return i + 1;
			}
		}
		return 0;
	}

	public String getLineTerminator() {
		return lineTerminator;
	}

	public String getFancyLineTerminator() {
		return lineTerminator.replaceFirst("\\n", "LF").replaceFirst("\\r", "CR");
	}

	public boolean hasMulipleLines() {
		return lineBreaks.size() > 0;
	}

//	public String readFile(final File file, final Charset charset)
//			throws FileNotFoundException, IOException
//	{
//		Path path = Paths.get(fileName);
//		Charset charset = Charset.forName(charsetName != null ? charsetName : DEFAULT_CHARSET);
//		return readFile(path, charset);
//	}

	public boolean isEmpty() {
		return text.isEmpty();
	}


	public String readFile(final File file) throws FileNotFoundException, IOException {
		return readFile(file, DEFAULT_CHARSET);
	}

	public String readFile(final File file, final Charset charset)
			throws FileNotFoundException, IOException
	{
		this.charset = (charset != null) ? charset : DEFAULT_CHARSET;
		BufferedReader source = null;
		try {
			source = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), this.charset), BUFFER_LEN);
			setText(readFrom(source));
		}
		finally {
			if (source != null) {
				source.close();
			}
		}
		return text;
	}

	/**
	 *
	 * @param file
	 * @param charsetName
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalCharsetNameException
	 * @throws UnsupportedCharsetException
	 */
	public void readFile(final File file, final String charsetName) throws FileNotFoundException, IOException,
			IllegalCharsetNameException, UnsupportedCharsetException {
		if (charsetName != null) {
			readFile(file, Charset.forName(charsetName));
		}
		else {
			readFile(file);
		}
	}

	private String readFrom(final BufferedReader source) throws IOException
	{
		final StringBuffer content = new StringBuffer();
		final char[] charBuffer = new char[BUFFER_LEN];
		int charsRead = source.read(charBuffer, 0, charBuffer.length);
		while(charsRead != -1) {
			content.append(charBuffer, 0, charsRead);
			charsRead = source.read(charBuffer, 0, charBuffer.length);
		}
		return content.toString();
	}

	public String readStdin() throws IOException {
		BufferedReader source = null;
		try {
			source = new BufferedReader(new InputStreamReader(System.in));
			setText(readFrom(source));
		}
		finally {
			if (source != null) {
				source.close();
			}
		}
		return text;
	}

	public String replaceAll(final String regex, final String replacement) {
		setText(text.replaceAll(regex, replacement));
		return text;
	}

	public void setCharset(final Charset charset) {
		this.charset = charset;
	}

	public void setCharset(final String charsetName) {
		charset = Charset.forName(charsetName);
	}

	public void setLineTerminator(final String lineTerminator) {
		this.lineTerminator = lineTerminator;
	}

	public void setText(final String text) {
		if (text == null) {
			this.text = "";
			findLineTerminator(this.text);
			return;
		}
		lineTerminator = findLineTerminator(text);
		if (lineTerminator.equals("\n")) {
			this.text = text;
		}
		else {
			this.text = text.replaceAll(lineTerminator, "\n");
		}
		findLineBreaks();
	}

	/**
	 * Return a fancy string containing the line and column of the specified index.
	 * @param index The character-index to use.
	 * @return A string containing the line and column ("line:column")
	 */
	public String toPositinalString(final int index) {
		for (int i = lineBreaks.size(); i > 0; --i) {
			final int currentLineBreak = lineBreaks.get(i - 1);
			if (index > currentLineBreak) {
				final int line = i + 1;
				final int column = index - currentLineBreak;
				return String.format("%d:%d", line, column);
			}
		}
		return String.format("1:%d", index + 1);
	}

	/**
	 * Return the actual text string.
	 */
	@Override
	public String toString() {
		return text;
	}

	public void writeFile(final File file) throws FileNotFoundException, IOException
	{
		if (charset == null) {
			charset = Charset.defaultCharset();
		}
//		System.out.format("File: %s, line terminator: %s, charset: %s%n", file,
//				lineTerminator.replaceAll("\n", "LF").replaceAll("\r", "CR"), charset);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), charset), BUFFER_LEN);
			writer.write(text.replaceAll("\n", lineTerminator));
		}
		finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
