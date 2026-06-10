package com.example.shoppingmall.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository
import org.springframework.batch.core.launch.JobOperator
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.batch.autoconfigure.BatchProperties
import org.springframework.boot.batch.autoconfigure.JobLauncherApplicationRunner
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableBatchProcessing(
	transactionManagerRef = "transactionManager",
)
@EnableJdbcJobRepository(
	dataSourceRef = "dataSource",
	transactionManagerRef = "transactionManager",
)
@EnableConfigurationProperties(BatchProperties::class)
@Configuration
class BatchConfig {
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "spring.batch.job", name = ["enabled"], havingValue = "true", matchIfMissing = true)
	fun jobLauncherApplicationRunner(
		jobOperator: JobOperator,
		properties: BatchProperties,
	): JobLauncherApplicationRunner {
		val runner = JobLauncherApplicationRunner(jobOperator)
		val jobName = properties.job.name
		if (jobName.isNotBlank()) {
			runner.setJobName(jobName)
		}
		return runner
	}
}
