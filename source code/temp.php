<?php
$host = "127.0.0.1";
   	$user = "root";
   	$pass = "";

   	//database information
   	$databaseName = "iot";
   	$tableName = "temp_table";

   	//Connect to mysql database
   	$con = mysql_connect($host,$user,$pass);
   	$dbs = mysql_select_db($databaseName, $con);
$tableName2='temp_table2';
$search=1.0;
//$conn = mysqli_connect($server,$account,$passwd,$db_name) or die ('erro');
mysql_query("set names utf8");
if(isset($_GET['temp']))
{
$temp=$_GET['temp'];
//$sql="Update `temp_table` set (temp) VALUES ('$temp') where id=1;";
$sql="UPDATE $tableName SET `temp`= $temp where id=1";
mysql_query($sql);
//$sql="INSERT INTO `temp_table2` (temp1) VALUES ('$_GET['temp']');";
$sql2="INSERT INTO `$tableName2` (temp1) VALUES ('$temp');";
mysql_query($sql2);
//  echo $search;
$search=mysql_query("SELECT settemp FROM $tableName"); //check 目前狀態是關or開
   	$result=mysql_fetch_row($search);

   	//echo $result[0];
if($temp> $result[0]){
	echo "^on!";
	}
else{
	echo "^off!";
	}
}
?>