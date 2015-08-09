<?php

mysql_connect("localhost","root","");
mysql_select_db("db_tap-n-tour");

$sql=mysql_query("select * from dummy")or die("dead");

while($result=mysql_fetch_array($sql))
{
echo $result['name'];
}
?>