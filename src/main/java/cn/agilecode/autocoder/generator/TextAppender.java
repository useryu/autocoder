package cn.agilecode.autocoder.generator;

import javax.swing.JTextArea;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class TextAppender extends WriterAppender {
	
	private JTextArea textArea;

	public TextAppender() {
		super();
	}

	@Override
	public void append(LoggingEvent event) {
		super.append(event);
		this.textArea.append(event.getMessage().toString());
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}
	
	
}
