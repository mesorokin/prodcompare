package com.sorokin.batch;

import com.sorokin.model.Product;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;

import javax.sql.DataSource;

public class BatchConfiguration {

    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;

    public FlatFileItemReader<Product> reader() {
        return new FlatFileItemReaderBuilder<Product>()
                .build();
    }

    public ProductItemProcessor processor() {
        return new ProductItemProcessor();
    }

    public JdbcBatchItemWriter<Product> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Product>()
                .dataSource(dataSource)
                .build();
    }

    public Step step(JdbcBatchItemWriter<Product> writer,
                     FlatFileItemReader<Product> reader,
                     ProductItemProcessor processor) {
        return stepBuilderFactory.get("step")
                .<Product, Product>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}