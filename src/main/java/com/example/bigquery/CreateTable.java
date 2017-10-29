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
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.LegacySQLTypeName;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableInfo;

// [END imports]

public class CreateTable {
	public static void main(String... args) throws Exception {
		// [START create_client]

		GoogleCredentials credentials;
		File credentialsPath = new File("E:\\b.json"); // TODO: update to your key path.
		try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
			credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
		}

		BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();

		TableId tableId = TableId.of("my_new_dataset", "my_table_id");
		// Table field definition
		Field stringField = Field.of("StringField", LegacySQLTypeName.STRING);
		// Table schema definition
		Schema schema = Schema.of(stringField);
		// Create a table
		StandardTableDefinition tableDefinition = StandardTableDefinition.of(schema);
		Table createdTable = bigquery.create(TableInfo.of(tableId, tableDefinition));
		System.out.println("Table created");

	}
}
// [END all]