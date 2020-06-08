
<?php
if (($_FILES['id_photo']['name']!="")){
    $name = $_POST ["name"];
    $phone = $_POST ["phone"];
    $id = $_POST ["id"];

// Where the file is going to be stored
	$target_dir = "upload/userImages/";
	$file = $_FILES['id_photo']['name'];
	$path = pathinfo($file);
	$filename = $id;
	$ext = $path['extension'];
	$temp_name = $_FILES['id_photo']['tmp_name'];
	$path_filename_ext = $target_dir.$filename.".".$ext;

// Check if file already exists
if (file_exists($path_filename_ext)) {
 echo "User already exists.";
 }else{
 move_uploaded_file($temp_name,$path_filename_ext);
    $response = "success";
    echo $response;
 }
}

$conn=mysqli_connect("localhost","root","","123");

if ($conn -> connect_error){
die ("Connection failed:".$conn->connect_error);
}
$sql = $conn->prepare('INSERT INTO user (id, name, phone_number, id_photo) VALUES (?,?,?,?)');
$sql->bind_param('dsss', $id, $name, $phone, $path_filename_ext); // 's' specifies the variable type => 'string'. and 'sss' means we are passing 4
$sql->execute();
?>