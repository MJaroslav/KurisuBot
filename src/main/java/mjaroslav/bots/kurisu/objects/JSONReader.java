package mjaroslav.bots.kurisu.objects;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONReader<T> {
	public T json;
	public T defaults;
	private String filePath;
	private File file;
	private File folder;
	private Gson gson;

	public JSONReader(T object, File file, boolean isPretty) {
		json = object;
		defaults = object;
		this.file = file;
		filePath = file.getAbsolutePath();
		folder = file.getParentFile();
		if (isPretty)
			gson = new GsonBuilder().setPrettyPrinting().create();
		else
			gson = new GsonBuilder().create();
	}

	public JSONReader(T object, String file, boolean isPretty) {
		json = object;
		defaults = object;
		if (isPretty)
			gson = new GsonBuilder().setPrettyPrinting().create();
		else
			gson = new GsonBuilder().create();
		setFile(file);
	}

	public JSONReader(T object, boolean isPretty) {
		json = object;
		defaults = object;
		folder = file.getParentFile();
		if (isPretty)
			gson = new GsonBuilder().setPrettyPrinting().create();
		else
			gson = new GsonBuilder().create();
	}

	public JSONReader(T object) {
		json = object;
		defaults = object;
		gson = new GsonBuilder().create();
	}

	public boolean toDefault() {
		json = defaults;
		return write();
	}

	public boolean init() {
		file = new File(filePath);
		folder = file.getParentFile();
		if (!folder.exists() || !folder.isDirectory())
			folder.mkdirs();
		if (!file.exists() || !file.isFile()) {
			try {
				file.createNewFile();
				toDefault();
				return write();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return read();
	}

	public boolean read() {
		try {
			Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
			json = gson.fromJson(reader, (Class<T>) json.getClass());
			reader.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean write() {
		try {
			Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
			gson.toJson(json, writer);
			writer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public File getFile() {
		return file;
	}

	public JSONReader<T> setFile(String file) {
		return setFile(new File(file));
	}

	public JSONReader<T> setFile(File file) {
		this.file = file;
		this.filePath = file.getAbsolutePath();
		this.folder = file.getParentFile();
		return this;
	}

	public File getFolder() {
		return folder;
	}

	public JSONReader<T> setGson(Gson gson) {
		this.gson = gson;
		return this;
	}
}
