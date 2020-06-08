<?php


    $Username = $_POST ["username"];
    $Password = $_POST ["password"];

$conn=mysqli_connect("localhost","root","","123");

if ($conn -> connect_error){
die ("Connection field:".$conn->connect_error);
}

$stmt = $conn->prepare('SELECT * FROM `driver` WHERE username = ? and password = ? ');
$stmt->bind_param('ss', $Username, $Password); // 's' specifies the variable type => 'string'. and 'ss' means we are passing 2

$stmt->execute();

$result = $stmt->get_result();
$response = array();
if ($result -> num_rows > 0)
{    
$row = mysqli_fetch_row($result); 
    
    $name = $row[3];
    $password = $row[1];
    
    $code = "login_success" ;
    array_push($response,array("code"=>$code,"name"=>$name,"password"=>$password));
    echo json_encode($response);
}

else {
    $code = "login_failed";
    $message = "user not found... Plaese Try Again...";
    array_push($response,array("code"=>$code,"message"=>$message));
    echo json_encode($response);
    
}
mysqli_close($conn)

?>