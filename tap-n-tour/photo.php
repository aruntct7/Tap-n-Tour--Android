<?php
$base = $_REQUEST["photo"];

mysql_connect("localhost","root","");
mysql_select_db("db_tap-n-tour");
//$url="http://192.168.1.109//nbimages/";
 

$imagename = "img_".date("Y-m-d-H-m-s").".jpg";
//echo $imagename; 




$sql=mysql_query("insert into photo values('".$_REQUEST['name']."','".$imagename."')");
echo "Registered";

if (isset($base)) {
 
//$suffix = createRandomID();
 $image_name=$imagename;
// base64 encoded utf-8 string
$binary = base64_decode($base);
 
// binary, utf-8 bytes
 
header("Content-Type: bitmap; charset=utf-8");
 
$file = fopen("../tap-n-tour/" . $image_name, "wb");
 
fwrite($file, $binary);
 
fclose($file);
 
//die($image_name);
 
} else {
 
//die("No POST");
}
function createRandomID() {
 
$chars = "abcdefghijkmnopqrstuvwxyz0123456789?";
//srand((double) microtime() * 1000000);
 
$i = 0;
 
$pass = "";
 
while ($i <= 5) {
 
$num = rand() % 33;
 
$tmp = substr($chars, $num, 1);
 
$pass = $pass . $tmp;
 
$i++;
}
return $pass;
}
?>