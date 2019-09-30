
package io.github.drautb;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

public class App {

  private static final String TABLE_NAME = "drautb-sort-2019";

  private static final AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder
      .standard()
      .withRegion("us-east-1")
      .withCredentials(new ProfileCredentialsProvider("fh2-us-east-1"))
      .build();

  public static void main(String[] args) {
    Map<String, AttributeValue> item = new HashMap<>();
    item.put("lock_name", new AttributeValue("test-lock"));
    item.put("name", new AttributeValue("mark"));

    Map<String, String> expressionAttributeNames = new HashMap<>();
    expressionAttributeNames.put("#N", "name");

    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
    expressionAttributeValues.put(":current_name", new AttributeValue("jim"));

    PutItemRequest putItemRequest = new PutItemRequest(TABLE_NAME, item)
        .withConditionExpression("#N = :current_name")
        .withExpressionAttributeNames(expressionAttributeNames)
        .withExpressionAttributeValues(expressionAttributeValues);

    dynamoDbClient.putItem(putItemRequest);
  }

}
