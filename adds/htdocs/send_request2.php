<?php
$sender_id = $_POST ["sender_id"];
$image = $_POST['image'];
$origin_latitude = $_POST ["origin_latitude"];
$origin_longitude = $_POST ["origin_longitude"];

$target_dir = 'upload/requests/images/'.$sender_id.".jpeg" ;
$fainal_dir = "http//10.0.2.2/".$target_dir;

$conn=mysqli_connect("localhost","root","","123");

if ($conn -> connect_error){
die ("Connection failed:".$conn->connect_error);
}
else
{
     $sql = "INSERT INTO request (sender_id,image, origin_latitude, origin_longitude) VALUES ('$sender_id','$fainal_dir','$origin_latitude','$origin_longitude')";
     $response = array();
     if ( mysqli_query($conn, $sql) ) {
         
       
         header('Content-Type: bitmap; charset=utf-8');
         $file = fopen($target_dir, 'wb');
          
       
        fwrite($file, base64_decode($image));
         
        fclose($file);
       
        $result = "1";
        $message = "Successful";
      array_push($response,array("result" => $result));
      echo json_encode($response);
        mysqli_close($conn);

    } 
    else {
        
        $result= "0";
         array_push($response,array("result" => $result));
        echo json_encode($response);
        mysqli_close($conn);
    }
}

