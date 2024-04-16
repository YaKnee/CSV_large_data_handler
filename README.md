# Large Data Handler

- Read in 2 CSV files (Returns and Orders),
- Table-view of all returned items,
- Table-view of customer performance based on orders made, and accumulated sales and profits,
- Chart and table-view of the total sales or customers as well as the average for any of the following: City, State, Region, Segment, or Year,
- Table-view of total order information made by each customer. 


## Instructions to Run App
### Via release zip folder:
1. Extract the files from the the zip folder into a chosen folder (remember path to folder).
2. Open folder: Right click -> open <strong><em>or</em></strong> use CLI command: ``` cd path/to/folder ```
3. Run jar file:  Right click -> open <strong><em>or</em></strong> use CLI command:  ``` java -jar app-all.jar ```

### Via Git Repository:
1. Create new folder (remember path to folder),
2. Clone repository into folder: Download zip and extract <strong><em>or</em></strong> use CLI command ```git clone https://github.com/YaKnee/CSV_large_data_handler.git```,
3. Open folder: Right click -> open <strong><em>or</em></strong> use CLI command: ```cd path/to/folder```,
4.  | Option 1 | Option 2 |
    | :---: | :---: |
    | Run the code from Main.java in your chosen IDE or use <br>```./gradlew run``` in main branch. | 1. Navigate to Libs folder: app -> build -> libs <strong><em>or</em></strong> use CLI command: ```cd app/build/libs```, <br>2.  Run jar file:  Right click -> open <strong><em>or</em></strong> use CLI command: <br>```java -jar app-all.jar``` |
<br><br>

## Screencast
User and developer perspectives, known bugs, and self-evaluation: [https://www.youtube.com/watch?v=XQpxs5C4dsM](https://www.youtube.com/watch?v=XQpxs5C4dsM)
<br><br>

## Table for Self-Evaluation

| Feature | Your Points | Max |
| :--- | :---: | :---: |
| User is able to navigate in the UI.<br>UI is easy to use, follows common guidelines either in CLI or GUI app. | 18 | 20 |
| App has excellent exception handling. | 6 | 10 |
| User is able to search for customers by name. | 10 | 10 |
| User is able to select one of the customers (from search) and view a summary of all the orders made by the customer in a table view (either ascii or gui). | 20 | 20 |
| User is able to show statistics from the dataset:<br><ul><li>What is the average sales amount of the orders?</li><li>Who is the best customer(highest total sales)?</li><li>The amount of customers per state?</li><li>How many customers per segment?</li><li>What is the total sales per year?</li><li>What is the total sales per region?</li></ul> | <br> <ul><li>10</li><li>10</li><li>10</li><li>10</li><li>10</li><li>10</li></ul> | <br><ul><li>10</li><li>10</li><li>10</li><li>10</li><li>10</li><li>10</li></ul> |
| Unit Testing for Business Logic. | 6 | 20 |
| JavaDoc Comments. | 20 | 20 |
| README.md | 10 | 10 |
| GUI | 45 | 50 |
| GUI: does not hang/suspend when handling large dataset. | 16 | 20 |
| Checkstyle code quality. | 10 | 10 |
| Screencast. | 8 | 10 |
| Git commits. | 10 | 10 |
| Proper releases. | 10 | 10 | 
| **Bonus**: GUI: User is able to view graphical charts from the data. | 30 | 30 |
| SUM | 290 | 320 |
