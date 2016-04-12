# SimpleTable
API to work with 2 dimensional data. Data from CSV or a table

## Building a simple table
  ```
  Table table = new SimpleTable("tableWithData")
                  .withColumns("A", "B", "C", "D")
                  .addRow(new SimpleRow("a1", "b1", "c1", "1"))
                  .addRow(new SimpleRow("a0000000001", "b0000000001", "c0000000001", "1"))
                  .addRow(new SimpleRow("a0000000002", "b0000000002", "c0000000002", "2"))
  ```
## Save the table to a CSV file
  ```
  table.writeAsCsv(new File("/var/tmp", "tableWithData.csv"))
  ```
## Save the to a html file
  ```
  table.writeAsHtml(new File("/var/tmp", "tableWithData.html"));
  ```
## The AVERAGE, COUNT, COUNTALL, MAX, MIN, SUM Aggregate functions
  ```
  Table input = new SimpleTable("Sample").addColumn("Count")
                .addRow(new SimpleRow("1"))
                .addRow(new SimpleRow("2"))
                .addRow(new SimpleRow("3"))
                .addRow(new SimpleRow().withCell(null)).writeAsHtml(htmlFile);

  ListMultimap<String, AggregateType> aggregateColumns = LinkedListMultimap.create();
  aggregateColumns.put("Count", AggregateType.AVERAGE);
  aggregateColumns.put("Count", AggregateType.COUNT);
  aggregateColumns.put("Count", AggregateType.COUNTALL);
  aggregateColumns.put("Count", AggregateType.MAX);
  aggregateColumns.put("Count", AggregateType.MIN);
  aggregateColumns.put("Count", AggregateType.SUM);

  Table result = input.aggregate(null, aggregateColumns).writeAsHtml(htmlFile);
  ```      
