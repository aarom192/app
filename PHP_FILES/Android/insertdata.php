<?php

$servername = "localhost";
$username = "root";
$password = "1258";
$dbname = "db_test";
 
 $con = new mysqli($servername, $username, $password, $dbname);
 
 mysqli_query($con,"SET NAMES 'utf8'");
 //$sql = "SELECT *FROM test_chinese;";
 //mysqli_query($con,"SET NAMES 'utf8'");
 
 //$name = $_POST['name'];
 //$calorie = $_POST['calorie'];
 
 
 $Sql_Query = "INSERT INTO conference(cid, name) VALUES (7,'g’ƒ')";
 
 //creating an statment with the query
$stmt = $con->prepare($Sql_Query);

//executing that statment

  if($stmt->execute()){
 
 echo 'Data Submit Successfully';
 
 }
 else{
 
 echo 'Try Again';
 
 }

?>