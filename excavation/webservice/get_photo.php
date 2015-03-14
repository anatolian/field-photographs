<?php
	header('Content-type: application/json');
	include('connect.php');


	$area_easting=$_REQUEST['area_easting'];
	$area_northing=$_REQUEST['area_northing'];
	$context_number=$_REQUEST['context_number'];
	$sample_number=$_REQUEST['sample_number'];
	$sample_photo_type=$_REQUEST['sample_photo_type'];
	$samba_share_path=GetValue('options.procedure_properties','property_value','property','samba_sharepath_workflow3');
	$upload_directory=$samba_share_path."/".$area_easting."/".$area_northing."/".$context_number."/".$sample_number."_".$sample_photo_type."_";

	if($area_easting != '' && $area_northing != '' && $context_number!= '' && $sample_number != '' && $sample_photo_type != '')
	{
		  $images=glob($upload_directory."*.jpg");
			$count=0;
			foreach($images as $image){

			$count++;
			$result['responseData'][$count]=array("image_path"=>$SITE_URL.$image,'area_easting'=>$area_easting,'area_northing'=>$area_northing, 'context_number'=>$context_number, 'sample_number'=>$sample_number);	
	
		}
	}
	else
	{
		$result['responseData']=array('result'=>'failed', 'message'=>'not data found');
	}	

$result=json_encode($result);

echo $result;
?>
