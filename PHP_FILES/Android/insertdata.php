<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<?php

$servername = "localhost";
$username = "root";
$password = "1258";
$dbname = "db_test";
 
 $con = mysqli_connect($servername, $username, $password, $dbname);
 
 //$db_selected = mysql_select_db($dbname, $con);
 
 mysqli_query($con, 'SET NAMES utf8');
 
 //mysql_set_charset('utf8');
 
 
// mysqli_query($con,"SET NAMES 'utf8'");
 //$sql = "SELECT *FROM test_chinese;";
 //mysqli_query($con,"SET NAMES 'utf8'");
 $name = '緑茶';
 $cid =10;
 
 $name = $_POST['name'];
 $cid = $_POST['calorie'];
 
 $Sql_Query = "INSERT INTO test_chinese(id, name) VALUES ('$cid', '$name')";
 //$Sql_Query = "INSERT INTO conference(cid, name) VALUES ('$cid', '$name')";
 //$Sql_Query = "INSERT INTO conference(cid, name) VALUES (9, '緑茶')";
//executing that statment

  if(mysqli_query($con, $Sql_Query)){
 
 echo 'Data Submit Successfully';
 
 }
 else{
 
 echo 'Try Again';
 
 }

?>
