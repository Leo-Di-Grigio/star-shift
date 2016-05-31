package org.starshift;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.owlengine.config.OwlConfig;

public class Config extends OwlConfig {

	private static final String FILE_CONFIG = "config.cfg";
	private static final Charset CHARSET = StandardCharsets.UTF_8;
	
	private static final String WIDTH_KEY = "width:";
	private static final String HEIGHT_KEY = "height:";
	private static final String FULLSCREEN_KEY = "fullscreen:";
	
	private static final int WIDTH_DEFAULT_VALUE = 1280;
	private static final int HEIGHT_DEFAULT_VALUE = 768;
	private static final boolean FULLSCREEN_DEFAULT_VALUE = false;
	
	private int width;
	private int height;
	
	private boolean fullscreen;

	public Config() {
		File file = new File(FILE_CONFIG);
		
		if(file.isFile() && file.exists()){
			try {
				processList(Files.readAllLines(Paths.get(FILE_CONFIG), CHARSET));
			} 
			catch (IOException e) {
				setDefault();
			}
		}
		else{
			try {
				setDefault();
				file.createNewFile();
				writeDefault(file);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setDefault() {
		this.width = WIDTH_DEFAULT_VALUE;
		this.height = HEIGHT_DEFAULT_VALUE;
		this.fullscreen = FULLSCREEN_DEFAULT_VALUE;
	}
	
	private void writeDefault(File file) {
		try {
			PrintWriter out = new PrintWriter(FILE_CONFIG);
			out.write(WIDTH_KEY + " " + WIDTH_DEFAULT_VALUE + "\n");
			out.write(HEIGHT_KEY + " " + HEIGHT_DEFAULT_VALUE + "\n");
			out.write(FULLSCREEN_KEY + " " + FULLSCREEN_DEFAULT_VALUE);
			out.close();
		} 
		catch (FileNotFoundException e) {
			
		}
	}

	private void processList(List<String> lines) {
		for(String line: lines){
			String [] words = line.split(" ");
			
			if(words.length > 1){
				switch (words[0]) {
					case WIDTH_KEY:
						try{
							width = Integer.parseInt(words[1]);
						}
						catch(NumberFormatException | NullPointerException e){
							width = WIDTH_DEFAULT_VALUE;
						}
						break;
						
					case HEIGHT_KEY:
						try{
							height = Integer.parseInt(words[1]);
						}
						catch(NumberFormatException | NullPointerException e){
							height = HEIGHT_DEFAULT_VALUE;
						}
						break;
						
					case FULLSCREEN_KEY:
						try{
							fullscreen = Boolean.parseBoolean(words[1]);
						}
						catch(NullPointerException e){
							fullscreen = FULLSCREEN_DEFAULT_VALUE;
						}
						break;
	
					default:
						break;
				}
			}
		}
	}

	@Override
	public int frameWidth() {
		return width;
	}
	
	@Override
	public int frameHeight() {
		return height;
	}
	
	@Override
	public int smothSamples() {
		return 4;
	}
	
	@Override
	public boolean frameFullscreen() {
		return fullscreen;
	}
	
	@Override
	public String frameTitle() {
		return "Start Shift";
	}
}
