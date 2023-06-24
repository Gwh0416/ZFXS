<?php
$url = 'http://localhost:8080/api/app/v1/chat1';

// 初始化cURL会话
$curl = curl_init();

// 设置cURL选项
curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
curl_setopt($curl, CURLOPT_HEADER, false);
curl_setopt($curl, CURLOPT_FOLLOWLOCATION, true);
curl_setopt($curl, CURLOPT_HTTPHEADER, [
    'Accept: text/event-stream', // 设置接受SSE的头部
]);

// 发送GET请求并获取响应
$response = curl_exec($curl);

// 处理响应
if ($response === false) {
    // 请求失败
    $error = curl_error($curl);
    // 处理错误
    echo "Error: " . $error;
} else {
    // 请求成功
    // 处理响应数据
    echo $response;
}

// 关闭cURL会话
curl_close($curl);
?>
