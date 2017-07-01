<?php
$server='127.0.0.1';
$account='root';
$passwd='';
$db_name='iot';
$tableName='temp_table';
$conn = mysqli_connect($server,$account,$passwd,$db_name) or die ('erro');
mysqli_query($conn,"set names utf8");
if(isset($_POST['fname']))
{
$settemp=$_POST['fname'];
//$sql="Update `temp_table` set (temp) VALUES ('$temp') where id=1;";
$sql="UPDATE $tableName SET `settemp`= $settemp where id=1";
mysqli_query($conn,$sql);
echo "設定成功";
header("Location: http://127.0.0.1:8081/temp.html"); 
}
?>