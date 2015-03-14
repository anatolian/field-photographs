<?php
	//header('Content-type: application/json');

	include('connect.php');
	$area_easting=$_REQUEST['area_easting'];
	$area_northing=$_REQUEST['area_northing'];
	$image_path=basename($_FILES['image_path']['name']);
	$image_count=count($_FILES['image_path']['name']);
	$date_name=$_REQUEST['date_name'];
	//$batch_name=$_REQUEST['batch_name'];
	//$samba_share_path='workflow2'; 
	$samba_share_path=GetValue('options.procedure_properties','property_value','property','samba_sharepath_workflow2');
   	$context_subpath=GetValue('options.procedure_properties','property_value','property','context_subpath 3d');
	$upload_directory=$samba_share_path."/".$context_subpath;
	$area_easting_dir=$upload_directory."/".$area_easting;
	$area_northing_dir=$area_easting_dir."/".$area_northing;
	//echo date('Y-m-d H:i');
	$date=date('YmdHi');
	$date_dir=$area_northing_dir."/".$date."/";	
	
	$images=glob($date_dir."*.jpg");
	$count=1;
	foreach($images as $image){ $image; $count++; }
	$fileName = basename($_FILES["image_path"]["name"]);
		
	if($area_easting !='' && $area_northing !=''){
		if (!file_exists($area_easting_dir)) {
			custom_mkdir($area_easting_dir);
		}
		if(!file_exists($area_northing_dir)) {
			custom_mkdir($area_northing_dir);
		}
		/*if(!file_exists($date_dir)) {
			custom_mkdir($date_dir);
		}*/
		if($date_name == '' )
		{
			$date_name=	$date;
			custom_mkdir($date_dir);
		}
		else
		{
			$date_dir=$area_northing_dir."/".$date_name."/";
		}
		/*if($batch_name=='')
		{
			$batch_name=rand(0,9999);
			$batch_dir=$date_dir.$batch_name;
			custom_mkdir($batch_dir);
		}
		else
		{
			$batch_dir=$date_dir.$batch_name."/";
		}*/
		//echo $date_dir;
		//$images = glob($date_dir."*.jpg");
		$images = glob($date_dir."*.jpg");
		//$images = glob($batch_dir."*.jpg");
		//echo $image=$date_dir."1.jpg";
		//print_r($images);
		$count=1;
		foreach($images as $image) { $image; ++$count; }
		$fileName = basename($_FILES["image_path"]["name"]);
		$fileName=$count.".jpg";
		$target_path=$date_dir.$fileName;
		move_uploaded_file($_FILES['image_path']['tmp_name'],$target_path);			
		$result['responseData']=array('result'=>'success','date_name'=>$date_name);
	}else {
		$error="please insert required field";
		$result['responseData']=array('result'=>'failed', 'message'=>$error);		
	}
	$result=json_encode($result);
	echo $result;
?>