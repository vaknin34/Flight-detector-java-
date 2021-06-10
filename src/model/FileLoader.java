package model;

public interface FileLoader <T>{
	T load(String path);
}
