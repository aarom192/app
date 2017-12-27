<?php

$servername = "localhost";
$username = "root";
$password = "1258";
$dbname = "db_test";

try {
//creating a new connection object using mysqli 
$conn = new mysqli($servername, $username, $password, $dbname);

mysqli_query($conn,"SET NAMES 'utf8'");
 
//if there is some error connecting to the database
//with die we will stop the further execution by displaying a message causing the error 
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 
//creating an array for storing the data 
$heroes = array(); 
 
//this is our sql query 
$sql = "SELECT id, name, calorie, store FROM test_chinese;";
 
//creating an statment with the query
$stmt = $conn->prepare($sql);
 
//executing that statment
$stmt->execute();
 
//binding results for that statment 
$stmt->bind_result($id, $name, $calorie, $store);
 
//looping through all the records
while($stmt->fetch()){
 
 //pushing fetched data in an array 
 $temp = [
 'id'=>$id,
 'name'=>$name,
 'calorie'=>$calorie,
  'store'=>$store
 ];
 
 //pushing the array inside the hero array 
 array_push($heroes, $temp);
}
 
//displaying the data in json format 
echo json_encode($heroes);
    } catch (PDOException $e) {
    	    print "ƒGƒ‰[!: " . $e->getMessage() . "<br/>";
           die();
    }
    

?>