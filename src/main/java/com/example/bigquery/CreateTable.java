/*
  Copyright 2016, Google, Inc.
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
      http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.example.bigquery;

import java.io.File;
import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQuery.QueryOption;
import com.google.cloud.bigquery.BigQuery.QueryResultsOption;
import com.google.cloud.bigquery.BigQueryError;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.DatasetInfo;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.LegacySQLTypeName;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.QueryResponse;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// [END imports]
import java.util.Map.Entry;

public class CreateTable {
	public static void main(String... args) throws Exception {
		// [START create_client]

		GoogleCredentials credentials;
		File credentialsPath = new File("E:\\b.json"); // TODO: update to your key path.
		try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
			credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
		}

		BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();

		String datasetId = "data_set_created";
		bigquery.create(DatasetInfo.newBuilder(datasetId).build());

		TableId tableId = TableId.of(datasetId, "Table_sahil_created");
		// Table field definition
		Field stringField = Field.of("StringField", LegacySQLTypeName.STRING);
		// Table schema definition
		Schema schema = Schema.of(stringField);
		// Create a table
		StandardTableDefinition tableDefinition = StandardTableDefinition.of(schema);
		bigquery.create(TableInfo.of(tableId, tableDefinition));

		TableId tableId1 = TableId.of("data_set_created", "Table_sahil_created");
		// Values of the row to insert
		Map<String, Object> rowContent = new HashMap<>();
		rowContent.put("booleanField", true);
		// Bytes are passed in base64
		rowContent.put("bytesField", "Cg0NDg0="); // 0xA, 0xD, 0xD, 0xE, 0xD in base64
		// Records are passed as a map
		Map<String, Object> recordsContent = new HashMap<>();
		recordsContent.put("stringField", "Hello, World!");
		rowContent.put("recordField", recordsContent);
		InsertAllResponse response = bigquery
				.insertAll(InsertAllRequest.newBuilder(tableId1).addRow("rowId", rowContent)
						// More rows can be added in the same RPC by invoking .addRow() on the builder
						.build());
		if (response.hasErrors()) {
			// If any of the insertions failed, this lets you inspect the errors
			for (Entry<Long, List<BigQueryError>> entry : response.getInsertErrors().entrySet()) {
				// inspect row error
			}
		}
	}
}
// [END all]