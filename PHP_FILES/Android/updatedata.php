<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<?php

$servername = "localhost";
$username = "root";
$password = "1258";
$dbname = "db_test";

 
$con = mysqli_connect($servername, $username, $password, $dbname);
 
 //this is our sql query 
 mysqli_query($con, 'SET NAMES utf8');
 
   $originalname = $_POST['originalname'];
   $name = $_POST['name'];
   //$calorie = $_POST['calorie'];
   //$store = $_POST['store'];
     $setName = '紅茶';
     $findName = '綠茶';
 
 $Sql_Query = "UPDATE test_chinese SET name=('$name') WHERE name=('$originalname')";
//executing that statment

  if(mysqli_query($con, $Sql_Query)){
 
 echo 'Data Submit Successfully';
 
 }
 else{
 
 echo 'Try Again';
 
 }

?>