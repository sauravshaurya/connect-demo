package com.connect.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

public class FileSourceConnector extends SourceConnector{
	
	public static final String FILE_NAME="file";
	public static final String TOPIC_NAMES="topic";
	public static final String TASK_BATCH_SIZE="batch.size";
	
	public static final int DEFAULT_TASK_BATCH_SIZE = 2000;
	
	private static final ConfigDef CONFIG_DEF= new ConfigDef()
			.define(FILE_NAME, Type.STRING,Importance.HIGH,"Source filename")
			.define(TOPIC_NAMES, Type.STRING,Importance.HIGH,"Name of the task")
			.define(TASK_BATCH_SIZE, Type.INT,DEFAULT_TASK_BATCH_SIZE, Importance.LOW, "The maximum number of records the Source task can read from file one time");
	
	private String filename;
	private String topic;
	private int batchSize;
	
	@Override
	public String version() {
		return AppInfoParser.getVersion();
	}
	
	public void start(Map<String,String> props) {
		AbstractConfig config = new AbstractConfig(CONFIG_DEF,props);
		filename=config.getString(FILE_NAME);
		List<String> topics=config.getList(TOPIC_NAMES);
		if(topics.size()!=1) {
			throw new ConfigException("'topic' requires definition of a single topic");
		}
		topic=topics.get(0);
		batchSize=config.getInt(TASK_BATCH_SIZE);
		
	}
	
	@Override
	public Class<? extends Task> taskClass(){
		return FileSourceTask.class;
		
	}
	
	public List<Map<String,String>> taskConfigs(int maxTasks){
		ArrayList<Map<String,String>> configs= new ArrayList<>();
		
		Map<String,String> config = new HashMap<>();
		config.put(FILE_NAME, filename);
		config.put(TASK_BATCH_SIZE, String.valueOf(batchSize));
		config.put(TOPIC_NAMES,topic);
		configs.add(config);
		
		return configs;
	}
	
	@Override
	public void stop() {
		//No background monitoring
	}
	
	public ConfigDef config() {
		return CONFIG_DEF;
	}

}
