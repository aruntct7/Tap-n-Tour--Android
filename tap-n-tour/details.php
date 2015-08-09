<?php

mysql_connect("localhost","root","");
mysql_select_db("db_tap-n-tour");

$sql=mysql_query("insert into details(eventtype,description,videos,audios,photos) values ('".$_REQUEST['eventtype']."','".$_REQUEST['description']."','".$_REQUEST['videos']."','".$_REQUEST['audios']."','".$_REQUEST['photos']."')")or die("dead");
echo 1;

?>