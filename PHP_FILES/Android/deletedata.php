<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<?php

$servername = "localhost";
$username = "root";
$password = "1258";
$dbname = "db_test";

 
 $con = mysqli_connect($servername, $username, $password, $dbname);
 
 $id = $_POST['id'];
 //$id = 64;
 
 $Sql_Query = "DELETE FROM test_chinese where id =('$id')";
//executing that statment

  if(mysqli_query($con, $Sql_Query)){
 
 echo 'Data Submit Successfully';
 
 }
 else{
 
 echo 'Try Again';
 
 }

?>