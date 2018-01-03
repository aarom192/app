<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<?php

$servername = "localhost";
$username = "root";
$password = "1258";
$dbname = "db_test";
 
 $con = mysqli_connect($servername, $username, $password, $dbname);
 
 //$db_selected = mysql_select_db($dbname, $con);
 
 mysqli_query($con, 'SET NAMES utf8');
 
 $name = $_POST['name'];
 $calorie = $_POST['calorie'];
 
 $Sql_Query = "INSERT INTO test_chinese(name, calorie) VALUES ('$name','$calorie')";
//executing that statment

  if(mysqli_query($con, $Sql_Query)){
 
 echo 'Data Submit Successfully';
 
 }
 else{
 
 echo 'Try Again';
 
 }

?>
