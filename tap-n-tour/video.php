<?php
$base = $_REQUEST['video'];
mysql_connect("localhost","root","");
mysql_select_db("db_tap-n-tour");

$imagename = "videos_"."_".date("Y-m-d-H-m-s").".mp4";
echo $base;
$sql=mysql_query("insert into video(name,video) values ('".$_REQUEST['name']."','".$imagename."')");
echo "registered";
if (isset($base)) {
 $image_name=$imagename;
$binary = base64_decode($base);
echo $binary;
header("'Content-type: video/mp4'; charset=utf-8");
 
$file = fopen("../tap-n-tour/" . $image_name, "wb");
 
fwrite($file, $binary);
 
fclose($file);

 

 } else {
 
die("No POST");
}
?>