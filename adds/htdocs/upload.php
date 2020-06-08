<?php
$name = $_POST ["name"];
$phone = $_POST ["phone"];
$id = $_POST ["id"];
$id_photo = $_POST['id_photo'];
$target_dir = 'upload/userImages/'.$id.".jpeg";
$fainal_dir = "http//10.0.2.2/".$target_dir;
$conn=mysqli_connect("localhost","root","","123");

if ($conn -> connect_error){
die ("Connection failed:".$conn->connect_error);
}
else
{
    
    $sql = "INSERT INTO user (id, name, phone_number,id_photo) VALUES ('$id','$name','$phone','$fainal_dir')";
     $response = array();
     if ( mysqli_query($conn, $sql) ) {
         
       
         header('Content-Type: bitmap; charset=utf-8');
         $file = fopen($target_dir, 'wb');
        fwrite($file, base64_decode($id_photo));
        fclose($file);
        $result = "1";
        $message = "Successful Registeration";
      array_push($response,array("result" => $result,"message" => $message));
      echo json_encode($response);
        mysqli_close($conn);

    } else {
        
        $result= "0";
         array_push($response,array("result" => $result));
        echo json_encode($response);
        mysqli_close($conn);
    }
}


?>
 


