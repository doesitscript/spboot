package com.sapfgl.demo.spbootdckr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
    	return getJsonFromSQL();
    	//String output = execSQL();
    	//String text = "<html><head></head>";
    	//text += "<body><h3>Hello from SpringBoot SQL Docker</h3>";
    	//text +="<h3>List of workers retrieved from the SQLServer database:</h3>"+output+"</body></html>";
    	//return text;
    }

    public static void main(String[] args) {
    	Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }
        SpringApplication.run(Application.class, args);
    }
    
    
    public String getJsonFromSQL() {

        // Connect to database
           //String hostName = "your_server.database.windows.net";
           String hostName = "swsqlserverdb.database.windows.net";
           String dbName = "swsqldb";
           String user = "xswang";
           String password = "Build4Docker+8";
           String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
           Connection connection = null;
           
           String output = "{ \"worker\": [";
           try {
                   connection = DriverManager.getConnection(url);
                   String schema = connection.getSchema();
                   System.out.println("Successful connection - Schema: " + schema);
                   // Create and execute a SELECT SQL statement.
                   String selectSql = "SELECT * FROM dbo.worker";
                   try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(selectSql)) {
                	   	   boolean first = true;
                           while (resultSet.next()) {
                               String line = "{ \"first_name\" : \""+resultSet.getString(1).trim()+ "\", \"last_name\" : \""+ resultSet.getString(2).trim()+"\"}";
                               System.out.println(line);
                               if (first)
                            	   output += line.trim();
                               else
                            	   output += "," + line.trim();
                           }
                    connection.close();
                   }                   
                   output +="] }";
           }
           catch (Exception e) {
                   e.printStackTrace();
           }
           return output;
       }

    public String execSQL() {

        // Connect to database
           //String hostName = "your_server.database.windows.net";
           String hostName = "swsqlserverdb.database.windows.net";
           String dbName = "swsqldb";
           String user = "xswang";
           String password = "Build4Docker+8";
           String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
           Connection connection = null;
           
           //String output = "<table boarder=1 width=30% style=\" border-collapse:collapse; boarder:1px solid black;\" >";
           //output += "<tr><th colspan='2'>Worker Name</th></tr>";
           String output = "<table border='1p' width='50%' style=' board-collapse:collapse; boarder: 1px solid black;'><tr><th colspan='2'>Worker Name</th></tr>";
           try {
                   connection = DriverManager.getConnection(url);
                   String schema = connection.getSchema();
                   System.out.println("Successful connection - Schema: " + schema);
                   System.out.println("Query data example:");
                   System.out.println("=========================================");

                   // Create and execute a SELECT SQL statement.
                   String selectSql = "SELECT * FROM dbo.worker";
                   //String selectSql = "SELECT TOP 20 pc.Name as CategoryName, p.name as ProductName " 
                   //    + "FROM [SalesLT].[ProductCategory] pc "  
                   //    + "JOIN [SalesLT].[Product] p ON pc.productcategoryid = p.productcategoryid";

                   try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(selectSql)) {

                           // Print results from select statement
                           System.out.println("Top 20 workers:");
                           while (resultSet.next())
                           {

                               String line = "<td>"+resultSet.getString(1).trim()+ "</td><td>"+ resultSet.getString(2).trim()+"</td>";
                               System.out.println(line);
                               output += "<tr>"+line.trim()+"</tr>";
                           }
                    connection.close();
                   }                   
                   output +="</table>";
           }
           catch (Exception e) {
                   e.printStackTrace();
           }
           return output;
       }

}