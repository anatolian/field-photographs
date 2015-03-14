<?php
	error_reporting(0);
	header('Content-type: application/json');
	include('connect.php');
	$font_path = "GILSANUB.TTF";
	$valid_formats = array("jpg",  "bmp","jpeg");
	$font_size = 10;
	$area_easting=$_REQUEST['area_easting'];
	$area_northing=$_REQUEST['area_northing'];
	$context_number=$_REQUEST['context_number'];
	$sample_number=$_REQUEST['sample_number'];
	$sample_photo_type=$_REQUEST['sample_photo_type'];
	$image_path=basename($_FILES['image_path']['name']);
	$water_mark_text_2=$area_easting." : ". $area_northing. " : ". $context_number . " : " . $sample_number;
	$samba_share_path=GetValue('options.procedure_properties','property_value','property','samba_sharepath_workflow3');
	$area_easting_dir=$samba_share_path."/".$area_easting;
	$area_northing_dir=$area_easting_dir."/".$area_northing;
    $context_number_dir=$area_northing_dir."/".$context_number;
	$sample_number_dir=$context_number_dir."/".$sample_number."/";
	$fileName = basename($_FILES["image_path"]["name"]);
	if($area_easting !='' && $area_northing !='' && $context_number != '' && $sample_number !='' && $sample_photo_type != ''){
		if (!file_exists($area_easting_dir)) {
			custom_mkdir($area_easting_dir);
		}
		if(!file_exists($area_northing_dir)) {
			custom_mkdir($area_northing_dir);
		}
		if(!file_exists($context_number_dir)) {
			custom_mkdir($context_number_dir);
		}
		if(!file_exists($sample_number_dir)) {
			custom_mkdir($sample_number_dir);
		}
		$images=glob($sample_number_dir."*.jpg");
		$count=1;
		print_r($images);
		foreach($images as $image){ $image; $count++; }
		
		$fileName = basename($_FILES["image_path"]["name"]);
		$fileName=$sample_photo_type."_".$count.".jpg";
		$target_path=$sample_number_dir.$fileName;
		move_uploaded_file($_FILES['image_path']['tmp_name'],$sample_number_dir.$image_path);		
	//	if(watermark_text($sample_number_dir.$image_path, $target_path))
        //       echo  $demo_image = $new_name;
		move_uploaded_file($_FILES['image_path']['tmp_name'],$target_path);			
		$result['responseData']=array('result'=>'success');
	}else {
		$error="please insert required field";
		$result['responseData']=array('result'=>'failed', 'message'=>$error);		
	}
	function watermark_text($oldimage_name, $new_image_name){
	//echo $oldimage_name;
	//echo "<br>";
	//echo $new_image_name;
    global $font_path, $font_size, $water_mark_text_2;
   list($owidth,$oheight) = getimagesize($oldimage_name);
    $width = $owidth;
	$height = $oheight;  
	$posx=$width/4; 
	$posy= $height*90/100;
    $image = imagecreatetruecolor($width, $height);
    $image_src = imagecreatefromjpeg($oldimage_name);
    imagecopyresampled($image, $image_src, 0, 0, 0, 0, $width, $height, $owidth, $oheight);
   	$black = imagecolorallocate($image, 0, 0, 0);
    $oranage = imagecolorallocate($image, 255, 102, 0);
   // imagettftext($image, $font_size, 0, 30, 190, $black, $font_path, $water_mark_text_1);
    imagettftext($image, $font_size, 0, $posx , $posy, $oranage, $font_path, $water_mark_text_2);
    imagejpeg($image, $new_image_name, 100);
    imagedestroy($image);
    unlink($oldimage_name);
    return true;
}
	$result=json_encode($result);
	echo $result;
?>