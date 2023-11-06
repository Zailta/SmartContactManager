package com.smartManager.Configuration.SpringBatch;

import javax.sql.DataSource;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.smartManager.Entity.SMContactEntity;

@Configuration
@EnableBatchProcessing
public class SMBatchConfig {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JobBuilder builder;
	@Autowired
	private StepBuilder stepBuilder;
	
	@Bean
	private FlatFileItemReader<SMContactEntity> getFileReader(){
		FlatFileItemReader<SMContactEntity> fileReader = new FlatFileItemReader<SMContactEntity>();
		fileReader.setResource(new ClassPathResource("C:\\Users\\mohd.mohammad.yaqoob\\Documents\\records.csv"));
		fileReader.setLineMapper(getLineMapper());
		fileReader.setLinesToSkip(1);
		return fileReader;
	}
	
	private LineMapper<SMContactEntity> getLineMapper(){
		//reads the coloums
		DefaultLineMapper<SMContactEntity> lineMapper = new DefaultLineMapper<SMContactEntity>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		//gvining coloumn name
		delimitedLineTokenizer.setNames(new String[]{"UserName", "Email", "PhoneNumber", "NickName"});
		delimitedLineTokenizer.setIncludedFields(new int[] {0,1,2,3});
		BeanWrapperFieldSetMapper<SMContactEntity> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<SMContactEntity>();
		beanWrapperFieldSetMapper.setTargetType(SMContactEntity.class);
		lineMapper.setLineTokenizer(delimitedLineTokenizer);
		lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return lineMapper;
	}
	
	@Bean
	public SMUserItemProcessor processor() {
		return new SMUserItemProcessor();
	}
	
	@Bean 
	public JdbcBatchItemWriter<SMContactEntity> getBatchWriter(){
		JdbcBatchItemWriter<SMContactEntity> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<SMContactEntity>());
		jdbcBatchItemWriter.setSql("insert into abc");
		jdbcBatchItemWriter.setDataSource(this.dataSource);
		return jdbcBatchItemWriter;
	}
	@Bean
	public Job getUserJob() {
		return this.builder
				.incrementer(new RunIdIncrementer())
				.flow(getStep())
				.end()
				.build();
		
	}
	
	@Bean
	public Step getStep() {
		return this.stepBuilder
				.<SMContactEntity,SMContactEntity> chunk(10)
				.reader(getFileReader())
				.processor(processor())
				.writer(getBatchWriter())
				.build();
		
				
	}
	
	
	
}
