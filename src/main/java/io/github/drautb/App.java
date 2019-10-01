
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
  private static final long LEASE_DURATION_30_SECONDS = 30;

  private static final AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder
      .standard()
      .withRegion("us-east-1")
      .withCredentials(new ProfileCredentialsProvider("fh2-us-east-1"))
      .build();

  public static void main(String[] args) {
    String ownerId = "component-a";

    Map<String, AttributeValue> item = new HashMap<>();
    item.put("lock_name", new AttributeValue("shared-resource-lock"));
    item.put("acquired", new AttributeValue().withN(Long.toString(getCurrentTimestampSeconds())));
    item.put("owner", new AttributeValue(ownerId));

    Map<String, String> expressionAttributeNames = new HashMap<>();
    expressionAttributeNames.put("#O", "owner");

    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
    expressionAttributeValues.put(":owner_id", new AttributeValue(ownerId));
    expressionAttributeValues.put(":expiration",
        new AttributeValue().withN(Long.toString(getCurrentTimestampSeconds() - LEASE_DURATION_30_SECONDS)));

    PutItemRequest putItemRequest = new PutItemRequest(TABLE_NAME, item)
        .withConditionExpression("#O = :owner_id OR attribute_not_exists(#O) OR acquired < :expiration")
        .withExpressionAttributeNames(expressionAttributeNames)
        .withExpressionAttributeValues(expressionAttributeValues);

    dynamoDbClient.putItem(putItemRequest);
  }

  private static long getCurrentTimestampSeconds() {
    return System.currentTimeMillis() / 1000;
  }

}
