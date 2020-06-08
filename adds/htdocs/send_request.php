<?php
$sender_id = $_POST ["sender_id"];
$record = $_POST ["record"];
$image = $_POST['image'];
$origin_latitude = $_POST ["origin_latitude"];
$origin_longitude = $_POST ["origin_longitude"];
$target_dir = 'upload/requests/records/'.$sender_id.".3gp" ;
$target_dir2 = 'upload/requests/images/'.$sender_id.".jpeg" ;
$fainal_dir = "http//10.0.2.2/".$target_dir;
$fainal_dir2 = "http//10.0.2.2/".$target_dir2;
$conn=mysqli_connect("localhost","root","","123");

if ($conn -> connect_error){
die ("Connection failed:".$conn->connect_error);
}
else
{
     $sql = "INSERT INTO request (sender_id, record,image, origin_latitude, origin_longitude) VALUES ('$sender_id','$fainal_dir','$fainal_dir2','$origin_latitude','$origin_longitude')";
     $response = array();
     if ( mysqli_query($conn, $sql) ) {
         
       
         header('Content-Type: bitmap; charset=utf-8');
         $file = fopen($target_dir, 'wb');
          $file2= fopen($target_dir2, 'wb');
         fwrite($file, base64_decode($audio));
        fwrite($file2, base64_decode($image));
         
        fclose($file);
         fclose($file2);
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

